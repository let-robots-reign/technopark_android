package com.edumage.bmstu_enrollee.DbEntities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_info")
public class UserInfo {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "user_birthday")
    private String userBirthday;

    public UserInfo(String userName, String userBirthday) {
        this.userName = userName;
        this.userBirthday = userBirthday;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserBirthday() {
        return userBirthday;
    }
}
