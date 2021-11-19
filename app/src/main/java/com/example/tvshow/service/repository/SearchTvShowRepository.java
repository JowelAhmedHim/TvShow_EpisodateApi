package com.example.tvshow.service.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshow.service.model.TvShow;
import com.example.tvshow.service.network.ApiClient;
import com.example.tvshow.service.network.ApiService;
import com.example.tvshow.service.response.TvShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTvShowRepository {

    private ApiService apiService;
    public SearchTvShowRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);

    }

    public LiveData<TvShowResponse> searchTvShow(String query, int page){
        MutableLiveData<TvShowResponse> data = new MutableLiveData<>();
        apiService.searchTvShow(query,page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
