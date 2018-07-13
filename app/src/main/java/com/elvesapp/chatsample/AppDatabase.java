package com.elvesapp.chatsample;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.elvesapp.chatsample.dao.UserDAO;
import com.elvesapp.chatsample.entities.User;

@Database(entities = {User.class}, version = 1, exportSchema =  false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDAO userDao();
}