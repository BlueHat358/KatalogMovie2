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

import com.example.katalogmovie.R;
import com.example.katalogmovie.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

        TextView judul, rilis, deskripsi;
        ImageView gambar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tv_judul);
            rilis = itemView.findViewById(R.id.tv_rilis);
            deskripsi = itemView.findViewById(R.id.tv_deskripsi);
            gambar = itemView.findViewById(R.id.img_image);
        }

        public void bindView(MovieResult movieResult) {
            judul.setText(movieResult.getmTitle());
            rilis.setText(movieResult.getmReleaseDate());
            deskripsi.setText(movieResult.getmOverview());
            String image = movieResult.getmPosterPath();

            Log.d(TAG, "bindView: " + movieResult.getmPosterPath());

            Picasso.with(context).load("https://image.tmdb.org/t/p/w185/" +image).fit().centerInside().into(gambar);
        }
    }
}
