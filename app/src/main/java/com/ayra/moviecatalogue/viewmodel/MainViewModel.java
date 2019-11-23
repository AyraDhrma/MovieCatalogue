package com.ayra.moviecatalogue.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ayra.moviecatalogue.api.ApiInterface;
import com.ayra.moviecatalogue.api.ApiService;
import com.ayra.moviecatalogue.data.entity.Movie;
import com.ayra.moviecatalogue.data.entity.TvShow;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.data.response.TvShowResponse;
import com.ayra.moviecatalogue.ui.view.main.MainActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Movie>> movie = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TvShow>> tvShow = new MutableLiveData<>();
    private MutableLiveData<MovieResponse> movies = new MutableLiveData<>();
    private MutableLiveData<TvShowResponse> shows = new MutableLiveData<>();
    private ApiInterface apiInterface = ApiService.getService().create(ApiInterface.class);

    public void setMovies(String apiKey, String language, int page) {
        apiInterface.getPopularMovies(apiKey, language, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    movies.postValue(response.body());
                } else {
                    movies.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
            }
        });
    }

    public MutableLiveData<MovieResponse> getMovies() {
        return movies;
    }

    public MutableLiveData<TvShowResponse> getShows() {
        return shows;
    }

    public void setShows(String apiKey, String language, int page) {
        apiInterface.getPopularTvShow(apiKey, language, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.body() != null) {
                    shows.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
            }
        });
    }

    public MutableLiveData<ArrayList<Movie>> getFavMovie() {
        return movie;
    }

    public void setFavMovie(ArrayList<Movie> movies) {
        movie.postValue(movies);
    }

    public MutableLiveData<ArrayList<TvShow>> getFavTvShows() {
        return tvShow;
    }

    public void setFavTvShows(ArrayList<TvShow> tvShow) {
        this.tvShow.postValue(tvShow);
    }

    public void searchMovie(String query) {
        Call<MovieResponse> call = apiInterface.searchMovies(query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                try {
                    if (response.body() != null) {
                        ArrayList<Movie> movieResult = response.body().getMovies();
                        movie.setValue(movieResult);
                        Log.d(MainActivity.class.getSimpleName(), "onResponse: " + movieResult);
                    }
                } catch (Exception e) {
                    Log.d(MainActivity.class.getSimpleName(), "onResponse: " + e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.d(MainActivity.class.getSimpleName(), "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    public MutableLiveData<ArrayList<Movie>> getMovieResults() {
        return movie;
    }

}
