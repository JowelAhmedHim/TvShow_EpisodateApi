package com.example.tvshow.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshow.service.response.TvShowResponse;
import com.example.tvshow.service.repository.MostPopularTvShowRepository;

public class MostPopularTvShowViewModel extends ViewModel {

    private MostPopularTvShowRepository mostPopularTvShowRepository;

    public MostPopularTvShowViewModel() {

        mostPopularTvShowRepository = new MostPopularTvShowRepository();

    }

    public LiveData<TvShowResponse> getMostPopularTvShow(int page){
        return mostPopularTvShowRepository.getMostPopularTvShows(page);
    }
}
