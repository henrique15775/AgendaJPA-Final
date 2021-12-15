package modelo;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Programação Orientada a Objetos
 * Prof. Fausto Maranhão Ayres
 **********************************/

@Entity 
@Cacheable(false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Convidado extends Participante {
	private String empresa; 
	
	public Convidado() {};
	
	public Convidado(String nome, String email, String empresa) {
		super(nome, email);
		this.empresa=empresa;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	@Override
	public String toString() 	{
		String texto = super.toString() + "\n empresa="+empresa;
		return texto;
	}
}

