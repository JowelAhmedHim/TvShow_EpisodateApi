package com.example.tvshow.listener;

import com.example.tvshow.service.model.TvShow;

public interface WatchlistListener {

    void onTvShowClicked(TvShow tvShow);
    void removeTvShowFromWatchList(TvShow tvShow, int position);
}
