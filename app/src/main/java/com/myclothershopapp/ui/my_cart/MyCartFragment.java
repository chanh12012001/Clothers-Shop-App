package com.myclothershopapp.ui.my_cart;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myclothershopapp.CartAdapter;
import com.myclothershopapp.CartItemModel;
import com.myclothershopapp.DBquerries;
import com.myclothershopapp.DeliveryActivity;
import com.myclothershopapp.R;
import com.myclothershopapp.RewardModel;
import com.myclothershopapp.ui.my_rewards.MyRewardsFragment;

import java.util.ArrayList;

public class MyCartFragment extends Fragment {

    public MyCartFragment() {
        setRetainInstance(true);
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;
    private Button ContinueBtn;
    private Dialog loadingDialog;
    public static CartAdapter cartAdapter;
    private TextView totalAmount;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        totalAmount=view.findViewById(R.id.total_cart_amount);
//////////loading dialog

        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //////////loading dialog

        cartItemsRecyclerView=view.findViewById(R.id.cart_items_recycler_view);
        ContinueBtn=view.findViewById(R.id.cart_continue_btn);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

        cartAdapter=new CartAdapter(DBquerries.cartItemModelList,totalAmount,true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        ContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveryActivity.cartItemModelList=new ArrayList<>();
                DeliveryActivity.fromCart=true;

                for(int x=0;x<DBquerries.cartItemModelList.size();x++){
                    CartItemModel cartItemModel=DBquerries.cartItemModelList.get(x);
                    if(cartItemModel.isInStock()){
                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                loadingDialog.show();
                if(DBquerries.addressesModelList.size() == 0) {
                    DBquerries.loadAddresses(getContext(), loadingDialog,true);
                }else {
                    loadingDialog.dismiss();
                    startActivity(new Intent(getContext(), DeliveryActivity.class));
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.notifyDataSetChanged();

        if(DBquerries.rewardModelList.size() == 0){
            loadingDialog.show();
            DBquerries.loadRewards(getContext(),loadingDialog,false);
        }

        if(DBquerries.cartItemModelList.size() == 0){
            DBquerries.cartList.clear();
            DBquerries.loadCartList(getContext(),loadingDialog,true,new TextView(getContext()),totalAmount);
        }else {
            if(DBquerries.cartItemModelList.get(DBquerries.cartItemModelList.size()-1).getType() == CartItemModel.TOTAL_AMOUNT){
                LinearLayout parent=(LinearLayout)totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(CartItemModel cartItemModel:DBquerries.cartItemModelList){
            if(!TextUtils.isEmpty(cartItemModel.getSelectedCoupanId())){
                for(RewardModel rewardModel: DBquerries.rewardModelList){
                    if(rewardModel.getCoupanId().equals(cartItemModel.getSelectedCoupanId())){
                        rewardModel.setAlreadyUsed(false);

                    }
                }
                cartItemModel.setSelectedCoupanId(null);
                if(MyRewardsFragment.rewardsAdapter != null){
                    MyRewardsFragment.rewardsAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}