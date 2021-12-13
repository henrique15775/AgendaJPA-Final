package daojpa;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.lang.reflect.Field;

import modelo.Convidado;
import modelo.Participante;
import modelo.Reuniao;


public class DAOConvidado extends DAO<Convidado>{
	public Convidado read (Object chave) {
		try{
			String nome = (String) chave;
			TypedQuery<Convidado> q = manager.createQuery(
					"select convidado from Convidado convidado where convidado.nome=:nome", 
					Convidado.class);
			q.setParameter("nome", nome);
			return q.getSingleResult();
			
		}catch(NoResultException e){
			return null;
			}
		}
	
	public List<Convidado> readAll(){
		TypedQuery<Convidado> q = manager.createQuery("select convidado from Convidado convidado", Convidado.class);
		return  q.getResultList();
	}

	//Consultas
	
	
	public List<Reuniao> reuniaotTemConvidado(){
		TypedQuery<Reuniao> q = manager.createQuery("select reuniao from Reuniao reuniao",Reuniao.class);
		List<Convidado> convidados = this.readAll();
		List<Reuniao> reunioes_convidado = new ArrayList<Reuniao>();
		for(Convidado c: convidados) {
			for(Reuniao r: c.getReunioes()) {
				if(reunioes_convidado.contains(r) == false) {
					reunioes_convidado.add(r);
				}
				}
			}
		return reunioes_convidado;
		
	}
	
	/*
	public List<Reuniao> reuniaoTemConvidado() {
		Query q = manager.query();
		q.constrain(Convidado.class);
		List<Convidado> convidados = q.execute();
		ArrayList<Reuniao> reunioes_com_convidado = new ArrayList<Reuniao>();
		for(Convidado c: convidados) {
			for(Reuniao r: c.getReunioes()) {
				if(reunioes_com_convidado.contains(r) == false) {
					reunioes_com_convidado.add(r);
				}
				}
			}
		return reunioes_com_convidado;	
		}
		
	*/
	
}
