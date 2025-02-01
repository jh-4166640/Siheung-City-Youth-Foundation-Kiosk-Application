package com.test.bg2kiosk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람이 울리면 실행될 작업 설정
        String[] spaceNames = intent.getStringArrayExtra("spaceNames");

        // WorkManager로 전달할 데이터 생성
        Data inputData = new Data.Builder()
                .putStringArray("spaceNames", spaceNames)
                .build();

        // VisitorStatisticsWorker 실행
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(VisitorStatisticsWorker.class)
                .setInputData(inputData)  // 전달된 데이터 설정
                .build();

        // WorkManager로 작업 예약
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}
