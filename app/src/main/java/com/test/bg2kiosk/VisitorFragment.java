package com.test.bg2kiosk;
//VisitorFragment.java
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;

import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;


import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.test.bg2kiosk.databinding.FragmentVisitorBinding; // 바인딩 클래스 임포트
import android.widget.Toast;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class VisitorFragment extends Fragment {

    protected boolean checkedGender;
    protected boolean checkedAge;
    private FragmentVisitorBinding binding; // ViewBinding 객체
    private SpaceViewModel spaceViewModel;
    private int checkSpace = 0;


    private ArrayList<ToggleButton> toggleButtonList = new ArrayList<>();

    /*data 쓰기 관련 변수*/
    protected final int LENTH_OF_DATA = 3;
    protected int[] visitorData = new int[LENTH_OF_DATA];


    public VisitorFragment() {
        checkedGender = false;
        checkedAge = false;
        checkSpace = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ViewBinding 초기화
        binding = FragmentVisitorBinding.inflate(inflater, container, false);
        spaceViewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel.class);
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


        binding.radioGroupAge.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.age1to8.getId()){ // 유아 .. matching 남(9), 여(10)
                visitorData[1] = 0;
                checkedAge = true;
            }
            else if (checkedId == binding.age9to13.getId()){ // 9~13 .. matching 남(1), 여(2)
                visitorData[1] = 1;
                checkedAge = true;
            }
            else if (checkedId == binding.age14to16.getId()){ // 14~16 .. matching 남(3), 여(4)
                visitorData[1] = 2;
                checkedAge = true;
            }
            else if (checkedId == binding.age17to19.getId()){ // 17~19 .. matching 남(5), 여(6)
                visitorData[1] = 3;
                checkedAge = true;
            }
            else if (checkedId == binding.age20to24.getId()){ // 20~24 .. matching 남(7), 여(8)
                visitorData[1] = 4;
                checkedAge = true;
            }
            else if (checkedId == binding.age25over.getId()) { // 24 이상 .. matching 남(11), 여(12)
                visitorData[1] = 5;
                checkedAge = true;
            }
        });
        if(spaceViewModel.getSpaceNames().getValue() != null && spaceViewModel.getNumOfSpace().getValue() != null) {
            SpaceSelectCreate(spaceViewModel.getSpaceNames().getValue(), spaceViewModel.getNumOfSpace().getValue());
        }


        // 확인 버튼 클릭 시 선택된 정보 처리
        binding.btnSubmit.setOnClickListener(v -> {
            if(!checkedGender) {
                Toast.makeText(getContext(), "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
            if(!checkedAge){
                Toast.makeText(getContext(), "나이를 선택하세요.", Toast.LENGTH_SHORT).show();
            }
            if(checkedGender && checkedAge){
                // space 선택 확인 함수
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(new Date());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(()-> {
                    AppDatabase db = AppDatabase.getDatabase(getContext());
                    int exp = 0;
                    boolean insert_check = false;
                    String[] task = spaceViewModel.getSpaceTask().getValue();
                    String[] classification = spaceViewModel.getProgramClassification().getValue();
                    String[] area = spaceViewModel.getProgramArea().getValue();
                    for (String name : spaceViewModel.getSpaceNames().getValue()) {
                        VisitorStatistics statistics = db.visitorStatisticsDao().getStatistics(today, name);
                        if (statistics == null) {
                            Log.d("statistics null","null!!!!!");
                            statistics = new VisitorStatistics(today, task[exp], classification[exp], area[exp], name);
                            insert_check = true;
                        }
                        Log.d("checkSpace", checkSpace + "");
                        Log.d("susic", (checkSpace & (int) Math.pow(2, exp)) + "");
                        if ((checkSpace & (int) Math.pow(2, exp)) == (int) Math.pow(2, exp)) {
                            switch (visitorData[1]) {
                                case 0:
                                    statistics.Increase_infant(visitorData[0]);
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    statistics.Increase_youth(visitorData[0], visitorData[1]);
                                    break;
                                case 5:
                                    statistics.Increase_adult(visitorData[0]);
                                    break;
                            }
                        }
                        statistics.show_statistics();
                        int currentExp = exp;

                        getActivity().runOnUiThread(() -> {
                            String toggleId = "toggleSpace" + currentExp;
                            binding.getRoot().findViewById(toggleId.hashCode()).setSelected(false);
                        });
                        if(insert_check){
                            db.visitorStatisticsDao().insert(statistics);
                            insert_check=false;
                        } else{
                            db.visitorStatisticsDao().update(statistics); //room에 저장
                        }
                        exp++;
                    }
                    getActivity().runOnUiThread(() -> {
                        binding.radioGroupGender.clearCheck();
                        binding.radioGroupAge.clearCheck();
                        //버튼 초기화 필수
                        checkedGender = false;
                        checkedAge = false;
                        checkSpace = 0;
                        for (ToggleButton tgbtn : toggleButtonList) {
                            tgbtn.setChecked(false);
                            tgbtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                    });
                });
            }
        });
        spaceViewModel.getSpaceNames().observe(getViewLifecycleOwner(), names -> {
            binding.SpaceButtonContainer.removeAllViews();
            SpaceSelectCreate(spaceViewModel.getSpaceNames().getValue(),spaceViewModel.getNumOfSpace().getValue());
        });

        // View 반환
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // ViewBinding 객체 해제
    }
    public void SpaceSelectCreate(String[] spaceName, int numOfSpaces){
        toggleButtonList.clear();
        // 동적으로 ToggleButton 추가
        for (int i = 0; i < numOfSpaces; i++) {
            // ToggleButton 생성
            ToggleButton toggleButton = new ToggleButton(getContext());
            try{
                toggleButton.setText(spaceName[i]);
                toggleButton.setTextOn(spaceName[i]);
                toggleButton.setTextOff(spaceName[i]);
                toggleButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } catch(IndexOutOfBoundsException e){
                toggleButton.setText("시설 이름을 설정하지 않았습니다.");
                toggleButton.setTextOn("시설 이름을 설정하지 않았습니다.");
                toggleButton.setTextOff("시설 이름을 설정하지 않았습니다.");
            }
            int idx = i;
            String toggleId = "toggleSpace"+idx;

            //toggleButton.setId(View.generateViewId());
            toggleButton.setId(toggleId.hashCode());  // 고유 ID 설정
            toggleButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if(isChecked){
                    checkSpace |= (int)Math.pow(2,idx);
                    toggleButton.setBackgroundColor(Color.parseColor("#FFD400"));
                }
                else{
                    checkSpace &= ~(int)Math.pow(2,idx);
                    toggleButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            });
            toggleButtonList.add(toggleButton);
            // ToggleButton을 LinearLayout에 추가
            binding.SpaceButtonContainer.addView(toggleButton);
        }
    }
}
