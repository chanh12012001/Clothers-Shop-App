package com.example.clothersshopapp;

import android.content.Context;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.clothersshopapp.DBqueries.categoryModelList;
import static com.example.clothersshopapp.DBqueries.lists;
import static com.example.clothersshopapp.DBqueries.loadCategories;
import static com.example.clothersshopapp.DBqueries.loadFragmentData;
import static com.example.clothersshopapp.DBqueries.loadedCategoriesNames;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
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
        noInternetConnection = view.findViewById(R.id.no_internet_connection);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);

            categoryRecyclerView = view .findViewById(R.id.category_recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager((getActivity()));
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categoryRecyclerView.setLayoutManager(layoutManager);
            categoryAdapter = new CategoryAdapter(categoryModelList);
            categoryRecyclerView.setAdapter(categoryAdapter);

            if (categoryModelList.size() == 0){
                loadCategories(categoryAdapter, getContext());
            }else{
                categoryAdapter.notifyDataSetChanged();
            }

            homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
            LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
            testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            homePageRecyclerView.setLayoutManager(testingLayoutManager);

            if (lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                adapter = new HomePageAdapter(lists.get(0));
                loadFragmentData(adapter, getContext(),0, "Home");
            }else{
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);

        } else {
            Glide.with(this).load(R.drawable.no_internet_connection).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }
        return view;
    }
}