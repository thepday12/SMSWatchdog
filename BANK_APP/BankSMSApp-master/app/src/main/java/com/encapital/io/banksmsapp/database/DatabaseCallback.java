package com.encapital.io.banksmsapp.database;



import com.encapital.io.banksmsapp.model.DepositEntity;

import java.util.List;

public interface DatabaseCallback {

  void onMessagesLoaded(List<DepositEntity> messages);

}
