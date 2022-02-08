package com.encapital.io.dnsebanksmsnoti.remote;

public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = "https://teams.entrade.com.vn/hooks/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
