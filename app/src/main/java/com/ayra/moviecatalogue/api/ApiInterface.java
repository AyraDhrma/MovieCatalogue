package com.ayra.moviecatalogue.api;

import com.ayra.moviecatalogue.data.model.Movie;
import com.ayra.moviecatalogue.data.model.TvShow;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.data.response.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/popular")
    Call<TvShowResponse> getPopularTvShow(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<Movie> getSelectedMovie(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("tv/{tv_id}")
    Call<TvShow> getSelectedShow(
            @Path("tv_id") String id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}
