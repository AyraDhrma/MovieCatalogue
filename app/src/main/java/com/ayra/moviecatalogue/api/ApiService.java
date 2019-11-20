package com.ayra.moviecatalogue.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static Retrofit createService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
