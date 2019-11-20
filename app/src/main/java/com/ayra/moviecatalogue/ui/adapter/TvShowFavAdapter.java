package com.ayra.moviecatalogue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.data.model.TvShow;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class TvShowFavAdapter extends RecyclerView.Adapter<TvShowFavAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<TvShow> tvShows = new ArrayList<>();

    public TvShowFavAdapter(Context context) {
        this.context = context;
    }

    public void setFavShow(ArrayList<TvShow> favShow) {
        this.tvShows = favShow;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        }
    }
}
