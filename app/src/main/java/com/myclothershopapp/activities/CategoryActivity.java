package com.myclothershopapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myclothershopapp.DBqueries;
import com.myclothershopapp.adapter.HomePageAdapter;
import com.myclothershopapp.model.HorizontalProductScrollModel;
import com.myclothershopapp.R;
import com.myclothershopapp.model.HomePageModel;
import com.myclothershopapp.model.SliderModel;
import com.myclothershopapp.model.WishlistModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
     private RecyclerView categoryRecyclerView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("categoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /////////////// HomePage Fake List Start
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("#FCE8E9",""));
        sliderModelFakeList.add(new SliderModel("#FCE8E9",""));
        sliderModelFakeList.add(new SliderModel("#FCE8E9",""));
        sliderModelFakeList.add(new SliderModel("#FCE8E9",""));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#FCE8E9"));
        homePageModelFakeList.add(new HomePageModel(2,"","#FCE8E9",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#FCE8E9",horizontalProductScrollModelFakeList));

        //////////////// HomePage Fake List End


        categoryRecyclerView = findViewById(R.id.category_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);
        adapter = new HomePageAdapter(homePageModelFakeList,this);

        int listPosition = 0;
        for (int x = 0; x < DBqueries.loadedCategoriesNames.size(); x++){
            if (DBqueries.loadedCategoriesNames.get(x).equals(title.toUpperCase())){
              listPosition = x;
            }
        }
        if (listPosition == 0){
            DBqueries.loadedCategoriesNames.add(title.toUpperCase());
            DBqueries.lists.add(new ArrayList<HomePageModel>());
          DBqueries.loadFragmentData(categoryRecyclerView,this, DBqueries.loadedCategoriesNames.size() - 1,title, this);
        }else {
            adapter = new HomePageAdapter(DBqueries.lists.get(listPosition),this);

        }
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
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.main2_search_icon){
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
           return true;
        }else if (id == android.R.id.home){
             finish();
             return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
