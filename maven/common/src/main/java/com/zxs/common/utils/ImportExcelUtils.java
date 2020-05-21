package com.zxs.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportExcelUtils {

    public static void importExcle(String fileName, InputStream inputStream) throws IOException {
        DecimalFormat df = new DecimalFormat("0");
        if (fileName.endsWith("xls")) {
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            // 行数
            int realRows = sheet.getLastRowNum() + 1;
            for (int i = 1; i < realRows; i++) { // 从第2行开始取数据,默认第一行是表头.
                HSSFRow row = sheet.getRow(i);
                // 列数
                int cellNum = row.getLastCellNum();
                for (int j = 0; j < cellNum; j++) {
                    HSSFCell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }
                    if (CellType.NUMERIC == row.getCell(0).getCellTypeEnum()) {
                        System.out.println(" 行：" + i + " 列：" + j + " 值：" + df.format(cell.getNumericCellValue()).trim());
                    } else if (CellType.STRING == row.getCell(0).getCellTypeEnum()) {
                        System.out.println(" 行：" + i + " 列：" + j + " 值：" + cell.getStringCellValue());
                    } else if (CellType.FORMULA == cell.getCellTypeEnum()) {
                        try {
                            System.out.println(" 行：" + i + " 列：" + j + " 值：" + cell.getNumericCellValue());
                        } catch (IllegalStateException e) {
                            System.out.println(" 行：" + i + " 列：" + j + " 值：" + cell.getStringCellValue());
                        }
                    } else if (CellType.BLANK == cell.getCellTypeEnum()) {
                        System.out.println(" 行：" + i + " 列：" + j + " 值：空");
                    }
                }
            }
        } else if (fileName.endsWith("xlsx")) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 行数
            int realRows = sheet.getLastRowNum() + 1;
            for (int i = 1; i < realRows; i++) {// 从第1行开始取数据,默认第一行是表头.
                XSSFRow row = sheet.getRow(i);
                // 列数
                int cellNum = row.getLastCellNum();
                for (int j = 0; j < cellNum; j++) {
                    XSSFCell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }
                    if (CellType.NUMERIC == cell.getCellTypeEnum()) {
                        System.out.println(" 行：" + i + " 列：" + j + " 值：" + df.format(cell.getNumericCellValue()).trim());
                    } else if (CellType.STRING == cell.getCellTypeEnum()) {
                        System.out.println(" 行：" + i + " 列：" + j + " 值：" + cell.getStringCellValue());
                    } else if (CellType.FORMULA == cell.getCellTypeEnum()) {
                        try {
                            System.out.println(" 行：" + i + " 列：" + j + " 值：" + cell.getNumericCellValue());
                        } catch (IllegalStateException e) {
                            System.out.println(" 行：" + i + " 列：" + j + " 值：" + cell.getStringCellValue());
                        }
                    } else if (CellType.BLANK == cell.getCellTypeEnum()) {
                        System.out.println(" 行：" + i + " 列：" + j + " 值：空");
                    }
                }
            }
        }
        return;
    }

}
