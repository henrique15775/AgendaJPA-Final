package daojpa;

import java.lang.reflect.Field;
import modelo.Participante;
import modelo.Reuniao;


import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


public class DAOParticipante extends DAO<Participante>{
	
	public Participante read (Object chave) {
		try{
			
			
			String nome = (String) chave;
			TypedQuery<Participante> q = manager.createQuery("select p from Participante p where p.nome=:nome",	Participante.class);
			q.setParameter("nome", nome);
			return q.getSingleResult();
		/*Query q = manager.query();
		q.constrain(Participante.class);
		q.descend("nome").constrain(nome);
		List<Participante> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;*/
		}catch(NoResultException e) {
			return null;
		}
		}
	
	public List<Participante> readAll(){
		//uso do join fetch para recuperação imediata dos telefones
//		TypedQuery<Reuniao> q = manager.createQuery("select reuniao from Reuniao r join fetch r.participantes t order by r.nome", Reuniao.class);
		
		TypedQuery<Participante> q = manager.createQuery("select participante from Participante participante", Participante.class);
		return  q.getResultList();
	}

	

	//Consultas
	/*
	public  List<Participante> readByCaracteres(String caracteres) {
		Query q = manager.query();
		q.constrain(Participante.class);
		q.descend("nome").constrain(caracteres).like();
		List<Participante> result = q.execute(); 
		return result;
	}
	
	public Participante readByEmail(String e){
		Query q = manager.query();
		q.constrain(Participante.class);
		q.descend("email").constrain(e);
		List<Participante> resultados = q.execute();
		if(resultados.size()==0)
			return null;
		else
			return resultados.get(0);
	}
	
	public List<Participante> readAll(){
		Query q = manager.query();
		q.constrain(Participante.class);
		q.descend("nome");
		List<Participante> resultados = q.execute();
		if (resultados.size()>0)
			return resultados;
		else
			return null;
	}
	*/
}