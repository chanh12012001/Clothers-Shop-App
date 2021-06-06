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

public class MyWishlistFragment extends Fragment {

    public MyWishlistFragment() {
        // Required empty public constructor
    }

    private RecyclerView wishlistRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(layoutManager);

        List<WishlistModel> wishlistModelList = new ArrayList<>();
        wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",1,"3",145,"200000đ","250000đ", "Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",1,"2",145,"200000đ","250000đ", "Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",0,"5",145,"200000đ","250000đ", "Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.img_horizontal_item1, "Áo thun nam-Trắng-XL",3,"4.5",145,"200000đ","250000đ", "Cash on delivery"));

        WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList, true);
        wishlistRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();

        return view;
    }
}