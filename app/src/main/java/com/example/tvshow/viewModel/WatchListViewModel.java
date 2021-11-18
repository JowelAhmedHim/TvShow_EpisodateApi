package com.example.tvshow.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.tvshow.service.database.TvShowDatabase;
import com.example.tvshow.service.model.TvShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchListViewModel extends AndroidViewModel {

    private TvShowDatabase tvShowDatabase;

    public WatchListViewModel(Application application){
        super(application);
        tvShowDatabase = TvShowDatabase.getTvShowDatabase(application);

    }

    public Flowable<List<TvShow>> loadWatchList(){
        return tvShowDatabase.tvShowDao().getWatchList();
    }

    public Completable removeTvShowFromWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}
