
package daojpa;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Participante;
import modelo.Reuniao;

public class DAOParticipante extends DAO<Participante>{

	public Participante read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<Participante> q = manager.createQuery("select p from Participante p where p.nome=:n", Participante.class);
			q.setParameter("n", nome);
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	//  //pode-se sobrescrever o metodo readAll da classe DAO para ordenar o resultado 
	public List<Participante> readAll(){
		TypedQuery<Participante> q = manager.createQuery("select p from Participante p  order by p.nome", Participante.class);
		return  q.getResultList();
	}
	
	
	

}
