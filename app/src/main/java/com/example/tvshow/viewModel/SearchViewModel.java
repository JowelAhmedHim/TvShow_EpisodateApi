package com.example.tvshow.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshow.service.model.TvShow;
import com.example.tvshow.service.repository.SearchTvShowRepository;
import com.example.tvshow.service.response.TvShowResponse;

public class SearchViewModel  extends ViewModel {

    private SearchTvShowRepository searchTvShowRepository;

    public SearchViewModel(){
        searchTvShowRepository = new SearchTvShowRepository();
    }

    public LiveData<TvShowResponse> searchTvShow(String query,int page){
        return searchTvShowRepository.searchTvShow(query,page);
    }
}
