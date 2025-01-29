package com.test.bg2kiosk;
//loginFragment.java
import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.test.bg2kiosk.databinding.FragmentLoginBinding; // 바인딩 클래스 임포트
import com.test.bg2kiosk.databinding.DialogChangePasswordBinding; // 바인딩 클래스 임포트

public class loginFragment extends Fragment {
    private FragmentLoginBinding loginBinding; // ViewBinding 객체
    private String admin_password = "youth";
    private final String MASTERKEY = "223";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        loginBinding.loginButton.setOnClickListener(v -> {
            String getpassword = loginBinding.adminPassword.getText().toString();
            if(getpassword.equals(admin_password) && getpassword  != null){
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new AdminFragment());
                transaction.addToBackStack(null);  // 뒤로 가기 버튼을 눌렀을 때 이전 Fragment로 돌아가기
                transaction.commit();
            }
            else{
                Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        loginBinding.changePasswordButton.setOnClickListener(v -> {
            DialogChangePasswordBinding dialogbinding = DialogChangePasswordBinding.inflate(getLayoutInflater());
            new AlertDialog.Builder(loginBinding.getRoot().getContext())
                    .setTitle("비밀번호 변경")
                    .setMessage("여기에 비밀번호를 입력하세요.")
                    .setView(dialogbinding.getRoot())  // 바인딩된 레이아웃을 다이얼로그에 추가
                    .setPositiveButton("확인", (dialog, id) -> {
                        String keyInput =dialogbinding.masterKey.getText().toString();
                        if(!keyInput.equals(MASTERKEY)){
                            Toast.makeText(getContext(), "masterkey가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String userInput = dialogbinding.changePassword.getText().toString(); // EditText에서 입력값 가져오기
                            setAdmin_password(userInput);
                            Toast.makeText(getContext(), "비밀번호가 변경 완료", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("취소", (dialog, id) -> {
                        // 취소 시 처리할 코드
                    })
                    .create()
                    .show();
            String getChangePassword = loginBinding.adminPassword.getText().toString();
            setAdmin_password(getChangePassword);
        });
        return loginBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginBinding = null; // ViewBinding 객체 해제
    }
    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }
}

