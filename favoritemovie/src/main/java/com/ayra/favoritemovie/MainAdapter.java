package com.ayra.favoritemovie;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<Movie> movies = new ArrayList<>();
    private Activity activity;

    MainAdapter(Activity activity) {
        this.activity = activity;
    }

    ArrayList<Movie> getMovies() {
        return movies;
    }

    void setMovies(ArrayList<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
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
        Movie movie = movies.get(position);
        holder.titleMovie.setText(movie.getTitle());
        Glide.with(holder.itemView.getContext())
                .load("http://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(holder.ivMovie);
        holder.dateMovie.setText(movie.getReleaseDate());
        holder.ratingBar.setRating(movie.getRating() / 2);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_movie)
        ImageView ivMovie;
        @BindView(R.id.title_movie)
        TextView titleMovie;
        @BindView(R.id.date_movie)
        TextView dateMovie;
        @BindView(R.id.rating_movie)
        RatingBar ratingBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
