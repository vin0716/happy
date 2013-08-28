package happy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.table.TableColumnExt;

public class Happy extends JFrame implements ActionListener {
	private JXDatePicker date1, date2;
	private Date startDate, endDate;
	private JComboBox tradeLine, trendLine, reinvestCombo;
	private JXTable table;
	private JTextField capitalField, cashField, priceField, quantityField, endField, tradeField, trendField, beforeField, resultField;
	private Model model;
	private String[] columns, hiddenColumns;
	private int[] widths;
	private boolean buy, reinvest, batch;
	private JTextArea summary;
	private int startRow, endRow, buyRow, tradeCount, win, lose, before;
	private double change, capital, winCapital, loseCapital, totalCharge;
	private String trade, trend;
	private Excel excel;
	private SimpleDateFormat dateFormat;
	private DecimalFormat decimalFormat;
	private DecimalFormat decimalFormat1;

	public static void main(String[] args) {
		new Happy();
	}

	public Happy() {
		super("Happy");
		columns = new String[] {"Date", "Start", "High", "Low", "End", "5", "18", "Today", "Before", "Amount", "Trade", "Charge", "Profit", "Capital"};
		hiddenColumns = new String[] {"High", "Low", "Charge"};
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		decimalFormat = new DecimalFormat("#,##0");
		decimalFormat1 = new DecimalFormat("0.00");
		excel = new Excel();
		getContentPane().add(createPanel(), BorderLayout.WEST);
		getContentPane().add(createTable(), BorderLayout.CENTER);
		setSize(1280, 768);
		WindowListener windowListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		addWindowListener(windowListener);
		pack();
		setVisible(true);
	}

