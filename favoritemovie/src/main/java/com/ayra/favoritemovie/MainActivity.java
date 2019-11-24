package com.ayra.favoritemovie;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoadCallback {

    public static final String AUTHORITY = "com.ayra.moviecatalogue";
    public static final String TABLE_NAME = "movie";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();
    private static final String EXTRA_STATE = "extra_state";
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.error_text)
    TextView errorText;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setHasFixedSize(true);
        adapter = new MainAdapter(this);
        rvMovie.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("Data Observer");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver dataObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);

        if (savedInstanceState == null) {
            new loadMovieAsync(this, this).execute();
        } else {
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (movies != null) {
                adapter.setMovies(movies);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(EXTRA_STATE, adapter.getMovies());
    }

    @Override
    public void preExecute() {
        runOnUiThread(() -> {
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        if (movies.size() > 0) {
            adapter.setMovies(movies);
        } else {
            adapter.setMovies(new ArrayList<>());
            errorText.setVisibility(View.VISIBLE);
        }
    }

    public static class loadMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallback> weakCallback;

        private loadMovieAsync(Context context, LoadCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            ArrayList<Movie> movies = new ArrayList<>();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            if (dataCursor != null) {
                while (dataCursor.moveToNext()) {
                    String title = dataCursor.getString(dataCursor.getColumnIndexOrThrow("title"));
                    String imagePath = dataCursor.getString(dataCursor.getColumnIndexOrThrow("posterPath"));
                    String releaseDate = dataCursor.getString(dataCursor.getColumnIndexOrThrow("releaseDate"));
                    float rating = dataCursor.getFloat(dataCursor.getColumnIndexOrThrow("rating"));
                    String overview = dataCursor.getString(dataCursor.getColumnIndexOrThrow("overview"));
                    int id = dataCursor.getInt(dataCursor.getColumnIndexOrThrow("id"));
                    movies.add(new Movie(title, imagePath, releaseDate, rating, overview, id));
                }
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new loadMovieAsync(context, (LoadCallback) context).execute();

        }
    }
}