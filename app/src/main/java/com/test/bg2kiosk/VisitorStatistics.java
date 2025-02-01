package com.test.bg2kiosk;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "visitor_statistics")
public class VisitorStatistics {
    @PrimaryKey(autoGenerate = true)
    private int id; // 기본 키를 auto-generate로 설정

    /*
     * date | spaceName | Total | total_youth | age9to13_male | age9to13_female |
     * age14to16_male | age14to16_female | age17to19_male | age17to19_female |
     * age20to24_male | age20to24_female | total_infant | age0to8_male | age0to8_female |
     * total_adult | age25over_male | age25over_female
     */
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "spaceName")
    private String spaceName;

    @ColumnInfo(name = "Total")
    private int Total;

    @ColumnInfo(name = "total_youth")
    private int total_youth;

    @ColumnInfo(name = "age9to13_male")
    private int age9to13_male;
    @ColumnInfo(name = "age9to13_female")
    private int age9to13_female;

    @ColumnInfo(name = "age14to16_male")
    private int age14to16_male;
    @ColumnInfo(name = "age14to16_female")
    private int age14to16_female;

    @ColumnInfo(name = "age17to19_male")
    private int age17to19_male;
    @ColumnInfo(name = "age17to19_female")
    private int age17to19_female;

    @ColumnInfo(name = "age20to24_male")
    private int age20to24_male;
    @ColumnInfo(name = "age20to24_female")
    private int age20to24_female;

    @ColumnInfo(name = "total_infant")
    private int total_infant;
    @ColumnInfo(name = "age0to8_male")
    private int age0to8_male;
    @ColumnInfo(name = "age0to8_female")
    private int age0to8_female;

    @ColumnInfo(name = "total_adult")
    private int total_adult;
    @ColumnInfo(name = "age25over_male")
    private int age25over_male;
    @ColumnInfo(name = "age25over_female")
    private int age25over_female;

    public VisitorStatistics(String date, String spaceName){
        this.date = date;
        this.spaceName = spaceName;
        this.Total = 0;
        this.total_youth = 0;
        this.age9to13_male = 0;
        this.age9to13_female = 0;
        this.age14to16_male = 0;
        this.age14to16_female = 0;
        this.age17to19_male = 0;
        this.age17to19_female = 0;
        this.age20to24_male = 0;
        this.age20to24_female = 0;
        this.total_infant = 0;
        this.age0to8_male = 0;
        this.age0to8_female = 0;
        this.total_adult = 0;
        this.age25over_male = 0;
        this.age25over_female = 0;
    }


    public void Increase_infant(int gender){
        if(gender == 1) this.age0to8_male++;
        else if(gender == 2) this.age0to8_female++;
        this.total_infant++;
    }
    public void Increase_youth(int gender, int age){
        switch(age)
        {
            case 1:
                if (gender == 1) {
                    this.age9to13_male++;
                } else if(gender == 2){
                    this.age9to13_female++;
                }
                break;
            case 2:
                if (gender == 1) {
                    this.age14to16_male++;
                } else if(gender == 2){
                    this.age14to16_female++;
                }
                break;
            case 3:
                if (gender == 1) {
                    this.age17to19_male++;
                } else if(gender == 2){
                    this.age17to19_female++;
                }
                break;
            case 4:
                if (gender == 1) {
                    this.age20to24_male++;
                } else if(gender == 2){
                    this.age20to24_female++;
                }
                break;
        }
        this.total_youth++;
    }
    public void Increase_adult(int gender){
        if(gender == 1){
            this.age25over_male++;
        }else if(gender == 2){
            this.age25over_female++;
        }
        this.total_adult++;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }

    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getSpaceName(){
        return this.spaceName;
    }
    public void setSpaceName(String spaceName){
        this.spaceName = spaceName;
    }

    public int getTotal(){
        return this.Total;
    }
    public void setTotal(int total){
        this.Total = total;
    }

    public int getTotal_youth(){
        return this.total_youth;
    }
    public void setTotal_youth(int total_youth){
        this.total_youth = total_youth;
    }

    public int getAge9to13_male(){
        return this.age9to13_male;
    }
    public void setAge9to13_male(int age9to13_male){
        this.age9to13_male = age9to13_male;
    }

    public int getAge9to13_female() {
        return this.age9to13_female;
    }
    public void setAge9to13_female(int age9to13_female) {
        this.age9to13_female = age9to13_female;
    }

    public int getAge14to16_male() {
        return this.age14to16_male;
    }
    public void setAge14to16_male(int age14to16_male) {
        this.age14to16_male = age14to16_male;
    }

    public int getAge14to16_female() {
        return this.age14to16_female;
    }
    public void setAge14to16_female(int age14to16_female) {
        this.age14to16_female = age14to16_female;
    }

    public int getAge17to19_male() {
        return this.age17to19_male;
    }
    public void setAge17to19_male(int age17to19_male) {
        this.age17to19_male = age17to19_male;
    }

    public int getAge17to19_female() {
        return this.age17to19_female;
    }
    public void setAge17to19_female(int age17to19_female) {
        this.age17to19_female = age17to19_female;
    }

    public int getAge20to24_male() {
        return this.age20to24_male;
    }
    public void setAge20to24_male(int age20to24_male) {
        this.age20to24_male = age20to24_male;
    }

    public int getAge20to24_female() {
        return this.age20to24_female;
    }
    public void setAge20to24_female(int age20to24_female) {
        this.age20to24_female = age20to24_female;
    }

    public int getTotal_infant() {
        return this.total_infant;
    }
    public void setTotal_infant(int total_infant) {
        this.total_infant = total_infant;
    }

    public int getAge0to8_male() {
        return this.age0to8_male;
    }
    public void setAge0to8_male(int age0to8_male) {
        this.age0to8_male = age0to8_male;
    }

    public int getAge0to8_female() {
        return this.age0to8_female;
    }
    public void setAge0to8_female(int age0to8_female) {
        this.age0to8_female = age0to8_female;
    }

    public int getTotal_adult() {
        return this.total_adult;
    }
    public void setTotal_adult(int total_adult) {
        this.total_adult = total_adult;
    }

    public int getAge25over_male() {
        return this.age25over_male;
    }
    public void setAge25over_male(int age25over_male) {
        this.age25over_male = age25over_male;
    }

    public int getAge25over_female() {
        return this.age25over_female;
    }
    public void setAge25over_female(int age25over_female) {
        this.age25over_female = age25over_female;
    }
}
