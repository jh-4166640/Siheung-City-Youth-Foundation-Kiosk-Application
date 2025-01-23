package com.test.bg2kiosk;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.test.bg2kiosk.databinding.FragmentAdminBinding; // 바인딩 클래스 임포트

public class adminFragment extends Fragment {
    private FragmentAdminBinding adminBinding; // ViewBinding 객체

    private final String admin_password = "youth";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        adminBinding = FragmentAdminBinding.inflate(inflater, container, false);
        adminBinding.loginButton.setOnClickListener(v -> {
            String getpassword = adminBinding.adminPassword.getText().toString();
            if(getpassword.equals(admin_password) && getpassword  != null){

            }
            else{
                Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return adminBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adminBinding = null; // ViewBinding 객체 해제
    }
}

