package com.ayra.moviecatalogue.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ayra.moviecatalogue.api.ApiInterface;
import com.ayra.moviecatalogue.api.ApiService;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.data.response.TvShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    private static MainRepository mainRepository;
    private ApiInterface apiInterface;

    private MainRepository() {
        apiInterface = ApiService.createService(ApiInterface.class);
    }

    public static MainRepository getInstance() {
        if (mainRepository == null) {
            mainRepository = new MainRepository();
        }
        return mainRepository;
    }

    public MutableLiveData<MovieResponse> getMovies(String apiKey, String language, int page) {
        final MutableLiveData<MovieResponse> movies = new MutableLiveData<>();
        apiInterface.getPopularMovies(apiKey, language, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    movies.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                movies.setValue(null);
            }
        });
        return movies;
    }

    public MutableLiveData<TvShowResponse> getShows(String apiKey, String language, int page) {
        final MutableLiveData<TvShowResponse> shows = new MutableLiveData<>();
        apiInterface.getPopularTvShow(apiKey, language, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.body() != null) {
                    shows.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                shows.setValue(null);
            }
        });
        return shows;
    }

}
