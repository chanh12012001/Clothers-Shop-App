package com.example.clothersshopapp;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.example.clothersshopapp.DBqueries.categoryModelList;
import static com.example.clothersshopapp.DBqueries.lists;
import static com.example.clothersshopapp.DBqueries.loadCategories;
import static com.example.clothersshopapp.DBqueries.loadFragmentData;
import static com.example.clothersshopapp.DBqueries.loadedCategoriesNames;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private RecyclerView homePageRecyclerView;
    private List<CategoryModel> categoryModeFakelList = new ArrayList<>();
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private ImageView noInternetConnection;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        categoryRecyclerView = view .findViewById(R.id.category_recyclerview);
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.purple_200),getContext().getResources().getColor(R.color.purple_200),getContext().getResources().getColor(R.color.purple_200));


        LinearLayoutManager layoutManager = new LinearLayoutManager((getActivity()));
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);


        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);
        ////categoryFakeList////
        categoryModeFakelList.add(new CategoryModel("null",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));
        categoryModeFakelList.add(new CategoryModel("",""));

        ////categoryFakeList////

        ////homepageFakeList////
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2,"","#dfdfdf",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#dfdfdf",horizontalProductScrollModelFakeList));
        ////homepageFakeList////

        categoryAdapter = new CategoryAdapter(categoryModeFakelList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        adapter = new HomePageAdapter(homePageModelFakeList);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
         networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);
            if (categoryModelList.size() == 0){
                loadCategories(categoryRecyclerView, getContext());
            }else{
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }



            if (lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homePageRecyclerView, getContext(),0, "Home");
            }else{
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }

        } else {
            Glide.with(this).load(R.drawable.no_internet_connection).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }

        ////refresh_layout////
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                categoryModelList.clear();
                lists.clear();
                loadedCategoriesNames.clear();
                if (networkInfo != null && networkInfo.isConnected() == true) {
                    noInternetConnection.setVisibility(View.GONE);
                    categoryAdapter = new CategoryAdapter(categoryModeFakelList);
                    adapter = new HomePageAdapter(homePageModelFakeList);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                    homePageRecyclerView.setAdapter(adapter);
                    loadCategories(categoryRecyclerView,getContext());
                    loadedCategoriesNames.add("HOME");
                    lists.add(new ArrayList<HomePageModel>());
                    loadFragmentData(homePageRecyclerView, getContext(),0, "Home");
                }
                else {
                    Glide.with(getContext()).load(R.drawable.no_internet_connection).into(noInternetConnection);
                    noInternetConnection.setVisibility(View.VISIBLE);
                }
            }
        });
        ////refresh_layout////

        return view;
    }
}