package com.ayra.moviecatalogue.ui.view.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.adapter.SearchAdapter;
import com.ayra.moviecatalogue.data.entity.Movie;
import com.ayra.moviecatalogue.viewmodel.MainViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private SearchAdapter searchAdapter;
    @BindView(R.id.shimmer_container)
    ShimmerFrameLayout shimmer;
    @BindView(R.id.rv_result)
    RecyclerView rvResult;
    @BindView(R.id.error_data_not_load)
    TextView tvError;
    private String query;
    private Observer<ArrayList<Movie>> getResult = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null && movies.size() > 0) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                tvError.setVisibility(View.GONE);
                searchAdapter.setMovies(movies);
            } else {
                tvError.setVisibility(View.VISIBLE);
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
            }
        }
    };


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        // Setup RecyclerView
        setUpRecyclerView();

        // Get Query Search From Main Activity SearchView
        if (getArguments() != null) {
            query = getArguments().getString("search");
        }

        // Display Result With ViewModel
        displayResult(query);

        return view;
    }

    private void displayResult(String query) {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.searchMovie(query);
        viewModel.getMovieResults().observe(this, getResult);
    }

    private void setUpRecyclerView() {
        rvResult.setLayoutManager(new LinearLayoutManager(getContext()));
        searchAdapter = new SearchAdapter(getActivity());
        rvResult.setAdapter(searchAdapter);
        shimmer.startShimmer();
    }

}
