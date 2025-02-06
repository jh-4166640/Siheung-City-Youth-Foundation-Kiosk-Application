package com.test.bg2kiosk;
//AdminFragment.java
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.activityViewModels;
import com.test.bg2kiosk.databinding.FragmentAdminBinding; // 바인딩 클래스 임포트
import android.widget.EditText;
import android.widget.TableRow;
import android.view.Gravity;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class AdminFragment extends Fragment{
    private FragmentAdminBinding adminBinding;
    private static ArrayList<EditText> spaceList = new ArrayList<>();
    private SpaceViewModel spaceViewModel;
    private static final String TAG = "adminfragment";
    private static final String sheetName = "Data_input";


    // 파일 선택 ActivityResultLauncher
    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri fileUri = result.getData().getData();
                    if (fileUri != null) {
                        SaveDataToExcel(fileUri);
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adminBinding = FragmentAdminBinding.inflate(inflater, container, false);
        spaceViewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel.class);

        adminBinding.btnSaveXLSXdata.setOnClickListener(v->{
            openFilePicker();
        });

        adminBinding.addSpaceButton.setOnClickListener(v -> {
            addSpace();
        });
        adminBinding.delSpaceButton.setOnClickListener(v -> {
            delSpace();
        });
        adminBinding.SaveSpaceButton.setOnClickListener(v -> {
            String[] spaceNames = SaveSpaceInfo();
            spaceViewModel.setSpaceNames(spaceNames);
            Toast.makeText(getContext(), "변경사항이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        });
        CreateSpaceEditor(spaceViewModel.getNumOfSpace().getValue());


        return adminBinding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adminBinding = null; // ViewBinding 객체 해제
    }
    public void CreateSpaceEditor(final int numSpace){
        //Toast.makeText(getContext(), numSpace, Toast.LENGTH_SHORT).show();
        if(numSpace == 0) return;
        String[] cur_name = spaceViewModel.getSpaceNames().getValue();
        int rows = 0;
        int spcnt = 0;

        if(numSpace < 3)                            rows = 1;
        else if(numSpace > 3 && numSpace <= 6)      rows = 2;
        else if(numSpace > 6 && numSpace <= 9)      rows = 3;
        else if(numSpace > 9 && numSpace <= 12)     rows = 4;
        else if(numSpace > 12 && numSpace <= 15)    rows = 5;
        for(int r = 0;r<rows;r++){
            TableRow tableRow = new TableRow(getContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            tableRow.setLayoutParams(params);
            tableRow.setGravity(Gravity.CENTER);
            int cols = 3;
            if (r == rows - 1) {
                cols = (numSpace % 3 == 0) ? 3 : numSpace % 3;  // 마지막 행의 남은 개수 계산
            }
            else cols = 3;
            params.setMargins(50,0,100,50);
            for (int col = 0; col < cols; col++) {
                EditText editSpace = new EditText(getContext());
                if(spcnt <= cur_name.length-1){
                    try{
                        if(cur_name[spcnt] != null && cur_name[spcnt] != ""){ //.equals("")형태로 바꿀거임
                            editSpace.setText(cur_name[spcnt]);
                        }
                        else{ editSpace.setHint("시설" + (spcnt+1) + "을 입력하세요"); }
                    } catch(IndexOutOfBoundsException e){editSpace.setHint("시설" + (spcnt+1) + "을 입력하세요");}
                }
                else {editSpace.setHint("시설" + (spcnt+1) + "을 입력하세요");}
                editSpace.setHeight(100);
                editSpace.setWidth(500);
                editSpace.setLayoutParams(params);
                editSpace.setPadding(10,10,10,10);
                tableRow.addView(editSpace);
                spaceList.add(editSpace);
                spcnt++;
            }
            adminBinding.tableLayout.addView(tableRow);
        }
    }

    private void addSpace() {
        int cur_space = spaceViewModel.getNumOfSpace().getValue();
        if(cur_space  >= 15) return;
        cur_space++;
        int rowCount = adminBinding.tableLayout.getChildCount();
        TableRow lastRow = (rowCount > 0) ? (TableRow) adminBinding.tableLayout.getChildAt(rowCount-1): null;
        if(lastRow != null && lastRow.getChildCount() < 3){ //마지막 TableRow가 3미만이면 그 Row에 추가
            EditText editText = new EditText(getContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            lastRow.setGravity(Gravity.CENTER);
            params.setMargins(50,0,100,50);
            EditText editSpace = new EditText(getContext());
            editSpace.setHint("시설" + cur_space + "을 입력하세요");
            editSpace.setHeight(100);
            editSpace.setWidth(500);
            editSpace.setLayoutParams(params);
            editSpace.setPadding(10,10,10,10);
            lastRow.addView(editSpace);
            spaceList.add(editSpace);
        } else { // 3개로 꽉 채워져 있다면 새 행 추가
            TableRow new_tableRow = new TableRow(getContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            new_tableRow.setLayoutParams(params);
            new_tableRow.setGravity(Gravity.CENTER);
            params.setMargins(50,0,100,50);
            EditText editSpace = new EditText(getContext());
            editSpace.setHint("시설" + cur_space + "을 입력하세요");
            editSpace.setHeight(100);
            editSpace.setWidth(500);
            editSpace.setLayoutParams(params);
            editSpace.setPadding(10,10,10,10);
            new_tableRow.addView(editSpace);
            spaceList.add(editSpace);
            adminBinding.tableLayout.addView(new_tableRow);
        }
        spaceViewModel.setNumOfSpace(cur_space);
    }
    private void delSpace(){
        int cur_space = spaceViewModel.getNumOfSpace().getValue();
        if(cur_space <= 1) return;
        int rowCount = adminBinding.tableLayout.getChildCount();
        if(rowCount == 0) return;
        TableRow lastRow = (rowCount > 0) ? (TableRow) adminBinding.tableLayout.getChildAt(rowCount-1): null;
        int childCount = lastRow.getChildCount();
        if(childCount > 0)
        {
            lastRow.removeViewAt(childCount-1);
            spaceList.remove(spaceList.size()-1);
            if(lastRow.getChildCount() == 0){
                adminBinding.tableLayout.removeView(lastRow);
            }
            spaceViewModel.setNumOfSpace(cur_space-1);
        }
    }
    @NonNull
    private String[] SaveSpaceInfo(){
        String[] spaceName = new String[spaceList.size()];
        for(int i = 0;i<spaceList.size();i++){
            spaceName[i] = spaceList.get(i).getText().toString();
        }
        return spaceName;
    }

    private void openFilePicker(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(intent);
    }
    private String getFileNameFromUri(Uri uri) {
        String fileName = "unknown.xlsx"; // 기본 파일명 설정
        Cursor cursor = requireContext().getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex != -1 && cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return fileName;
    }
    private void SaveDataToExcel(Uri fileUri) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            Workbook workbook = null;
            try {
                // 파일에 대한 권한을 요청합니다.
                requireContext().getContentResolver().takePersistableUriPermission(fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                // 파일 읽기
                inputStream = requireContext().getContentResolver().openInputStream(fileUri);
                if (inputStream == null) {
                    throw new IOException("Failed to open InputStream for fileUri: " + fileUri);
                }

                String fileName = getFileNameFromUri(fileUri);
                requireActivity().runOnUiThread(() -> adminBinding.tvFileName.setText(fileName));

                workbook = new XSSFWorkbook(inputStream);
                Sheet dataInputSheet = workbook.getSheetAt(0); // 첫 번째 시트

                // 데이터베이스에서 데이터 조회
                AppDatabase db = AppDatabase.getDatabase(getContext());
                List<VisitorStatistics> cur_dataList = db.visitorStatisticsDao().getAllStatistics();

                int lastRowNum = 1;
                for (VisitorStatistics data : cur_dataList) {
                    Row row = dataInputSheet.createRow(lastRowNum++);
                    row.createCell(0).setCellValue(data.getDate());
                    row.createCell(1).setCellValue(data.getSpaceName());
                    row.createCell(2).setCellValue(data.getTotal());
                    row.createCell(3).setCellValue(data.getTotal_youth());
                    row.createCell(4).setCellValue(data.getAge9to13_male());
                    row.createCell(5).setCellValue(data.getAge9to13_female());
                    row.createCell(6).setCellValue(data.getAge14to16_male());
                    row.createCell(7).setCellValue(data.getAge14to16_female());
                    row.createCell(8).setCellValue(data.getAge17to19_male());
                    row.createCell(9).setCellValue(data.getAge17to19_female());
                    row.createCell(10).setCellValue(data.getAge20to24_male());
                    row.createCell(11).setCellValue(data.getAge20to24_female());
                    row.createCell(12).setCellValue(data.getTotal_infant());
                    row.createCell(13).setCellValue(data.getAge0to8_male());
                    row.createCell(14).setCellValue(data.getAge0to8_female());
                    row.createCell(15).setCellValue(data.getTotal_adult());
                    row.createCell(16).setCellValue(data.getAge25over_male());
                    row.createCell(17).setCellValue(data.getAge25over_female());
                }
                requireContext().getContentResolver().takePersistableUriPermission(fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // 파일 쓰기
                outputStream = requireContext().getContentResolver().openOutputStream(fileUri);
                if (outputStream == null) {
                    throw new IOException("Failed to open OutputStream for fileUri: " + fileUri);
                }
                requireContext().getContentResolver().takePersistableUriPermission(fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                workbook.write(outputStream); // 수정된 데이터를 덮어쓰기
                requireActivity().runOnUiThread(() -> {
                    Log.d(TAG, "엑셀 파일에 데이터 저장 성공");
                    Toast.makeText(getContext(), "엑셀 파일이 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Log.e(TAG, "엑셀 파일 저장 실패", e);
                    Toast.makeText(getContext(), "엑셀 파일 저장 실패", Toast.LENGTH_SHORT).show();
                });
            } finally {
                // 스트림 및 리소스 안전하게 닫기
                try {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                    if (workbook != null) workbook.close();
                } catch (IOException e) {
                    Log.e(TAG, "스트림 닫기 실패", e);
                }
            }
        });
    }

}
