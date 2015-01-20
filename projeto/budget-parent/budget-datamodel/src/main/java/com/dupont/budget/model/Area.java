package com.dupont.budget.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Área do Centro de Custo
 * 
 * @author <a href="asouza@redhat.com">Ângelo Galvão</a>
 * @since 2014
 *
 */
@Entity
@Table(name = "area")
public class Area extends NamedAbstractEntity<Long> {

	private static final long serialVersionUID = 8630905655777582547L;
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy = "area", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<PapelUsuario> papeis;

	public Set<PapelUsuario> getPapeis() { 
		if(papeis == null)
			papeis = new HashSet<PapelUsuario>();
		return papeis;
	}
	public void setPapeis(Set<PapelUsuario> papeis) { 
		this.papeis = papeis;
	}
	public PapelUsuario getLider()
	{
		for(PapelUsuario papel : papeis)
		{
			if(papel.getPapel().getNome().contains("LIDER_"))
			{
				return papel;
			}
		}
		return null;
	}
	public PapelUsuario getResponsavelNotas()
	{
		for(PapelUsuario papel : papeis)
		{
			if(papel.getPapel().getNome().contains("NOTAS_"))
			{
				return papel;
			}
		}
		return null;
	}	
	
	public void setResponsavelNotas(PapelUsuario papelUsuario)
	{
		papeis.add(papelUsuario);
		
	}
	public void setLider(PapelUsuario papelUsuario)
	{
		papeis.add(papelUsuario);
		
	}
}
