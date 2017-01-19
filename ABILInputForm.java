package it.sgsbp.rcp.abil.impl;

import it.ibm.eric.ui.widgets.button.CheckButtonWidget;
import it.ibm.eric.ui.widgets.combo.ComboWidget;
import it.ibm.eric.ui.widgets.label.LabelWidget;
import it.ibm.eric.ui.widgets.table.TableWidget;
import it.ibm.eric.ui.widgets.text.DatePickerWidget;
import it.ibm.eric.ui.widgets.text.InputPatterns;
import it.ibm.eric.ui.widgets.text.TextWidget;
import it.sgsbp.rcp.abil.model.ABILObjectModel;
import it.sgsbp.rcp.common.form.SgsForm;
import it.sgsbp.rcp.common.ui.WidgetUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

/**
 * Definition of the input form. This component is responsible for graphically
 * present the input layout of the plugin by setting the components size and
 * their disposition in the window. All the registered events are managed in the
 * input form controller component.
 * 
 * @author Fabio Signorini - GS01491
 * @version 11/gen/2017
 * @see ABILInputFormController
 */
public class ABILInputForm extends SgsForm
{
	// LABEL WIDGETS
	private LabelWidget labelDipendenza, labelData, labelInterventoUtente, labelHorizontalLine, labelEliminaAbilitazioni,
			labelAggiornaStripeLibretto, labelAbilitaOffLine, labelAggiornaStatino, labelDisabilitaCash, labelSoscScollegaCassa,
			labelRipBancaFilUnificata, labelStasCambioStatoAssegni, labelRiportoDepRisparmio, labelAbilitaRistampa, labelMotivo;

	// COMBO WIDGETS
	private ComboWidget dipendenza;

	// TEXT WIDGETS
	private TextWidget userId, nominativo, motivo;

	// CHECK WIDGETS
	private CheckButtonWidget eliminaAbilitazioni, aggiornaStripeLibretto, abilitaOffLine, aggiornaStatino, disabilitaCash, soscScollegaCassa,
			ripBancaFilUnificata, stasCambioStatoAssegni, riportoDepRisparmio, abilitaRistampa;

	// DATE PICKER WIDGETS
	private DatePickerWidget data;

	// TABLE WIDGETS
	private TableWidget autorizzazioniPerNominativoTable, funzioniDisponibiliTable;

	private static final int VERTICAL_SPACE_BETWEEN_WIDGETS = 15;
	private static final int SPACE_FROM_WIDGET_TO_LABEL = 35;
	private static final int BIG_VERTICAL_SPACE = 20;
	private static final int SMALL_VERTICAL_SPACE = 5;
	private static final int CHECKBUTTON_SEPARATION = 80;

	public ABILInputForm(Composite parent, int style)
	{
		super(parent, style);
		initialize();
	}

