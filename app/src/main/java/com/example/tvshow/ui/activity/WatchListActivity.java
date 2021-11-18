package com.example.tvshow.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ActivityWatchListBinding;
import com.example.tvshow.listener.WatchlistListener;
import com.example.tvshow.service.model.TvShow;
import com.example.tvshow.ui.adapter.WatchlistAdapter;
import com.example.tvshow.viewModel.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchListBinding activityWatchListBinding;
    private WatchListViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TvShow> watchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchListBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_watch_list);
        doInitialization();

    }

    private void doInitialization(){
        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        watchList = new ArrayList<>();

    }

    private void loadWatchList(){
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchList().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tvShows -> {
            activityWatchListBinding.setIsLoading(false);
            if (watchList.size() > 0){
                watchList.clear();
            }
            watchList.addAll(tvShows);
            watchlistAdapter = new WatchlistAdapter(watchList,this);
            activityWatchListBinding.watchListRecyclerview.setAdapter(watchlistAdapter);
            activityWatchListBinding.watchListRecyclerview.setVisibility(View.VISIBLE);
            compositeDisposable.dispose();

        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWatchList();
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {

        Intent intent = new Intent(getApplicationContext(),TvShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);

    }

    @Override
    public void removeTvShowFromWatchList(TvShow tvShow, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.removeTvShowFromWatchList(tvShow)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(()->{
            watchList.remove(position);
            watchlistAdapter.notifyItemRemoved(position);
            watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
            compositeDisposable.dispose();
        }));
    }
}