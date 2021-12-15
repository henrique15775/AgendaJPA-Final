
package daojpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Convidado;
import modelo.Participante;
import modelo.Reuniao;

public class DAOReuniao extends DAO<Reuniao>{

	public Reuniao read (Object chave){
		try{
			int id = (int) chave;
			TypedQuery<Reuniao> q = manager.createQuery("select r from Reuniao r where r.id=:n", Reuniao.class);
		q.setParameter("n", id);
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
 
	public List<Reuniao> readAll(){
		TypedQuery<Reuniao> q = manager.createQuery("select r from Reuniao r  order by r.id", Reuniao.class);
		return  q.getResultList();
	}
	
	
	
	
	public List<Participante> readByData(String nome, int mes) {
		try {
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
			
			}catch(NoResultException e){
				return null;
			}
	}
	
}
