package com.test.bg2kiosk;
//visitorStatisticsWorker.java
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class VisitorStatisticsWorker extends Worker {
    private SpaceViewModel spaceViewModel;
    public VisitorStatisticsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        spaceViewModel = new ViewModelProvider.AndroidViewModelFactory((Application) getApplicationContext())
                .create(SpaceViewModel.class);
    }

    @NonNull
    @Override
    public Result doWork() { //날짜가 지날 때마다 쿼리문 생성
        // 현재 날짜를 "yyyy-MM-dd" 형식으로 생성
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(new Date());
        String[] spaceNames = spaceViewModel.getSpaceNames().getValue();
        // Room 데이터베이스 인스턴스 가져오기
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        for(int idx = 0;idx<spaceNames.length;idx++){
            db.visitorStatisticsDao().insert(new VisitorStatistics(currentDate, spaceNames[idx]));
        }

        return Result.success();
    }

}
