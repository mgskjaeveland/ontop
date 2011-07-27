/***
 * Copyright (c) 2008, Mariano Rodriguez-Muro. All rights reserved.
 * 
 * The OBDA-API is licensed under the terms of the Lesser General Public License
 * v.3 (see OBDAAPI_LICENSE.txt for details). The components of this work
 * include:
 * 
 * a) The OBDA-API developed by the author and licensed under the LGPL; and, b)
 * third-party components licensed under terms that may be different from those
 * of the LGPL. Information about such licenses can be found in the file named
 * OBDAAPI_3DPARTY-LICENSES.txt.
 */

package it.unibz.krdb.obda.gui.swing.panel;

import it.unibz.krdb.obda.gui.swing.OBDADataQueryAction;
import it.unibz.krdb.obda.gui.swing.utils.DialogUtils;
import it.unibz.krdb.obda.gui.swing.utils.SPARQLQueryStyledDocument;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.queryanswering.QueryController;
import it.unibz.krdb.obda.utils.OBDAPreferenceChangeListener;
import it.unibz.krdb.obda.utils.OBDAPreferences;

import java.awt.Color;
import java.awt.Font;
import java.net.URI;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.text.StyleContext;

// import edu.stanford.smi.protege.resource.Icons;
// import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

/**
 * 
 * Creates a new panel to execute queries. Remember to execute the
 * setResultsPanel function to indicate where to display the results.
 * 
 * Remember to execute all setXActions when creating this objects
 * 
 * @author mariano
 */
