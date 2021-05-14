package com.example.clothersshopapp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothersshopapp.CategoryAdapter;
import com.example.clothersshopapp.CategoryModel;
import com.example.clothersshopapp.HomePageAdapter;
import com.example.clothersshopapp.R;
import com.example.clothersshopapp.SliderModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
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
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Home"));

        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
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
//        RecyclerView testing = view.findViewById(R.id.home_page_recyclerview);
//        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
//        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        testing.setLayoutManager(testingLayoutManager);
//
//        List<HomePageModel> homePageModelList = new ArrayList<>();
//        homePageModelList.add(new HomePageModel(0,sliderModelList));
//
//        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
//        testing.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        ////
        return view;
    }
}