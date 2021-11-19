package com.example.tvshow.viewModel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshow.service.database.TvShowDatabase;
import com.example.tvshow.service.model.TvShow;
import com.example.tvshow.service.response.TvShowDetailsResponse;
import com.example.tvshow.service.repository.TvShowDetailsRepository;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;


public class TvShowDetailsViewModel extends AndroidViewModel {

    private TvShowDetailsRepository tvShowDetailsRepository;
    private TvShowDatabase tvShowDatabase;


    public TvShowDetailsViewModel (@NonNull Application application){
        super(application);
        tvShowDetailsRepository = new TvShowDetailsRepository();
        tvShowDatabase = TvShowDatabase.getTvShowDatabase(application);
    }

    public LiveData<TvShowDetailsResponse> getTvShowDetails(String tvShowId){
        return tvShowDetailsRepository.getTvShowDetails(tvShowId);
    }

    public Completable addWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);
    }

    public Flowable<TvShow> getTvShowFromWatchList(String tvShowId){
        return tvShowDatabase.tvShowDao().getTvShowFromWatchList(tvShowId);
    }

    public Completable removeTvShowFromWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }


}
