package com.example.katalogmovie.adapter;

import android.content.Context;
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
import com.example.katalogmovie.model.Movie;
import com.example.katalogmovie.model.MovieResult;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public static final String TAG = "TAG";

    private List<MovieResult> movieResultList = new ArrayList<>();
    private Context context;

    public void setMovieResultList(List<MovieResult> movieResultList) {
        this.movieResultList = movieResultList;
    }
    public List<MovieResult> getMovieResultList() {
        return movieResultList;
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
            judul.setText(movieResult.getjudul());
            rilis.setText(movieResult.getrilis());
            deskripsi.setText(movieResult.getdeskripsi());
            String image = movieResult.getimage();

            Log.d(TAG, "bindView: " + movieResult.getimage());

            Glide.with(context).load("https://image.tmdb.org/t/p/w185/" +image).into(gambar);
        }
    }

}
