
package moje;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class XLS
{
	HSSFWorkbook	workbook;
	HSSFSheet		sheet;
	public int		rows;

	public XLS() {
	    FileInputStream file = null;
		try {
			file = new FileInputStream(new File("graf.xls"));
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}		 
	    try {
			workbook = new HSSFWorkbook(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	    sheet = workbook.getSheetAt(0);
	    rows = 0;
	}
	
	public void addRow(Integer x, Double y) {
		Row row = sheet.createRow(rows);
		row.createCell(0).setCellValue(x);
		row.createCell(1).setCellValue(y);
		rows++;
	}

	public void write() {
		try {
			FileOutputStream outFile = new FileOutputStream(new File("graf.xls"));
			workbook.write(outFile);
			outFile.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		XLS xls = new XLS();
		xls.addRow(0, 4.5);
		xls.addRow(0, 4.5);
		xls.addRow(0, 4.5);
		xls.write();
	}

}
