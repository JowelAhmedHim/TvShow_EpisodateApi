package com.example.tvshow.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ActivityMainBinding;
import com.example.tvshow.listener.TvShowListener;
import com.example.tvshow.service.model.TvShow;
import com.example.tvshow.service.repository.TvShowDetailsRepository;
import com.example.tvshow.ui.adapter.TvShowAdapter;
import com.example.tvshow.viewModel.MostPopularTvShowViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TvShowListener {

    private ActivityMainBinding activityMainBinding;
    private MostPopularTvShowViewModel viewModel;
    private List<TvShow> tvShows = new ArrayList<>();
    private TvShowAdapter tvShowAdapter;
    private int currentPage = 1;
    private int totalAvailablePage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        doInitialization();

    }

    private void doInitialization(){
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTvShowViewModel.class);
        tvShowAdapter = new TvShowAdapter(tvShows,this);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowAdapter);
        activityMainBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!activityMainBinding.tvShowRecyclerView.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePage){
                        currentPage += 1;
                        getMostPopularTvShow();
                    }
                }
            }
        });
        activityMainBinding.imageWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new  Intent(getApplicationContext(),WatchListActivity.class));
            }
        });

        activityMainBinding.imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
        getMostPopularTvShow();
    }

    private void getMostPopularTvShow(){
        toggleLoading();
        viewModel.getMostPopularTvShow(currentPage).observe(this,mostPopularTvShowResponse-> {
           toggleLoading();
            if (mostPopularTvShowResponse != null){
                totalAvailablePage = mostPopularTvShowResponse.getTotalPages();
                if (mostPopularTvShowResponse.getTvShows() != null){
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTvShowResponse.getTvShows());
                    tvShowAdapter.notifyItemRangeInserted(oldCount,tvShows.size());
                }
            }

        });
    }

    private void toggleLoading(){
        if (currentPage == 1){
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            }else {
                activityMainBinding.setIsLoading(true);
            }
        }else {
            if (activityMainBinding.getIsLoadingMore()!= null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            }else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }
}