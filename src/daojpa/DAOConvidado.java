
package daojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Convidado;
import modelo.Reuniao;

public class DAOConvidado extends DAO<Convidado>{

	public Convidado read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<Convidado> q = manager.createQuery("select c from Convidado c where c.nome=:n", Convidado.class);
			q.setParameter("n", nome);
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	//  //pode-se sobrescrever o metodo readAll da classe DAO para ordenar o resultado 
	public List<Convidado> readAll(){
		TypedQuery<Convidado> q = manager.createQuery("select c from Convidado c  order by c.nome", Convidado.class);
		return  q.getResultList();
	}
	
	
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
	
	
	
}