package com.example.clothersshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.clothersshopapp.ui.home.HomePageModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView catrgoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("CatogoryName");
        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catrgoryRecyclerView = findViewById(R.id.category_recyclerview);
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
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));

        categoryAdapter = new CategoryAdapter(categoryModelList);
        catrgoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        ////
        List<SliderModel>sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"077AE4" ));


        ////
        ////
//        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
//        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        catrgoryRecyclerView.setLayoutManager(testingLayoutManager);
//
//        List<HomePageModel> homePageModelList = new ArrayList<>();
//        homePageModelList.add(new HomePageModel(0,sliderModelList));
//
//        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
//        catrgoryRecyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

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

        if (id == R.id.action_search) {
            //todo: search
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}