package com.example.clothersshopapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;
    private Button continueBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemsRecyclerView = view.findViewById(R.id.cart_item_recyclerview);
        continueBtn = view.findViewById(R.id.btn_cart_continue);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.img_horizontal_item1,"Áo khoác nam-đen-XL ",2,"250000đ","300000đ",1,0,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.img_horizontal_item1,"Áo thun nam-trắng-XXL ",1,"100000đ","200000đ",1,1,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.img_horizontal_item1,"Áo khoác nam-đen-XL ",2,"250000đ","300000đ",2,0,1));
        cartItemModelList.add(new CartItemModel(1,"600000đ","100000","free"));

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(getContext(), AddAddressActivity.class);
                getContext().startActivity(deliveryIntent);
            }
        });

        return  view;
    }
}