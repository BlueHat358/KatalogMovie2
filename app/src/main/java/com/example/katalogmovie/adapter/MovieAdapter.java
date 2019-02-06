package com.example.katalogmovie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katalogmovie.R;
import com.example.katalogmovie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> list) {
        this.context = context;
        this.MovieList = list;
    }

    private ArrayList<Movie> MovieList;

    public void clearData(){
        MovieList.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movieItem = MovieList.get(position);

        String judul = movieItem.getJudul();
        String rilis = movieItem.getRilis();
        String deskripsi = movieItem.getDeskripsi();
        String image = movieItem.getImage();

        holder.judul.setText(judul);
        holder.rilis.setText(rilis);
        holder.deskripsi.setText(deskripsi);

        Picasso.with(context).load("https://image.tmdb.org/t/p/w185/" + image).fit().centerInside().into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return MovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul, rilis, deskripsi;
        ImageView gambar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tv_judul);
            rilis = itemView.findViewById(R.id.tv_rilis);
            deskripsi = itemView.findViewById(R.id.tv_deskripsi);
            gambar = itemView.findViewById(R.id.img_image);
        }
    }
}
