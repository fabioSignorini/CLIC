package it.sgsbp.rcp.abil.impl;

import it.ibm.eric.framework.FrameworkPlugin;
import it.ibm.eric.framework.controller.form.Form;
import it.ibm.eric.framework.logging.IMCPLogger;
import it.ibm.eric.ui.widgets.AbstractWidget;
import it.ibm.eric.ui.widgets.combo.ComboWidget;
import it.ibm.eric.ui.widgets.combo.KeyedItem;
import it.ibm.eric.ui.widgets.table.TableWidget;
import it.ibm.eric.ui.widgets.text.TextWidget;
import it.sgsbp.rcp.abil.model.ABILObjectModel;
import it.sgsbp.rcp.common.constants.ClicConstants;
import it.sgsbp.rcp.common.controller.SgsFormController;
import it.sgsbp.rcp.common.ui.CICSReturnCodeHandlerTemplate;
import it.sgsbp.rcp.common.ui.listener.ListenerUtil;
import it.sgsbp.rcp.common.ui.listener.MethodInvokingCallback;
import it.sgsbp.rcp.qc.impl.QcFormController;
import it.sgsbp.tpSpo.bean.inout.BaseInput;
import it.sgsbp.tpSpo.bean.inout.BaseOutput;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

/**
 * Definition of the input form controller. This component is responsible for
 * instantiating the input form and for manage all the related events, in
 * particular:
 * <ul>
 * <li>setting the focus and the initial default values in the input text widget
 * if necessary</li>
 * <li>register the events associated both with the displayed buttons (ESCI,
 * INVIA) and with the sensible key (TAB, ENTER)</li>
 * <li>setting the collected input values in the common container for later use
 * among components</li>
 * <li>manage the transition fired from the ESCI and the INVIA buttons</li>
 * <li>perform all the necessary sanity checks on the input values in order to
 * avoid incorrect behaviors</li>
 * <li>checking the outcome coming from the service call and firing a transition
 * towards the output visualization if correct, displaying an error message
 * otherwise</li>
 * </ul>
 * 
 * @author Fabio Signorini - EST1571
 * @version 11/gen/2017
 * @see ABILInputForm
 * @see ABILObjectModel
 */
public class ABILInputFormController extends SgsFormController
{
	private ABILInputForm form = null;
	private ABILObjectModel model = null;
	private static final IMCPLogger LOGGER = FrameworkPlugin.getDefault().getLogManager();
	private final String[] warningButtons = new String[]
	{ "Annulla", "Ok" };

	@Override
	protected Form createForm()
	{
		form = new ABILInputForm(parent, SWT.NONE);
		return form;
	}

	@Override
	protected void initializeContext()
	{
		model = (ABILObjectModel) getObjectModel();
		super.initializeContext();
	}

	@Override
	protected void initializeForm()
	{
		try
		{
			form.getData().setDataValue(ClicConstants.FORMATTER_DDMMYYYY.parse(sessioneOperatore.getDataSolare()));
		}
		catch (Exception e)
		{
			logger.error(IMCPLogger.RCP, e);
		}
		form.getData().setEditable(false);
		form.getData().setEnabled(false);
		form.getUserId().setDataValue("");
		form.getUserId().setEditable(false);
		form.getUserId().setEnabled(false);
		form.getNominativo().setDataValue("");
		form.getNominativo().setEditable(false);
		form.getNominativo().setEnabled(false);
		form.getMotivo().setDataValue("");
		form.getMotivo().setEditable(true);
		form.getMotivo().setEnabled(true);
		form.getDipendenza().getCombo().setFocus();

		form.getEliminaAbilitazioni().setDataValue(false);
		form.getAggiornaStripeLibretto().setDataValue(false);
		form.getAbilitaOffLine().setDataValue(false);
		form.getAggiornaStatino().setDataValue(false);
		form.getDisabilitaCash().setDataValue(false);
		form.getSoscScollegaCassa().setDataValue(false);
		form.getRipBancaFilUnificata().setDataValue(false);
		form.getStasCambioStatoAssegni().setDataValue(false);
		form.getRiportoDepRisparmio().setDataValue(false);
		form.getAbilitaRistampa().setDataValue(false);

		// SETTAGGIO DELLE DIPENDENZE INIZIALI
		callGetDipendenzeService(form.getDipendenza());

		// final AutorizzazioniPerNominativo arrayAutorizzazioniPerNominativo[]
		// = new AutorizzazioniPerNominativo[]
		// { new AutorizzazioniPerNominativo("BP00002", "ROSSI CARLO", ""), new
		// AutorizzazioniPerNominativo("H090001", "ROSSI PAOLO", ""),
		// new AutorizzazioniPerNominativo("TEST001", "TEST001", "") };
		// List<AutorizzazioniPerNominativo> listaAutorizzazioniPerNominativo =
		// Arrays.asList(arrayAutorizzazioniPerNominativo);
		// model.setListaAutorizzazioniPerNominativo(listaAutorizzazioniPerNominativo);
		// setIntoForm();

		// try
		// {
		// CausaleFactory causaleFactory = CausaleCommonService.getFactory();
		// model.getMapCausali().putAll(causaleFactory.getMappaCausali());
		// }
		// catch (Exception e)
		// {
		// LOGGER.error(MCPLogger.RCP,
		// "Errore nel recupero della mappa delle causali", e);
		// e.printStackTrace();
		// }
		// form.getContoCorrente().getControl().setFocus();
		// form.getStampaContabile().setEnabled(false);
		super.initializeForm();
	}

