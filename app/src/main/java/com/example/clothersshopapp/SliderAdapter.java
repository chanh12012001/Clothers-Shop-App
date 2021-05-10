package com.example.clothersshopapp;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<SliderModel> sliderModelList;
    public SliderAdapter(List<SliderModel> sliderModelList){
        this.sliderModelList = sliderModelList;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        return super.instantiateItem(container, position) ;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o){
        return false;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 0;
    }
}
