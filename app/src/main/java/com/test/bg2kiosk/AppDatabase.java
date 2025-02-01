package com.test.bg2kiosk;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {VisitorStatistics.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract VisitorStatisticsDAO visitorStatisticsDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "visitor_database")
                            .fallbackToDestructiveMigration() // 개발 중 스키마 변경 시 기존 데이터를 삭제
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
