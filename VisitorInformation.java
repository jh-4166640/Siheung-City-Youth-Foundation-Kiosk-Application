package com.test.bg2kiosk;

import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Button;
import android.graphics.Color;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.lang.Math;

public class VisitorInformation extends Fragment{

    private final RadioGroup radioGroupGender;
    private final Button submitBtn;
    private final Button[] ageBtnGroup;
    private Button selectedAgeBtn = null;
    private ToggleButton[] spaceBtnGroup;
    private int SelectedSpace = 0;
    private String[] spacename;


    public VisitorInformation(RadioGroup radioGroupGender, Button submitBtn, Button[] ageBtnGroup, ToggleButton[] spaceBtnGroup, String[] spacename)
    {
        this.radioGroupGender = radioGroupGender;
        this.ageBtnGroup = ageBtnGroup;
        this.submitBtn = submitBtn;
        this.spaceBtnGroup = spaceBtnGroup;
        this.spacename = spacename;
        // Click Listener 등록
        for(Button agebtn: this.ageBtnGroup) {
            setAgeButtonClickListener(agebtn);
        }
        int roomnum = 0;
        for(ToggleButton spacebtn: this.spaceBtnGroup){
            setSpaceButtonClickListener(spacebtn, (int)Math.pow(2,roomnum), this.spacename[roomnum]);
            roomnum++;
        }
        setSubmitBtnClickListener(this.submitBtn);
    }

    public String getSelectedGender(Context context){ // 성별을 선택하는 라디오버튼 핸들러
        // int로 선언해도 되는 이유는 component의 Id가 고유의 정수 값으로 저장되기 때문이다.
        int selectedId = radioGroupGender.getCheckedRadioButtonId();

        if(selectedId != -1){ //선택된 버튼이 있으면 선택된 라디오버튼의 Id가 selectedId에 들어있다.
            RadioButton selectedRadioButton = radioGroupGender.findViewById(selectedId);

            return selectedRadioButton.getText().toString(); // 선택된 라디오버튼의 텍스트 값 반환
        }
        else { // 선택된 버튼이 없으면
            // Toast 메세지로 성별이 선택되지 않았음을 알리는 메세지를 출력한다.
            Toast.makeText(context, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    public void setAgeButtonClickListener(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미 선택된 버튼이 있으면 선택 해제
                if (selectedAgeBtn != null) {
                    selectedAgeBtn.setTextColor(Color.BLACK);  // 기본 텍스트 색상
                    selectedAgeBtn.setBackgroundColor(Color.WHITE);  // 기본 배경색
                }
                // 클릭된 버튼을 선택된 버튼으로 설정
                if (selectedAgeBtn != button) {
                    selectedAgeBtn = button;
                    selectedAgeBtn.setTextColor(Color.WHITE);  // 선택된 버튼 텍스트 색상
                    selectedAgeBtn.setBackgroundColor(Color.GREEN);  // 선택된 버튼 배경색

                } else {
                    selectedAgeBtn = null;  // 선택 해제
                    button.setTextColor(Color.BLACK);  // 기본 텍스트 색상
                    button.setBackgroundColor(Color.WHITE);  // 기본 배경색
                }
            }
        });
    }
    public String getSelectedAge(Context context){ // 나이를 선택하는 버튼 그룹(라디오 버튼 처럼 작동)
        if(selectedAgeBtn == ageBtnGroup[0]){ // 1~8세
            return "8";
        }
        else if(selectedAgeBtn == ageBtnGroup[1]){ // 9~13세
            return "13";
        }
        else if(selectedAgeBtn == ageBtnGroup[2]){ // 14~16세
            return "16";
        }
        else if(selectedAgeBtn == ageBtnGroup[3]){ // 17~19세
            return "19";
        }
        else if(selectedAgeBtn == ageBtnGroup[4]){ // 20~24세
            return "24";
        }
        else if(selectedAgeBtn == ageBtnGroup[5]){ // 25세 이상
            return "25";
        }
        else {  //선택 안함
            // Toast 메세지로 성별이 선택되지 않았음을 알리는 메세지를 출력한다.
            Toast.makeText(context, "나이를 선택하세요.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    public void addSelectedSpace(int num){
        this.SelectedSpace += num;
    }
    public void deleteSelectedSpace(int num){
        this.SelectedSpace -= num;
    }
    public String getSelectedSpace(){
        return Integer.toString(SelectedSpace);
    }
    public void setSpaceButtonClickListener(final ToggleButton spbtn, int spaceUsenum, String spbtntext){
        spbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                if(status){
                    // 활성화
                    addSelectedSpace(spaceUsenum);
                    spbtn.setTextColor(Color.BLACK);  // 기본 텍스트 색상
                    spbtn.setBackgroundColor(Color.YELLOW);  // 기본 배경색
                    spbtn.setText(spbtntext);
                }
                else{
                    // 비활성화
                    deleteSelectedSpace(spaceUsenum);
                    spbtn.setTextColor(Color.BLACK);  // 기본 텍스트 색상
                    spbtn.setBackgroundColor(Color.WHITE);  // 기본 배경색
                    spbtn.setText(spbtntext);
                }
            }
        });
    }
    public String[] InfoCheck(){
        String [] info_buffer = new String[4]; // 성별, 나이, 이용시설이 Excel 데이터베이스에 저장되기 전에 저장할 용도

        info_buffer[3] = "chk";
        Context genderContext = radioGroupGender.getContext();
        String selectedGender = getSelectedGender(genderContext);
        if (selectedGender != null) {
            info_buffer[0] = selectedGender;
        } else {
            info_buffer[3] = null;
        }
        Context ageContext = ageBtnGroup[0].getContext();
        String selectedAge = getSelectedAge(ageContext);
        if (selectedAge != null) {
            info_buffer[1] = selectedAge;
        } else {
            info_buffer[3] = null;
        }
        info_buffer[2] = getSelectedSpace();
        return info_buffer;
    }
    public void setSubmitBtnClickListener(Button sbbtn){
        sbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] peopleInfo=InfoCheck();
                if(peopleInfo[3] != null) {

                }
            }
        });
    }
}