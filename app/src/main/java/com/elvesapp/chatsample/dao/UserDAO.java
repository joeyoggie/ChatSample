package com.elvesapp.chatsample.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.elvesapp.chatsample.entities.User;

import java.util.List;

@Dao
public abstract class UserDAO {
    @Query("SELECT * FROM user")
    public abstract List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    public abstract List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE name LIKE :name LIMIT 1")
    public abstract User findByName(String name);

    @Query("SELECT * FROM user WHERE id LIKE :id LIMIT 1")
    public abstract User findByID(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(User... users);

    @Delete
    public abstract void delete(User user);
}