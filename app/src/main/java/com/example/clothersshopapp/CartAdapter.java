package com.example.clothersshopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;

    public CartAdapter(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout,viewGroup,false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                int resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                int freeCoupens = cartItemModelList.get(position).getFreeCoupens();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                int offerApplied = cartItemModelList.get(position).getOffersApplied();

                ((CartItemViewholder)viewHolder).setItemDetails(resource,title,freeCoupens,productPrice,cuttedPrice,offerApplied);
                break;
            case CartItemModel.TOTAL_AMOUT:
                String totalItemPrice = cartItemModelList.get(position).getTotalItemPrice();
                String discountPrice = cartItemModelList.get(position).getDiscountPrice();
                String deliveryPrice = cartItemModelList.get(position).getDeliveryPrice();

                ((CartTotalAmountViewholder)viewHolder).setTotalAmount(totalItemPrice,discountPrice,deliveryPrice);

                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private ImageView freeCoupensIcon;
        private TextView freeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offerApplied;
        private TextView coupensApplied;
        private TextView productQuantity;

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_product);
            productTitle = itemView.findViewById(R.id.tv_product_title_cart_item);
            freeCoupensIcon = itemView.findViewById(R.id.iv_icon_coupon);
            freeCoupens = itemView.findViewById(R.id.tv_free_coupon);
            productPrice = itemView.findViewById(R.id.tv_product_price_cart_item);
            cuttedPrice = itemView.findViewById(R.id.tv_cutted_price);
            offerApplied = itemView.findViewById(R.id.tv_offers_applied);
            coupensApplied = itemView.findViewById(R.id.tv_coupens_applied);
            productQuantity = itemView.findViewById(R.id.tv_product_quantity);
        }

        private void setItemDetails(int resource, String title, int freeCoupensNo, String productPriceText, String cuttedPriceText, int offerAppliedNo) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (freeCoupensNo > 0) {
                freeCoupensIcon.setVisibility(View.VISIBLE);
                freeCoupens.setVisibility(View.VISIBLE);
                if (freeCoupensNo == 1) {
                    freeCoupens.setText("free " + freeCoupensNo + " coupen");
                } else {
                    freeCoupens.setText("free " + freeCoupensNo + " coupens");
                }
            } else {
                freeCoupensIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }
            productPrice.setText(productPriceText);
            cuttedPrice.setText(cuttedPriceText);
            if (offerAppliedNo > 0) {
                offerApplied.setVisibility(View.VISIBLE);
                offerApplied.setText(offerAppliedNo + " Offers applied");
            } else {
                offerApplied.setVisibility(View.INVISIBLE);
            }
        }
    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder {

        private TextView totalItemPrice;
        private TextView discountItemPrice;
        private TextView deliveryItemPrice;

        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItemPrice = itemView.findViewById(R.id.tv_provisional_price);
            discountItemPrice = itemView.findViewById(R.id.tv_discount_price);
            deliveryItemPrice = itemView.findViewById(R.id.tv_delivery_price);
        }

        private void setTotalAmount(String totalItemPriceText, String discountItemPriceText, String deliveryItemPriceText) {
            totalItemPrice.setText(totalItemPriceText);
            discountItemPrice.setText(discountItemPriceText);
            deliveryItemPrice.setText(deliveryItemPriceText);
        }
    }
}
