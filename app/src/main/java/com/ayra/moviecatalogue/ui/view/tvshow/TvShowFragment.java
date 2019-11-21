package com.ayra.moviecatalogue.ui.view.tvshow;


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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.data.entity.TvShow;
import com.ayra.moviecatalogue.data.response.TvShowResponse;
import com.ayra.moviecatalogue.ui.adapter.TvShowAdapter;
import com.ayra.moviecatalogue.ui.viewmodel.MainViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {

    private RecyclerView rvTvShow;
    private TvShowAdapter tvShowAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView tvError;
    private ArrayList<TvShow> showList = new ArrayList<>();

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

        // Show Movie
        displayTvShow();
    }

    private void displayTvShow() {
        String LANGUAGE = "en-US";
        int page = 1;
        String API_KEY = "0a296602e2e9f2572735bf2c91763741";
        MainViewModel tvShowViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        tvShowViewModel.setShows(API_KEY, LANGUAGE, page);
        tvShowViewModel.getShows().observe(this, new Observer<TvShowResponse>() {
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
                    ArrayList<TvShow> shows = tvShowResponse.getResults();
                    showList.addAll(shows);
                    tvShowAdapter.notifyDataSetChanged();
                }
            }
        });
        setRecyclerView();
    }

    private void setRecyclerView() {
        if (tvShowAdapter == null) {
            tvShowAdapter = new TvShowAdapter(getContext());
            tvShowAdapter.setTvShows(showList);
            rvTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
            rvTvShow.setAdapter(tvShowAdapter);
            rvTvShow.setHasFixedSize(true);
            rvTvShow.setItemAnimator(new DefaultItemAnimator());
        } else {
            tvShowAdapter.notifyDataSetChanged();
        }
    }

}