	private JPanel createPanel() {
		JPanel datePanel1 = new JPanel();
		datePanel1.add(new JLabel("Start"));
		Calendar calendar = Calendar.getInstance();
		calendar.set(1985, 0, 4);
		date1 = new JXDatePicker(calendar.getTime());
		date1.setFormats("yyyy-MM-dd");
		datePanel1.add(date1);
		JPanel datePanel2 = new JPanel();
		datePanel2.add(new JLabel("End"));
		date2 = new JXDatePicker();
		date2.setFormats("yyyy-MM-dd");
		datePanel2.add(date2);
		JPanel datePanel = new JPanel(new VerticalLayout());
		datePanel.setBorder(BorderFactory.createTitledBorder("Date"));
		datePanel.add(datePanel1);
		datePanel.add(datePanel2);
		JPanel tradePanel = new JPanel();
		tradePanel.add(new JLabel("Trade"));
		tradeLine = new JComboBox(new String[] {"5", "18"});
		tradeLine.setSelectedItem("5");
		tradePanel.add(tradeLine);
		JPanel trendPanel = new JPanel();
		trendPanel.add(new JLabel("Trend"));
		trendLine = new JComboBox(new String[] {"", "5", "18"});
		trendLine.setSelectedItem("18");
		trendPanel.add(trendLine);
		JPanel linePanel = new JPanel(new VerticalLayout());
		linePanel.setBorder(BorderFactory.createTitledBorder("Line"));
		linePanel.add(tradePanel);
		linePanel.add(trendPanel);
		JPanel capitalPanel = new JPanel();
		capitalPanel.add(new JLabel("Capital"));
		capitalField = new JTextField("100,000,000");
		capitalField.setHorizontalAlignment(JTextField.RIGHT);
		capitalPanel.add(capitalField);
		JPanel reinvestPanel = new JPanel();
		reinvestPanel.add(new JLabel("Reinvest"));
		reinvestCombo = new JComboBox(new String[] {"False", "True"});
		reinvestPanel.add(reinvestCombo);
		JPanel investPanel = new JPanel(new VerticalLayout());
		investPanel.setBorder(BorderFactory.createTitledBorder("Invest"));
		investPanel.add(capitalPanel);
		investPanel.add(reinvestPanel);
		summary = new JTextArea(9, 10);
		JButton analyze = new JButton("Analyze");
		analyze.setActionCommand("Analyze");
		analyze.addActionListener(this);
		JButton batch = new JButton("Batch");
		batch.setActionCommand("Batch");
		batch.addActionListener(this);
		JButton save = new JButton("Save");
		save.setActionCommand("Save");
		save.addActionListener(this);
		JPanel endPanel = new JPanel();
		endPanel.add(new JLabel("End"));
		endField = new JTextField();
		endField.setHorizontalAlignment(JTextField.RIGHT);
		endPanel.add(endField);
		JPanel panel5 = new JPanel();
		panel5.add(new JLabel("Trade"));
		tradeField = new JTextField();
		tradeField.setHorizontalAlignment(JTextField.RIGHT);
		panel5.add(tradeField);
		JPanel panel18 = new JPanel();
		panel18.add(new JLabel("Trend"));
		trendField = new JTextField();
		trendField.setHorizontalAlignment(JTextField.RIGHT);
		panel18.add(trendField);
		JPanel beforePanel = new JPanel();
		beforePanel.add(new JLabel("Before"));
		beforeField = new JTextField();
		beforeField.setHorizontalAlignment(JTextField.RIGHT);
		beforePanel.add(beforeField);
		JPanel resultPanel = new JPanel();
		JButton decide = new JButton("D");
		decide.setActionCommand("Decide");
		decide.setToolTipText("Decide");
		decide.addActionListener(this);
		resultPanel.add(decide);
		resultField = new JTextField();
		resultPanel.add(resultField);
		JPanel decisionPanel = new JPanel(new VerticalLayout());
		decisionPanel.setBorder(BorderFactory.createTitledBorder("Decision"));
		decisionPanel.add(endPanel);
		decisionPanel.add(panel5);
		decisionPanel.add(panel18);
		decisionPanel.add(beforePanel);
		decisionPanel.add(resultPanel);
		JPanel cashPanel = new JPanel();
		cashPanel.add(new JLabel("Cash"));
		cashField = new JTextField("200,000,000");
		cashField.setHorizontalAlignment(JTextField.RIGHT);
		cashPanel.add(cashField);
		JPanel pricePanel = new JPanel();
		pricePanel.add(new JLabel("Price"));
		priceField = new JTextField();
		priceField.setHorizontalAlignment(JTextField.RIGHT);
		priceField.setToolTipText("Current Kodex Price");
		pricePanel.add(priceField);
		JPanel quantityPanel = new JPanel();
		JButton amount = new JButton("A");
		amount.setActionCommand("Amount");
		amount.setToolTipText("Amount");
		amount.addActionListener(this);
		quantityPanel.add(amount);
		quantityField = new JTextField();
		quantityField.setHorizontalAlignment(JTextField.RIGHT);
		quantityPanel.add(quantityField);
		JPanel kodexPanel = new JPanel(new VerticalLayout());
		kodexPanel.setBorder(BorderFactory.createTitledBorder("Kodex"));
		kodexPanel.add(cashPanel);
		kodexPanel.add(pricePanel);
		kodexPanel.add(quantityPanel);
		JPanel panel = new JPanel(new VerticalLayout());
		panel.add(datePanel);
		panel.add(linePanel);
		panel.add(investPanel);
		panel.add(new JScrollPane(summary));
		panel.add(analyze);
		panel.add(batch);
		panel.add(save);
		panel.add(decisionPanel);
		panel.add(kodexPanel);
		setComponentSize(panel, 125, 20);
		return panel;
	}

	private JScrollPane createTable() {
		widths = new int[] {70, 60, 60, 60, 60, 60, 60, 60, 60, 60, 100, 100, 100, 100};
		model = new Model(columns);
		table = new JXTable(model);
		table.setCellSelectionEnabled(true);
		table.setHighlighters(new LineHighlighter(this, HighlightPredicate.ALWAYS));
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(widths[i]);
			column.setIdentifier(columns[i]);
			column.setCellRenderer(new Renderer(this));
		}
		for (int i = 0; i < hiddenColumns.length; i++) {
			TableColumnExt column = table.getColumnExt(hiddenColumns[i]);
			column.setVisible(false);
		}
		table.setColumnControlVisible(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return new JScrollPane(table);
	}

