package com.test.bg2kiosk;
//MainActivity.java
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.google.android.material.tabs.TabLayoutMediator;

import com.test.bg2kiosk.databinding.ActivityMainBinding;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ViewBinding 초기화
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scheduleWorkAtSpecificTime(9,10);
        // TabLayout 및 ViewPager2 초기화
        setupViewPagerAndTabs();
    }

    private void setupViewPagerAndTabs() {
        // ViewPager2 어댑터 설정
        binding.viewPager.setAdapter(new FragmentAdapter(this));

        // TabLayoutMediator로 TabLayout과 ViewPager2 연결
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("메인화면");
            } else if (position == 1) {
                tab.setText("관리자페이지");
            }
        }).attach();
    }

    // ViewPager2 어댑터 클래스
    private static class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull AppCompatActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new VisitorFragment(); // "메인화면" Fragment
            } else {
                return new loginFragment(); // "관리자페이지" Fragment
            }
        }

        @Override
        public int getItemCount() {
            return 2; // 총 두 개의 탭
        }
    }
    private void scheduleWorkAtSpecificTime(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 정확한 알람을 예약할 수 있는 권한이 있는지 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    // 이미 지나간 시간이라면 내일 같은 시간에 예약
                    calendar.add(Calendar.DATE, 1);
                }

                // 알람을 설정할 때 전달할 데이터
                Intent intent = new Intent(this, AlarmReceiver.class);

                // PendingIntent 생성
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                // 알람 예약 (정확한 시간에 실행)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

}

