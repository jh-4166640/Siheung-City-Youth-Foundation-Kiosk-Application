package com.test.bg2kiosk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.test.bg2kiosk.databinding.FragmentAdminBinding; // 바인딩 클래스 임포트

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import android.widget.Toast;

public class AdminFragment extends Fragment{
    private FragmentAdminBinding adminBinding;
    private static final int REQUEST_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adminBinding = FragmentAdminBinding.inflate(inflater, container, false);
        String filepath = "";
        readExcelFile(filepath);

        return adminBinding.getRoot();
    }
    @Override
    public void onDestroyView() {

        super.onDestroyView();
        adminBinding = null; // ViewBinding 객체 해제
    }
    private void readExcelFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            // 첫 번째 시트를 선택합니다
            Sheet sheet = workbook.getSheetAt(0);

            // 시트의 모든 행을 순회합니다
            for (Row row : sheet) {
                // 각 행의 모든 셀을 순회합니다
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            content.append(cell.getStringCellValue()).append("\t");
                            break;
                        case NUMERIC:
                            content.append(cell.getNumericCellValue()).append("\t");
                            break;
                        case BOOLEAN:
                            content.append(cell.getBooleanCellValue()).append("\t");
                            break;
                        default:
                            content.append(" ").append("\t");
                            break;
                    }
                }
                content.append("\n");
            }

            // 읽어온 데이터를 TextView에 설정
            adminBinding.tvFilePath.setText(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error reading file", Toast.LENGTH_SHORT).show();
        }

    }

}
