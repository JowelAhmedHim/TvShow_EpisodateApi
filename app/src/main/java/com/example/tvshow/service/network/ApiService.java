package com.example.tvshow.service.network;

import com.example.tvshow.service.response.TvShowDetailsResponse;
import com.example.tvshow.service.response.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TvShowResponse> getMostPopularTvShows(@Query("page")int page);

    @GET("show-details")
    Call<TvShowDetailsResponse> getTvShowDetails(@Query("q") String tvShowId);
}
