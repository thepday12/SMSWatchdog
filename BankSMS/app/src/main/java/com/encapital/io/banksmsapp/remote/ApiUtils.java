package com.encapital.io.banksmsapp.remote;

public class ApiUtils {
    private ApiUtils() {}

    private static final String BASE_URL = "https://suat.entrade.com.vn/cash-deposit/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
