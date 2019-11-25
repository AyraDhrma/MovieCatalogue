package com.ayra.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.ayra.moviecatalogue.db.AppDao;
import com.ayra.moviecatalogue.db.AppDatabase;

import java.util.Objects;

public class FavoriteProvider extends ContentProvider {

    public static final String AUTHORITY = "com.ayra.moviecatalogue";
    public static final String TABLE_NAME = "movie";
    public static final int FAVORITE = 1;
    public static final int FAVORITE_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Create Uri Matcher
    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", FAVORITE_ID);
    }

    private AppDao appDao;

    @Override
    public boolean onCreate() {
        AppDatabase appDatabase = Room.databaseBuilder(Objects.requireNonNull(getContext()), AppDatabase.class, "db_catalog").build();
        appDao = appDatabase.getAppDao();

        return true;
    }

    // Query Select All For Provider
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case FAVORITE:
                cursor = appDao.selectAllMovie();
                break;
            case FAVORITE_ID:
                cursor = appDao.selectMovieById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
