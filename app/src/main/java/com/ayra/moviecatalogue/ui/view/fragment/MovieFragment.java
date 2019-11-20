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

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.data.model.Movie;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.ui.adapter.MovieAdapter;
import com.ayra.moviecatalogue.ui.viewmodel.MainViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private final ArrayList<Movie> movies = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private RecyclerView rvMovie;
    private TextView tvError;
    private ShimmerFrameLayout shimmerFrameLayout;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovie = view.findViewById(R.id.rv_movie);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_container);
        tvError = view.findViewById(R.id.error_data_not_load);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shimmerFrameLayout.startShimmer();

        // Set RecyclerView
        setRecyclerView();

        // Show Movie
        displayMovie();

    }

    private void setRecyclerView() {
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(getActivity());
        rvMovie.setAdapter(movieAdapter);
    }

    private void displayMovie() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getMovie().observe(this, new Observer<MovieResponse>() {
            @Override
            public void onChanged(MovieResponse movieResponse) {
                if (movieResponse == null) {
                    tvError.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                } else {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    tvError.setVisibility(View.GONE);
                    List<Movie> movieResponseMovies = movieResponse.getMovies();
                    movies.addAll(movieResponseMovies);
                    movieAdapter.setMovies(movies);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
