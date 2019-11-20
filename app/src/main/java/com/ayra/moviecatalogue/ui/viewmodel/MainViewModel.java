package com.ayra.moviecatalogue.ui.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ayra.moviecatalogue.api.ApiInterface;
import com.ayra.moviecatalogue.api.ApiService;
import com.ayra.moviecatalogue.data.model.Movie;
import com.ayra.moviecatalogue.data.model.TvShow;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.data.response.TvShowResponse;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Movie>> movie = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<TvShow>> tvShow = new MutableLiveData<>();
    private ApiInterface apiInterface = ApiService.createService().create(ApiInterface.class);

    public void setShowList(String apiKey, String language, int page) {
        apiInterface.getPopularTvShow(apiKey, language, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.body() != null) {
                    ArrayList<TvShow> tvShows = response.body().getResults();
                    tvShow.postValue(tvShows);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                tvShow.postValue(null);
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public MutableLiveData<ArrayList<TvShow>> getTvShowList() {
        return tvShow;
    }

    public void setMovieList(String apiKey, String language, int page) {
        apiInterface.getPopularMovies(apiKey, language, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    ArrayList<Movie> movies = response.body().getMovies();
                    movie.postValue(movies);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                movie.postValue(null);
            }
        });
    }

    public MutableLiveData<ArrayList<Movie>> getMovieList() {
        return movie;
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

}
