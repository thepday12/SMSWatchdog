package com.encapital.io.dnsebanksmsnoti.remote;

import com.encapital.io.dnsebanksmsnoti.model.BankNotification;
import com.encapital.io.dnsebanksmsnoti.model.DepositResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @POST("y6hron4i4igepdx8sunhqg8a9c")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DepositResponse> sendMessageDeposit(
            @Body BankNotification bankNotification
    );
}