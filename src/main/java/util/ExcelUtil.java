package util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {

	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	private static Map<String, Map<String, String>> rowdata = new LinkedHashMap<>();
	private static Map<String, List<Map<String, String>>> sheetMap = new LinkedHashMap<>();
	
	public static void readExcel() {
		logger.info("Reading Excel file...");
		String path = System.getProperty("user.dir") + "/src/test/resources/testdata/TestData.xlsx";
		try {
			Workbook wb = WorkbookFactory.create(new File(path));
			int sheetCount = wb.getNumberOfSheets();
			for (int s = 0; s < sheetCount; s++) { // sheet loop
				logger.debug("Processing sheet: " + wb.getSheetName(s));
				List<Map<String, String>> sheetdata = new ArrayList<>();
				
				Sheet sheet = wb.getSheetAt(s);
				System.out.println("Sheet Name: " + sheet.getSheetName());
				int rowCount = sheet.getPhysicalNumberOfRows();
				System.out.println("Number of rows: " + rowCount);
				
				Row headerRow = sheet.getRow(0); // header row
				for (int i = 0; i < rowCount; i++) { // row loop
					logger.debug("Processing row: " + i);
					Map<String, String> celldata = new HashMap<>();
					Row row = sheet.getRow(i);
					int cellCount = row.getPhysicalNumberOfCells();
					for (int j = 0; j < cellCount; j++) { // cell loop
						celldata.put(headerRow.getCell(j).toString(), row.getCell(j).toString());
						logger.debug("Cell data - " + headerRow.getCell(j).toString() + ": " + row.getCell(j).toString());
					}
					rowdata.put(celldata.get("TestCaseName"), celldata);
					sheetdata.add(celldata);
				}
				
				sheetMap.put(sheet.getSheetName(), sheetdata);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error reading Excel file: " + e.getMessage());
		}
	}

	public static Map<String, String> getRowdata(String testCaseName) {
		return rowdata.get(testCaseName);
	}
	
	public static List<Map<String, String>> getSheetData(String sheetName) {
		return sheetMap.get(sheetName);
	}
	
	
	public static void main(String[] args) {
		readExcel();
		rowdata.forEach((k, v) -> {
			System.out.println("TestCaseName: " + k + " Data: " + v);
		});
		
	}
}
