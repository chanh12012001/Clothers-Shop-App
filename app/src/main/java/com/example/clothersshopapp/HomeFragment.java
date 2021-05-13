package com.example.clothersshopapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.clothersshopapp.CategoryAdapter;
import com.example.clothersshopapp.CategoryModel;
import com.example.clothersshopapp.R;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import io.grpc.okhttp.internal.framed.FrameReader;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;

    //////Banner slider
    private ViewPager bannerSliderViewPager;
    private List<SliderModel> sliderModelList;
    private int currentPage = 2;
    private Timer timer;
    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;
    //////Banner slider


    ////// Strip Ad
    private ImageView stripAdImage;
    private ConstraintLayout stripAdContainer;
    ////// Strip Ad


    ////// Horizontal product layout
    private TextView horizontalLayoutTitle;
    private TextView horizontalViewAll;
    private RecyclerView horizontalRecyclerview;
    ////// Horizontal product layout

    ////// GridView product layout
    private TextView gridLayoutTitle;
    private TextView gridViewAll;
    private GridView gridView;
    ////// GridView product layout


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view .findViewById(R.id.category_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager((getActivity()));
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));

        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        //------------------Banner slider-----------------
        bannerSliderViewPager = view.findViewById(R.id.viewpage_banner);

        sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.drawable.ic_about_us, "#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_menu_camera,"#FFADDFF6"));

        sliderModelList.add(new SliderModel(R.drawable.img_banner_1,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_logo_shop,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_gmail,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_about_us,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_menu_camera,"#FFADDFF6"));

        sliderModelList.add(new SliderModel(R.drawable.img_banner_1,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_logo_shop,"#FFADDFF6"));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);

        bannerSliderViewPager.setCurrentItem(currentPage);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pageLooper();
                }
            }
        };
        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

        startBannerSlideShow();

        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLooper();
                stopBannerSlideShow();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startBannerSlideShow();
                }
                return false;
            }
        });
        //-------------------Banner slider---------------------


        //-------------------Strip Ad -------------------------
        stripAdImage = view.findViewById(R.id.strip_ad_image);
        stripAdContainer = view.findViewById(R.id.strip_ad_container);

        stripAdImage.setImageResource(R.drawable.img_strip_ad_1);
        stripAdContainer.setBackgroundColor(Color.parseColor("#C8CBCC"));
        //-------------------Strip Ad -------------------------


        //-------------------Horizontal Product Layout---------
        horizontalLayoutTitle = view.findViewById(R.id.horizontal_scroll_layout_title);
        horizontalViewAll = view.findViewById(R.id.tv_view_all_horizontal);
        horizontalRecyclerview = view.findViewById(R.id.horizontal_scroll_layout_recyclerview);

        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo Sơ mi", "200000d","TP. Hà Nội"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo khoác nam", "400000d","Bình Định"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));


        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalRecyclerview.setLayoutManager(linearLayoutManager);

        horizontalRecyclerview.setAdapter(horizontalProductScrollAdapter);
        horizontalProductScrollAdapter.notifyDataSetChanged();

        //-------------------Horizontal Product Layout---------

        //------------------- Grid Product Layout--------------
        gridLayoutTitle = view.findViewById(R.id.tv_grid_product_layout_title);
        gridViewAll = view.findViewById(R.id.tv_view_all_grid_product_layout);
        gridView = view.findViewById(R.id.gridview_product_layout);

        gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList));
        //------------------- Grid Product Layout--------------

        ////////////////////TESTING//////////////////////////
        RecyclerView testing = view.findViewById(R.id.testing);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(0,sliderModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.img_strip_ad_1,"#000000"));
        homePageModelList.add(new HomePageModel(2,getString(R.string.deal_of_the_day),horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(3,getString(R.string.deal_of_the_day),horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(0,sliderModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.ic_wishlist,"#ffff00"));
        homePageModelList.add(new HomePageModel(0,sliderModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.img_horizontal_item1,"#ff0000"));

        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
        testing.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ////////////////////TESTING//////////////////////////

        return view;
    }

    //------------------Banner slider-----------------
    private void pageLooper() {
        if (currentPage == sliderModelList.size() - 2) {
            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage,false);
        }

        if (currentPage == 1) {
            currentPage = sliderModelList.size() - 3;
            bannerSliderViewPager.setCurrentItem(currentPage,false);
        }
    }

    private void startBannerSlideShow() {

        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if  (currentPage >= sliderModelList.size()) {
                    currentPage = 1;
                }
                bannerSliderViewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);
    }

    private void stopBannerSlideShow() {
        timer.cancel();
    }
    //------------------Banner slider-----------------

}