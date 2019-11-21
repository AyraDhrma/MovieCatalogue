package com.ayra.moviecatalogue.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ayra.moviecatalogue.data.entity.Movie;
import com.ayra.moviecatalogue.data.entity.TvShow;
import com.ayra.moviecatalogue.data.repository.MainRepository;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.data.response.TvShowResponse;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Movie>> movie = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<TvShow>> tvShow = new MutableLiveData<>();
    private MutableLiveData<MovieResponse> movies;
    private MutableLiveData<TvShowResponse> shows;
    private MainRepository repository;

    public void setMovies(String apiKey, String language, int page) {
        if (movies != null) {
            return;
        }
        repository = MainRepository.getInstance();
        movies = repository.getMovies(apiKey, language, page);
    }

    public MutableLiveData<MovieResponse> getMovies() {
        return movies;
    }

    public MutableLiveData<TvShowResponse> getShows() {
        return shows;
    }

    public void setShows(String apiKey, String language, int page) {
        if (shows != null) {
            return;
        }
        repository = MainRepository.getInstance();
        shows = repository.getShows(apiKey, language, page);
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
