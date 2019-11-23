package com.ayra.moviecatalogue.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.data.entity.TvShow;
import com.ayra.moviecatalogue.ui.view.detail.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<TvShow> tvShows = new ArrayList<>();

    public TvShowAdapter(Context context) {
        this.context = context;
    }

    public void setTvShows(ArrayList<TvShow> tvShows) {
        this.tvShows = tvShows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        TvShow tvShow = tvShows.get(position);
        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
        Glide.with(context)
                .load(IMAGE_BASE_URL + tvShow.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_image_black_24dp).error(R.drawable.ic_broken_image_black_24dp))
                .into(holder.ivPoster);
        holder.tvTitle.setText(tvShow.getName());
        holder.tvDate.setText(tvShow.getFirstAirDate());
        holder.ratingBar.setRating(tvShow.getRating() / 2);
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPoster;
        final TextView tvTitle;
        final TextView tvDate;
        final RatingBar ratingBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.image_movie);
            ivPoster.setClipToOutline(true);
            tvTitle = itemView.findViewById(R.id.title_movie);
            ratingBar = itemView.findViewById(R.id.rating_movie);
            tvDate = itemView.findViewById(R.id.date_movie);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_SHOW, tvShows.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

    }

}