package com.jack.jackzkrt.bean;

/**
 * Created by xcong on 2017/10/27.
 *
 * @des ${TODO}
 */

public class LevenBean {
    private String Id;
    private String LevelName;
    private String LevelNumber;
    private String LevelUpName;
    private String LevelDateTime;
    private String LevelDate;


    public LevenBean(String name) {

        LevelName = name;

    }





    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }




    public String getLevelName() {
        return LevelName;
    }

    public void setLevelName(String levelName) {
        LevelName = levelName;
    }

    public String getLevelNumber() {
        return LevelNumber;
    }

    public void setLevelNumber(String levelNumber) {
        LevelNumber = levelNumber;
    }

    public String getLevelUpName() {
        return LevelUpName;
    }

    public void setLevelUpName(String levelUpName) {
        LevelUpName = levelUpName;
    }

    public String getLevelDateTime() {
        return LevelDateTime;
    }

    public void setLevelDateTime(String levelDateTime) {
        LevelDateTime = levelDateTime;
    }

    public String getLevelDate() {
        return LevelDate;
    }

    public void setLevelDate(String levelDate) {
        LevelDate = levelDate;
    }
}
