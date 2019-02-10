package com.example.favorite.network;

import com.example.favorite.model.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String key_api = "56c57613f92ff6ae9064bc04ced5da14";
    String base_url = "https://api.themoviedb.org/3/";

    @GET("movie/{id}")
    Call<MovieResult> getMovieById(@Path("id") String id, @Query("api_key") String apiKey);
}
