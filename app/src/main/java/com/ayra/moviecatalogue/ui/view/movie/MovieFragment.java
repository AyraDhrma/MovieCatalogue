package com.ayra.moviecatalogue.ui.view.movie;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.adapter.MovieAdapter;
import com.ayra.moviecatalogue.data.entity.Movie;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.viewmodel.MainViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private ArrayList<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.error_data_not_load)
    TextView tvError;
    @BindView(R.id.shimmer_container)
    ShimmerFrameLayout shimmerFrameLayout;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Start Shimmer
        shimmerFrameLayout.startShimmer();

        // Show Movie
        displayMovie();

    }

    private void displayMovie() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        int page = 1;
        String LANGUAGE = "en-US";
        String API_KEY = "0a296602e2e9f2572735bf2c91763741";
        mainViewModel.setMovies(API_KEY, LANGUAGE, page);
        mainViewModel.getMovies().observe(this, new Observer<MovieResponse>() {
            @Override
            public void onChanged(MovieResponse movieResponse) {
                if (movieResponse.getMovies() != null) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    tvError.setVisibility(View.GONE);
                    ArrayList<Movie> movies = movieResponse.getMovies();
                    movieList.addAll(movies);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
        setRecyclerView();
    }

    private void setRecyclerView() {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(getContext());
            movieAdapter.setMovies(movieList);
            rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
            rvMovie.setAdapter(movieAdapter);
            rvMovie.setHasFixedSize(true);
            rvMovie.setItemAnimator(new DefaultItemAnimator());
        } else {
            movieAdapter.notifyDataSetChanged();
        }
    }

}

