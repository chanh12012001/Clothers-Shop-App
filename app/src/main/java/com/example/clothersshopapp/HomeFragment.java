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
    private List<CategoryModel> categoryModelFakelList = new ArrayList<>();
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private RecyclerView categoryRecyclerView;
    private RecyclerView homePageRecyclerView;
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

        ///categoryModelFakelList////
        categoryModelFakelList.add(new CategoryModel("null",""));
        categoryModelFakelList.add(new CategoryModel("null",""));
        categoryModelFakelList.add(new CategoryModel("null",""));
        categoryModelFakelList.add(new CategoryModel("null",""));
        categoryModelFakelList.add(new CategoryModel("null",""));
        categoryModelFakelList.add(new CategoryModel("null",""));
        categoryModelFakelList.add(new CategoryModel("null",""));
        ///categoryModelFakelList////
        ////homepageFakeList////
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#ffffff"));
        homePageModelFakeList.add(new HomePageModel(2,"","#ffffff",horizontalProductScrollModelList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#ffffff",horizontalProductScrollModelList));
        ////homepageFakeList////
        categoryAdapter = new CategoryAdapter(categoryModelFakelList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        adapter = new HomePageAdapter(homePageModelFakeList);
        homePageRecyclerView.setAdapter(adapter);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
         networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerView.setAdapter(adapter);


            if (categoryModelList.size() == 0){
                loadCategories(categoryRecyclerView, getContext());
            }else{
                categoryAdapter.notifyDataSetChanged();
            }



            if (lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                adapter = new HomePageAdapter(lists.get(0));
                loadFragmentData(homePageRecyclerView, getContext(),0, "Home");
            }else{
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }


        } else {
            Glide.with(this).load(R.drawable.no_internet_connection).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }

        /////Refresh Layout////

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                categoryModelList.clear();
                lists.clear();
                loadedCategoriesNames.clear();

                if (networkInfo != null && networkInfo.isConnected() == true) {
                    noInternetConnection.setVisibility(View.GONE);

                    categoryRecyclerView.setAdapter(categoryAdapter);

                    loadCategories(categoryRecyclerView, getContext());
                    loadedCategoriesNames.add("HOME");
                    lists.add(new ArrayList<HomePageModel>());
                    adapter = new HomePageAdapter(lists.get(0));
                    loadFragmentData(homePageRecyclerView, getContext(), 0, "Home");
                } else {
                    Glide.with(getContext()).load(R.drawable.no_internet_connection).into(noInternetConnection);
                    noInternetConnection.setVisibility(View.VISIBLE);
                }
            }
        });

        /////Refresh Layout////

        return view;
    }
}