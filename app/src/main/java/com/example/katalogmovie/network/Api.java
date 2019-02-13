package com.example.katalogmovie.network;

import com.example.katalogmovie.model.Movie;
import com.example.katalogmovie.model.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String key_api = "56c57613f92ff6ae9064bc04ced5da14";
    String base_url = "https://api.themoviedb.org/3/";
    String image_url = "https://image.tmdb.org/t/p/w342/";

    @GET("movie/now_playing")
    Call<Movie> getNowPlaying(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<Movie> getUpcoming(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieResult> getMovieById(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("search/movie/")
    Call<Movie> getSearch(@Query("query") String q, @Query("api_key") String apiKey);
}
