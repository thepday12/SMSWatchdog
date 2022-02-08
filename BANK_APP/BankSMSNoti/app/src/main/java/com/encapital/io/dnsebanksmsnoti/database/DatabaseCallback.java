package com.encapital.io.dnsebanksmsnoti.database;



import com.encapital.io.dnsebanksmsnoti.model.DepositEntity;

import java.util.List;

public interface DatabaseCallback {

  void onMessagesLoaded(List<DepositEntity> messages);

}
