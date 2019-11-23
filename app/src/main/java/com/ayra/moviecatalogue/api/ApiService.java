package com.ayra.moviecatalogue.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static final String API_KEY = "0a296602e2e9f2572735bf2c91763741";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    @NonNull
    public static Retrofit getService() {
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl httpUrl = request.url()
                        .newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build();

                request = request.newBuilder()
                        .url(httpUrl)
                        .build();

                return chain.proceed(request);
            }
        }).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
