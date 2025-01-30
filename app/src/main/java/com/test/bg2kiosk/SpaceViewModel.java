// SpaceViewModel.java
package com.test.bg2kiosk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SpaceViewModel extends ViewModel {

    private final MutableLiveData<String[]> spaceNames = new MutableLiveData<>(new String[0]);
    private final MutableLiveData<Integer> numOfSpace = new MutableLiveData<>(5);

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

}
