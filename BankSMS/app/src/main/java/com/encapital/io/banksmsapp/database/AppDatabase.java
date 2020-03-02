package com.encapital.io.banksmsapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.encapital.io.banksmsapp.dao.DepositMessageDao;
import com.encapital.io.banksmsapp.model.DepositEntity;


@Database(entities = {DepositEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DepositMessageDao depositMessageDao();
}
