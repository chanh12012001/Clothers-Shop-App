package com.myclothershopapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myclothershopapp.R;
import com.myclothershopapp.model.CartItemModel;
import com.myclothershopapp.model.RewardModel;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView coupensRecyclerView;
    private LinearLayout selectedCoupen;
    private String productOriginalPrice;
    private TextView selectedCoupenTitle;
    private TextView selectedCoupenExpiryDate;
    private TextView selectedCoupenBody;
    private TextView discountedPrice;
    private int cartItemPosition = -1;
    private List<CartItemModel> cartItemModelList;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }
    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
    }
    public MyRewardsAdapter(int cartItemPosition,List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedPrice,List<CartItemModel> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartItemModelList = cartItemModelList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (useMiniLayout){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);

        }else {
          view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        String coupenId = rewardModelList.get(position).getCouenId();
         String type = rewardModelList.get(position).getType();
         Date validity = rewardModelList.get(position).getTimestamp();
         String body = rewardModelList.get(position).getCoupenBody();
         String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getdiscORamt();
        Boolean alreadyUsed = rewardModelList.get(position).getAlreadyUsed();
        viewholder.setData(coupenId,type,validity,body,upperLimit,lowerLimit,discORamt,alreadyUsed);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title);
            coupenExpiryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);
        }
        private void setData(final String coupenId,final String type, final Date validity, final String body, final String upperLimit, final String lowerLimit, final String discORamt, final boolean alreadyUsed){

            if (type.equals("Discount")){
                coupenTitle.setText(type);
            }else {
                coupenTitle.setText("Thẻ giảm "+discORamt+" đồng");
            }

            if (alreadyUsed){
             coupenBody.setText("Phiếu giảm giá này đã được sử dụng. Bạn có thể chọn phiếu giảm giá khác.");
             coupenBody.setTextColor(itemView.getContext().getResources().getColor(R.color.textColorPrimary));
             coupenExpiryDate.setText("Đã được sử dụng ");
             coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.textColorPrimary));
             coupenTitle.setTextColor(itemView.getContext().getResources().getColor(R.color.textColorPrimary));
            }else {
                coupenTitle.setTextColor(itemView.getContext().getResources().getColor(R.color.textColorPrimary));
                coupenBody.setTextColor(itemView.getContext().getResources().getColor(R.color.textColorPrimary));
                coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.coupenvalidity));
                coupenExpiryDate.setText("HSD: "+ DateFormat.getDateInstance().format(validity));
            }
            coupenBody.setTextColor(itemView.getContext().getResources().getColor(R.color.textColorPrimary));
            coupenBody.setText(body);

            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!alreadyUsed){
                            selectedCoupenTitle.setText(type);
                            selectedCoupenExpiryDate.setText(DateFormat.getDateInstance().format(validity));
                            selectedCoupenBody.setText(body);
                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                                if (type.equals("Discount")) {
                                    Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(discORamt) / 100;
                                    discountedPrice.setText(String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + " đồng");
                                    discountedPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.successGreen));
                                } else {
                                    discountedPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.successGreen));
                                    discountedPrice.setText(String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discORamt)) + " đồng");

                                }
                                 if (cartItemPosition != -1) {
                                     cartItemModelList.get(cartItemPosition).setSelectedCoupenId(coupenId);
                                 }

                            } else {
                                if (cartItemPosition != -1) {
                                     cartItemModelList.get(cartItemPosition).setSelectedCoupenId(null);
                                }
                                discountedPrice.setText("Mã giảm giá không hợp lệ ");
                                discountedPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.unsuccessred));
                                DynamicToast.make(itemView.getContext(), "Sản phẩm không phù hợp với các điều khoản của mã giảm giá . ", Toast.LENGTH_SHORT).show();

                            }

                            if (coupensRecyclerView.getVisibility() == View.GONE) {
                                coupensRecyclerView.setVisibility(View.VISIBLE);
                                selectedCoupen.setVisibility(View.GONE);
                            } else {
                                coupensRecyclerView.setVisibility(View.GONE);
                                selectedCoupen.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        }
    }
}