public class QueryInterfacePanel extends javax.swing.JPanel implements SavedQueriesPanelListener, TableModelListener,
		OBDAPreferenceChangeListener {

	/**
	 * Variable currentGroup is the group's id to which belongs the selected
	 * query Variable currentId is the query's id that is selected
	 */
	private static final long									serialVersionUID			= -5902798157183352944L;
	private ResultViewTablePanel								viewpanel;
	private SPARQLQueryStyledDocument							_styled_doc					= null;
	private it.unibz.krdb.obda.gui.swing.OBDADataQueryAction	executeUCQAction			= null;
	private OBDADataQueryAction									executeEQLAction			= null;
	private OBDADataQueryAction									retrieveUCQExpansionAction	= null;
	private OBDADataQueryAction									retrieveUCQUnfoldingAction	= null;
	private OBDADataQueryAction									retrieveEQLUnfoldingAction	= null;
	// private GetDefaultSPARQLPrefixAction prefixAction = null;
	private QueryController										qc							= null;
	private QueryInterfacePanel									instance					= null;
	private double												execTime					= 0;
	private String												currentGroup				= null;
	private String												currentId					= null;
	private OBDAModel											apic						= null;
	private URI													baseuri						= null;

	/** Creates new form QueryInterfacePanel */
	public QueryInterfacePanel(OBDAModel apic, URI baseuri, OBDAPreferences prefs) {
		this.qc = apic.getQueryController();
		instance = this;
		this.apic = apic;
		this.baseuri = baseuri;
		prefs.registerPreferenceChangedListener(this);
		initComponents();
		
        


//		Font font = new Font("Dialog", Font.PLAIN, 14);
//		queryTextPane.setFont(font);
		StyleContext style = new StyleContext();
		_styled_doc = new SPARQLQueryStyledDocument(style, prefs);

		
		queryTextPane.setDocument(_styled_doc);
		queryTextPane.setBackground(Color.WHITE);
		queryTextPane.setCaretColor(Color.BLACK);
	}

	public void setOBDAModel(OBDAModel api) {
		this.apic = api;
	}
	
	public void setBaseURI(URI baseuri) {
		this.baseuri = baseuri;
	}

	public void setResultsPanel(ResultViewTablePanel viewpanel) {
		this.viewpanel = viewpanel;
	}

//	 public void setGetSPARQLDefaultPrefixAction(GetDefaultSPARQLPrefixAction action) {
//		 prefixAction = action;
//		 _styled_doc.setGetDefaultSPARQLPrefixAction(action);
//	 }

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		eqlPopupMenu = new javax.swing.JPopupMenu();
		getEQLSQLExpansion = new javax.swing.JMenuItem();
		sparqlPopupMenu = new javax.swing.JPopupMenu();
		getSPARQLExpansion = new javax.swing.JMenuItem();
		getSPARQLSQLExpansion = new javax.swing.JMenuItem();
		panel_query_buttons = new javax.swing.JPanel();
		jPanelStatusContainer = new javax.swing.JPanel();
		jLabelStatus = new javax.swing.JLabel();
		jCheckBoxShort = new javax.swing.JCheckBox();
		buttonAttachPrefix = new javax.swing.JButton();
		buttonExecute = new javax.swing.JButton();
		buttonSaveQuery = new javax.swing.JButton();
		jPanelQueryPaneContainer = new javax.swing.JPanel();
		jLabelHeader = new javax.swing.JLabel();
		jScrollQueryPane = new javax.swing.JScrollPane();
		queryTextPane = new javax.swing.JTextPane();

		getEQLSQLExpansion.setText("Get SQL for EQL query...");
		getEQLSQLExpansion.setEnabled(false);
		getEQLSQLExpansion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getEQLSQLExpansionActionPerformed(evt);
			}
		});
		eqlPopupMenu.add(getEQLSQLExpansion);

		sparqlPopupMenu.setComponentPopupMenu(sparqlPopupMenu);

		getSPARQLExpansion.setText("Get expansion this UCQ...");
		getSPARQLExpansion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getSPARQLExpansionActionPerformed(evt);
			}
		});
		sparqlPopupMenu.add(getSPARQLExpansion);

		getSPARQLSQLExpansion.setText("Get expanded/unfolded query for this UCQ...");
		getSPARQLSQLExpansion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getSPARQLSQLExpansionActionPerformed(evt);
			}
		});
		sparqlPopupMenu.add(getSPARQLSQLExpansion);

		setLayout(new java.awt.GridBagLayout());

		panel_query_buttons.setLayout(new java.awt.GridBagLayout());

		jPanelStatusContainer.setLayout(new java.awt.GridBagLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		jPanelStatusContainer.add(jLabelStatus, gridBagConstraints);

		jCheckBoxShort.setText("show short URIs");
		jCheckBoxShort.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBoxShortActionPerformed(evt);
			}
		});
		jPanelStatusContainer.add(jCheckBoxShort, new java.awt.GridBagConstraints());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		panel_query_buttons.add(jPanelStatusContainer, gridBagConstraints);

		buttonAttachPrefix.setText("Attach Prefixes");
		buttonAttachPrefix.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonAttachPrefixActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		panel_query_buttons.add(buttonAttachPrefix, gridBagConstraints);

		buttonExecute.setMnemonic('x');
		buttonExecute.setText("Execute");
		buttonExecute.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonExecuteActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		panel_query_buttons.add(buttonExecute, gridBagConstraints);

		buttonSaveQuery.setText("Save");
		buttonSaveQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonSaveActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		panel_query_buttons.add(buttonSaveQuery, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(panel_query_buttons, gridBagConstraints);

		jPanelQueryPaneContainer.setLayout(new java.awt.BorderLayout());

		jLabelHeader.setFont(new java.awt.Font("Arial", 1, 11));
		jLabelHeader.setForeground(new java.awt.Color(153, 153, 153));
		jLabelHeader.setText("Insert your ABox Query:");
		jPanelQueryPaneContainer.add(jLabelHeader, java.awt.BorderLayout.NORTH);

		queryTextPane.setFont(new java.awt.Font("Lucida Grande", 0, 14));
		queryTextPane.setComponentPopupMenu(sparqlPopupMenu);
		jScrollQueryPane.setViewportView(queryTextPane);

		jPanelQueryPaneContainer.add(jScrollQueryPane, java.awt.BorderLayout.CENTER);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(jPanelQueryPaneContainer, gridBagConstraints);
	}// </editor-fold>//GEN-END:initComponents

	private void jCheckBoxShortActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBoxShortActionPerformed
	// TODO add your handling code here:
	}// GEN-LAST:event_jCheckBoxShortActionPerformed

	private void getEQLSQLExpansionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_getEQLSQLExpansionActionPerformed
	// OBDADataQueryAction action = this.getRetrieveEQLUnfoldingAction();
	// action.run(eqlTextPane.getText(), null);
	}// GEN-LAST:event_getEQLSQLExpansionActionPerformed

	private void getSPARQLExpansionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_getSPARQLExpansionActionPerformed
		Thread queryRunnerThread = new Thread(new Runnable() {
			public void run() {

				OBDADataQueryAction action = QueryInterfacePanel.this.getRetrieveUCQExpansionAction();
				action.run(queryTextPane.getText(), null);
			}
		});
		queryRunnerThread.start();

	}// GEN-LAST:event_getSPARQLExpansionActionPerformed

	private void getSPARQLSQLExpansionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_getSPARQLSQLExpansionActionPerformed
		Thread queryRunnerThread = new Thread(new Runnable() {
			public void run() {
				OBDADataQueryAction action = QueryInterfacePanel.this.getRetrieveUCQUnfoldingAction();
				action.run(queryTextPane.getText(), null);
			}
		});
		queryRunnerThread.start();
	}// GEN-LAST:event_getSPARQLSQLExpansionActionPerformed

	private void buttonAttachPrefixActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonAdvancedPropertiesActionPerformed

		// prefixAction.run();
		// String prefix = (String) prefixAction.getResult();
		// String currentcontent = queryTextPane.getText();
		// String newcontent = prefix + "\n" + currentcontent;
		// queryTextPane.setText(newcontent);

		SelectPrefixPanel dialog = new SelectPrefixPanel(apic.getPrefixManager().getPrefixMap(), queryTextPane, baseuri.toString());
		dialog.show();
	}// GEN-LAST:event_buttonAdvancedPropertiesActionPerformed

	private void buttonExecuteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonExecuteActionPerformed
		try {
			// TODO Handle this such that there is a listener checking the
			// progress of the execution

			Thread queryRunnerThread = new Thread(new Runnable() {

				public void run() {
					OBDADataQueryAction action = QueryInterfacePanel.this.getExecuteUCQAction();
					action.run(queryTextPane.getText(), instance);

					execTime = action.getExecutionTime();
					int rows = action.getNumberOfRows();
					updateStatus(rows);

				};
			});
			queryRunnerThread.start();

		} catch (Exception e) {
			e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}// GEN-LAST:event_buttonExecuteActionPerformed

	private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonSaveActionPerformed

		JDialog saveDialog = new JDialog();
		saveDialog.setModal(true);
		String query = "";
		SaveQueryPanel savePanel;

		query = this.queryTextPane.getText();

		if ((currentGroup != null && currentGroup != "") && (currentId != null && currentId != ""))
			savePanel = new SaveQueryPanel(query, saveDialog, qc, currentGroup, currentId);
		else if ((currentGroup != null && currentGroup != "") && (currentId == null || currentId == ""))
			savePanel = new SaveQueryPanel(query, saveDialog, qc, currentGroup, currentId);
		else
			savePanel = new SaveQueryPanel(query, saveDialog, qc, currentId);// ?????

		saveDialog.getContentPane().add(savePanel, java.awt.BorderLayout.CENTER);
		saveDialog.pack();
		DialogUtils.centerDialogWRTParent(this, saveDialog);
		saveDialog.setVisible(true);
	}// GEN-LAST:event_buttonSaveActionPerformed

	public void selectedQuerychanged(String new_group, String new_query, String new_id) {
		queryTextPane.setText(new_query);
		currentGroup = new_group;
		currentId = new_id;
	}

	public void setExecuteUCQAction(OBDADataQueryAction executeUCQAction) {
		this.executeUCQAction = executeUCQAction;
	}

	public OBDADataQueryAction getExecuteUCQAction() {
		return executeUCQAction;
	}

	public void setExecuteEQLAction(OBDADataQueryAction executeEQLAction) {
		this.executeEQLAction = executeEQLAction;
	}

	public OBDADataQueryAction getExecuteEQLAction() {
		return executeEQLAction;
	}

	public void setRetrieveUCQExpansionAction(OBDADataQueryAction retrieveUCQExpansionAction) {
		this.retrieveUCQExpansionAction = retrieveUCQExpansionAction;
	}

	public OBDADataQueryAction getRetrieveUCQExpansionAction() {
		return retrieveUCQExpansionAction;
	}

	public void setRetrieveUCQUnfoldingAction(OBDADataQueryAction retrieveUCQUnfoldingAction) {
		this.retrieveUCQUnfoldingAction = retrieveUCQUnfoldingAction;
	}

	public OBDADataQueryAction getRetrieveUCQUnfoldingAction() {
		return retrieveUCQUnfoldingAction;
	}

	public void setRetrieveEQLUnfoldingAction(OBDADataQueryAction retrieveEQLUnfoldingAction) {
		this.retrieveEQLUnfoldingAction = retrieveEQLUnfoldingAction;
	}

	public OBDADataQueryAction getRetrieveEQLUnfoldingAction() {
		return retrieveEQLUnfoldingAction;
	}

	public void updateStatus(int rows) {

		Double d = Double.valueOf(execTime / 1000);
		String s = "Execution time: " + d + " sec     Number of tuples retrieved: " + rows;
		jLabelStatus.setText(s);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton		buttonAttachPrefix;
	private javax.swing.JButton		buttonExecute;
	private javax.swing.JButton		buttonSaveQuery;
	private javax.swing.JPopupMenu	eqlPopupMenu;
	private javax.swing.JMenuItem	getEQLSQLExpansion;
	private javax.swing.JMenuItem	getSPARQLExpansion;
	private javax.swing.JMenuItem	getSPARQLSQLExpansion;
	private javax.swing.JCheckBox	jCheckBoxShort;
	private javax.swing.JLabel		jLabelHeader;
	private javax.swing.JLabel		jLabelStatus;
	private javax.swing.JPanel		jPanelQueryPaneContainer;
	private javax.swing.JPanel		jPanelStatusContainer;
	private javax.swing.JScrollPane	jScrollQueryPane;
	private javax.swing.JPanel		panel_query_buttons;
	private javax.swing.JTextPane	queryTextPane;
	private javax.swing.JPopupMenu	sparqlPopupMenu;

	// End of variables declaration//GEN-END:variables

	// @Override
	public void tableChanged(TableModelEvent e) {
		Double d = Double.valueOf(execTime / 1000);
		int rows = ((TableModel) e.getSource()).getRowCount();
		String s = "Execution time: " + d + " sec     Number of tuples retrieved: " + rows;
		jLabelStatus.setText(s);

	}

	public boolean isShortURISelect() {
		return jCheckBoxShort.isSelected();
	}

	public String getQuery() {
		return queryTextPane.getText();
	}

	@Override
	public void preferenceChanged() {
		String query = queryTextPane.getText();
		queryTextPane.setText(query);		
	}
}
