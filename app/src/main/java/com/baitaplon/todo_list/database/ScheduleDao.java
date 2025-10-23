package com.baitaplon.todo_list.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.baitaplon.todo_list.model.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE userId = :userId ORDER BY startTime ASC")
    List<Schedule> getSchedulesByUser(int userId);
}
