package com.test.bg2kiosk;
//MainActivity.java
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import com.test.bg2kiosk.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ViewBinding 초기화
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
}

