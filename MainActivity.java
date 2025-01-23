package com.test.bg2kiosk;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {
    private TabLayout tablayout; // 상단 Tab
    private ViewPager viewpager; // 상단 Tab에서 선택된 화면을 viewpager로 출력
    private VisitorInformation visitorInformationManager;
    private LinearLayout SpaceButtonContainer; // space 버튼을 담을 레이아웃
    private ToggleButton[] spaceBtnGroup; // space 버튼들
    private int spaceNum = 5; // 공간 시설 갯수 ex)
    private String[] spaceName; // 공간 시설 이름들

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewPager);

        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return null;
                    case 1:
                        return null;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 0;
            }
        });

        spaceName = new String[spaceNum];
        /* 임시로 시설 이름 설정 */
        spaceName[0] = "청공";
        spaceName[1] = "댄스";
        spaceName[2] = "밴드";
        spaceName[3] = "다목적";
        spaceName[4] = "esport";
        /*--------------------------Visitor Info 작업------------------------*/
        // VisitorInformation 초기화
        RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);
        Button submitBtn = findViewById(R.id.btnSubmit);
        Button[] btnGroupAge = {
                findViewById(R.id.age1to8),
                findViewById(R.id.age9to13),
                findViewById(R.id.age14to16),
                findViewById(R.id.age17to19),
                findViewById(R.id.age20to24),
                findViewById(R.id.age25over)
        };
        /*
        this space for get space Number and space Name
         */
        /*--------------------------Visitor Info Space 설정------------------------*/
        SpaceButtonContainer = findViewById(R.id.SpaceButtonContainer);
        spaceBtnGroup = new ToggleButton[spaceNum];
        for (int i = 0; i < spaceNum; i++) {
            ToggleButton spaceButton = new ToggleButton(this);
            spaceButton.setText((spaceName[i]));
            spaceButton.setTextOn(spaceName[i]);
            spaceButton.setTextOff(spaceName[i]);
            spaceButton.setTextColor(Color.BLACK);  // 기본 텍스트 색상
            spaceButton.setBackgroundColor(Color.WHITE);  // 기본 배경색
            spaceButton.setId(View.generateViewId());
            SpaceButtonContainer.addView(spaceButton);
            spaceBtnGroup[i] = spaceButton;
        }
        visitorInformationManager = new VisitorInformation(radioGroupGender, submitBtn, btnGroupAge, spaceBtnGroup, spaceName);
        /*-----------------------------------------------------------------*/
    }
}
