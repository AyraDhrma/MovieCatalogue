package com.ayra.moviecatalogue.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ayra.moviecatalogue.data.entity.Movie;
import com.ayra.moviecatalogue.data.entity.TvShow;

import java.util.List;

@Dao
public interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavMovie(Movie... movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavShow(TvShow... tvShows);

    @Query("SELECT * FROM movie")
    List<Movie> getAllMovie();

    @Query("SELECT * FROM tvshow")
    List<TvShow> getAllShow();

    @Query("DELETE FROM movie WHERE id = :id")
    void deleteMovieById(int id);

    @Query("DELETE FROM tvshow WHERE id = :id")
    void deleteShowById(int id);

    @Query("SELECT title FROM movie WHERE title = :title")
    String getMovieByTitle(String title);

    @Query("SELECT name FROM tvshow WHERE name = :name")
    String getShowByTitle(String name);

}
