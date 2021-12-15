package modelo;

import java.util.ArrayList;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Programação Orientada a Objetos
 * Prof. Fausto Maranhão Ayres
 **********************************/
@Entity
@Cacheable(false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
	
public class Participante {
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private int id;
	private String nome; 
	private String email;
	
	
	

	@ManyToMany(mappedBy="participantes", 
		cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, 	
		fetch=FetchType.LAZY)  	
	private ArrayList <Reuniao> reunioes = new ArrayList <Reuniao> ();
	
	
	
	
	public Participante() {}
	public Participante(String nome, String email) 	{
		super();
		this.nome = nome;
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public ArrayList<Reuniao> getReunioes() 	{
		return reunioes;
	}

	public void adicionar(Reuniao r)	{
		reunioes.add(r);
	}

	public void remover(Reuniao r)	{
		reunioes.remove(r);
	}


	public int getTotalReunioes() 	{
		return reunioes.size();
	}

	@Override
	public String toString() 	{
		String texto = "Nome: " + nome + ", email: " + email + ", Reuniões: ";
		
		if (reunioes.isEmpty())
			texto += " sem reunião";
		else 	
			for(Reuniao r : reunioes) 	
				texto += " " + r.getId();

		return texto;
	}
}

