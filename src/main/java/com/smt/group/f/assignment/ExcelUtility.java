package com.smt.group.f.assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {


    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private File file;
    public static String PASS="PASS";
    public static String FAIL="FAIL";
    public static int STATUS_COLUMN=9;
    public static int ACTUAL_RESULT_COLUMN=8;
    public static int TEST_CASE_ID_COLUMN=0;
    public static int TEST_CASE_START_ROW=7;
    public static int TEST_CASE_END_ROW=10;


    public ExcelUtility() {
        readExcelFile();
    }

    private void readExcelFile() {
        try {
            this.file = new File("Template-TestCase.xlsx");
            FileInputStream fis = new FileInputStream(file);
            this.workbook = new XSSFWorkbook(fis);
            this.sheet = workbook.getSheet("Sheet1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // print all cell values
    public void printAllCellValues() {
        for (Row row : this.sheet) {
            for (Cell cell : row) {
                System.out.print(row.getRowNum() + " " + cell.getColumnIndex() + " -> " + cell.getStringCellValue() + " ");
                //System.out.print(cell.getStringCellValue() + " ");
            }
            System.out.println("\n--------------------------------");
        }
    }

    // get cell value
    public String getCellValue(int row, int column) {
        return this.sheet.getRow(row).getCell(column).getStringCellValue();
    }

    //set cell value
    public void setCellValue(int row, int column, String value) {
        this.sheet.getRow(row).getCell(column).setCellValue(value);
    }



    public XSSFSheet getSheet() {
        return this.sheet;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    //save workbook
    public void saveWorkbook() {
        try {
            FileOutputStream fos = new FileOutputStream(this.file);
            this.workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
