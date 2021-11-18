package com.example.tvshow.service.response;

import com.example.tvshow.service.model.TvShowDetail;
import com.google.gson.annotations.SerializedName;

public class TvShowDetailsResponse {

    @SerializedName("tvShow")
    private TvShowDetail tvShowDetail;

    public TvShowDetail getTvShowDetail() {
        return tvShowDetail;
    }
}
