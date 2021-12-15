package fachada;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Programação Orientada a Objetos
 * Prof. Fausto Maranhão Ayres
 * Grupo: Luís Henrique Ferreira Freire(20201370005)
 **********************************/

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/*
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport; // send comentado a pedido do professor para fazer o teste.
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
*/
import daojpa.DAO;
import daojpa.DAOConvidado;
import daojpa.DAOParticipante;
import daojpa.DAOReuniao;
import modelo.Convidado;
import modelo.Participante;
import modelo.Reuniao;

public class Fachada {
	private static String emailOrigem ;				//email de origem 
	private static String senhaOrigem ;				//senha do email de origem
	private static boolean emailDesabilitado ;		//desabilitar envio?
	private static DAOParticipante daoparticipante = new DAOParticipante();
	private static DAOConvidado daoconvidado = new DAOConvidado();
	private static DAOReuniao daoreuniao = new DAOReuniao();

	public static void setEmailSenha(String email, String senha) {
		emailOrigem = email;
		senhaOrigem = senha;
	}
	public static void desabilitarEmail(boolean status) {
		emailDesabilitado = status;
	}

	public static void inicializar()  {
		DAO.open();
	}

	public static void	finalizar() {
		DAO.close();
	}
	
	

	public static Participante criarParticipante(String nome, String email) throws Exception {
		
		nome = nome.trim ();
		email = email.trim();

		//inicio da transacao
		DAO.begin();
		//Verificar se o participande existe
		Participante p = daoparticipante.read(nome); //localizarParticipante
				if (p!=null) {
					DAO.rollback();	
					throw new Exception("Participante " + nome + " ja cadastrado(a)");
				}
		
		//Cadastrar participante na reunião
		p = new Participante (nome, email);

		//persistir novo participante
		daoparticipante.create(p);
		//...
		//fim da transacao
		DAO.commit();
		return p;	
	}	

	public static Convidado criarConvidado(String nome, String email, String empresa) 
			throws Exception{
		nome = nome.trim();
		email = email.trim();
		empresa = empresa.trim();
		
		//inicio da transacao
		DAO.begin();
		//Verificar se o convidado existe
		Participante p = daoconvidado.read(nome); //localizarParticipante
					if (p!=null) {
						DAO.rollback();	
						throw new Exception("Participante " + nome + " ja cadastrado(a)");
					}
		//Verificar se o Email existe
		//p = daoconvidado.readByEmail(email);
				//if (p!=null)
					//throw new Exception("Email " + email + " ja cadastrado(a)");

		//Cadastrar participante na reunião
		Convidado conv = new Convidado(nome, email, empresa);

		//persistir novo convidado no banco
		daoconvidado.create(conv);
		//...
		//fim da transacao
		DAO.commit();
		return conv;	
	}	

	public static Reuniao criarReuniao (String datahora, String assunto, ArrayList<String> nomes) throws Exception{
		assunto = assunto.trim();

		//inicio da transacao
		DAO.begin();
		
		LocalDateTime dth;
		try {
			DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			dth  = LocalDateTime.parse(datahora, parser); 
		}
		catch(DateTimeParseException e) {
			DAO.rollback();	
			throw new Exception ("reuniao com formato de data invalido");
		}

		//Verificar o tamanho da lista de participantes se é > 2
		if (nomes.size()<2) { 
			DAO.rollback();	
			throw new Exception ("Reunião sem quórum mínimo de dois participantes");
		}

		ArrayList<Participante> participantes = new ArrayList<>();
		for(String n : nomes) { 
			//Verificar se o participante existe
			Participante p =  daoparticipante.read(n);//localizarParticipante
			if(p == null) { 
				DAO.rollback();	
				throw new Exception ("Participante " + n + " inexistente"); 
			}

			//Verificar se o participante já está em outra reunião no mesmo horário
			for (Reuniao r1 : p.getReunioes()) 	{
				Duration duracao = Duration.between(r1.getDatahora(), dth); //(d - hinicio)
				long horas = duracao.toHours();
				if(Math.abs(horas) < 2) 
					throw new Exception("Participante já está em outra reunião nesse horário");
			}
			participantes.add(p);
		}
		Reuniao r = new Reuniao(dth, assunto);	
		
		//relacionar participante e reuniao 
		for(Participante p : participantes)	{
			r.adicionar(p);
			p.adicionar(r);
		}

		//persistir nova reuniao no banco
		
		r.setAssunto(assunto);
		r.setDatahora(dth);
		daoreuniao.create(r);
		//...
		//fim da transacao
		DAO.commit();
		//enviar email para participantes
		//for(Participante p : participantes)	
			//enviarEmail(p.getEmail(), "nova reunião", "Você foi agendado para a reunião na data:"+r.getDatahora()+" e assunto:"+assunto);

		return r;
	}

	public static void 	adicionarParticipanteReuniao(String nome, int id) throws Exception 	{
		nome = nome.trim();

		//inicio da transacao
		DAO.begin();
		//Verificar se o participante existe
		Participante p = daoparticipante.read(nome); //localizarParticipante
		if (p==null) {
			DAO.rollback();	
			throw new Exception("Participante " + nome + " não cadastrado(a)");
		}

		//Verificar de a reuniaão existe no repositório
		Reuniao r = daoreuniao.read(id);//localizarReuniao
		if(r == null) {
			DAO.rollback();
			throw new Exception("Reuniao " + id + " não cadastrada");
		}


		//Verificar se o participante já participa desta reunião
		if(r.localizarParticipante(nome) == p) 
			throw new Exception("Participante " + nome + " já cadastrado na reunião " + id);

		//Verificar se o participante já está em outra reunião no mesmo horário
		for (Reuniao r1 : p.getReunioes()) 	{
			LocalDateTime hinicio = r1.getDatahora();
			Duration duracao = Duration.between(r1.getDatahora(), r.getDatahora());
			long horas = duracao.toHours();
			if(Math.abs(horas) < 2) {
				DAO.rollback();
				throw new Exception("Participante já está em outra reunião nesse horário");
			}
		}

		//Adicionar o participante na reunião e vice-versa
		r.adicionar(p);
		p.adicionar(r);

		//atualizar reuniao no banco
		daoreuniao.update(r);
		//...
		//fim da transacao
		DAO.commit();
		//enviar email para o novo participante
	//	enviarEmail(p.getEmail(), "novo participante", "Você foi adicionado a reunião na data:"+r.getDatahora()+" e assunto:"+r.getAssunto());

	}

