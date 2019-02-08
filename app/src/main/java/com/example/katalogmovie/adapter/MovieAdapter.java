package com.example.katalogmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.katalogmovie.R;
import com.example.katalogmovie.Support.ItemClickSupport;
import com.example.katalogmovie.model.MovieResult;
import com.example.katalogmovie.ui.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.katalogmovie.ui.DetailActivity.MOVIE_DETAIL;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public static final String TAG = "TAG";

    private List<MovieResult> movieResultList = new ArrayList<>();
    private Context context;

    public void setMovieResultList(List<MovieResult> movieResultList) {
        this.movieResultList = movieResultList;
    }

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.movie_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindView(movieResultList.get(i));
    }

    @Override
    public int getItemCount() {
        return movieResultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView rv_movie;

        TextView judul, rilis, deskripsi;
        ImageView gambar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tv_judul);
            rilis = itemView.findViewById(R.id.tv_rilis);
            deskripsi = itemView.findViewById(R.id.tv_deskripsi);
            gambar = itemView.findViewById(R.id.img_image);
            rv_movie = itemView.findViewById(R.id.rv_Movie);
        }

        public void bindView(MovieResult movieResult) {
            judul.setText(movieResult.getmTitle());
            rilis.setText(movieResult.getmReleaseDate());
            deskripsi.setText(movieResult.getmOverview());
            String image = movieResult.getmPosterPath();

            Log.d(TAG, "bindView: " + movieResult.getmPosterPath());

            Glide.with(context).load("https://image.tmdb.org/t/p/w185/" +image).into(gambar);
        }
    }

    private void showSelectedMovie(MovieResult movie){

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(MOVIE_DETAIL, movie);
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmPosterPath());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmId());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmVoteAverage());
        context.startActivity(intent);
    }

}
