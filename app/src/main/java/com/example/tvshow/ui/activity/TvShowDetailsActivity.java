package com.example.tvshow.ui.activity;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ActivityTvShowDetailsBinding;
import com.example.tvshow.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.tvshow.service.model.TvShow;
import com.example.tvshow.ui.adapter.EpisodeAdapter;
import com.example.tvshow.ui.adapter.ImageSliderAdapter;
import com.example.tvshow.viewModel.MostPopularTvShowViewModel;
import com.example.tvshow.viewModel.TvShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;


public class TvShowDetailsActivity extends AppCompatActivity {

    private ActivityTvShowDetailsBinding activityTvShowDetailsBinding;
    private TvShowDetailsViewModel tvShowDetailsViewModel;

    private BottomSheetDialog bottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;

    private TvShow tvShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvShowDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_tv_show_details);
        doInitialization();

    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        activityTvShowDetailsBinding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TvShow)getIntent().getSerializableExtra("tvShow");
        getTvShowDetails();
    }

    private void getTvShowDetails(){
        activityTvShowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTvShowDetails(tvShowId).observe(
                this, tvShowDetailsResponse -> {
                    activityTvShowDetailsBinding.setIsLoading(false);
                    if (tvShowDetailsResponse.getTvShowDetail() != null){
                        if (tvShowDetailsResponse.getTvShowDetail().getPictures() != null){
                            loadImageSlider(tvShowDetailsResponse.getTvShowDetail().getPictures());
                        }
                        activityTvShowDetailsBinding.setTvShowImageUrl(
                                tvShowDetailsResponse.getTvShowDetail().getImagePath()
                        );
                        activityTvShowDetailsBinding.imageTvShow.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailsResponse.getTvShowDetail().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityTvShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.textReadMore.setOnClickListener(v -> {
                            if (activityTvShowDetailsBinding.textReadMore.getText().toString().equals("Read More")){
                                activityTvShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvShowDetailsBinding.textDescription.setEllipsize(null);
                                activityTvShowDetailsBinding.textReadMore.setText("Read Less");
                            }else {
                                activityTvShowDetailsBinding.textDescription.setMaxLines(4);
                                activityTvShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvShowDetailsBinding.textReadMore.setText("Read More");
                            }
                        });

                        activityTvShowDetailsBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailsResponse.getTvShowDetail().getRating())
                                )
                        );
                        if (tvShowDetailsResponse.getTvShowDetail().getGenres() != null){
                            activityTvShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetail().getGenres()[0]);

                        }else {
                            activityTvShowDetailsBinding.setGenre("N/A");
                        }
                        activityTvShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetail().getRuntime() + "Min");
                        activityTvShowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.buttonWebsite.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetail().getUrl()));
                            startActivity(intent);
                        });
                        activityTvShowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.buttonEpisode.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.buttonEpisode.setOnClickListener(v -> {
                            if (bottomSheetDialog == null){
                                bottomSheetDialog = new BottomSheetDialog(TvShowDetailsActivity.this);
                                layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TvShowDetailsActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodeContainer),
                                        false
                                );
                                bottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                layoutEpisodesBottomSheetBinding.episodeRecyclerView.setAdapter(
                                        new EpisodeAdapter(tvShowDetailsResponse.getTvShowDetail().getEpisodes())
                                );

                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format(
                                                "Episodes | %s",tvShow.getName()
                                        )
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(v1 -> bottomSheetDialog.dismiss());
                            }

                            FrameLayout frameLayout = bottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if (frameLayout != null){
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
                            }

                            bottomSheetDialog.show();
                        });
                        activityTvShowDetailsBinding.imageWatchList.setOnClickListener(v -> new CompositeDisposable().add(tvShowDetailsViewModel.addWatchList(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    activityTvShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_baseline_check_24);
                                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                                })

                        ));
                        activityTvShowDetailsBinding.imageWatchList.setVisibility(View.VISIBLE);
                        loadBasicTvShowDetails();
                    }
                }
        );
    }

    private void loadImageSlider(String[] sliderImage){
        activityTvShowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvShowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImage));
        activityTvShowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImage.length);
        activityTvShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count){
        ImageView [] indicator =  new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(0,0,8,0);
        for(int j = 0 ; j<indicator.length; j++){
             indicator[j] = new ImageView(getApplicationContext());
             indicator[j].setImageDrawable(ContextCompat.getDrawable(
                   getApplicationContext(),
                   R.drawable.background_slider_indicator_inactive
             ));
             indicator[j].setLayoutParams(layoutParams);
             activityTvShowDetailsBinding.layoutSliderIndicator.addView(indicator[j]);

        }

        activityTvShowDetailsBinding.layoutSliderIndicator.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position){
        int childCount = activityTvShowDetailsBinding.layoutSliderIndicator.getChildCount();
        for (int i = 0; i<childCount ;i++){
            ImageView imageView = (ImageView) activityTvShowDetailsBinding.layoutSliderIndicator.getChildAt(i);
            if (i == position){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_slider_indicator_active)
                );
            }else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_slider_indicator_inactive)

                );
            }
        }
    }

    private void loadBasicTvShowDetails(){
        activityTvShowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvShowDetailsBinding.setNetworkCountry(
                tvShow.getNetwork() + "(" +
                        tvShow.getCountry() + ")"
        );
        activityTvShowDetailsBinding.setStatus(tvShow.getStatus());
        activityTvShowDetailsBinding.setStartDate(tvShow.getStartDate());
        activityTvShowDetailsBinding.tvShowName.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.networkCountry.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
    }


}