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
import com.ayra.moviecatalogue.data.entity.Movie;
import com.ayra.moviecatalogue.ui.view.detail.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Movie> movies = new ArrayList<>();

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
        Glide.with(context)
                .load(IMAGE_BASE_URL + movies.get(position).getPosterPath())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_image_black_24dp).error(R.drawable.ic_broken_image_black_24dp))
                .into(holder.ivPoster);
        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.tvDate.setText(movies.get(position).getReleaseDate());
        holder.ratingBar.setRating(movies.get(position).getRating() / 2);
    }

    @Override
    public int getItemCount() {
        return movies.size();
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
                    intent.putExtra(DetailActivity.EXTRA_MOVIE, movies.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }

}
