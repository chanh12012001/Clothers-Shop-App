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

public class MyRewardsFragment extends Fragment {

    public MyRewardsFragment() {
        // Required empty public constructor
    }

    private RecyclerView rewardsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);

        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardsRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel(R.drawable.ic_coupen_fill2, "Mã giảm \n     giá","Giảm 100k", "Cho tất cả đơn hàng từ 1 triệu", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_free_ship, "Miễn phí \n   ship","Tối đa 15k", "Cho tất cả đơn hàng từ 100k", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_coupen_fill2, "Mã giảm \n     giá","Giảm 100k", "Cho tất cả đơn hàng từ 1 triệu", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_free_ship, "Miễn phí \n   ship","Tối đa 15k", "Cho tất cả đơn hàng từ 0 đồng", "HSD: 12/07/2001"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList, false);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        return view;
    }
}