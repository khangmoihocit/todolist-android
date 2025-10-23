package com.baitaplon.todo_list.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.baitaplon.todo_list.model.Note;
import com.baitaplon.todo_list.model.Schedule;
import com.baitaplon.todo_list.model.User;

@Database(entities = {User.class, Note.class, Schedule.class}, version = 1)
public abstract class OnTimeDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "ontime.db";
    private static OnTimeDatabase instance;

    public abstract UserDao userDao();
    public abstract ScheduleDao scheduleDao();
    public abstract NoteDao noteDao();

    public static synchronized OnTimeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            OnTimeDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
