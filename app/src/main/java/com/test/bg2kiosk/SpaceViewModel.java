// SpaceViewModel.java
package com.test.bg2kiosk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SpaceViewModel extends ViewModel {

    // 공간 이름을 저장할 LiveData
    private final MutableLiveData<List<String>> spaceNames = new MutableLiveData<>(new ArrayList<>());

    // 공간 이름을 가져오는 메소드
    public LiveData<List<String>> getSpaceNames() {
        return spaceNames;
    }

    // 공간 이름을 업데이트하는 메소드
    public void setSpaceNames(List<String> names) {
        spaceNames.setValue(names);  // 새로운 값으로 LiveData 업데이트
    }

    // 공간 이름을 추가하는 메소드
    public void addSpaceName(String name) {
        List<String> currentList = spaceNames.getValue();
        if (currentList != null) {
            currentList.add(name);
            spaceNames.setValue(currentList);  // 변경된 리스트로 업데이트
        }
    }
}
