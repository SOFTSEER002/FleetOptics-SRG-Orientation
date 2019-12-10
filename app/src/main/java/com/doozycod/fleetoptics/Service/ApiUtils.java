package com.doozycod.fleetoptics.Service;

import retrofit2.Retrofit;

public class ApiUtils {
    //      api service for webservices
    private ApiUtils() {}
    public static final String BASE_URL = "http://116.203.84.80/fleetoptics/api/";

    public static ApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
