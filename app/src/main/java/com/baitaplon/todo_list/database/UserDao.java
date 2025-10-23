package com.baitaplon.todo_list.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.baitaplon.todo_list.model.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
}

