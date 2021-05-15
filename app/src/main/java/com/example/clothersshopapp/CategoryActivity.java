package com.example.clothersshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryRecyclerView = findViewById(R.id.category_recyclerview);

        //------------------Banner slider-----------------
        List<SliderModel> sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.drawable.ic_about_us, "#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_menu_camera,"#FFADDFF6"));

        sliderModelList.add(new SliderModel(R.drawable.img_banner_1,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_logo_shop,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_gmail,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_about_us,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_menu_camera,"#FFADDFF6"));

        sliderModelList.add(new SliderModel(R.drawable.img_banner_1,"#FFADDFF6"));
        sliderModelList.add(new SliderModel(R.drawable.ic_logo_shop,"#FFADDFF6"));
        //-------------------Banner slider---------------------

        //-------------------Horizontal Product Layout---------
        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo Sơ mi", "200000d","TP. Hà Nội"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo khoác nam", "400000d","Bình Định"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));

        //-------------------Horizontal Product Layout---------

        ////////////////////TESTING//////////////////////////
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(0,sliderModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.img_strip_ad_1,"#000000"));
        homePageModelList.add(new HomePageModel(2,getString(R.string.deal_of_the_day),horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(3,getString(R.string.deal_of_the_day),horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.ic_wishlist,"#ffff00"));
        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle action bar item click here. The action bar will automatically handle clicks on the Home/Up button
        //, so long as you specify a parent activity in AndroidManifest.xml

        int id = item.getItemId();

        //noinspection SimlifiableIfStatement
        if (id == R.id.action_search) {
            //todo: search
            return true;
        } else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}