package happy;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;

public class Renderer extends DefaultTableCellRenderer {
	private SimpleDateFormat dateFormat;
	private DecimalFormat decimalFormat1;
	private DecimalFormat decimalFormat2;
	private Happy happy;

	public Renderer(Happy happy) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		decimalFormat1 = new DecimalFormat("0.00");
		decimalFormat2 = new DecimalFormat("#,##0");
		this.happy = happy;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		String identifier = (String)table.getColumnModel().getColumn(column).getIdentifier();
		if (identifier.equals("Date") || identifier.equals("Trade")) {
			setHorizontalAlignment(JLabel.CENTER);
		} else {
			setHorizontalAlignment(JLabel.RIGHT);
		}
		if (identifier.equals("Date")) {
			setText(dateFormat.format(value));
		} else if (identifier.equals("Amount") || identifier.equals("Charge") || identifier.equals("Profit") || identifier.equals("Capital")) {
			if (value == null || value.equals("")) {
				setText("");
			} else {
				setText(decimalFormat2.format(value));
			}
		} else if (identifier.equals("Trade")) {
			if (value == null) {
				setText(new String());
			} else {
				setText(value.toString());
			}
		} else {
			if (value == null || value.equals("")) {
				setText("");
			} else {
				setText(decimalFormat1.format(value));
			}
		}
		setBorder(null);
		if (row > 0) {
			// if (identifier.equals("End") || identifier.equals(happy.getTradeLine()) || identifier.equals(happy.getTrendLine())) {
			// double point = (double)value;
			// double pointBefore = (double)table.getValueAt(row - 1, column);
			// if (point > pointBefore) {
			// setBorder(BorderFactory.createLineBorder(Color.RED, 2));
			// } else if (point < pointBefore) {
			// setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
			// } else {
			// setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
			// }
			// }
			if (identifier.equals("End")) {
				double point = (double)value;
				double trade = (double)table.getModel().getValueAt(row, happy.getColumnIndex("5"));
				if (point > trade) {
					setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				} else if (point < trade) {
					setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
				} else {
					setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				}
			} else if (identifier.equals(happy.getTrendLine())) {
				double point = (double)value;
				double pointBefore = (double)table.getValueAt(row - 1, column);
				if (point > pointBefore) {
					setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				} else if (point < pointBefore) {
					setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
				} else {
					setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				}
			}
		}
		return this;
	}
}