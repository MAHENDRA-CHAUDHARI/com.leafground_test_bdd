package com.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class DataReader {

    public static String[][] getDataSheetTCData(String workBook, String sheetName, String testCaseName) {
        String[][] filterData = new String[0][0];

        try (FileInputStream fis = new FileInputStream(new File("./DataSheets/" + workBook));
             var workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet '" + sheetName + "' not found in workbook.");
                return filterData;
            }

            DataFormatter formatter = new DataFormatter();
            int rowCount = sheet.getPhysicalNumberOfRows();
            if (rowCount < 2) { // no data rows
                return filterData;
            }

            // Count meaningful data columns (exclude first column)
            Row headerRow = sheet.getRow(0);
            int colCount = 0;
            for (int c = 1; c < headerRow.getLastCellNum(); c++) {
                Cell cell = headerRow.getCell(c);
                if (cell != null && !formatter.formatCellValue(cell).trim().isEmpty()) {
                    colCount++;
                }
            }

            if (colCount == 0) {
                return filterData; // no data columns
            }

            // Collect rows matching the test case name
            List<String[]> matchedRows = new ArrayList<>();
            for (int r = 1; r < rowCount; r++) { // skip header row
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Cell firstCell = row.getCell(0); // test case name
                if (firstCell != null && formatter.formatCellValue(firstCell).equals(testCaseName)) {
                    String[] rowData = new String[colCount];
                    for (int c = 1; c <= colCount; c++) { // start from 1 to skip test case column
                        Cell cell = row.getCell(c);
                        rowData[c - 1] = (cell == null) ? "" : formatter.formatCellValue(cell);
                    }
                    matchedRows.add(rowData);
                }
            }
            // Convert to 2D array
            filterData = matchedRows.toArray(new String[0][]);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filterData;
    }
}