package com.example.clothersshopapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrdersRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrdersRecyclerView = view.findViewById(R.id.recyclerview_my_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.img_horizontal_item1, "Áo thun nam trắng-XXL", "Delivered on Mon, 15th Jan 2021",2));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.img_horizontal_item1, "Áo thun nam trắng-XXL", "Delivered on Mon, 15th Jan 2021",3));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.img_horizontal_item1, "Áo thun nam trắng-XXL", "Cancelled",1));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.img_horizontal_item1, "Áo thun nam trắng-XXL", "Delivered on Mon, 15th Jan 2021",0));


        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return view;
    }
}