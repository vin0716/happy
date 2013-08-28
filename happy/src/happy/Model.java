package happy;

import javax.swing.table.DefaultTableModel;

public class Model extends DefaultTableModel {
	public Model(String[] columns) {
		super(columns, 0);
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
