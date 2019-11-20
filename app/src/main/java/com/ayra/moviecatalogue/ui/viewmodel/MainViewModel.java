package com.ayra.moviecatalogue.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ayra.moviecatalogue.data.model.Movie;
import com.ayra.moviecatalogue.data.model.TvShow;
import com.ayra.moviecatalogue.data.repository.MainRepository;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.data.response.TvShowResponse;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private final LiveData<TvShowResponse> tvShowResponseLiveData;
    private final MutableLiveData<ArrayList<Movie>> movie = new MutableLiveData<>();
    private final LiveData<MovieResponse> movieResponseLiveData;
    private final MutableLiveData<ArrayList<TvShow>> tvShows = new MutableLiveData<>();

    public MainViewModel() {
        MainRepository repository = new MainRepository();
        String LANGUAGE = "en-US";
        int page = 1;
        String API_KEY = "0a296602e2e9f2572735bf2c91763741";
        this.movieResponseLiveData = repository.getMovieList(API_KEY, LANGUAGE, page);
        this.tvShowResponseLiveData = repository.getShowList(API_KEY, LANGUAGE, page);
    }

    public LiveData<MovieResponse> getMovie() {
        return movieResponseLiveData;
    }

    public LiveData<TvShowResponse> getTvShow() {
        return tvShowResponseLiveData;
    }

    public MutableLiveData<ArrayList<Movie>> getFavMovie() {
        return movie;
    }

    public void setFavMovie(ArrayList<Movie> movies) {
        movie.postValue(movies);
    }

    public MutableLiveData<ArrayList<TvShow>> getFavTvShows() {
        return tvShows;
    }

    public void setFavTvShows(ArrayList<TvShow> tvShow) {
        tvShows.postValue(tvShow);
    }

}
