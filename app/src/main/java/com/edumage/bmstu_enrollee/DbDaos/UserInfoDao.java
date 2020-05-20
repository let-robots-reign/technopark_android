package com.edumage.bmstu_enrollee.DbDaos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.edumage.bmstu_enrollee.DbEntities.UserInfo;

@Dao
public abstract class UserInfoDao {

    @Query("SELECT * FROM user_info LIMIT 1")
    public abstract UserInfo getUserInfo();

    @Insert
    public abstract void insertUserInfo(UserInfo info);

    @Transaction
    public void replaceUserInfo(UserInfo newUserInfo) {
        deleteAllInfo();
        insertUserInfo(newUserInfo);
    }

    @Query("DELETE FROM user_info")
    public abstract void deleteAllInfo();
}