	@Override
	protected void initialize()
	{
		super.initialize();
		Composite comp = null;
		comp = createCompositeWithTitle(this, ABILObjectModel.NOME_FUNZIONE + " - " + ABILObjectModel.DESCRIZIONE);

		// org.eclipse.swt.widgets.Button b1 = new
		// org.eclipse.swt.widgets.Button(comp, SWT.MULTI);
		// FormData fd_b1 = new FormData();
		// fd_b1.top = new FormAttachment(50, 20);
		// fd_b1.left = new FormAttachment(50, 50);
		// b1.setLayoutData(fd_b1);
		//
		// org.eclipse.swt.widgets.Button b2 = new
		// org.eclipse.swt.widgets.Button(comp, SWT.MULTI);
		// FormData fd_b2 = new FormData();
		// fd_b2.top = new FormAttachment(b1, 0, SWT.TOP);
		// fd_b2.left = new FormAttachment(b1, 20, SWT.RIGHT);
		// b2.setLayoutData(fd_b2);

		/**
		 * DIPENDENZA
		 */
		labelDipendenza = WidgetUtils.getNewLabelBoldWidget(comp, "DIPENDENZA");
		FormData fd_labelDipendenza = new FormData();
		fd_labelDipendenza.top = new FormAttachment(10, 20);
		fd_labelDipendenza.left = new FormAttachment(0, 50);
		labelDipendenza.setLayoutData(fd_labelDipendenza);

		dipendenza = WidgetUtils.getNewComboWidget(comp, "dipendenza", "Dipendenza", "listaDipendenze", SWT.NONE | SWT.READ_ONLY);
		FormData fd_dipendenza = new FormData(170, SWT.DEFAULT);
		fd_dipendenza.bottom = new FormAttachment(labelDipendenza, 0, SWT.CENTER);
		fd_dipendenza.left = new FormAttachment(labelDipendenza, SPACE_FROM_WIDGET_TO_LABEL, SWT.RIGHT);
		dipendenza.setLayoutData(fd_dipendenza);

		/**
		 * DATA
		 */
		labelData = WidgetUtils.getNewLabelBoldWidget(comp, "DATA");
		FormData fd_labelData = new FormData();
		fd_labelData.bottom = new FormAttachment(dipendenza, 0, SWT.BOTTOM);
		fd_labelData.left = new FormAttachment(dipendenza, 135, SWT.RIGHT);
		labelData.setLayoutData(fd_labelData);

		data = WidgetUtils.getNewDatePicker(comp, "data", "Data odierna");
		FormData fd_data = new FormData();
		fd_data.bottom = new FormAttachment(labelData, 0, SWT.CENTER);
		fd_data.left = new FormAttachment(labelData, SPACE_FROM_WIDGET_TO_LABEL, SWT.RIGHT);
		data.setLayoutData(fd_data);

		/**
		 * AUTORIZZAZIONI PER NOMINATIVO TABLE
		 */
		autorizzazioniPerNominativoTable = new TableWidget(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		autorizzazioniPerNominativoTable.tableOption(true, true, true);
		autorizzazioniPerNominativoTable.getTable().setLinesVisible(true);
		autorizzazioniPerNominativoTable.getTable().setHeaderVisible(true);
		FormData fd_autorizzazioniPerNominativoTable = new FormData();
		fd_autorizzazioniPerNominativoTable.top = new FormAttachment(data, VERTICAL_SPACE_BETWEEN_WIDGETS, SWT.BOTTOM);
		fd_autorizzazioniPerNominativoTable.left = new FormAttachment(labelDipendenza, 0, SWT.LEFT);
		fd_autorizzazioniPerNominativoTable.right = new FormAttachment(data, -46, SWT.RIGHT);
		fd_autorizzazioniPerNominativoTable.bottom = new FormAttachment(50, 0);
		autorizzazioniPerNominativoTable.setLayoutData(fd_autorizzazioniPerNominativoTable);
		autorizzazioniPerNominativoTable.setDataName("listaAutorizzazioniPerNominativo");

		autorizzazioniPerNominativoTable.addColumn(SWT.CENTER, "USER ID", 110, "userID");
		autorizzazioniPerNominativoTable.addColumn(SWT.CENTER, "NOMINATIVO", 220, "nominativo");
		autorizzazioniPerNominativoTable.addColumn(SWT.CENTER, "AUTORIZZAZIONI ATTIVE", 295, "autorizzazioniAttive");

		/**
		 * FUNZIONI DISPONIBILI TABLE
		 */
		funzioniDisponibiliTable = new TableWidget(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		funzioniDisponibiliTable.tableOption(true, true, true);
		funzioniDisponibiliTable.getTable().setLinesVisible(true);
		funzioniDisponibiliTable.getTable().setHeaderVisible(true);
		FormData fd_funzioniDisponibiliTable = new FormData();
		fd_funzioniDisponibiliTable.top = new FormAttachment(autorizzazioniPerNominativoTable, 0, SWT.TOP);
		fd_funzioniDisponibiliTable.left = new FormAttachment(autorizzazioniPerNominativoTable, SPACE_FROM_WIDGET_TO_LABEL, SWT.RIGHT);
		// fd_funzioniDisponibiliTable.right = new FormAttachment(data, -46,
		// SWT.RIGHT);
		fd_funzioniDisponibiliTable.bottom = new FormAttachment(autorizzazioniPerNominativoTable, 0, SWT.BOTTOM);
		funzioniDisponibiliTable.setLayoutData(fd_funzioniDisponibiliTable);
		funzioniDisponibiliTable.setDataName("listaFunzioniDisponibili");

		funzioniDisponibiliTable.addColumn(SWT.CENTER, "AUTORIZZAZIONI", 100, "autorizzazione");

		/**
		 * HORIZONTAL LINE
		 */
		labelHorizontalLine = new LabelWidget(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelHorizontalLine.setBackground(WidgetUtils.COLOR_BLACK);
		FormData fd_labelHorizontalLine = new FormData();
		fd_labelHorizontalLine.top = new FormAttachment(autorizzazioniPerNominativoTable, VERTICAL_SPACE_BETWEEN_WIDGETS, SWT.BOTTOM);
		fd_labelHorizontalLine.left = new FormAttachment(autorizzazioniPerNominativoTable, 0, SWT.LEFT);
		fd_labelHorizontalLine.right = new FormAttachment(autorizzazioniPerNominativoTable, 0, SWT.RIGHT);
		labelHorizontalLine.setLayoutData(fd_labelHorizontalLine);

		/**
		 * LABEL INTERVENTO UTENTE
		 */
		labelInterventoUtente = WidgetUtils.getNewLabelBoldWidget(comp, "Intervento sull'utente");
		labelInterventoUtente.getLabel().setForeground(WidgetUtils.COLOR_RED);
		FormData fd_labelInterventoUtente = new FormData();
		fd_labelInterventoUtente.top = new FormAttachment(labelHorizontalLine, BIG_VERTICAL_SPACE, SWT.BOTTOM);
		fd_labelInterventoUtente.left = new FormAttachment(labelHorizontalLine, 0, SWT.LEFT);
		labelInterventoUtente.setLayoutData(fd_labelInterventoUtente);

		/**
		 * USER ID SELEZIONATO
		 */
		userId = WidgetUtils.getNewTextWidget(comp, 12, "userId", "User ID selezionato", InputPatterns.ALPHANUMERIC);
		FormData fd_userId = new FormData(COMPOSITE_SIZE_12, SWT.DEFAULT);
		fd_userId.top = new FormAttachment(labelInterventoUtente, VERTICAL_SPACE_BETWEEN_WIDGETS, SWT.BOTTOM);
		fd_userId.left = new FormAttachment(labelInterventoUtente, 0, SWT.LEFT);
		userId.setLayoutData(fd_userId);
		userId.setCopyPasteManagement(TextWidget.MERGED_TEXT); // NEcessario per
																// Copia e
																// Incolla
																// parziali

		/**
		 * NOMINATIVO SELEZIONATO
		 */
		nominativo = WidgetUtils.getNewTextWidget(comp, 20, "nominativo", "Nominativo selezionato", InputPatterns.ALPHANUMERIC);
		FormData fd_nominativo = new FormData(COMPOSITE_SIZE_20, SWT.DEFAULT);
		fd_nominativo.bottom = new FormAttachment(userId, 0, SWT.BOTTOM);
		fd_nominativo.left = new FormAttachment(userId, 15, SWT.RIGHT);
		nominativo.setLayoutData(fd_nominativo);
		nominativo.setCopyPasteManagement(TextWidget.MERGED_TEXT); // NEcessario
																	// per Copia
																	// e Incolla
																	// parziali

		/**
		 * CHECKBUTTON AGGIORNA STRIPE LIBRETTO
		 */
		aggiornaStripeLibretto = new CheckButtonWidget(comp, SWT.NONE);
		aggiornaStripeLibretto.setDataName("aggiornaStripeLibretto");
		aggiornaStripeLibretto.setText("AGGIORNA STRIPE LIBRETTO");
		aggiornaStripeLibretto.getControl().setAlignment(SWT.LEFT);
		FormData fd_aggiornaStripeLibretto = new FormData();
		fd_aggiornaStripeLibretto.top = new FormAttachment(userId, VERTICAL_SPACE_BETWEEN_WIDGETS, SWT.BOTTOM);
		fd_aggiornaStripeLibretto.left = new FormAttachment(userId, 0, SWT.LEFT);
		aggiornaStripeLibretto.setLayoutData(fd_aggiornaStripeLibretto);

		/**
		 * CHECKBUTTON ABILITA OFF-LINE
		 */
		abilitaOffLine = new CheckButtonWidget(comp, SWT.NONE);
		abilitaOffLine.setDataName("abilitaOffLine");
		abilitaOffLine.setText("ABILITA OFF-LINE");
		abilitaOffLine.getControl().setAlignment(SWT.LEFT);
		FormData fd_abilitaOffLine = new FormData();
		fd_abilitaOffLine.bottom = new FormAttachment(aggiornaStripeLibretto, 0, SWT.BOTTOM);
		fd_abilitaOffLine.left = new FormAttachment(aggiornaStripeLibretto, CHECKBUTTON_SEPARATION, SWT.RIGHT);
		abilitaOffLine.setLayoutData(fd_abilitaOffLine);

		/**
		 * CHECKBUTTON AGGIORNA STATINO
		 */
		aggiornaStatino = new CheckButtonWidget(comp, SWT.NONE);
		aggiornaStatino.setDataName("aggiornaStatino");
		aggiornaStatino.setText("AGGIORNA STATINO");
		aggiornaStatino.getControl().setAlignment(SWT.LEFT);
		FormData fd_aggiornaStatino = new FormData();
		fd_aggiornaStatino.bottom = new FormAttachment(abilitaOffLine, 0, SWT.BOTTOM);
		fd_aggiornaStatino.left = new FormAttachment(abilitaOffLine, CHECKBUTTON_SEPARATION, SWT.RIGHT);
		aggiornaStatino.setLayoutData(fd_aggiornaStatino);

		/**
		 * CHECKBUTTON DISABILITA CASH
		 */
		disabilitaCash = new CheckButtonWidget(comp, SWT.NONE);
		disabilitaCash.setDataName("disabilitaCash");
		disabilitaCash.setText("DISABILITA CASH");
		disabilitaCash.getControl().setAlignment(SWT.LEFT);
		FormData fd_disabilitaCash = new FormData();
		fd_disabilitaCash.top = new FormAttachment(aggiornaStripeLibretto, VERTICAL_SPACE_BETWEEN_WIDGETS, SWT.BOTTOM);
		fd_disabilitaCash.left = new FormAttachment(aggiornaStripeLibretto, 0, SWT.LEFT);
		disabilitaCash.setLayoutData(fd_disabilitaCash);

		/**
		 * CHECKBUTTON SOSC-SCOLLEGA CASSA
		 */
		soscScollegaCassa = new CheckButtonWidget(comp, SWT.NONE);
		soscScollegaCassa.setDataName("soscScollegaCassa");
		soscScollegaCassa.setText("SOSC-SCOLLEGA CASSA");
		soscScollegaCassa.getControl().setAlignment(SWT.LEFT);
		FormData fd_soscScollegaCassa = new FormData();
		fd_soscScollegaCassa.bottom = new FormAttachment(disabilitaCash, 0, SWT.BOTTOM);
		fd_soscScollegaCassa.left = new FormAttachment(abilitaOffLine, 0, SWT.LEFT);
		soscScollegaCassa.setLayoutData(fd_soscScollegaCassa);

		/**
		 * CHECKBUTTON RIP.BANCA-FIL.UNIFICATA
		 */
		ripBancaFilUnificata = new CheckButtonWidget(comp, SWT.NONE);
		ripBancaFilUnificata.setDataName("ripBancaFilUnificata");
		ripBancaFilUnificata.setText("RIP.BANCA-FIL.UNIFICATA");
		ripBancaFilUnificata.getControl().setAlignment(SWT.LEFT);
		FormData fd_ripBancaFilUnificata = new FormData();
		fd_ripBancaFilUnificata.bottom = new FormAttachment(soscScollegaCassa, 0, SWT.BOTTOM);
		fd_ripBancaFilUnificata.left = new FormAttachment(aggiornaStatino, 0, SWT.LEFT);
		ripBancaFilUnificata.setLayoutData(fd_ripBancaFilUnificata);

		/**
		 * CHECKBUTTON STAS CAMBIO STATO ASSEGNI
		 */
		stasCambioStatoAssegni = new CheckButtonWidget(comp, SWT.NONE);
		stasCambioStatoAssegni.setDataName("stasCambioStatoAssegni");
		stasCambioStatoAssegni.setText("STAS CAMBIO STATO ASSEGNI");
		stasCambioStatoAssegni.getControl().setAlignment(SWT.LEFT);
		FormData fd_stasCambioStatoAssegni = new FormData();
		fd_stasCambioStatoAssegni.top = new FormAttachment(disabilitaCash, VERTICAL_SPACE_BETWEEN_WIDGETS, SWT.BOTTOM);
		fd_stasCambioStatoAssegni.left = new FormAttachment(disabilitaCash, 0, SWT.LEFT);
		stasCambioStatoAssegni.setLayoutData(fd_stasCambioStatoAssegni);

		/**
		 * CHECKBUTTON RIPORTO DEP. A RISPARMIO
		 */
		riportoDepRisparmio = new CheckButtonWidget(comp, SWT.NONE);
		riportoDepRisparmio.setDataName("riportoDepRisparmio");
		riportoDepRisparmio.setText("RIPORTO DEP. A RISPARMIO");
		riportoDepRisparmio.getControl().setAlignment(SWT.LEFT);
		FormData fd_riportoDepRisparmio = new FormData();
		fd_riportoDepRisparmio.bottom = new FormAttachment(stasCambioStatoAssegni, 0, SWT.BOTTOM);
		fd_riportoDepRisparmio.left = new FormAttachment(soscScollegaCassa, 0, SWT.LEFT);
		riportoDepRisparmio.setLayoutData(fd_riportoDepRisparmio);

		/**
		 * CHECKBUTTON ABILITA RISTAMPA
		 */
		abilitaRistampa = new CheckButtonWidget(comp, SWT.NONE);
		abilitaRistampa.setDataName("abilitaRistampa");
		abilitaRistampa.setText("ABILITA RISTAMPA");
		abilitaRistampa.getControl().setAlignment(SWT.LEFT);
		FormData fd_abilitaRistampa = new FormData();
		fd_abilitaRistampa.bottom = new FormAttachment(riportoDepRisparmio, 0, SWT.BOTTOM);
		fd_abilitaRistampa.left = new FormAttachment(ripBancaFilUnificata, 0, SWT.LEFT);
		abilitaRistampa.setLayoutData(fd_abilitaRistampa);

		/**
		 * CHECKBUTTON ELIMINA ABILITAZIONI
		 */
		eliminaAbilitazioni = new CheckButtonWidget(comp, SWT.NONE);
		eliminaAbilitazioni.setDataName("eliminaAbilitazioni");
		eliminaAbilitazioni.setText("ELIMINA TUTTE LE ABILITAZIONI");
		eliminaAbilitazioni.getControl().setAlignment(SWT.LEFT);
		eliminaAbilitazioni.setForeground(WidgetUtils.COLOR_BLUE);
		FormData fd_eliminaAbilitazioni = new FormData();
		fd_eliminaAbilitazioni.bottom = new FormAttachment(nominativo, 0, SWT.CENTER);
		fd_eliminaAbilitazioni.left = new FormAttachment(aggiornaStatino, 0, SWT.LEFT);
		eliminaAbilitazioni.setLayoutData(fd_eliminaAbilitazioni);

		/**
		 * MOTIVO
		 */
		labelMotivo = WidgetUtils.getNewLabelBoldWidget(comp, "MOTIVO");
		FormData fd_labelMotivo = new FormData();
		fd_labelMotivo.top = new FormAttachment(stasCambioStatoAssegni, BIG_VERTICAL_SPACE, SWT.BOTTOM);
		fd_labelMotivo.left = new FormAttachment(stasCambioStatoAssegni, 0, SWT.LEFT);
		labelMotivo.setLayoutData(fd_labelMotivo);

		motivo = WidgetUtils.getNewTextWidget(comp, 24, "motivo", "Motivo delle abilitazioni/disabilitazioni", InputPatterns.ALPHANUMERIC_SPACES);
		FormData fd_motivo = new FormData(COMPOSITE_SIZE_24, SWT.DEFAULT);
		fd_motivo.bottom = new FormAttachment(labelMotivo, 0, SWT.CENTER);
		fd_motivo.left = new FormAttachment(labelMotivo, SPACE_FROM_WIDGET_TO_LABEL, SWT.RIGHT);
		motivo.setLayoutData(fd_motivo);
		motivo.setCopyPasteManagement(TextWidget.MERGED_TEXT); // NEcessario per
																// Copia e
																// Incolla
																// parziali

		/**
		 * BOTTONI CONFERMA ED ANNULLA
		 */
		createButtonsAndPromptWidgetForInputForm(comp);

	}

	public LabelWidget getLabelDipendenza()
	{
		return labelDipendenza;
	}

	public LabelWidget getLabelData()
	{
		return labelData;
	}

	public LabelWidget getLabelInterventoUtente()
	{
		return labelInterventoUtente;
	}

	public LabelWidget getLabelHorizontalLine()
	{
		return labelHorizontalLine;
	}

	public LabelWidget getLabelEliminaAbilitazioni()
	{
		return labelEliminaAbilitazioni;
	}

	public LabelWidget getLabelAggiornaStripeLibretto()
	{
		return labelAggiornaStripeLibretto;
	}

	public LabelWidget getLabelAbilitaOffLine()
	{
		return labelAbilitaOffLine;
	}

	public LabelWidget getLabelAggiornaStatino()
	{
		return labelAggiornaStatino;
	}

	public LabelWidget getLabelDisabilitaCash()
	{
		return labelDisabilitaCash;
	}

	public LabelWidget getLabelSoscScollegaCassa()
	{
		return labelSoscScollegaCassa;
	}

	public LabelWidget getLabelRipBancaFilUnificata()
	{
		return labelRipBancaFilUnificata;
	}

	public LabelWidget getLabelStasCambioStatoAssegni()
	{
		return labelStasCambioStatoAssegni;
	}

	public LabelWidget getLabelRiportoDepRisparmio()
	{
		return labelRiportoDepRisparmio;
	}

	public LabelWidget getLabelAbilitaRistampa()
	{
		return labelAbilitaRistampa;
	}

	public LabelWidget getLabelMotivo()
	{
		return labelMotivo;
	}

	public ComboWidget getDipendenza()
	{
		return dipendenza;
	}

	public TextWidget getUserId()
	{
		return userId;
	}

	public TextWidget getNominativo()
	{
		return nominativo;
	}

	public TextWidget getMotivo()
	{
		return motivo;
	}

	public CheckButtonWidget getEliminaAbilitazioni()
	{
		return eliminaAbilitazioni;
	}

	public CheckButtonWidget getAggiornaStripeLibretto()
	{
		return aggiornaStripeLibretto;
	}

	public CheckButtonWidget getAbilitaOffLine()
	{
		return abilitaOffLine;
	}

	public CheckButtonWidget getAggiornaStatino()
	{
		return aggiornaStatino;
	}

	public CheckButtonWidget getDisabilitaCash()
	{
		return disabilitaCash;
	}

	public CheckButtonWidget getSoscScollegaCassa()
	{
		return soscScollegaCassa;
	}

	public CheckButtonWidget getRipBancaFilUnificata()
	{
		return ripBancaFilUnificata;
	}

	public CheckButtonWidget getStasCambioStatoAssegni()
	{
		return stasCambioStatoAssegni;
	}

	public CheckButtonWidget getRiportoDepRisparmio()
	{
		return riportoDepRisparmio;
	}

	public CheckButtonWidget getAbilitaRistampa()
	{
		return abilitaRistampa;
	}

	@Override
	public DatePickerWidget getData()
	{
		return data;
	}

	public TableWidget getAutorizzazioniPerNominativoTable()
	{
		return autorizzazioniPerNominativoTable;
	}

	public void setDipendenza(ComboWidget dipendenza)
	{
		this.dipendenza = dipendenza;
	}

	public void setUserId(TextWidget userId)
	{
		this.userId = userId;
	}

	public void setNominativo(TextWidget nominativo)
	{
		this.nominativo = nominativo;
	}

	public void setMotivo(TextWidget motivo)
	{
		this.motivo = motivo;
	}

	public void setEliminaAbilitazioni(CheckButtonWidget eliminaAbilitazioni)
	{
		this.eliminaAbilitazioni = eliminaAbilitazioni;
	}

	public void setAggiornaStripeLibretto(CheckButtonWidget aggiornaStripeLibretto)
	{
		this.aggiornaStripeLibretto = aggiornaStripeLibretto;
	}

	public void setAbilitaOffLine(CheckButtonWidget abilitaOffLine)
	{
		this.abilitaOffLine = abilitaOffLine;
	}

	public void setAggiornaStatino(CheckButtonWidget aggiornaStatino)
	{
		this.aggiornaStatino = aggiornaStatino;
	}

	public void setDisabilitaCash(CheckButtonWidget disabilitaCash)
	{
		this.disabilitaCash = disabilitaCash;
	}

	public void setSoscScollegaCassa(CheckButtonWidget soscScollegaCassa)
	{
		this.soscScollegaCassa = soscScollegaCassa;
	}

	public void setRipBancaFilUnificata(CheckButtonWidget ripBancaFilUnificata)
	{
		this.ripBancaFilUnificata = ripBancaFilUnificata;
	}

	public void setStasCambioStatoAssegni(CheckButtonWidget stasCambioStatoAssegni)
	{
		this.stasCambioStatoAssegni = stasCambioStatoAssegni;
	}

	public void setRiportoDepRisparmio(CheckButtonWidget riportoDepRisparmio)
	{
		this.riportoDepRisparmio = riportoDepRisparmio;
	}

	public void setAbilitaRistampa(CheckButtonWidget abilitaRistampa)
	{
		this.abilitaRistampa = abilitaRistampa;
	}

	public void setData(DatePickerWidget data)
	{
		this.data = data;
	}

	public void setAutorizzazioniPerNominativoTable(TableWidget autorizzazioniPerNominativoTable)
	{
		this.autorizzazioniPerNominativoTable = autorizzazioniPerNominativoTable;
	}

	public TableWidget getFunzioniDisponibiliTable()
	{
		return funzioniDisponibiliTable;
	}

}
