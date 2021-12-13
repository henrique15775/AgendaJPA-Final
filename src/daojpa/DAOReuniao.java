package daojpa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.db4o.query.Query;

import modelo.Participante;
import modelo.Pessoa;
import modelo.Reuniao;

public class DAOReuniao extends DAO<Reuniao>{
	
public Reuniao read(Object chave) {	
	try{
		int id = (int) chave;
		TypedQuery<Reuniao> q = manager.createQuery(
				"select reuniao from Reuniao reuniao where r.id=:id", 
				Reuniao.class);
		q.setParameter("id", id);
		return q.getSingleResult();
		
	}catch(NoResultException e){
		return null;
		}
	}

public List<Reuniao> readAll(){
	
//	TypedQuery<Reuniao> q = manager.createQuery("select reuniao from Reuniao r join fetch r.participantes t order by r.nome", Reuniao.class);
	
	TypedQuery<Reuniao> q = manager.createQuery("select reuniao from Reuniao reuniao", Reuniao.class);
	return  q.getResultList();
}

public List<Participante> readByData(String nome, int mes){
	
	TypedQuery<Reuniao> q = manager.createQuery("select reuniao from Reuniao reuniao", Reuniao.class);
	List<Participante> reunioes_com_participante = new ArrayList<Participante>();
	List<Reuniao> reunioes = q.getResultList();
	
	for(Reuniao r: reunioes) {
		LocalDateTime data = r.getDatahora();
		int month = data.getMonthValue();
		if(r.localizarParticipante(nome) != null && month==mes) {
			reunioes_com_participante.addAll(r.getParticipantes());
		}
	}
	
	return reunioes_com_participante;
	
}
	
}