package com.encapital.io.banksmsapp.remote;

import com.encapital.io.banksmsapp.model.DepositMessage;
import com.encapital.io.banksmsapp.model.DepositResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @POST("deposits")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<DepositResponse> sendMessageDeposit(@Header("X-AUTH-SIGNATURE") String signature,
                                             @Body DepositMessage depositMessage
                                             );
}