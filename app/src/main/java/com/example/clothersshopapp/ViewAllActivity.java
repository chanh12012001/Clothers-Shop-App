package com.example.clothersshopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Sản phẩm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        gridView = findViewById(R.id.grid_view);

        int layout_code = getIntent().getIntExtra("layout_code",-1);

        if(layout_code == 0) {
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            List<WishlistModel> wishlistModelList = new ArrayList<>();
            wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",1,"3",145,"200000đ","250000đ", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",1,"2",145,"200000đ","250000đ", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",0,"5",145,"200000đ","250000đ", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",3,"4.5",145,"200000đ","250000đ", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",1,"3",145,"200000đ","250000đ", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",1,"2",145,"200000đ","250000đ", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",0,"5",145,"200000đ","250000đ", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",3,"4.5",145,"200000đ","250000đ", "Cash on delivery"));
            WishlistAdapter adapter = new WishlistAdapter(wishlistModelList,false);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else if (layout_code == 1) {
            gridView.setVisibility(View.VISIBLE);

            List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo thun", "100000d", "TP. Hồ Chí Minh"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo Sơ mi", "200000d", "TP. Hà Nội"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo khoác nam", "400000d", "Bình Định"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo thun", "100000d", "TP. Hồ Chí Minh"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo thun", "100000d", "TP. Hồ Chí Minh"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo thun", "100000d", "TP. Hồ Chí Minh"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo thun", "100000d", "TP. Hồ Chí Minh"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo Sơ mi", "200000d", "TP. Hà Nội"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo khoác nam", "400000d", "Bình Định"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo thun", "100000d", "TP. Hồ Chí Minh"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo thun", "100000d", "TP. Hồ Chí Minh"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1, "Áo thun", "100000d", "TP. Hồ Chí Minh"));

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
            gridView.setAdapter(gridProductLayoutAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}