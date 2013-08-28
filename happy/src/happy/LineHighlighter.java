package happy;

import java.awt.Color;
import java.awt.Component;

import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

public class LineHighlighter extends AbstractHighlighter {
	private Happy happy;

	public LineHighlighter(Happy happy, HighlightPredicate predicate) {
		super(predicate);
		this.happy = happy;
	}

	protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
		JXTable table = (JXTable)adapter.getComponent();
		String string = (String)table.getValueAt(adapter.row, table.getColumnModel().getColumnIndex("Trade"));
		if (string == null) {
			string = new String();
		}
		Color background = getBackgroundColor(string);
		Color selectedBackground = getBackgroundColor("Selected");
		renderer.setForeground(table.getForeground());
		renderer.setBackground(adapter.isSelected() ? selectedBackground : background);
		return renderer;
	}

	public static Color getBackgroundColor(String string) {
		switch (string) {
			case "":
				return Color.WHITE;
			case "BUY":
				return getColor("FFE4E1");
			case "SELL":
				return getColor("E6E6FA");
			case "Selected":
				return Color.YELLOW;
			default:
				return Color.WHITE;
		}
	}

	public static Color getColor(String hex) {
		int red = convertToInt(hex.substring(0, 1)) * 16 + convertToInt(hex.substring(1, 2));
		int green = convertToInt(hex.substring(2, 3)) * 16 + convertToInt(hex.substring(3, 4));
		int blue = convertToInt(hex.substring(4, 5)) * 16 + convertToInt(hex.substring(5, 6));
		return new Color(red, green, blue);
	}

	public static int convertToInt(String string) {
		if (string.equals("0")) {
			return 0;
		} else if (string.equals("1")) {
			return 1;
		} else if (string.equals("2")) {
			return 2;
		} else if (string.equals("3")) {
			return 3;
		} else if (string.equals("4")) {
			return 4;
		} else if (string.equals("5")) {
			return 5;
		} else if (string.equals("6")) {
			return 6;
		} else if (string.equals("7")) {
			return 7;
		} else if (string.equals("8")) {
			return 8;
		} else if (string.equals("9")) {
			return 9;
		} else if (string.equals("A")) {
			return 10;
		} else if (string.equals("B")) {
			return 11;
		} else if (string.equals("C")) {
			return 12;
		} else if (string.equals("D")) {
			return 13;
		} else if (string.equals("E")) {
			return 14;
		} else if (string.equals("F")) {
			return 15;
		} else {
			return 0;
		}
	}
}
