package com.ayra.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.data.entity.TvShow;
import com.ayra.moviecatalogue.db.AppDao;
import com.ayra.moviecatalogue.db.AppDatabase;
import com.ayra.moviecatalogue.ui.view.main.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FavSeriesRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private static final String TAG = MainActivity.class.getSimpleName();
    private final Context mContext;
    private final ArrayList<TvShow> shows = new ArrayList<>();
    private AppDatabase appDatabase;

    FavSeriesRemoteViewsFactory(Context Context) {
        this.mContext = Context;
    }

    @Override
    public void onCreate() {
        final long token = Binder.clearCallingIdentity();
        appDatabase = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "db_catalog")
                .allowMainThreadQueries()
                .build();
        Binder.restoreCallingIdentity(token);
    }

    @Override
    public void onDataSetChanged() {
        try {
            AppDao appDao = appDatabase.getAppDao();
            shows.clear();
            shows.addAll(appDao.getAllShow());
        } catch (Exception e) {
            Log.d(TAG, "onDataSetChanged: " + e);
        }
    }

    @Override
    public void onDestroy() {
        appDatabase.close();
    }

    @Override
    public int getCount() {
        return shows.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try {
            Bitmap bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(IMG_URL + shows.get(i).getPosterPath())
                    .apply(new RequestOptions().centerCrop())
                    .submit(700, 500)
                    .get();
            views.setImageViewBitmap(R.id.imageView_widget, bitmap);
        } catch (Exception e) {
            Log.d(TAG, "getViewAt: " + e);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(FavMovieWidget.EXTRA_ITEM, i);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        views.setOnClickFillInIntent(R.id.imageView_widget, intent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
