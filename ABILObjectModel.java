package it.sgsbp.rcp.abil.model;

import it.ibm.eric.ui.widgets.combo.KeyedItem;
import it.sgsbp.rcp.abil.impl.ABILInputFormController;
import it.sgsbp.rcp.abil.impl.AutorizzazioniPerNominativo;
import it.sgsbp.rcp.common.service.SgsModel;
import it.sgsbp.tpSpo.bean.inout.BaseOutput;

import java.util.List;

/**
 * Object model definition. This class represents a common container used for
 * sharing values between all the components of the plugin.
 * 
 * @author Fabio Signorini - GS01491
 * @version 11/gen/2017
 * @see ABILInputFormController
 * @see ABILOutputFormController
 * @see ABILService
 */
public class ABILObjectModel extends SgsModel
{
	public static final String NOME_FUNZIONE = "ABIL";
	public static final String DESCRIZIONE = "ABILITA/DISABILITA UTENTI A FUNZIONI RISERVATE";

	// varibili di utilità
	private List<KeyedItem> listaDipendenze;
	private List<AutorizzazioniPerNominativo> listaAutorizzazioniPerNominativo;
	// private final ArrayList<AutorizzazioniPerNominativo>
	// listaAutorizzazioniPerNominativo;

	// input al servizio CICS

	// output dal servizio CICS
	private BaseOutput baseOutput = null;

	public ABILObjectModel()
	{
		super();
	}

	@Override
	public String getNomeFunzione()
	{
		return NOME_FUNZIONE;
	}

	@Override
	public String getDescrizione()
	{
		return DESCRIZIONE;
	}

	public BaseOutput getBaseOutput()
	{
		return baseOutput;
	}

	public void setBaseOutput(BaseOutput baseOutput)
	{
		this.baseOutput = baseOutput;
	}

	public List<KeyedItem> getListaDipendenze()
	{
		return listaDipendenze;
	}

	public void setListaDipendenze(List<KeyedItem> listaDipendenze)
	{
		this.listaDipendenze = listaDipendenze;
	}

	public List<AutorizzazioniPerNominativo> getListaAutorizzazioniPerNominativo()
	{
		return listaAutorizzazioniPerNominativo;
	}

	public void setListaAutorizzazioniPerNominativo(List<AutorizzazioniPerNominativo> listaAutorizzazioniPerNominativo)
	{
		this.listaAutorizzazioniPerNominativo = listaAutorizzazioniPerNominativo;
	}

}
