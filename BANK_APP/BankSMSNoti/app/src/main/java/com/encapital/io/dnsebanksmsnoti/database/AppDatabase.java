package com.encapital.io.dnsebanksmsnoti.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.encapital.io.dnsebanksmsnoti.dao.DepositMessageDao;
import com.encapital.io.dnsebanksmsnoti.model.DepositEntity;


@Database(entities = {DepositEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DepositMessageDao depositMessageDao();
}
