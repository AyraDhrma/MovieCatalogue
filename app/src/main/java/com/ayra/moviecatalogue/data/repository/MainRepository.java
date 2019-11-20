package com.ayra.moviecatalogue.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayra.moviecatalogue.api.ApiInterface;
import com.ayra.moviecatalogue.api.ApiService;
import com.ayra.moviecatalogue.data.model.Movie;
import com.ayra.moviecatalogue.data.model.TvShow;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.data.response.TvShowResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    private static final String TAG = MainRepository.class.getSimpleName();
    private final ApiInterface apiInterface;

    public MainRepository() {
        apiInterface = ApiService.createService().create(ApiInterface.class);
    }

    public LiveData<TvShowResponse> getShowList(String apiKey, String language, int page) {
        final MutableLiveData<TvShowResponse> tvData = new MutableLiveData<>();

        apiInterface.getPopularTvShow(apiKey, language, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        tvData.setValue(response.body());
                        Log.d(TAG, "Total TV " + response.body().getTotalResults());
                    }
                } else {
                    Log.d(TAG, "onResponse: Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                tvData.setValue(null);
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
            }
        });
        return tvData;
    }

    public LiveData<MovieResponse> getMovieList(String apiKey, String language, int page) {
        final MutableLiveData<MovieResponse> movieData = new MutableLiveData<>();

        apiInterface.getPopularMovies(apiKey, language, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        movieData.setValue(response.body());
                        Log.d(TAG, "Total movie " + response.body().getTotalResults());
                    }
                } else {
                    Log.d(TAG, "onResponse: Failure");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                movieData.setValue(null);
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
            }
        });
        return movieData;
    }

    public LiveData<Movie> getSelectedMovie(String id, String apiKey, String language) {
        final MutableLiveData<Movie> movieData = new MutableLiveData<>();
        Log.d(TAG, "getSelectedMovie: ");

        apiInterface.getSelectedMovie(id, apiKey, language).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    movieData.postValue(response.body());
                    Log.d(TAG, "onResponse: " + Objects.requireNonNull(response.body()).getTitle());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                movieData.postValue(null);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return movieData;
    }

    public LiveData<TvShow> getSelectedTvShow(String id, String apiKey, String language) {
        final MutableLiveData<TvShow> tvShowData = new MutableLiveData<>();

        apiInterface.getSelectedShow(id, apiKey, language).enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(@NonNull Call<TvShow> call, @NonNull Response<TvShow> response) {
                if (response.isSuccessful()) {
                    tvShowData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShow> call, @NonNull Throwable t) {
                tvShowData.postValue(null);
            }
        });
        return tvShowData;
    }

}
