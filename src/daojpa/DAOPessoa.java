/**IFPB - Curso SI - Disciplina de PERSISTENCIA DE OBJETOS
 * @author Prof Fausto Ayres
 */

package daojpa;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import modelo.Pessoa;

public class DAOPessoa extends DAO<Pessoa>{


	public Pessoa read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<Pessoa> q = manager.createQuery(
					"select p from Pessoa p where p.nome=:n", 
					Pessoa.class);
			q.setParameter("n", nome);
			return q.getSingleResult();
			
		}catch(NoResultException e){
			return null;
		}
	}

	//  sobrescrever o metodo readAll da classe DAO 
	public List<Pessoa> readAll(){
		//uso do join fetch para recuperação imediata dos telefones
		TypedQuery<Pessoa> q = manager.createQuery("select p from Pessoa p join fetch p.telefones t order by p.nome", Pessoa.class);
		return  q.getResultList();
	}

	//--------------------------------------------
	//  consultas
	//--------------------------------------------
	public  List<Pessoa> readByCaracteres(String caracteres) {
		TypedQuery<Pessoa> q = manager.createQuery
				("select p from Pessoa p where p.nome like :x  order by p.nome",Pessoa.class);
		q.setParameter("x", "%"+caracteres+"%");
		return q.getResultList();
	}

	public  List<Pessoa>  readByNTelefones(int n) {
		TypedQuery<Pessoa> q = manager.createQuery
				("select p from Pessoa p where SIZE(p.telefones) = :x",Pessoa.class);
		q.setParameter("x", n);
		return q.getResultList(); 
	}

	public  boolean  temTelefoneCelular(String nome) {
		try{
			Query q = manager.createQuery
					("select count(t) from Pessoa p join p.telefones t where p.nome = :x and t.numero like :y");
			q.setParameter("x", nome);
			q.setParameter("y", "9%"); //inicia com 9
			long cont = (Long) q.getSingleResult();
			return cont>0;	
		}catch(NoResultException e){
			return false;
		}
	}

	public  boolean  temTelefoneFixo(String nome) {
		try{
			Query q = manager.createQuery
					("select count(t) from Pessoa p join p.telefones t where p.nome = :x and t.numero like :y");
			q.setParameter("x", nome);
			q.setParameter("y", "3%"); //inicia com 3

			long cont = (Long) q.getSingleResult();
			return cont>0;	
		}catch(NoResultException e){
			return false;
		}
	}


}

