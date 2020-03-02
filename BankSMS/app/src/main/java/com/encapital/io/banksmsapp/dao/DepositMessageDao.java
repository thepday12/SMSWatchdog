package com.encapital.io.banksmsapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.encapital.io.banksmsapp.model.DepositEntity;

import java.util.List;
import io.reactivex.Maybe;
import io.reactivex.Single;


@Dao
public interface DepositMessageDao {
  @Query("SELECT * FROM `deposit-message`")
  Maybe<List<DepositEntity>> getAll();


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insertMessage(DepositEntity messages);

  @Update
  void updateMessage(DepositEntity... messages);

  @Delete
  void deleteMessage(DepositEntity message);
}
