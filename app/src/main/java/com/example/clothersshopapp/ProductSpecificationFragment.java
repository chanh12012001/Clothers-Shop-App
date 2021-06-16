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

public class ProductSpecificationFragment extends Fragment {

    public ProductSpecificationFragment() {
        // Required empty public constructor
    }

    private RecyclerView productSpecificationRecyclerview;
    public List<ProductSpecificationModel> productSpecificationModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_specification, container, false);

        productSpecificationRecyclerview = view.findViewById(R.id.recyclerview_product_specification);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        productSpecificationRecyclerview.setLayoutManager(linearLayoutManager);
//        productSpecificationModelList.add(new ProductSpecificationModel(0, "General"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Kho", "243884"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Thương hiệu", "No Brand"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Chất liệu", "Cotton"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Xuất xứ", "Việt Nam"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Gửi từ", "Quận 9, TPHCM"));
//
//
//        productSpecificationModelList.add(new ProductSpecificationModel(0, "ok"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Kho", "243884"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Thương hiệu", "No Brand"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Chất liệu", "Cotton"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Xuất xứ", "Việt Nam"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Gửi từ", "Quận 9, TPHCM"));
//

        ProductSpecificationAdapter productSpecificationAdapter = new ProductSpecificationAdapter(productSpecificationModelList);
        productSpecificationRecyclerview.setAdapter(productSpecificationAdapter);
        productSpecificationAdapter.notifyDataSetChanged();

        return view;
    }
}