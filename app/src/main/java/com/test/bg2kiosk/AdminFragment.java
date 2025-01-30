package com.test.bg2kiosk;
//AdminFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
//import androidx.fragment.app.activityViewModels;
import com.test.bg2kiosk.databinding.FragmentAdminBinding; // 바인딩 클래스 임포트
import android.widget.EditText;
import android.widget.TableRow;
import android.view.Gravity;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;


import java.util.ArrayList;

public class AdminFragment extends Fragment{
    private FragmentAdminBinding adminBinding;
    private static ArrayList<EditText> spaceList = new ArrayList<>();
    private SpaceViewModel spaceViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adminBinding = FragmentAdminBinding.inflate(inflater, container, false);
        spaceViewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel.class);

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
        //readExcelFile(filepath);

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
    private String[] SaveSpaceInfo(){
        String[] spaceName = new String[spaceList.size()];
        for(int i = 0;i<spaceList.size();i++){
            spaceName[i] = spaceList.get(i).getText().toString();
        }
        return spaceName;
    }
}
