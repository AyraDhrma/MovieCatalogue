package com.ayra.moviecatalogue.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ayra.moviecatalogue.data.model.Movie;
import com.ayra.moviecatalogue.data.model.TvShow;

@Database(entities = {Movie.class, TvShow.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao getAppDao();
}