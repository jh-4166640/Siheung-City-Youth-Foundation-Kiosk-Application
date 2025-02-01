package com.test.bg2kiosk;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface VisitorStatisticsDAO {

    // 데이터 삽입
    @Insert
    void insert(VisitorStatistics visitorStatistics);

    // 데이터 업데이트
    @Update
    void update(VisitorStatistics visitorStatistics);

    // 날짜별로 특정 시설, 나이 그룹에 해당하는 데이터를 조회
    @Query("SELECT * FROM visitor_statistics WHERE date = :date AND spaceName = :spaceName")
    VisitorStatistics getStatistics(String date, String spaceName);

    // 특정 날짜의 모든 통계 데이터를 가져오기
    @Query("SELECT * FROM visitor_statistics WHERE date = :date")
    List<VisitorStatistics> getStatisticsByDate(String date);

    // 모든 데이터를 가져오기 (필요시)
    @Query("SELECT * FROM visitor_statistics")
    List<VisitorStatistics> getAllStatistics();
}

