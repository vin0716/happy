package happy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdesktop.swingx.JXTable;

public class Excel {
	private SimpleDateFormat dateFormat;
	private DecimalFormat decimalFormat;
	private FormulaEvaluator evaluator;
	private Hashtable<String, CellStyle> styles;

	public Excel() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		decimalFormat = new DecimalFormat();
		styles = new Hashtable();
	}

	public String[] getAllSheet(File file) {
		XSSFWorkbook workbook = getXSSFWorkbook(file);
		int count = workbook.getNumberOfSheets();
		String[] strings = new String[count];
		for (int i = 0; i < count; i++) {
			strings[i] = workbook.getSheetName(i);
		}
		return strings;
	}

	public Vector<Object[]> importExcel(File file, String sheetName, int startRow) {
		XSSFWorkbook workbook = getXSSFWorkbook(file);
		evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Vector<Object[]> vector = new Vector();
		int count = sheet.getPhysicalNumberOfRows();
		for (int i = startRow; i < count; i++) {
			XSSFRow row = sheet.getRow(i);
			Object[] data = getCellData(row);
			vector.addElement(data);
		}
		return vector;
	}

	public XSSFWorkbook getXSSFWorkbook(File file) {
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(file));
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object[] getCellData(XSSFRow row) {
		int count = row.getPhysicalNumberOfCells();
		Object[] data = new Object[count];
		for (int i = 0; i < count; i++) {
			XSSFCell cell = row.getCell(i);
			String value = null;
			if (cell == null) {
				data[i] = new String();
				continue;
			}
			if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
				data[i] = new String();
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					data[i] = cell.getDateCellValue();
				} else {
					data[i] = cell.getNumericCellValue();
				}
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
				data[i] = cell.getStringCellValue().trim();
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
				data[i] = new String();
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
				data[i] = String.valueOf(cell.getBooleanCellValue());
			}
		}
		return data;
	}

	public void downloadTable(File file, JXTable table, String[] tableColumnsText, int[] tableColumnsWidth) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("kospi");
		setStyles(workbook);
		for (int i = 0; i < tableColumnsWidth.length; i++) {
			sheet.setColumnWidth(i, tableColumnsWidth[i] * 50);
		}
		XSSFRow header = sheet.createRow(0);
		for (int i = 0; i < tableColumnsText.length; i++) {
			XSSFCell cell = header.createCell(i);
			cell.setCellValue(tableColumnsText[i]);
			cell.setCellStyle(getStyle("header"));
		}
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			XSSFRow row = sheet.createRow(i + 1);
			for (int j = 0; j < model.getColumnCount(); j++) {
				XSSFCell cell = row.createCell(j);
				Object object = model.getValueAt(i, j);
				if (object instanceof String) {
					cell.setCellValue((String)model.getValueAt(i, j));
				} else if (object instanceof Date) {
					cell.setCellValue(dateFormat.format((Date)model.getValueAt(i, j)));
				} else if (object instanceof Double) {
					cell.setCellValue((double)object);
				}
				cell.setCellStyle(getStyle("content"));
			}
		}
		write(file, workbook);
	}

	public void write(File file, XSSFWorkbook workbook) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CellStyle getStyle(String name) {
		return styles.get(name);
	}

	public void setStyles(Workbook workbook) {
		CellStyle style = createBorderedStyle(workbook);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(headerFont);
		styles.put("header", style);
		style = createBorderedStyle(workbook);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font contentFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		style.setFont(contentFont);
		styles.put("content", style);
	}

	private static CellStyle createBorderedStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}
}
