package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Programação Orientada a Objetos
 * Prof. Fausto Maranhão Ayres
 **********************************/

@Entity 
@Cacheable(false)

public class Reuniao {
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private int id;
	
	// será autoincrementado dentro do  metodo create() no DAOreuniao 
	@Column(columnDefinition = "TIMESTAMP")	
	private LocalDateTime datahora;
	
	private String assunto;
	
	
	@ManyToMany(mappedBy="reunioes",cascade=CascadeType.ALL,		//default é false
			fetch=FetchType.LAZY)
	private List <Participante> participantes = new ArrayList <Participante>();


	public Reuniao() {}

	public void adicionar(Participante p)	{
		this.participantes.add(p);
	}

	public void remover(Participante p)	{
		this.participantes.remove(p);
	}

	public Participante localizarParticipante(String nome)	{
		for(Participante p: participantes)	{
			if(p.getNome().equals(nome))
				return p;
		}
		return null;
	}

	public List<Participante> getParticipantes() 	{
		return participantes;
	}

	public void setParticipantes(List<Participante> participantes) 	{
		this.participantes = participantes;
	}

	public int getTotalParticipantes()	{
		return participantes.size();
	}

	public int getId() 	{
		return id;
	}

	public void setId(int id) 	{
		this.id = id;
	}

	public LocalDateTime getDatahora() 	{
		return this.datahora;
		//return LocalDateTime.parse(this.datahora, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}

	public void setDatahora(LocalDateTime dth) 	{
		this.datahora = dth; //dth.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}

	public String getAssunto() 	{
		return assunto;
	}

	public void setAssunto(String assunto) 	{
		this.assunto = assunto;
	}

	@Override
	public String toString() 	{
		String texto = "id: " + id + ", Horário: " + datahora + ", Assunto: " + assunto;

		texto +=  "\n Participantes:";
		for(Participante p: participantes) {
			if(p != null) {
			texto += " " + p.getNome();
			}}
		return texto ;
		
	}
}





