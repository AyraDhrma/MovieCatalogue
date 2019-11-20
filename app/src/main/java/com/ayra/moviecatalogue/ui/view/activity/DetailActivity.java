package com.ayra.moviecatalogue.ui.view.activity;

import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.data.model.Movie;
import com.ayra.moviecatalogue.data.model.TvShow;
import com.ayra.moviecatalogue.db.AppDao;
import com.ayra.moviecatalogue.db.AppDatabase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_SHOW = "extra_show";
    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private ImageView ivBackground;
    private TextView tvTitle;
    private TextView tvOverview;
    private TextView tvDate;
    private AppDao appDao;
    private Movie movie;
    private TvShow tvShow;
    private TextView tvError;
    private FloatingActionButton btnFav;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView slideArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set UI
        initUI();

        // Set Toolbar
        setToolbar();

        // Get Parcelable
        getParcelableToDetail();

        // Create Room
        createRoom();

        // Check Condition Favorit
        checkFavorite();

        // Button Favorite Listener
        setButtonFavorite();

        // Set Behavior Bottom Sheet
        setBottomSheet();
    }

    private void setBottomSheet() {
        final Drawable imageArrowDown = getResources().getDrawable(R.drawable.arrow_down);
        final Drawable imageArrowUp = getResources().getDrawable(R.drawable.arrow_up);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    slideArrow.setImageDrawable(imageArrowDown);
                } else if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                    slideArrow.setImageDrawable(imageArrowUp);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    private void checkFavorite() {
        if (movie != null) {
            if (appDao.getMovieByTitle(movie.getTitle()) != null) {
                setFavoriteTrue();
            } else {
                setButtonFavorite();
            }
        } else if (tvShow != null) {
            if (appDao.getShowByTitle(tvShow.getName()) != null) {
                setFavoriteTrue();
            } else {
                setButtonFavorite();
            }
        }
    }

    private void setButtonFavorite() {
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie != null) {
                    if (appDao.getMovieByTitle(movie.getTitle()) != null) {
                        appDao.deleteMovieById(movie.getId());
                        setFavoriteFalse();
                        Toast.makeText(DetailActivity.this, getResources().getString(R.string.remove_from_favorite), Toast.LENGTH_SHORT).show();
                    } else {
                        insertToRoom();
                        Toast.makeText(DetailActivity.this, getResources().getString(R.string.add_to_myfavorite), Toast.LENGTH_SHORT).show();
                    }
                } else if (tvShow != null) {
                    if (appDao.getShowByTitle(tvShow.getName()) != null) {
                        appDao.deleteShowById(tvShow.getId());
                        setFavoriteFalse();
                        Toast.makeText(DetailActivity.this, getResources().getString(R.string.remove_from_favorite), Toast.LENGTH_SHORT).show();
                    } else {
                        insertToRoom();
                        Toast.makeText(DetailActivity.this, getResources().getString(R.string.add_to_myfavorite), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void insertToRoom() {
        try {
            if (movie != null) {
                appDao.insertFavMovie(movie);
                setFavoriteTrue();
            } else if (tvShow != null) {
                appDao.insertFavShow(tvShow);
                setFavoriteTrue();
            }
        } catch (SQLiteException e) {
            Toast.makeText(DetailActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createRoom() {
        appDao = Room.databaseBuilder(this, AppDatabase.class, "db_catalog")
                .allowMainThreadQueries()
                .build()
                .getAppDao();
    }

    private void initUI() {
        tvError = findViewById(R.id.error_data_not_load);
        ivBackground = findViewById(R.id.image_movie_detail);
        tvTitle = findViewById(R.id.title_detail);
        tvDate = findViewById(R.id.date_detail);
        tvOverview = findViewById(R.id.overview_detail);
        btnFav = findViewById(R.id.btn_favorite);
        slideArrow = findViewById(R.id.slide_arrow);
        LinearLayout bottomSheetLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
    }

    private void getParcelableToDetail() {
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        tvShow = getIntent().getParcelableExtra(EXTRA_SHOW);
        if (movie != null) {
            displayDetailsMovie(movie);
        } else if (tvShow != null) {
            displayDetailsShow(tvShow);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    private void displayDetailsShow(TvShow tvShow) {
        Glide.with(getApplicationContext())
                .load(IMAGE_BASE_URL + tvShow.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_image_black_24dp).error(R.drawable.ic_broken_image_black_24dp))
                .into(ivBackground);
        tvTitle.setText(tvShow.getName());
        tvDate.setText(tvShow.getFirstAirDate());
        tvOverview.setText(tvShow.getOverview());
    }

    private void displayDetailsMovie(Movie movie) {
        Glide.with(getApplicationContext())
                .load(IMAGE_BASE_URL + movie.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_image_black_24dp).error(R.drawable.ic_broken_image_black_24dp))
                .into(ivBackground);
        tvTitle.setText(movie.getTitle());
        tvDate.setText(movie.getReleaseDate());
        tvOverview.setText(movie.getOverview());
    }

    private void setToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setFavoriteTrue() {
        btnFav.setImageResource(R.drawable.favorite_true_24dp);
    }

    private void setFavoriteFalse() {
        btnFav.setImageResource(R.drawable.favorite_false_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
