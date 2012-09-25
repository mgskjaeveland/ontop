package it.unibz.krdb.obda.protege4.gui.component;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class SQLResultTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	private int selectedColumn;
	
	public SQLResultTable() {
		super();
		setAutoscrolls(false);
		setColumnSelectionAllowed(true);
		setCellSelectionEnabled(true);
		setDefaultRenderer(String.class, new ColumnHighlightRenderer());
		
		setPreferredScrollableViewportSize(getPreferredSize());
		setIntercellSpacing(new Dimension(1, 1));
		
		JTableHeader tableHeader = getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setDefaultRenderer(new HeaderHighlightRenderer());
		tableHeader.addMouseListener(new ColumnHeaderAdapter(this));
	}
	
	public void setColumnOnSelect(int index) {
		selectedColumn = index;
	}
	
	public int getColumnOnSelect() {
		return selectedColumn;
	}
	
	/**
	 * An adapter class to capture the column index when user selects the table header
	 */
	class ColumnHeaderAdapter extends MouseAdapter {
		
		private SQLResultTable table;
		
		public ColumnHeaderAdapter(SQLResultTable table) {
			this.table = table;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			JTableHeader header = table.getTableHeader();
			int index = header.columnAtPoint(e.getPoint());
			table.setColumnOnSelect(index);
			table.repaint();
			header.repaint();
			
			// Find any text field component that is on focus and put the table header name there
			Container parent = findParentContainer(table);
			Component compOnFocus = findFocus(parent);
			if (compOnFocus != null) {
				if (!(compOnFocus instanceof JComboBox) && compOnFocus instanceof JTextField) {
					JTextField tf = (JTextField) compOnFocus;
					String template = tf.getText();
					if (template.isEmpty()) {
						String text = String.format("$%s", table.getColumnName(index));
						tf.setText(text);
					} else {
						String text = String.format("%s{$%s}", template, table.getColumnName(index));
						tf.setText(text);
					}
				}
			}
		}
		
		private Container findParentContainer(Component c) {
			boolean loop = true;
			Component comp = c;
			while (loop) {
				comp = comp.getParent();
				if (comp != null) {
					if (comp instanceof JPanel) {
						String compName = comp.getName();
						if (compName != null && compName.equals("panel_master")) {
							return (Container) comp;
						}
					}
				} else {
					loop = false;
				}
			}
			return null;
		}
		
		private Component findFocus(Container c) {
			Component comps[] = c.getComponents();
			for (int i = 0; i < comps.length; i++) {
				if (comps[i].isFocusOwner()) {
					return comps[i];
				}
				if (comps[i] instanceof Container) {
					Component comp = findFocus((Container) comps[i]);
					if (comp != null) {
						return comp;
					}
				}
			}
			return null;
		}
	}

	/**
	 * A custom cell renderer to highlight the entire column cells if user selects the table header
	 */
	class ColumnHighlightRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public ColumnHighlightRenderer() {
			super();
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			
			SQLResultTable resultTable = (SQLResultTable) table;
			if (resultTable.getColumnOnSelect() == col) {
				setBackground(UIManager.getDefaults().getColor("Table.selectionBackground"));
			} else {
				setBackground(UIManager.getDefaults().getColor("Table.background"));
			}
			return this;
		}
	}
	
	/**
	 * A custom cell renderer to highlight the table header when selected
	 */
	class HeaderHighlightRenderer extends DefaultTableCellRenderer {
		
		private static final long serialVersionUID = 1L;
		
		public HeaderHighlightRenderer() {
			super();
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(SwingConstants.CENTER);
			setFont(new Font("Tahoma", Font.BOLD, 13));

			SQLResultTable resultTable = (SQLResultTable) table;
			if (resultTable.getColumnOnSelect() == col) {
				setBackground(UIManager.getDefaults().getColor("Button.select"));
			} else {
				setBackground(UIManager.getDefaults().getColor("Button.background"));
			}
			return this;
		}
	}
}
