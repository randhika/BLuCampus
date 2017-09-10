package com.mycampus.rontikeky.myacademic.RestApi;

/**
 * Created by Anggit on 01/08/2017.
 */
public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://myacademic.anggitprayogo.com/api/auth/";

    public static RestApiInterface getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(RestApiInterface.class);
    }
}
