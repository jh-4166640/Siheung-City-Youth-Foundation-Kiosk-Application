package com.test.bg2kiosk;
import android.util.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    private static final String TAG = "ExcelReader";

    public static List<List<String>> readExcelFromStream(InputStream inputStream) {
        List<List<String>> excelData = new ArrayList<>();

        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트만 읽음

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValue(cell)); // 셀 값 읽기
                }
                excelData.add(rowData);
            }

            workbook.close();
        } catch (Exception e) {
            Log.e(TAG, "Excel 파일 읽기 오류: ", e);
        }

        return excelData;
    }

    // 셀의 데이터를 String 형태로 변환하는 메서드
    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "UNKNOWN";
        }
    }
}
