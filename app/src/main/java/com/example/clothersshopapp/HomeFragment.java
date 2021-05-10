package com.example.clothersshopapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {



    public HomeFragment() {

    }
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    /////banner slider
    private ViewPager bannerSlideViewPager;
    private List<SliderModel> sliderModelList;
    private int CurrentPage = 2;
    private Timer timer ;
    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME =3000;
    /////banner slider


    public View onResume(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Electronics"));
        categoryModelList.add(new CategoryModel("link","Appliances"));
        categoryModelList.add(new CategoryModel("link","Furniture"));
        categoryModelList.add(new CategoryModel("link","Fashion"));
        categoryModelList.add(new CategoryModel("link","Toys"));
        categoryModelList.add(new CategoryModel("link","Sports"));
        categoryModelList.add(new CategoryModel("link","Wall Arts"));
        categoryModelList.add(new CategoryModel("link","Shoes"));

        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        /////banner slider
        bannerSlideViewPager = view.findViewById(R.id.banner_slider_view_paper);
        sliderModelList = new ArrayList<SliderModel>();
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher_round));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher_foreground));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher));


        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
       bannerSlideViewPager.setAdapter(sliderAdapter);
       bannerSlideViewPager.setClipToPadding(false);
       bannerSlideViewPager.setPageMargin(20);
        /////banner slider
       ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
          public void onPageScrolled(int i, float v, int i1) {

            }

           @Override
            public void onPageSelected(int i) {
                CurrentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if(i == ViewPager.SCROLL_STATE_IDLE){
                    pageLooper();
                }
            }
        };
       bannerSlideViewPager.addOnPageChangeListener(onPageChangeListener);
       startBannerSlideShow();
        bannerSlideViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLooper();
                stopBannerSildeShow();
                if(event.getAction() == MotionEvent.ACTION_UP){
                   startBannerSlideShow();
               }
                return false;
            }
        });
        return view;
    }
    /////banner slider
    private void pageLooper(){
        if(CurrentPage == sliderModelList.size() - 2){
            CurrentPage = 2;
            bannerSlideViewPager.setCurrentItem(CurrentPage,false);
        }
        if(CurrentPage == 1){
            CurrentPage = sliderModelList.size() - 3;
            bannerSlideViewPager.setCurrentItem(CurrentPage,false);
        }
    }
    private void startBannerSlideShow(){
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if(CurrentPage >= sliderModelList.size()){
                    CurrentPage=1;
                }
                bannerSlideViewPager.setCurrentItem(CurrentPage++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },DELAY_TIME,PERIOD_TIME);
    }
    private void stopBannerSildeShow(){
        timer.cancel();
    }
    /////banner slider
}