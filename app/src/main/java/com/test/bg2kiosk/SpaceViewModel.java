// SpaceViewModel.java
package com.test.bg2kiosk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SpaceViewModel extends ViewModel {

    private final MutableLiveData<String[]> spaceNames = new MutableLiveData<>(new String[0]);
    private final MutableLiveData<Integer> numOfSpace = new MutableLiveData<>(5);
    private final MutableLiveData<String> division = new MutableLiveData<>("");
    private final MutableLiveData<String> officeName = new MutableLiveData<>("");
    private final MutableLiveData<String[]> spaceTask= new MutableLiveData<>(new String[0]);
    private final MutableLiveData<String[]> programClassification = new MutableLiveData<>(new String[0]);
    private final MutableLiveData<String[]> programArea = new MutableLiveData<>(new String[0]);


    public LiveData<String[]> getSpaceNames() {
        return spaceNames;
    }
    // 배열을 LiveData에 설정하는 메소드
    public void setSpaceNames(String[] names) {
        spaceNames.setValue(names);  // 배열을 LiveData로 설정
    }
    public LiveData<Integer> getNumOfSpace() {
        return numOfSpace;
    }
    public void setNumOfSpace(int num) {
        numOfSpace.setValue(num);
    }
    /*본부*/
    public LiveData<String> getDivision() {
        return division;
    }
    public void setDivision(String div) {
        division.setValue(div);
    }
    /*시설명*/
    public LiveData<String> getOfficeName() {
        return officeName;
    }
    public void setOfficeName(String office) {
        officeName.setValue(office);
    }
    /*실행과제*/
    public LiveData<String[]> getSpaceTask() {
        return spaceTask;
    }
    public void setSpaceTask(String[] task) {
        spaceTask.setValue(task);  // 배열을 LiveData로 설정
    }
    /*구분*/
    public LiveData<String[]> getProgramClassification() {
        return programClassification;
    }
    public void setProgramClassification(String[] classification) {
        programClassification.setValue(classification);  // 배열을 LiveData로 설정
    }
    /*분야*/
    public LiveData<String[]> getProgramArea() {
        return programArea;
    }
    public void setProgramArea(String[] Area) {
        programArea.setValue(Area);  // 배열을 LiveData로 설정
    }

}
