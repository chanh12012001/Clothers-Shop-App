package com.example.clothersshopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (useMiniLayout) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        int icon =   rewardModelList.get(position).getIconCoupen();
        String title =   rewardModelList.get(position).getTitle();
        String discount =   rewardModelList.get(position).getDiscountPrice();
        String body =   rewardModelList.get(position).getCoupenBody();
        String date =   rewardModelList.get(position).getExpiryDate();

        viewholder.setData(icon, title, discount, body, date);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private ImageView coupenIcon;
        private TextView coupenTitle;
        private TextView coupenDiscountPrice;
        private TextView coupenBody;
        private TextView coupenExpiryDate;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            coupenIcon = itemView.findViewById(R.id.iv_icon_coupon_rewards_item);
            coupenTitle = itemView.findViewById(R.id.tv_coupen_title);
            coupenDiscountPrice = itemView.findViewById(R.id.tv_coupen_discount_price);
            coupenBody = itemView.findViewById(R.id.tv_coupen_body);
            coupenExpiryDate = itemView.findViewById(R.id.tv_coupen_validity);
        }

        private void setData(int resource, String title, String discountPrice, String body, String date) {
            coupenIcon.setImageResource(resource);
            coupenTitle.setText(title);
            coupenDiscountPrice.setText(discountPrice);
            coupenBody.setText(body);
            coupenExpiryDate.setText(date);

            if (useMiniLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetailsActivity.coupenIcon.setImageResource(resource);
                        ProductDetailsActivity.coupenTitle.setText(title);
                        ProductDetailsActivity.coupenDiscountPrice.setText(discountPrice);
                        ProductDetailsActivity.coupenBody.setText(body);
                        ProductDetailsActivity.coupenExpiryDate.setText(date);
                        ProductDetailsActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
