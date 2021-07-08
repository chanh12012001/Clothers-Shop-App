package com.example.clothersshopapp;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupens = cartItemModelList.get(position).getFreeCoupens();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offerApplied = cartItemModelList.get(position).getOffersApplied();

                ((CartItemViewholder)viewHolder).setItemDetails(productID,resource,title,freeCoupens,productPrice,cuttedPrice,offerApplied);
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

        private void setItemDetails(String productID, String resource, String title, Long freeCoupensNo, String productPriceText, String cuttedPriceText, Long offerAppliedNo) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_image_24)).into(productImage); //icon_placehodler
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

            productQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog quantityDialog = new Dialog(itemView.getContext());
                    quantityDialog.setContentView(R.layout.quantity_dialog);
                    quantityDialog.setCancelable(false);
                    EditText quantityNo = quantityDialog.findViewById(R.id.edt_quantity_no);
                    Button cancelBtn = quantityDialog.findViewById(R.id.btn_cancel_dialog);
                    Button okBtn = quantityDialog.findViewById(R.id.btn_ok_dialog);

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quantityDialog.dismiss();
                        }
                    });

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productQuantity.setText("Qty: " + quantityNo.getText());
                            quantityDialog.dismiss();
                        }
                    });
                    quantityDialog.show();
                }
            });
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
