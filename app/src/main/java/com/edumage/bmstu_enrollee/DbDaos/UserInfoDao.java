package com.edumage.bmstu_enrollee.DbDaos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.edumage.bmstu_enrollee.DbEntities.UserInfo;

@Dao
public interface UserInfoDao {

    @Query("SELECT * FROM user_info LIMIT 1")
    UserInfo getUserInfo();

    @Insert
    void insertUserInfo(UserInfo info);

    @Update
    void updateUserInfo(UserInfo newInfo);

    @Query("DELETE FROM user_info")
    void deleteAllInfo();
}
