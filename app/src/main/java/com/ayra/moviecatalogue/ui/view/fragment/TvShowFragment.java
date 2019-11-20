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
import com.ayra.moviecatalogue.data.model.TvShow;
import com.ayra.moviecatalogue.data.response.TvShowResponse;
import com.ayra.moviecatalogue.ui.adapter.TvShowAdapter;
import com.ayra.moviecatalogue.ui.viewmodel.MainViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {

    private RecyclerView rvTvShow;
    private TvShowAdapter tvShowAdapter;
    private final ArrayList<TvShow> tvShow = new ArrayList<>();
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView tvError;

    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTvShow = view.findViewById(R.id.rv_tv);
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
        displayTvShow();
    }

    private void setRecyclerView() {
        rvTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTvShow.setHasFixedSize(true);
        tvShowAdapter = new TvShowAdapter(getActivity());
        rvTvShow.setAdapter(tvShowAdapter);
    }

    private void displayTvShow() {
        MainViewModel tvShowViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        tvShowViewModel.getTvShow().observe(this, new Observer<TvShowResponse>() {
            @Override
            public void onChanged(TvShowResponse tvShowResponse) {
                if (tvShowResponse == null) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
                } else {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    tvError.setVisibility(View.GONE);
                    List<TvShow> movieResponseMovies = tvShowResponse.getResults();
                    tvShow.addAll(movieResponseMovies);
                    tvShowAdapter.setTvShows(tvShow);
                    tvShowAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
