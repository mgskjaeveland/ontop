/***
 * Copyright (c) 2008, Mariano Rodriguez-Muro.
 * All rights reserved.
 *
 * The OBDA-API is licensed under the terms of the Lesser General Public
 * License v.3 (see OBDAAPI_LICENSE.txt for details). The components of this
 * work include:
 * 
 * a) The OBDA-API developed by the author and licensed under the LGPL; and, 
 * b) third-party components licensed under terms that may be different from 
 *   those of the LGPL.  Information about such licenses can be found in the 
 *   file named OBDAAPI_3DPARTY-LICENSES.txt.
 */

package inf.unibz.it.obda.gui.swing.datasource.panels;


import inf.unibz.it.obda.api.controller.DatasourcesController;
import inf.unibz.it.obda.api.datasource.JDBCConnectionManager;
import inf.unibz.it.obda.domain.DataSource;
import inf.unibz.it.obda.gui.swing.exception.NoDatasourceSelectedException;
import inf.unibz.it.obda.validator.exception.NoConnectionException;

import java.awt.EventQueue;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;


/**
 *
 * @author  mariano
 */
public class SQLQueryPanel extends javax.swing.JPanel {
    
	
	DatasourcesController dsc=null;
	String execute_Query;
    /** Creates new form SQLQueryPanel */
    public SQLQueryPanel(DatasourcesController dsc,String execute_Query) {
    	
    	this(dsc);
    	this.execute_Query=execute_Query;
    	show_Result_Query();    
    }
    
    
    public SQLQueryPanel(DatasourcesController dsc) {
    	this.dsc = dsc;
        initComponents();
        this.queryTable.setFont(new Font("Arial", Font.PLAIN, 18));
        this.queryTable.setRowHeight(21);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        queryField = new javax.swing.JTextArea();
        executeButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        queryTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setMinimumSize(new java.awt.Dimension(156, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(156, 100));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel13.setText("SQL Query:");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel13.setRequestFocusEnabled(false);
        jLabel13.setVerifyInputWhenFocusTarget(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel13, gridBagConstraints);

        queryField.setColumns(20);
        queryField.setRows(2);
        queryField.setBorder(null);
        jScrollPane2.setViewportView(queryField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        jPanel1.add(jScrollPane2, gridBagConstraints);

        executeButton.setText("Excecute");
        executeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(executeButton, gridBagConstraints);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        queryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Results"
            }
        ));
        jScrollPane1.setViewportView(queryTable);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel2);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void executeButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               

		EventQueue.invokeLater(new Runnable() {
			public void run() {
					// "com.mysql.jdbc.Driver",
					// "jdbc:mysql://localhost/sattest", "mastro", "mastro");
//					ResultSetTableModelFactory modelfactory = ResultSetTableModelFactory.getInstance(dsc.getCurrentDataSource());
//
//					/***********************************************************
//					 * get Previous model, if any and close it before
//					 * proceeding.
//					 */
					TableModel oldmodel = queryTable.getModel();
					if ((oldmodel != null) && (oldmodel instanceof IncrementalResultSetTableModel)) {

						IncrementalResultSetTableModel rstm = (IncrementalResultSetTableModel) oldmodel;
						rstm.close();
					}
//
//					queryTable.setModel(modelfactory.getResultSetTableModel(queryField.getText()));

					DataSource current_ds = dsc.getCurrentDataSource();
					if(current_ds == null){ 				
						JOptionPane.showMessageDialog(null, "Pleas select a data source first");
					}else{
						JDBCConnectionManager man =JDBCConnectionManager.getJDBCConnectionManager();
						try {
							man.setProperty(JDBCConnectionManager.JDBC_AUTOCOMMIT, false);
							man.setProperty(JDBCConnectionManager.JDBC_RESULTSETTYPE, ResultSet.TYPE_FORWARD_ONLY);
							if(!man.isConnectionAlive(current_ds.getUri())){
								try {
									man.createConnection(current_ds);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						
							java.sql.ResultSet set = man.executeQuery(current_ds.getUri(), queryField.getText(),current_ds); 
							//java.sql.ResultSet set = man.executeQuery(current_ds.getUri(), execute_query,current_ds); //EK
							IncrementalResultSetTableModel model = new IncrementalResultSetTableModel(set);
							queryTable.setModel(model);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new RuntimeException(e);
							
						}
					}
			}
		});
    }                                             
//throw new RuntimeException(e);
    
 
//    /***************************************************************************
//	 * Verifies that there is a connected result set factory for the current
//	 * data sources. If there is no factory it creates it, if there is, but the
//	 * connection data for the current source and the data used to create the
//	 * factory is different it closes the previous one and creates a new one.
//	 * 
//	 * If there exists a factory and its data is ok it checks if it is
//	 * connected, if it is not it tries to connect it.
//	 * 
//	 * @throws NoDatasourceSelectedException
//	 * @throws NoConnectionException
//	 */
//	private ResultSetTableModelFactory getResultSetModelFactory() throws NoDatasourceSelectedException, NoConnectionException,
//			ClassNotFoundException, SQLException {
//
//	}
    private void show_Result_Query(){
    	queryField.setText(execute_Query);
    	TableModel oldmodel = queryTable.getModel();
    	
		if ((oldmodel != null) && (oldmodel instanceof IncrementalResultSetTableModel)) {

			IncrementalResultSetTableModel rstm = (IncrementalResultSetTableModel) oldmodel;
			rstm.close();
		}
		
		DataSource current_ds = dsc.getCurrentDataSource();
		if(current_ds == null){ 
		
			JOptionPane.showMessageDialog(null, "Pleas select a data source first");
		}else{
			JDBCConnectionManager man =JDBCConnectionManager.getJDBCConnectionManager();
			try {
				man.setProperty(JDBCConnectionManager.JDBC_AUTOCOMMIT, false);
				man.setProperty(JDBCConnectionManager.JDBC_RESULTSETTYPE, ResultSet.TYPE_FORWARD_ONLY);
				if(!man.isConnectionAlive(current_ds.getUri())){
					try {
						man.createConnection(current_ds);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			
				//java.sql.ResultSet set = man.executeQuery(current_ds.getUri(), queryField.getText(),current_ds); original
				java.sql.ResultSet set = man.executeQuery(current_ds.getUri(), execute_Query,current_ds); //EK
				IncrementalResultSetTableModel model = new IncrementalResultSetTableModel(set);
				queryTable.setModel(model);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				
			}
		}
}
    	
    

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton executeButton;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea queryField;
    private javax.swing.JTable queryTable;
    // End of variables declaration//GEN-END:variables
    
}