	public static void 	removerParticipanteReuniao(String nome, int id) throws Exception	{
		

		//inicio da transacao
		DAO.begin();
		
		nome = nome.trim();
		//Verificar se o participante existe
		Participante p = daoparticipante.read(nome); //localizarParticipante
		if (p==null) {
			DAO.rollback();	
			throw new Exception("Participante " + nome + " não cadastrado(a)");
		}

		//Verificar se a reunião está cadastrada
		Reuniao r = daoreuniao.read(id);//localizarReuniao 
		if(r == null) {
			DAO.rollback();
			throw new Exception("Reuniao " + id + " não cadastrada");
		}

		//Remover participante da reunião 
		
		r.remover(p);
		p.remover(r);
		
		//atualizar reuniao no banco
		if(r.getTotalParticipantes()<2) {
			daoreuniao.delete(r);
		}else {
			daoreuniao.update(r);
		}
		//...
		//fim da transacao
		DAO.commit();
		//enviar email para o  participante removido
	//	enviarEmail(p.getEmail(), "participante removido", "Você foi removido da reunião na data:"+r.getDatahora()+" e assunto:"+r.getAssunto());

		//Cancelar a reunião por falta de quórum mínimo de 2 participantes
		if(r.getTotalParticipantes() < 2) {
			cancelarReuniao(r.getId());
			
		}

	}

	public static void	cancelarReuniao(int id) throws Exception	{
		//inicio da transacao
				DAO.begin();
				//Verificar se a reunião está cadastrada
				Reuniao r = daoreuniao.read(id);//repositorio.localizarReuniao(id);
						if (r == null) {
							DAO.rollback();
							throw new Exception("Reuniao " + id + " não cadastrada");
						}
				System.out.println(r);
				//Remover participante da reunião
				for (Participante p : r.getParticipantes()) {
					p.remover(r);
					r.remover(p);
					daoparticipante.update(p);
					
				}

				//apagar reunião no banco
				daoreuniao.delete(r);
				
				//...
				//fim da transacao
				DAO.commit();
				//enviar email para todos os participantes
				
	}
		

	public static void apagarParticipante(String nome) throws Exception 
	{
		DAO.begin();
		nome = nome.trim();

		//inicio da transacao
		//Verificar se o participande existe
		Participante p = daoparticipante.read(nome); //localizarParticipante
		if (p==null) {
			DAO.rollback();
			throw new Exception("Participante " + nome + " nao cadastrado(a)");
		}
		//remover o participante das reunioes que participa
		//...
		List<Reuniao> reunioes_to_getout = p.getReunioes();
		daoparticipante.delete(p);

		DAO.commit();

		/**/
		for(Reuniao r : reunioes_to_getout) {
			DAO.begin();
			if(r==null) {
				DAO.rollback();
				throw new Exception("Reuniao " + nome + " nao cadastrado(a)");
			}
			r.remover(p);
			if(r.getTotalParticipantes() < 2) {

				System.out.println("Entrou aqui 145");
				daoreuniao.delete(r);

			}else {
				System.out.println("ATUALIZEI");
				daoreuniao.update(r);

			}
			DAO.commit();
		}

	}	

	public static List<Participante> listarParticipantes() {
		return daoparticipante.readAll(); //...
	}
	public static List<Convidado> listarConvidados() {
		return daoconvidado.readAll();//...
	}
	public static List<Reuniao> listarReunioes() 	{
		return daoreuniao.readAll();//...
	}
	public static List<Participante> consultaA(String nome, int mes) throws Exception{
		return daoreuniao.readByData(nome, mes);
		
	}
	public static List<Reuniao> consultaB( )throws Exception{
		return daoconvidado.reuniaotTemConvidado();
	}


	/*
	 * ********************************************************
	 * Obs: lembrar de desligar antivirus e 
	 * de ativar "Acesso a App menos seguro" na conta do gmail
	 * 
	 * biblioteca java.mail 1.6.2
	 * ********************************************************
	 */
	
	/*
	public static void enviarEmail(String emaildestino, String assunto, String mensagem) {
		try {
			if (Fachada.emailDesabilitado)
				return;

			String emailorigem = Fachada.emailOrigem;
			String senhaorigem = Fachada.senhaOrigem;

			//configurar email de origem
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			Session session;
			session = Session.getInstance(props,
					new javax.mail.Authenticator() 	{
				protected PasswordAuthentication getPasswordAuthentication() 	{
					return new PasswordAuthentication(emailorigem, senhaorigem);
				}
			});

			//criar e enviar email
			MimeMessage message = new MimeMessage(session);
			message.setSubject(assunto);		
			message.setFrom(new InternetAddress(emailorigem));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emaildestino));
			message.setText(mensagem);   // usar "\n" para quebrar linhas
			Transport.send(message);
		} 
		catch (MessagingException e) {
			System.out.println(e.getMessage());
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}*/
}
