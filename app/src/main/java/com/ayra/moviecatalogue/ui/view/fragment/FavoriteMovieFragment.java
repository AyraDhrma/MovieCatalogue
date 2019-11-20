package com.ayra.moviecatalogue.ui.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.data.model.Movie;
import com.ayra.moviecatalogue.db.AppDao;
import com.ayra.moviecatalogue.db.AppDatabase;
import com.ayra.moviecatalogue.ui.adapter.MovieFavAdapter;
import com.ayra.moviecatalogue.ui.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment {

    private RecyclerView rvFavMovie;
    private TextView tvError;
    private MovieFavAdapter movieFavAdapter;
    private AppDao appDao;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavMovie = view.findViewById(R.id.rv_movie);
        tvError = view.findViewById(R.id.error_data_not_load);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create Room
        createRoom();

        // Set RecyclerView
        setRecyclerView();

        // Get Data From ViewModel
        getDataFavorite();

    }

    private void getDataFavorite() {
        ArrayList<Movie> movies = (ArrayList<Movie>) appDao.getAllMovie();
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.setFavMovie(movies);
        mainViewModel.getFavMovie().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies == null) {
                    tvError.setVisibility(View.VISIBLE);
                } else {
                    tvError.setVisibility(View.GONE);
                    movieFavAdapter.setFavMovies(movies);
                    movieFavAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setRecyclerView() {
        rvFavMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavMovie.setHasFixedSize(true);
        movieFavAdapter = new MovieFavAdapter(getActivity());
        rvFavMovie.setAdapter(movieFavAdapter);
    }

    private void createRoom() {
        appDao = Room.databaseBuilder(Objects.requireNonNull(getActivity()), AppDatabase.class, "db_catalog")
                .allowMainThreadQueries()
                .build()
                .getAppDao();
    }


}