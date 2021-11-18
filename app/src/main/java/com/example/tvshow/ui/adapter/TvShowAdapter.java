package com.example.tvshow.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ItemContainerTvShowBinding;
import com.example.tvshow.listener.TvShowListener;
import com.example.tvshow.service.model.TvShow;

import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {

    private List<TvShow> tvShows;
    private LayoutInflater inflater;
    private TvShowListener tvShowListener;

    public TvShowAdapter(List<TvShow> tvShows, TvShowListener tvShowListener) {
        this.tvShows = tvShows;
        this.tvShowListener = tvShowListener;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null){
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding  = DataBindingUtil.inflate(
          inflater, R.layout.item_container_tv_show,viewGroup,false
        );
        return new TvShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder tvShowViewHolder, int i) {
            tvShowViewHolder.bindTvShow(tvShows.get(i));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder{

       private ItemContainerTvShowBinding itemContainerTvShowBinding;

       public TvShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding){
           super(itemContainerTvShowBinding.getRoot());
           this.itemContainerTvShowBinding = itemContainerTvShowBinding;
       }

       public void bindTvShow(TvShow tvShow){
           itemContainerTvShowBinding.setTvShow(tvShow);
           itemContainerTvShowBinding.executePendingBindings();
           itemContainerTvShowBinding.getRoot().setOnClickListener(v -> {
               tvShowListener.onTvShowClicked(tvShow);
           });
       }
    }

}
