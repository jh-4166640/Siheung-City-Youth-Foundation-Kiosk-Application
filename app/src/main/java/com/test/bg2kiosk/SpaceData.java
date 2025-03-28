package com.test.bg2kiosk;

import java.util.List;

public class SpaceData {
    public int numOfSpace;
    public String division;
    public String officeName;
    public List<String> spaceNames;
    public List<String> spaceTask;
    public List<String> programClassification;
    public List<String> programArea;

    public SpaceData(int numOfSpace, String division, String officeName, List<String> spaceTask,
                     List<String> programClassification, List<String> programArea, List<String> spaceNames){
        this.numOfSpace=numOfSpace;
        this.division=division;
        this.officeName=officeName;
        this.spaceTask=spaceTask;
        this.programClassification=programClassification;
        this.programArea=programArea;
        this.spaceNames=spaceNames;
    }
}
