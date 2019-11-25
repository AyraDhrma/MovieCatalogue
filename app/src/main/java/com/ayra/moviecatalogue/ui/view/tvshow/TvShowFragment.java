package com.ayra.moviecatalogue.ui.view.tvshow;


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
import com.ayra.moviecatalogue.adapter.TvShowAdapter;
import com.ayra.moviecatalogue.data.entity.TvShow;
import com.ayra.moviecatalogue.data.response.TvShowResponse;
import com.ayra.moviecatalogue.viewmodel.MainViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {

    @BindView(R.id.rv_tv)
    RecyclerView rvTvShow;
    private TvShowAdapter tvShowAdapter;
    @BindView(R.id.shimmer_container)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.error_data_not_load)
    TextView tvError;
    private ArrayList<TvShow> showList = new ArrayList<>();

    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);
        ButterKnife.bind(this, view);
        return view;
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
                if (tvShowResponse.getResults() != null) {
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
