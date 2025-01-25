package com.test.bg2kiosk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ToggleButton;
import androidx.fragment.app.Fragment;
import com.test.bg2kiosk.databinding.FragmentVisitorBinding; // 바인딩 클래스 임포트
import android.widget.Toast;
import java.util.ArrayList;

public class VisitorFragment extends Fragment {
    protected final int BUFFER_SIZE = 100;
    protected final int LENTH_OF_DATA = 3;
    protected int[] visitorData = new int[LENTH_OF_DATA];
    protected boolean checkedGender = false;
    protected boolean checkedAge = false;
    protected static int visitorCnt = 0;
    protected int[][] buffer_visitorData = new int[BUFFER_SIZE][LENTH_OF_DATA]; // arraylist로 변경하기
    private FragmentVisitorBinding binding; // ViewBinding 객체
    protected int numOfSpaces = 5;  // 예시로 10개의 ToggleButton 생성
    protected ArrayList<String> spaceName= new ArrayList<>();

    public void setNumOfSpaces(int num){
        numOfSpaces=num;
    }
    public int getNumOfSpaces(){
        return numOfSpaces;
    }
    public void setSpace(String[] Names){
        spaceName.clear();
        this.numOfSpaces = Names.length;
        for(int i= 0;i< Names.length;i++)
        {
            spaceName.add(Names[i]);
        }
    }

    public void resetDATA(){
        for(int i =0;i<LENTH_OF_DATA;i++){
            visitorData[i] = 0;
        }
    }

    public VisitorFragment() {
        resetDATA();
        checkedGender = false;
        checkedAge = false;
        visitorCnt = 0;
        for(int i =0 ;i<BUFFER_SIZE;i++){
            buffer_visitorData[i][LENTH_OF_DATA-1] = 0;
        }
        //spaceName[0]="테스트";
    }
    public void storeBuffer(int data[],int cnt){
        int d0 = data[0];
        int d1 = data[1];
        int d2 = data[2];
        if (buffer_visitorData[cnt][2] != 0) cnt++;
        buffer_visitorData[cnt][0] = d0;
        buffer_visitorData[cnt][1] = d1;
        buffer_visitorData[cnt][2] = d2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        resetDATA();
        // ViewBinding 초기화
        binding = FragmentVisitorBinding.inflate(inflater, container, false);

        // 성별 RadioGroup 설정
        binding.radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.radioMale.getId()) {
                visitorData[0] = 1; //남자면 1 더하고
                checkedGender = true;
            }
            else if (checkedId == binding.radioFemale.getId()) {
                visitorData[0] = 2; //여자면 2 더하고
                checkedGender = true;
            }
        });

        // 성별을 선택하면서 이미 1과 2가 더해져 있으므로
        binding.radioGroupAge.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.age1to8.getId()){ // 유아 .. matching 남(9), 여(10)
                visitorData[0] += 8;
                checkedAge = true;
            }
            else if (checkedId == binding.age9to13.getId()){ // 9~13 .. matching 남(1), 여(2)
                visitorData[0] += 0;
                checkedAge = true;
            }
            else if (checkedId == binding.age14to16.getId()){ // 14~16 .. matching 남(3), 여(4)
                visitorData[0] += 2;
                checkedAge = true;
            }
            else if (checkedId == binding.age17to19.getId()){ // 17~19 .. matching 남(5), 여(6)
                visitorData[0] += 4;
                checkedAge = true;
            }
            else if (checkedId == binding.age20to24.getId()){ // 20~24 .. matching 남(7), 여(8)
                visitorData[0] += 6;
                checkedAge = true;
            }
            else if (checkedId == binding.age25over.getId()) { // 24 이상 .. matching 남(11), 여(12)
                visitorData[0] += 10;
                checkedAge = true;
            }
        });


        // 동적으로 ToggleButton 추가
        for (int i = 0; i < numOfSpaces; i++) {
            // ToggleButton 생성
            ToggleButton toggleButton = new ToggleButton(getContext());
            try{
                toggleButton.setText(spaceName.get(i));
                toggleButton.setTextOn(spaceName.get(i));
                toggleButton.setTextOff(spaceName.get(i));
            } catch(IndexOutOfBoundsException e){
                toggleButton.setText("시설 이름을 설정하지 않았습니다.");
                toggleButton.setTextOn("시설 이름을 설정하지 않았습니다.");
                toggleButton.setTextOff("시설 이름을 설정하지 않았습니다.");
            }
            toggleButton.setId(View.generateViewId());  // 고유 ID 설정

            // ToggleButton을 LinearLayout에 추가
            binding.SpaceButtonContainer.addView(toggleButton);
        }
        // 확인 버튼 클릭 시 선택된 정보 처리
        binding.btnSubmit.setOnClickListener(v -> {
            if(checkedGender == false) {
                Toast.makeText(getContext(), "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
            if(checkedAge == false){
                Toast.makeText(getContext(), "나이를 선택하세요.", Toast.LENGTH_SHORT).show();
            }
            if(checkedGender == true && checkedAge == true){
                if(visitorCnt > BUFFER_SIZE-1){ // out of bound Error 방지
                    // xlsx 파일로 저장하는 함수
                    visitorCnt = 0;
                }
                storeBuffer(visitorData,visitorCnt++);
                resetDATA();
            }
        });

        // View 반환
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // ViewBinding 객체 해제
    }
}