	@Override
	protected void processEvent(SelectionEvent e)
	{
		if (e.widget == form.getEsci())
		{
			notifyProperty(ABILTransitions.TRANS_STATE_GOTO_FINAL);
			return;
		}
		if (e.widget == form.getInvia())
		{
			if (validateInput())
			{
				setDataModel();
				model.setCurrentFunction(ABILObjectModel.NOME_FUNZIONE);
				notifyProperty(ABILTransitions.TRANS_STATE_GOTO_SERVICECALL);
				return;
			}
		}
	}

	// controlla changevalue su textwidget se utile
	@Override
	public void refreshForm()
	{
		new CICSReturnCodeHandlerTemplate<BaseOutput>(model.getBaseOutput(), false)
		{
			@Override
			protected void doHandleReturnCodeOK()
			{
				super.doHandleReturnCodeOK();
				if (model.getCurrentFunction().equals(BaseInput.FUNZIONE_RAPPORTI_DATI))
				{
				}
			};

			@Override
			protected void doHandleReturnCodeKOBloccante()
			{
				super.doHandleReturnCodeKOBloccante();
				if (model.getCurrentFunction().equals(BaseInput.FUNZIONE_RAPPORTI_DATI))
				{
				}
			};

		}.handle();
	}

	@Override
	protected void registerFormEvents(SelectionListener formListener)
	{
		registerAction("F9.unique.id", gotoServiceCallAction);
		registerAction("ESC.unique.id", ESCAction);
		form.getEsci().addSelectionListener(this);
		form.getInvia().addSelectionListener(this);

		form.getAutorizzazioniPerNominativoTable().getTable().addListener(SWT.Selection, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				TableItem[] selectedRow = form.getAutorizzazioniPerNominativoTable().getTable().getSelection();
				if (selectedRow == null || selectedRow.length == 0)
				{
					return;
				}
				int index = form.getAutorizzazioniPerNominativoTable().getTable().getSelectionIndex();
				if (index >= 0)
				{
					form.getAutorizzazioniPerNominativoTable().getTable().setSelection(index);
					form.getAutorizzazioniPerNominativoTable().getTable().setFocus();
					String userID = selectedRow[0].getText(1);
					String nominativo = selectedRow[0].getText(2);
					// AutorizzazioniPerNominativo aut =
					// (AutorizzazioniPerNominativo)
					// selectedRow[0].getData("BEAN");
					form.getUserId().setDataValue((userID != null ? userID : "").trim());
					form.getNominativo().setDataValue((nominativo != null ? nominativo : "").trim());
				}
			}
		});

		form.getDipendenza().getCombo().addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// SETTAGGIO DEI NOMINATIVI
				callFillNominativiTable(form.getAutorizzazioniPerNominativoTable(), form.getDipendenza());
				form.getUserId().setDataValue("");
				form.getNominativo().setDataValue("");

				// SETTAGGIO DELLE AUTORIZZAZIONI

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				;
			}
		});

		// // N.C/CORRENTE -> COD.CAUS.
		// setTraverseListener(form.getContoCorrente(),
		// form.getCodiceCausale());
		// setKeyListner(form.getContoCorrente(), form.getCodiceCausale());
		//
		// // COD.CAUS. -> IMPORTO
		// setTraverseListenerCausale(form.getCodiceCausale());
		// setKeyListnerCausale(form.getCodiceCausale());
		// form.getCodiceCausale().getControl().addKeyListener(createKeyListenerF11());
		// setKeyListnerAltF2(form.getCodiceCausale());
		//
		// // IMPORTO -> STAMPA CONTABILE
		// setTraverseListener(form.getImporto(), form.getStampaContabile());
		// setKeyListner(form.getImporto(), form.getStampaContabile());
		//
		// // STAMPA CONTABILE -> DATA DI SCADENZA
		// setTraverseListener(form.getStampaContabile(),
		// form.getDataScadenza());
		// setKeyListner(form.getStampaContabile(), form.getDataScadenza());
		//
		// // DATA DI SCADENZA -> DESCRIZIONE
		// setTraverseListener(form.getDataScadenza(), form.getDescrizione());
		// setKeyListner(form.getDataScadenza(), form.getDescrizione());
		//
		// // DESCRIZIONE -> INVIA
		// setTraverseListener(form.getDescrizione(), form.getInvia());
		// setKeyListner(form.getDescrizione(), form.getInvia());
		//
		// // ESC
		// form.getDataScadenza().getControl().addKeyListener(createKeyListenerEscape(PRENTransitions.TRANS_STATE_GOTO_FINAL));
		// form.getContoCorrente().getControl().addKeyListener(createKeyListenerEscape(PRENTransitions.TRANS_STATE_GOTO_FINAL));
		// form.getCodiceCausale().getControl().addKeyListener(createKeyListenerEscape(PRENTransitions.TRANS_STATE_GOTO_FINAL));
		// form.getStampaContabile().getControl().addKeyListener(createKeyListenerEscape(PRENTransitions.TRANS_STATE_GOTO_FINAL));
		// form.getImporto().getControl().addKeyListener(createKeyListenerEscape(PRENTransitions.TRANS_STATE_GOTO_FINAL));
		// form.getDescrizione().getControl().addKeyListener(createKeyListenerEscape(PRENTransitions.TRANS_STATE_GOTO_FINAL));
	}

	private void setTraverseListener(final TextWidget textWidget, final Control focusWidget)
	{
		textWidget.getControl().addTraverseListener(new TraverseListener()
		{
			@Override
			public void keyTraversed(TraverseEvent e)
			{
				if (e.detail == SWT.TRAVERSE_TAB_NEXT)
				{
					e.doit = false;
					if (validateWidgetValue(textWidget))
					{
						focusWidget.setFocus();
					}
				}
			}
		});
	}

	private void setKeyListner(final TextWidget textWidget, final Control focusWidget)
	{
		textWidget.getControl().addKeyListener(ListenerUtil.createNewKeyListenerForEnter(new MethodInvokingCallback()
		{
			@Override
			public void invoke(TypedEvent event)
			{
				if (validateWidgetValue(textWidget))
				{
					focusWidget.setFocus();
				}
			}
		}));
	}

	private boolean validateWidgetValue(AbstractWidget widget)
	{
		boolean isValid = false;
		// if (widget.getDataName().equals("contoCorrente"))
		// {
		// isValid = validateContoCorrente((TextWidget) widget);
		// }
		// else if (widget.getDataName().equals("codiceCausale"))
		// {
		// if (model.getSegnoCausali() == null ||
		// model.getSegnoCausali().equals(SegnoCausaliDeposito.POS))
		// {
		// isValid = validateNotNullOrEmptyWidget(form.getPromptWidget(),
		// (TextWidget) widget) && validateCausale1POS();
		// }
		// else
		// {
		// isValid = validateNotNullOrEmptyWidget(form.getPromptWidget(),
		// (TextWidget) widget) && validateCausale1NEG();
		// }
		// }
		// else if (widget.getDataName().equals("importo"))
		// {
		// isValid = validateImporto((AmountWidget) widget,
		// form.getCodiceCausale());
		// }
		// else if (widget.getDataName().equals("stampaContabile"))
		// {
		// isValid = validateStampaContabile((TextWidget) widget);
		// }
		// else if (widget.getDataName().equals("dataScadenza"))
		// {
		// isValid = true;
		// }
		// else if (widget.getDataName().equals("descMovimento"))
		// {
		// isValid = true;
		// }
		// else
		// {
		// isValid = false;
		// }
		return isValid;
	}

	private KeyAdapter createKeyListenerEscape(final String transitionToBind)
	{
		return ListenerUtil.createNewKeyListenerForEscape(new MethodInvokingCallback()
		{
			@Override
			public void invoke(TypedEvent event)
			{
				notifyProperty(transitionToBind);
			}
		});
	}

	private final Action ESCAction = new Action("Exit")
	{
		@Override
		public void run()
		{
			QcFormController.getInstance().setEscTrx(true);
			notifyProperty(ABILTransitions.TRANS_STATE_GOTO_FINAL);
		}
	};

	private final Action gotoServiceCallAction = new Action("CallServiceF9")
	{
		@Override
		public void run()
		{
			if (validateInput())
			{
				setDataModel();
				QcFormController.getInstance().setEscTrx(true);
				model.setCurrentFunction(ABILObjectModel.NOME_FUNZIONE);
				notifyProperty(ABILTransitions.TRANS_STATE_GOTO_SERVICECALL);
			}
		}
	};

	private boolean validateInput()
	{
		boolean isInputValid = false;
		// return validateWidgetValue(form.getContoCorrente()) &&
		// validateWidgetValue(form.getCodiceCausale()) &&
		// validateWidgetValue(form.getImporto())
		// && validateWidgetValue(form.getStampaContabile()) &&
		// validateWidgetValue(form.getDataScadenza())
		// && validateWidgetValue(form.getDescrizione());

		return isInputValid;
	}

	private boolean validateContoCorrente(TextWidget numCCorrente)
	{
		boolean isValid = true;
		return isValid;
	}

	private boolean validateStampaContabile(TextWidget stampaContabileWidget)
	{
		boolean isValid = false;
		return isValid;
	}

	private void setDataModel()
	{
		// String contoCorrente = (String)
		// form.getContoCorrente().getDataValue();
		// String codiceCausale = (String)
		// form.getCodiceCausale().getDataValue();
		// BigDecimal importo = (BigDecimal) form.getImporto().getDataValue();
		// String stampaContabile = (String)
		// form.getStampaContabile().getDataValue();
		// Date dataScadenza = (Date) form.getDataScadenza().getDataValue();
		// String descMovimento = (String) form.getDescrizione().getDataValue();
		//
		// model.setContoCorrente(contoCorrente);
		// model.setCodiceCausale(codiceCausale);
		// model.setImporto(importo);
		// model.setStampaContabile(stampaContabile);
		// model.setDataScadenza(dataScadenza);
		// model.setDescMovimento(descMovimento);
	}

	@Override
	protected void removeFormEvents(SelectionListener formListener)
	{
		form.getInvia().removeSelectionListener(this);
		form.getEsci().removeSelectionListener(this);
		super.removeFormEvents(formListener);
	}

	private void callGetDipendenzeService(ComboWidget dipendenzeCombo)
	{
		final String arrayDipendenze[] = new String[]
		{ "", "HO9236", "HO9004" };
		ArrayList<KeyedItem> listaDipendenze = new ArrayList<KeyedItem>();
		for (String dipendenza : arrayDipendenze)
		{
			listaDipendenze.add(new KeyedItem("", dipendenza));
		}
		if (!listaDipendenze.isEmpty())
		{
			dipendenzeCombo.setItems(listaDipendenze);
			dipendenzeCombo.getCombo().select(0);
		}
		model.setListaDipendenze(listaDipendenze);
	}

	private void callFillNominativiTable(TableWidget autorizzazioniPerNominativoTableWidget, ComboWidget dipendenzaComboWidget)
	{
		int rows;
		if (dipendenzaComboWidget.getCombo().getText().equals("HO9236"))
		{
			autorizzazioniPerNominativoTableWidget.getTable().removeAll();
			rows = 5;
			for (int i = 1; i <= rows; i++)
			{
				TableItem item = new TableItem(autorizzazioniPerNominativoTableWidget.getTable(), SWT.NONE);

				TableEditor editor = new TableEditor(autorizzazioniPerNominativoTableWidget.getTable());
				Button b = new Button(autorizzazioniPerNominativoTableWidget.getTable(), SWT.MULTI);
				b.setText("CCombo");
				editor.grabHorizontal = true;
				editor.setEditor(b, item, 1);

				// TableItem item = new
				// TableItem(autorizzazioniPerNominativoTableWidget.getTable(),
				// SWT.NONE);
				// item.setText(1, "BP0000" + String.valueOf(i));
				// item.setText(2, "ROSSI CARLO" + "_" + String.valueOf(i));
				// item.setText(3, "rwx-" + String.valueOf(i));
			}
		}
		else if (dipendenzaComboWidget.getCombo().getText().equals("HO9004"))
		{
			autorizzazioniPerNominativoTableWidget.getTable().removeAll();
			rows = 3;
			for (int i = 1; i <= rows; i++)
			{
				TableItem item = new TableItem(autorizzazioniPerNominativoTableWidget.getTable(), SWT.NONE);
				item.setText(1, "SG0000" + String.valueOf(i));
				item.setText(2, "ROSSI PAOLO" + "_" + String.valueOf(i));
				item.setText(3, "rwx-" + String.valueOf(i));
			}
		}
		else
		{
			autorizzazioniPerNominativoTableWidget.getTable().removeAll();
		}
		// model.setListaAutorizzazioniPerNominativo(listaAutorizzazioniPerNominativo);

		// if (QcUtils.isFocusInForm())
		// {
		// notifyProperty(MOVCTransitions.TRANS_STATE_GOTO_SERVICE_GETCALL_INDR);
		// }
	}

	// ****************************************
	// ********** INTERNAL UTILS **************
	// ****************************************

}
