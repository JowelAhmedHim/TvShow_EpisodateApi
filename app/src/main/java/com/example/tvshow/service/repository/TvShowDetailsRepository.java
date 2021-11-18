package com.example.tvshow.service.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshow.service.response.TvShowDetailsResponse;
import com.example.tvshow.service.network.ApiClient;
import com.example.tvshow.service.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailsRepository {

    private ApiService apiService;
    public TvShowDetailsRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowDetailsResponse> getTvShowDetails(String tvShowId){
        MutableLiveData<TvShowDetailsResponse> data = new MutableLiveData<>();
        apiService.getTvShowDetails(tvShowId).enqueue(new Callback<TvShowDetailsResponse>() {
            @Override
            public void onResponse(Call<TvShowDetailsResponse> call, Response<TvShowDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowDetailsResponse> call, Throwable throwable) {
                data.setValue(null);
            }
        });

        return data;
    }
}
