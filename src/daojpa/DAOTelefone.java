/**IFPB - Curso SI - Disciplina de POB
 * @author Prof Fausto Ayres
 */
package daojpa;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Telefone;

public class DAOTelefone  extends DAO<Telefone>{
	
	public Telefone read (Object chave){
		try{
			String numero = (String) chave;
			TypedQuery<Telefone> q = manager.createQuery("select p from Telefone p where p.numero = :n ",Telefone.class);
			q.setParameter("n", numero);

			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	//  sobrescrever o metodo readAll da classe DAO 
	public List<Telefone> readAll(){
		TypedQuery<Telefone> q = manager.createQuery("select t from Telefone t order by t.id", Telefone.class);
//		TypedQuery<Telefone> q = manager.createQuery("select t from Telefone t order by t.numero", Telefone.class);
		return  q.getResultList();
	}

	
	//--------------------------------------------
	//  consultas
	//--------------------------------------------

	public List<Telefone> readByDigitos (String digitos){		
		TypedQuery<Telefone> q = manager.createQuery
				("select t from Telefone t where t.numero like '%"+ digitos +"%' order by t.numero",Telefone.class);
		return q.getResultList();
	}
}