	private void open() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();
		}
		// URL url = classLoader.getResource("kospi.xlsx");
		// File file = new File(url.getFile());
		Vector<Object[]> vector = excel.importExcel(new File("kospi.xlsx"), "kospi", 1);
		for (int i = 0; i < vector.size(); i++) {
			model.addRow(vector.elementAt(i));
		}
	}

	private void analyze() {
		clearTable();
		open();
		tradeCount = 0;
		win = 0;
		lose = 0;
		winCapital = 0;
		loseCapital = 0;
		totalCharge = 0;
		buy = false;
		before = 14;
		change = 5.3;
		capital = Double.parseDouble(capitalField.getText().replace(",", ""));
		// rate = 6.5;
		if (date1.getDate() == null) {
			JOptionPane.showMessageDialog(this, "Input Start Date", "Start", JOptionPane.WARNING_MESSAGE);
			return;
		}
		startRow = findDate(date1.getDate(), true);
		if (startRow == -1) {
			JOptionPane.showMessageDialog(this, "Start Date Not Exist", "Start", JOptionPane.WARNING_MESSAGE);
			return;
		}
		endRow = model.getRowCount() - 1;
		if (date2.getDate() != null) {
			endRow = findDate(date2.getDate(), false);
			if (endRow == -1) {
				JOptionPane.showMessageDialog(this, "End Date Not Exist", "End", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		trade = (String)tradeLine.getSelectedItem();
		trend = (String)trendLine.getSelectedItem();
		reinvest = Boolean.parseBoolean((String)reinvestCombo.getSelectedItem());
		buyRow = -1;
		trade();
		table.scrollCellToVisible(endRow, 0);
		if (!batch) {
			summary();
		} else {
			summary_batch();
		}
	}

	private void trade() {
		for (int i = startRow + before; i <= endRow; i++) {
			double end = (double)model.getValueAt(i, getColumnIndex("End"));
			double endYesterday = (double)model.getValueAt(i - 1, getColumnIndex("End"));
			double start = (double)model.getValueAt(i, getColumnIndex("Start"));
			double tradePoint = (double)model.getValueAt(i, getColumnIndex(trade));
			double trendPoint = (double)model.getValueAt(i, getColumnIndex(trend));
			double trendYesterday = (double)model.getValueAt(i - 1, getColumnIndex(trend));
			double trendBefore = (double)model.getValueAt(i - before, getColumnIndex(trend));
			double today = (end - endYesterday) / endYesterday * 100;
			model.setValueAt(today, i, getColumnIndex("Today"));
			double charge = (capital * 0.015) / 100;
			double trendRatio = ((trendPoint - trendBefore) / trendBefore) * 100;
			model.setValueAt(trendRatio, i, getColumnIndex("Before"));
			double startRatio = ((start - endYesterday) / endYesterday) * 100;
			double endRatio = ((end - endYesterday) / endYesterday) * 100;
			if (buy) {
				if (startRatio < -2.5) {
					sell(i, start, charge);
					continue;
				}
				if (end < tradePoint && trendPoint < trendYesterday && trendRatio < change) {
					sell(i, end, charge);
					continue;
				}
			} else {
				if (end >= tradePoint && trendPoint >= trendYesterday && trendRatio >= change * -1) {
					buy(i, charge);
					continue;
				}
			}
		}
	}

	private void buy(int i, double charge) {
		buy = true;
		buyRow = i;
		model.setValueAt("BUY", i, getColumnIndex("Trade"));
		model.setValueAt(charge, i, getColumnIndex("Charge"));
		totalCharge += charge;
	}

	private void sell(int i, double point, double charge) {
		buy = false;
		if (buyRow == -1) {
			return;
		}
		model.setValueAt("SELL", i, getColumnIndex("Trade"));
		model.setValueAt(charge, i, getColumnIndex("Charge"));
		double buyCharge = (double)model.getValueAt(buyRow, getColumnIndex("Charge"));
		double buyPoint = 0;
		buyPoint = (double)model.getValueAt(buyRow, getColumnIndex("End"));
		double profit = (point - buyPoint) / buyPoint * capital - (buyCharge + charge);
		model.setValueAt(profit, i, getColumnIndex("Profit"));
		buyRow = -1;
		tradeCount++;
		totalCharge += charge;
		if (profit > 0) {
			win++;
			winCapital += profit;
		} else {
			lose++;
			loseCapital += profit;
		}
		if (reinvest) {
			model.setValueAt(capital + profit, i, getColumnIndex("Capital"));
			capital += profit;
		} else {
			model.setValueAt(winCapital + loseCapital + capital, i, getColumnIndex("Capital"));
		}
	}

	private void summary() {
		StringBuilder string = new StringBuilder();
		string.append("Start : " + dateFormat.format(startDate) + "\n");
		Date date = date2.getDate();
		if (date != null) {
			string.append("End : " + dateFormat.format(endDate) + "\n");
		} else {
			date = (Date)model.getValueAt(endRow, getColumnIndex("Date"));
			string.append("End : " + dateFormat.format(date) + "\n");
		}
		string.append("Total Date : " + (endRow - startRow + 1) + "\n");
		string.append("Trade : " + trade + "\n");
		string.append("Trend : " + trend + "\n");
		string.append("Capital : " + capitalField.getText() + "\n");
		string.append("Reinvest : " + (reinvest ? "True" : "False") + "\n");
		string.append("Trade Count : " + tradeCount + "\n");
		if (reinvest) {
			string.append("Total Capital : " + decimalFormat.format(capital) + "\n");
		} else {
			string.append("Total Capital : " + decimalFormat.format(winCapital + loseCapital + capital) + "\n");
		}
		string.append("Total Charge : " + decimalFormat.format(totalCharge) + "\n");
		double rate = (double)win / (double)lose;
		string.append("Win / Lose : " + win + " / " + lose + " (" + decimalFormat1.format(rate * 100) + "%) \n");
		string.append("Average Per Win : " + decimalFormat.format(winCapital / win) + "\n");
		string.append("Average Per Lose : " + decimalFormat.format(loseCapital / lose) + "\n");
		if (reinvest) {
			double initialCapital = Double.parseDouble(capitalField.getText().replace(",", ""));
			string.append("Profit Per Trade: " + decimalFormat.format((capital - initialCapital - totalCharge) / tradeCount));
		} else {
			string.append("Profit Per Trade: " + decimalFormat.format((winCapital + loseCapital - totalCharge) / tradeCount));
		}
		summary.setText(string.toString());
	}

	private int findDate(Date date, boolean start) {
		if (start) {
			for (int i = 0; i < model.getRowCount(); i++) {
				Date value = (Date)model.getValueAt(i, getColumnIndex("Date"));
				if (value.compareTo(date) >= 0) {
					startDate = value;
					return i;
				}
			}
		} else {
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				Date value = (Date)model.getValueAt(i, getColumnIndex("Date"));
				if (value.compareTo(date) <= 0) {
					endDate = value;
					return i;
				}
			}
		}
		return -1;
	}

	public int getColumnIndex(String string) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals(string)) {
				return i;
			}
		}
		return -1;
	}

	private void amount() {
		if (cashField.getText().equals("")) {
			quantityField.setText("Input Cash");
		}
		if (priceField.getText().equals("")) {
			quantityField.setText("Input Kodex Price");
		}
		if (!cashField.getText().equals("") && !priceField.getText().equals("")) {
			double cash = Double.parseDouble(cashField.getText().replace(",", ""));
			cash = cash - (cash * 0.00015);
			int price = Integer.parseInt(priceField.getText());
			quantityField.setText(String.valueOf(cash / price));
		}
	}

	private void decide() {
		if (endRow == 0) {
			endField.setText("");
			tradeField.setText("");
			trendField.setText("");
			beforeField.setText("");
			resultField.setText("Analze First");
			return;
		}
		if (endField.getText().equals("")) {
			endField.setText(String.valueOf(model.getValueAt(endRow, getColumnIndex("End"))));
		}
		if (tradeField.getText().equals("")) {
			tradeField.setText(String.valueOf(model.getValueAt(endRow, getColumnIndex(trade))));
		}
		if (trendField.getText().equals("")) {
			trendField.setText(String.valueOf(model.getValueAt(endRow, getColumnIndex(trend))));
		}
		double end = Double.parseDouble(endField.getText());
		double endYesterday = (double)model.getValueAt(endRow - 1, getColumnIndex("End"));
		double start = (double)model.getValueAt(endRow, getColumnIndex("Start"));
		double tradePoint = Double.parseDouble(tradeField.getText());
		double trendPoint = Double.parseDouble(trendField.getText());
		double trendYesterday = (double)model.getValueAt(endRow - 1, getColumnIndex(trend));
		double trendBefore = (double)model.getValueAt(endRow - before, getColumnIndex(trend));
		double trendRatio = ((trendPoint - trendBefore) / trendBefore) * 100;
		double startRatio = ((start - endYesterday) / endYesterday) * 100;
		beforeField.setText(decimalFormat1.format(trendRatio));
		if (buy) {
			double buyPoint = (double)model.getValueAt(buyRow, getColumnIndex("End"));
			if (startRatio <= -2.5) {
				resultField.setText("Sell At Start");
			} else if (end < tradePoint && trendPoint < trendYesterday && trendRatio < change) {
				resultField.setText("Sell");
			} else {
				resultField.setText("Hold");
			}
		}
		if (!buy) {
			if (end >= tradePoint && trendPoint >= trendYesterday && trendRatio >= change * -1) {
				resultField.setText("Buy");
			} else {
				resultField.setText("Wait");
			}
		}
	}

	private void save() {
		try {
			JFileChooser fileChooser = new JFileChooser("D:\\");
			fileChooser.setFileFilter(new ExtensionFileFilter("Excel(*.xlsx)", new String[] {"xlsx"}));
			Calendar calendar = Calendar.getInstance();
			fileChooser.setSelectedFile(new File(calendar.get(calendar.YEAR) + "-" + (calendar.get(calendar.MONTH) + 1) + "-" + calendar.get(calendar.DATE)));
			Date d = new Date();
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".xlsx");
				if (file.exists()) {
					int decision = JOptionPane.showConfirmDialog(this, "File Exist Do You Want Rewrite?", "File Exist", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (decision == JOptionPane.YES_OPTION) {
						file.delete();
					} else {
						return;
					}
				}
				excel.downloadTable(file, table, columns, widths);
				JOptionPane.showMessageDialog(this, "Save Complete", file.getAbsolutePath(), JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void clearTable() {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
	}

	public String getTradeLine() {
		return trade;
	}

	public String getTrendLine() {
		return trend;
	}

	private void setComponentSize(JComponent component_, int width, int height) {
		for (int i = 0; i < component_.getComponentCount(); i++) {
			Component component = component_.getComponent(i);
			if (component instanceof JPanel) {
				setComponentSize((JPanel)component, width, height);
			} else if (component instanceof JSplitPane) {
				setComponentSize((JSplitPane)component, width, height);
			} else if (component instanceof JLabel) {
				((JLabel)component).setPreferredSize(new Dimension(width - 75, height));
			} else if (component instanceof JTextField) {
				((JTextField)component).setPreferredSize(new Dimension(width, height));
			} else if (component instanceof JButton) {
				((JButton)component).setPreferredSize(new Dimension(width - 75, height));
			} else if (component instanceof JPasswordField) {
				((JPasswordField)component).setPreferredSize(new Dimension(width, height));
			} else if (component instanceof JComboBox) {
				((JComboBox)component).setPreferredSize(new Dimension(width, height));
			} else if (component instanceof JList) {
				((JList)component).setPreferredSize(new Dimension(width, height + 50));
			} else if (component instanceof JXDatePicker) {
				((JXDatePicker)component).setPreferredSize(new Dimension(width, height));
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		String string = e.getActionCommand();
		if (string.equals("Analyze")) {
			batch = false;
			analyze();
		} else if (string.equals("Batch")) {
			batch = true;
			batch();
		} else if (string.equals("Amount")) {
			amount();
		} else if (string.equals("Decide")) {
			decide();
		} else if (string.equals("Save")) {
			save();
		}
	}

	public void batch() {
		double[] rateArray = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		// double[] rateArray = new double[] {5.5, 5.6, 5.7, 5.8, 5.9, 6, 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8, 6.9, 7};
		for (int i = 0; i < rateArray.length; i++) {
			// rate = rateArray[i];
			analyze();
		}
	}

	private void summary_batch() {
		StringBuilder string = new StringBuilder();
		string.append("Rate : " + "" + "\n");
		// string.append("Start : " + dateFormat.format(startDate) + "\n");
		// Date date = date2.getDate();
		// if (date != null) {
		// string.append("End : " + dateFormat.format(endDate) + "\n");
		// } else {
		// date = (Date)model.getValueAt(endRow, getColumnIndex("Date"));
		// string.append("End : " + dateFormat.format(date) + "\n");
		// }
		// string.append("Total Date : " + (endRow - startRow + 1) + "\n");
		// string.append("Trade : " + trade + "\n");
		// string.append("Trend : " + trend + "\n");
		// string.append("Capital : " + capitalField.getText() + "\n");
		// string.append("Reinvest : " + (reinvest ? "True" : "False") + "\n");
		string.append("Trade Count : " + tradeCount + "\n");
		if (reinvest) {
			string.append("Total Capital : " + decimalFormat.format(capital) + "\n");
		} else {
			string.append("Total Capital : " + decimalFormat.format(winCapital + loseCapital + capital) + "\n");
		}
		// string.append("Total Charge : " + decimalFormat.format(totalCharge) + "\n");
		double rate = (double)win / (double)lose;
		string.append("Win / Lose : " + win + " / " + lose + " (" + decimalFormat1.format(rate * 100) + "%) \n");
		// string.append("Average Per Win : " + decimalFormat.format(winCapital / win) + "\n");
		// string.append("Average Per Lose : " + decimalFormat.format(loseCapital / lose) + "\n");
		// string.append("Profit Per Trade: " + decimalFormat.format((capital - totalCharge) / tradeCount));
		System.out.println(string.toString());
	}
}
