package com.test.bg2kiosk;
//AdminFragment.java

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.activityViewModels;
import com.test.bg2kiosk.databinding.FragmentAdminBinding; // 바인딩 클래스 임포트
import android.widget.EditText;
import android.widget.TableRow;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProvider;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.io.File;
import java.io.IOException;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class AdminFragment extends Fragment{
    private FragmentAdminBinding adminBinding;
    private static ArrayList<EditText[]> spaceList = new ArrayList<>();
    private SpaceViewModel spaceViewModel;
    private static final String INTERNAL_FILE_NAME = "raw/visitor_data.xlsx";
    private static final String TAG = "adminfragment";
    private static final String SHEET_NAME = "데이터 기입창";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adminBinding = FragmentAdminBinding.inflate(inflater, container, false);
        spaceViewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel.class);

        adminBinding.btnSaveXLSXdata.setOnClickListener(v->{
            saveDataToExcel();
        });

        adminBinding.addSpaceButton.setOnClickListener(v -> {
            addSpace();
        });
        adminBinding.delSpaceButton.setOnClickListener(v -> {
            delSpace();
        });
        adminBinding.SaveSpaceButton.setOnClickListener(v -> {
            String[][] spaceData = SaveSpaceInfo();
            String division=adminBinding.Division.getText().toString();
            String office_name = adminBinding.OfficeName.getText().toString();
            if(division.length() == 0){
                spaceViewModel.setDivision("본부 미입력");
            }else{

                spaceViewModel.setDivision(division);
                adminBinding.Division.setText(division);
            }
            if(office_name.length()==0){
                spaceViewModel.setOfficeName("시설명 미입력");
            }else{
                spaceViewModel.setOfficeName(office_name);
                adminBinding.OfficeName.setText(office_name);
            }
            spaceViewModel.setSpaceTask(spaceData[0]);
            spaceViewModel.setProgramClassification(spaceData[1]);
            spaceViewModel.setProgramArea(spaceData[2]);
            spaceViewModel.setSpaceNames(spaceData[3]);
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

    private EditText setSpaceEditText(int spcnt, String[] cur_name, String hintText){
        EditText editSpace=new EditText(getContext());
        if(spcnt <= cur_name.length-1){
            try {
                if(cur_name[spcnt] != null && !cur_name[spcnt].equals("")) { //.equals("")형태로 바꿀거임
                    editSpace.setText(cur_name[spcnt]);
                    editSpace.setTextColor(Color.parseColor("#000000"));
                } else{
                    editSpace.setHint(hintText);
                    editSpace.setHintTextColor(Color.parseColor("#FF0000"));
                }
            }catch (IndexOutOfBoundsException e){
                Toast.makeText(getContext(),"생성 실패",Toast.LENGTH_SHORT).show();
            }
        } else {
            editSpace.setHint(hintText);
            editSpace.setHintTextColor(Color.parseColor("#FF0000"));
        }
        editSpace.setHeight(100);
        editSpace.setWidth(500);
        editSpace.setPadding(10,10,10,10);

        return editSpace;
    }

    private EditText setSpaceNameEditText(int spcnt, String[] cur_name){
        EditText editSpace=new EditText(getContext());
        if(spcnt <= cur_name.length-1){
            try {
                if(cur_name[spcnt] != null && !cur_name[spcnt].equals("")){ //.equals("")형태로 바꿀거임
                    editSpace.setText(cur_name[spcnt]);
                    editSpace.setTextColor(Color.parseColor("#000000"));
                } else{
                    editSpace.setHint("프로그램명을 입력하세요");
                    editSpace.setHintTextColor(Color.parseColor("#FF0000"));
                }
            }catch (IndexOutOfBoundsException e){
                Toast.makeText(getContext(),cur_name[spcnt] + " 실패",Toast.LENGTH_SHORT).show();
            }
        }else{
            editSpace.setHint("프로그램명을 입력하세요");
            editSpace.setHintTextColor(Color.parseColor("#FF0000"));
        }
        editSpace.setHeight(100);
        editSpace.setWidth(500);
        editSpace.setPadding(10,10,10,10);

        return editSpace;
    }
    public void CreateSpaceEditor(final int numSpace){
        //Toast.makeText(getContext(), numSpace, Toast.LENGTH_SHORT).show();
        if(numSpace == 0) return;
        String cur_division = spaceViewModel.getDivision().getValue();
        String cur_officeName = spaceViewModel.getOfficeName().getValue();
        spaceList.clear();
        if(cur_division != null && !cur_division.equals("본부 미입력")){
            adminBinding.Division.setText(cur_division);
        }
        if(cur_officeName != null && !cur_officeName.equals("시설명 미입력")){
            adminBinding.OfficeName.setText(cur_officeName);
        }
        String[] cur_name = spaceViewModel.getSpaceNames().getValue();
        String[] cur_task = spaceViewModel.getSpaceTask().getValue();
        String[] cur_classification = spaceViewModel.getProgramClassification().getValue();
        String[] cur_area = spaceViewModel.getProgramArea().getValue();

        int rows = numSpace;
        int spcnt = 0;

        TableRow tableHead = new TableRow(getContext());
        TableRow.LayoutParams tbr_params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        tableHead.setGravity(Gravity.CENTER);
        tableHead.setLayoutParams(tbr_params);
        tbr_params.setMargins(10,10,10,30);
        TextView[] headTextView = new TextView[4];
        String[] headName = {"실행과제", "프로그램 구분", "분야", "시설"};
        for(int i =0 ;i<4;i++){
            headTextView[i] = new TextView(getContext());
            headTextView[i].setText(headName[i]);
            headTextView[i].setHeight(100);
            headTextView[i].setWidth(500);
            headTextView[i].setGravity(Gravity.CENTER);
            tableHead.addView(headTextView[i]);
        }
        TextView headTextView1 = new TextView(getContext());
        TextView headTextView2 = new TextView(getContext());
        TextView headTextView3 = new TextView(getContext());
        TextView headTextView4 = new TextView(getContext());

        adminBinding.tableLayout.addView(tableHead);

        for(int r = 0;r<rows;r++){
            TableRow tableRow = new TableRow(getContext());

            tableRow.setLayoutParams(tbr_params);
            tableRow.setGravity(Gravity.CENTER);
            tbr_params.setMargins(10,10,10,30);

            EditText[] editSpace = new EditText[4];
            editSpace[0] = setSpaceEditText(spcnt, cur_task, "실행과제");
            editSpace[1] = setSpaceEditText(spcnt, cur_classification, "프로그램구분");
            editSpace[2] = setSpaceEditText(spcnt, cur_area, "분야");
            editSpace[3] = setSpaceNameEditText(spcnt, cur_name);
            /*
            editSpace[0].setLayoutParams(tbr_params);
            editSpace[1].setLayoutParams(tbr_params);
            editSpace[2].setLayoutParams(tbr_params);
            editSpace[3].setLayoutParams(tbr_params);
            */
            tableRow.addView(editSpace[0]);
            tableRow.addView(editSpace[1]);
            tableRow.addView(editSpace[2]);
            tableRow.addView(editSpace[3]);
            spaceList.add(editSpace);
            spcnt++;

            adminBinding.tableLayout.addView(tableRow);
        }
    }



    private void addSpace() {
        int cur_space = spaceViewModel.getNumOfSpace().getValue();
        String[] cur_name = spaceViewModel.getSpaceNames().getValue();
        String[] cur_task = spaceViewModel.getSpaceTask().getValue();
        String[] cur_classification = spaceViewModel.getProgramClassification().getValue();
        String[] cur_area = spaceViewModel.getProgramArea().getValue();
        if(cur_space  >= 15) return;
        cur_space++;
        int rowCount = adminBinding.tableLayout.getChildCount();
        TableRow newRow = new TableRow(getContext());

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        newRow.setLayoutParams(params);
        newRow.setGravity(Gravity.CENTER);
        EditText[] editSpace = new EditText[4];
        editSpace[0] = setSpaceEditText(cur_space, cur_task, "실행과제");
        editSpace[1] = setSpaceEditText(cur_space, cur_classification, "프로그램구분");
        editSpace[2] = setSpaceEditText(cur_space, cur_area, "분야");
        editSpace[3] = setSpaceNameEditText(cur_space, cur_name);
        newRow.addView(editSpace[0]);
        newRow.addView(editSpace[1]);
        newRow.addView(editSpace[2]);
        newRow.addView(editSpace[3]);
        spaceList.add(editSpace);
        spaceViewModel.setNumOfSpace(cur_space);
        adminBinding.tableLayout.addView(newRow);
    }
    private void delSpace(){
        int cur_space = spaceViewModel.getNumOfSpace().getValue();
        Log.d(TAG,cur_space + "...count");
        if(cur_space <= 1) return;
        int rowCount = adminBinding.tableLayout.getChildCount();
        if(rowCount == 0) return;
        TableRow lastRow = (rowCount > 0) ? (TableRow) adminBinding.tableLayout.getChildAt(rowCount-1): null;
        spaceList.remove(spaceList.size()-1);
        adminBinding.tableLayout.removeView(lastRow);
        spaceViewModel.setNumOfSpace(cur_space-1);
    }
    @NonNull
    private String[][] SaveSpaceInfo(){

        int arr_size = spaceList.size();
        String[][] result = new String[4][arr_size];
        String[] spaceTask = new String[arr_size];
        String[] spaceClassification = new String[arr_size];
        String[] spaceArea = new String[arr_size];
        String[] spaceName = new String[arr_size];
        for(int i = 0;i<arr_size;i++){
            EditText[] editTexts = spaceList.get(i);
            spaceTask[i] = editTexts[0].getText().toString();
            spaceClassification[i] = editTexts[1].getText().toString();
            spaceArea[i] = editTexts[2].getText().toString();
            spaceName[i] = editTexts[3].getText().toString();
        }
        result[0] = spaceTask;
        result[1] = spaceClassification;
        result[2] = spaceArea;
        result[3] = spaceName;
        return result;
    }

    public File getInternalFile(Context context) {
        File file = new File(context.getFilesDir(), INTERNAL_FILE_NAME);
        Log.d("FileCheck", "File path" + file.getAbsolutePath());
        return file;
    }


    private void saveDataToExcel(){
        String []headRows = {"월", "일자", "본부", "시설명", "실행과제", "프로그램구분", "분야", "프로그램명(띄어쓰기금지)",
        "합계","청소년(계)", "9-13(남)", "9-13(여)", "14-16(남)", "14-16(여)", "17-19(남)", "17-19(여)", "20-24(남)",
                "20-24(여)", "유아(계)", "유(남)", "유(여)", "일반(계)", "성인(남)","성인(여)"};
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            File inputFile = new File(getContext().getFilesDir(), "visitor_data.xlsx");
            try(FileInputStream fis = new FileInputStream(inputFile);
                Workbook workbook = new XSSFWorkbook(fis)){
                Log.d(TAG,"open data");
                AppDatabase db = AppDatabase.getDatabase(requireContext());
                List<VisitorStatistics> statisticsList = db.visitorStatisticsDao().getAllStatistics();

                Sheet statistic_sheet = workbook.getSheet(SHEET_NAME);
                if(statistic_sheet == null){ /**    must create headRow     **/
                    statistic_sheet = workbook.createSheet(SHEET_NAME);
                    Row headRow = statistic_sheet.createRow(0);
                    for(int i = 0; i<headRows.length;i++){
                        headRow.createCell(i).setCellValue(headRows[i]);
                    }
                    Log.d("make header", "헤더 행 생성 완료");
                }
                int LastRow = statistic_sheet.getLastRowNum() + 1;
                for (VisitorStatistics stats : statisticsList){
                    Log.d("Last Row", LastRow + "");
                    Row row = statistic_sheet.createRow(LastRow++);
                    String date = stats.getDate();
                    String month;
                    if(date.charAt(5) == '0')
                    {
                        month = date.charAt(6) + "월";
                    } else {
                        month = date.substring(5,7) + "월";
                    }
                    row.createCell(0).setCellValue(month); // 월
                    row.createCell(1).setCellValue(date);  // 일자

                    row.createCell(2).setCellValue(spaceViewModel.getDivision().getValue()); ///수정 해야 할 것 -> text Edit으로 몇본부 인지 받아오기(ex 사업3본부)
                    row.createCell(3).setCellValue(spaceViewModel.getOfficeName().getValue()); ///text Edit으로 무슨 시설인지 받아오기(ex 배곧2, 배곧1, 능곡)
                    row.createCell(4).setCellValue(stats.getSpaceTask()); //얘도 적을 수 있게
                    row.createCell(5).setCellValue(stats.getSpaceClassification()); //이것도 시설명 입력할 때 같이
                    row.createCell(6).setCellValue(stats.getSpaceArea()); //얘도 마찬가지

                    row.createCell(7).setCellValue(stats.getSpaceName()); // 프로그램명
                    row.createCell(8).setCellValue(stats.getTotal());

                    row.createCell(9).setCellValue(stats.getTotal_youth());
                    row.createCell(10).setCellValue(stats.getAge9to13_male());
                    row.createCell(11).setCellValue(stats.getAge9to13_female());
                    row.createCell(12).setCellValue(stats.getAge14to16_male());
                    row.createCell(13).setCellValue(stats.getAge14to16_female());
                    row.createCell(14).setCellValue(stats.getAge17to19_male());
                    row.createCell(15).setCellValue(stats.getAge17to19_female());
                    row.createCell(16).setCellValue(stats.getAge20to24_male());
                    row.createCell(17).setCellValue(stats.getAge20to24_female());

                    row.createCell(18).setCellValue(stats.getTotal_infant());
                    row.createCell(19).setCellValue(stats.getAge0to8_male());
                    row.createCell(20).setCellValue(stats.getAge0to8_female());

                    row.createCell(21).setCellValue(stats.getTotal_adult());
                    row.createCell(22).setCellValue(stats.getAge25over_male());
                    row.createCell(23).setCellValue(stats.getAge25over_female());
                    Log.d("statistics 확인", stats.toString());
                }
                try(FileOutputStream fos = new FileOutputStream(inputFile)){
                    workbook.write(fos);
                    fos.flush();
                    fos.close();
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG,"파일 쓰기에서 에러",e);
                    throw new RuntimeException(e);
                }
                saveToExternalStorage(inputFile);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "엑셀 데이터 저장 완료", Toast.LENGTH_SHORT).show();
                });
                db.visitorStatisticsDao().deleteAll();


                //fis.close(); //이게 있어야 되는지 아닌지 모르겠음

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG,"찾을 수 없다",e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"엑셀 데이터 저장 실패",e);
                throw new RuntimeException(e);
            }
        });
    }

    private void saveToExternalStorage(File internalFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = requireContext().getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "visitor_data.xlsx");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

            Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), values);
            if (uri != null) {
                try {
                    OutputStream os = resolver.openOutputStream(uri);
                    InputStream is = new FileInputStream(internalFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                    is.close();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "saveToExternal Error", e);
                }
            }
        }
    }
}
