package com.myclothershopapp.adapter;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myclothershopapp.DBqueries;
import com.myclothershopapp.PrefManager;
import com.myclothershopapp.R;
import com.myclothershopapp.activities.DeliveryActivity;
import com.myclothershopapp.activities.Main2Activity;
import com.myclothershopapp.activities.ProductDetailsActivity;
import com.myclothershopapp.model.CartItemModel;
import com.myclothershopapp.model.RewardModel;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;
    private TotalAmountinterface getTotalAmmount;
    private PrefManager prefManager;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn, TotalAmountinterface getTotalAmmount) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
        this.getTotalAmmount = getTotalAmmount;
        prefManager = new PrefManager(cartTotalAmount.getContext());

    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new cartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new cartTotalAmountViewholder(cartTotalView);
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
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();
                Long productQuantity = cartItemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartItemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();
                long stockQty = cartItemModelList.get(position).getStockQuantity();
                boolean COD = cartItemModelList.get(position).isCOD();
                int realproductprceone = DBqueries.getproductPrice(viewHolder.itemView.getContext(), productPrice);
                int productcuttedpricesetup = Integer.parseInt(cartItemModelList.get(position).getProductPrice());

                Map<String, Long> qtymap = cartItemModelList.get(position).getHashmap();

                ((cartItemViewholder) viewHolder).setItemsDetails(productID, resource, title, freeCoupens, String.valueOf(realproductprceone), cuttedPrice, offersApplied, position, inStock, String.valueOf(productQuantity), maxQuantity, qtyError, qtyIds, stockQty, COD, qtymap, productcuttedpricesetup);


                /////////////////  total ammount     ////////////////////
                int totalItemss = 0;
                int totalItemPrices = 0;
                String deliveryPrices;
                int totalAmounts;
                int savedAmounts = 0;


                for (int x = 0; x < cartItemModelList.size(); x++) {

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {
                        int quantity = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                        totalItemss = totalItemss + quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                            int realproductprceonef = DBqueries.getproductPrice(viewHolder.itemView.getContext(), cartItemModelList.get(x).getProductPrice());
                            totalItemPrices = totalItemPrices + realproductprceonef * quantity;
                        } else {
                            totalItemPrices = totalItemPrices + Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()) * quantity;
                        }

                        if (prefManager.getDiscountAvailable()) {
                            int realproductprceonef = DBqueries.getproductPrice(viewHolder.itemView.getContext(), cartItemModelList.get(x).getProductPrice());

                            savedAmounts = savedAmounts + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - realproductprceonef) * quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {

                                savedAmounts = savedAmounts + (realproductprceonef - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        } else {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmounts = savedAmounts + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        }
                    }

                }
                if (totalItemPrices > 200000) {
                    deliveryPrices = "FREE";
                    totalAmounts = totalItemPrices;
                } else {
                    deliveryPrices = "15000";
                    totalAmounts = totalItemPrices + 15000;
                }

                cartItemModelList.get(position).setTotalItems(totalItemss);
                cartItemModelList.get(position).setTotalItemPrice(totalItemPrices);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrices);
                cartItemModelList.get(position).setTotalAmount(totalAmounts);
                cartItemModelList.get(position).setSavedAmount(savedAmounts);

                getTotalAmmount.totalcartamount(cartItemModelList.get(position).getTotalAmount());
                cartTotalAmount.setText(cartItemModelList.get(position).getTotalAmount() + " đồng");
                //////////////////////////////////   total ammount    /////////////////////

                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;

                for (int x = 0; x < cartItemModelList.size(); x++) {

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {
                        int quantity = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                        totalItems = totalItems + quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                            totalItemPrice = totalItemPrice + DBqueries.getproductPrice(viewHolder.itemView.getContext(),cartItemModelList.get(x).getProductPrice()) * quantity;
                        } else {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()) * quantity;
                        }

                        if (prefManager.getDiscountAvailable()) {
                            savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - DBqueries.getproductPrice(viewHolder.itemView.getContext(),cartItemModelList.get(x).getProductPrice())) * quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmount = savedAmount + (DBqueries.getproductPrice(viewHolder.itemView.getContext(),cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        } else {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        }
                    }

                }


                if (totalItemPrice > 200000) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                } else {
                    deliveryPrice = "15000";
                    totalAmount = totalItemPrice + 15000;
                }
                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);
                ((cartTotalAmountViewholder) viewHolder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount);
                break;
            default:
                return;
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class cartItemViewholder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView freeCoupenIcon;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupensApplied;
        private TextView productQuantity;
        private LinearLayout coupenReedemptionLayout;
        private TextView coupenRedemptionBody;
        private LinearLayout deleteBtn;
        private Button redeemBtn;
        private ImageView codIndicator;

        //////////////////////////coupen dialog start
        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;
        private RecyclerView coupensRecyclerView;
        private LinearLayout selectedCoupen;
        private TextView originalPrice;
        private TextView discountedPrice;
        private LinearLayout applyORremoveBtnContainer;
        private TextView footerText;
        private Button removeCoupenBtn, applyCoupenBtn;
        private String productOriginalPrice;
        private TextView cartveriationAttributes;
        ///////////////////////// coupendialog end

        public cartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupenIcon = itemView.findViewById(R.id.free_coupen_icon);
            freeCoupens = itemView.findViewById(R.id.tv_free_coupen);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupensApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            coupenReedemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);
            coupenRedemptionBody = itemView.findViewById(R.id.tv_coupen_redemption);
            codIndicator = itemView.findViewById(R.id.cod_indicator);
            cartveriationAttributes = itemView.findViewById(R.id.cartveriation_attributes);
            redeemBtn = itemView.findViewById(R.id.coupen_redemption_btn);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
        }

        private void setItemsDetails(final String productID, String resource, String title, Long freeCoupensNo, final String productPriceText, String cuttedPriceText, Long offersAppliedNo, final int position, boolean inStock, final String quantity, final Long maxQuantity, boolean qtyError, final List<String> qtyIds, final long stockQty, boolean COD, final Map<String, Long> qtymap, int productcuttedpricesetup) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder_image)).into(productImage);
            productTitle.setText(title);

            final Dialog checkCoupenPriceDialog = new Dialog(itemView.getContext());
            checkCoupenPriceDialog.setContentView(R.layout.coupen_redem_dialog);
            checkCoupenPriceDialog.setCancelable(true);
            checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (COD) {
                codIndicator.setVisibility(View.VISIBLE);
            } else {
                codIndicator.setVisibility(View.INVISIBLE);
            }

            if (inStock) {
                if (freeCoupensNo > 0) {
                    freeCoupenIcon.setVisibility(View.GONE);
                    freeCoupens.setVisibility(View.GONE);
                    if (freeCoupensNo == 1) {
                        freeCoupens.setText("Free " + freeCoupensNo + " coupen");
                    } else {
                        freeCoupens.setText("Free " + freeCoupensNo + " coupens");
                    }
                } else {
                    freeCoupenIcon.setVisibility(View.INVISIBLE);
                    freeCoupens.setVisibility(View.INVISIBLE);
                }
                productPrice.setText(productPriceText + " đồng");
                productPrice.setTextColor(Color.parseColor("#000000"));
                DBqueries.setCuttedPrice(cuttedPrice, itemView.getContext(), String.valueOf(productcuttedpricesetup));
                cuttedPrice.setText(cuttedPriceText + " đồng"); //////////////////////////////////////////////////////////////
                coupenReedemptionLayout.setVisibility(View.GONE);


                ///////////////////////////////coupen dialog start
                ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
                coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
                selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
                coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
                coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
                coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);
                footerText = checkCoupenPriceDialog.findViewById(R.id.footer_text);
                applyORremoveBtnContainer = checkCoupenPriceDialog.findViewById(R.id.apply_or_remove_btns_containers);
                removeCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.remove_btn);
                applyCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.apply_btn);
                footerText.setVisibility(View.GONE);
                applyORremoveBtnContainer.setVisibility(View.VISIBLE);
                originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
                discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);

                LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                coupensRecyclerView.setLayoutManager(layoutManager);

                originalPrice.setText(productPrice.getText());
                productOriginalPrice = productPriceText;
                MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(position, DBqueries.rewardModelList, true, coupensRecyclerView, selectedCoupen, productOriginalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice, cartItemModelList);
                coupensRecyclerView.setAdapter(myRewardsAdapter);
                myRewardsAdapter.notifyDataSetChanged();

                applyCoupenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                            for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                if (rewardModel.getCouenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                    rewardModel.setAlreadyUsed(true);
                                    coupenReedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                                    coupenRedemptionBody.setText(rewardModel.getCoupenBody());
                                    redeemBtn.setText("Coupen");
                                }

                            }
                            coupensApplied.setVisibility(View.VISIBLE);
                            cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2));
                            productPrice.setText(discountedPrice.getText());
                            String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2)));
                            coupensApplied.setText("Đã áp dụng mã giảm " + offerDiscountedAmt + " đồng");
                            notifyItemChanged(cartItemModelList.size() - 1);
                            notifyDataSetChanged();
                            checkCoupenPriceDialog.dismiss();

                        }
                    }
                });

                removeCoupenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList) {
                            if (rewardModel.getCouenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                        coupenTitle.setText("Mã giảm giá");
                        coupenExpiryDate.setText("Hợp lệ");
                        coupenBody.setText("Nhấn vào biểu tượng ở góc trên cùng bên phải để chọn phiếu giảm giá của bạn .");
                        coupensApplied.setVisibility(View.INVISIBLE);
                        coupenReedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.copenRed));
                        coupenRedemptionBody.setText("Áp dụng mã giảm của bạn tại đây.");
                        redeemBtn.setText("Quy đổi");
                        cartItemModelList.get(position).setSelectedCoupenId(null);
                        productPrice.setText(productPriceText + " đồng");
                        notifyItemChanged(cartItemModelList.size() - 1);
                        checkCoupenPriceDialog.dismiss();
                        notifyDataSetChanged();
                    }
                });

                toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogRecyclerView();
                    }
                });

                if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                    for (RewardModel rewardModel : DBqueries.rewardModelList) {
                        if (rewardModel.getCouenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                            coupenReedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                            coupenRedemptionBody.setText(rewardModel.getCoupenBody());
                            redeemBtn.setText("Mã giảm giá");

                            coupenBody.setText(rewardModel.getCoupenBody());
                            if (rewardModel.getType().equals("Discount")) {
                                coupenTitle.setText(rewardModel.getType());
                            } else {
                                coupenTitle.setText(rewardModel.getdiscORamt() + "%");
                            }
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MM yyyy hh:mm", Locale.getDefault());
                            coupenExpiryDate.setText("HSD: " + simpleDateFormat.format(rewardModel.getTimestamp()));
                        }

                    }
                    discountedPrice.setText(cartItemModelList.get(position).getDiscountedPrice() + " đồng");
                    coupensApplied.setVisibility(View.VISIBLE);
                    productPrice.setText(cartItemModelList.get(position).getDiscountedPrice() + " đồng");
                    String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
                    coupensApplied.setText("Đã áp dụng mã giảm " + offerDiscountedAmt + "đồng");
                } else {
                    coupensApplied.setVisibility(View.INVISIBLE);
                    coupenReedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.copenRed));
                    coupenRedemptionBody.setText("Áp dụng mã giảm của bạn tại đây.");
                    redeemBtn.setText("Quy đổi");
                }

                //////////////////////////////coupen dialog end
                productQuantity.setText("Số lượng: " + quantity);
                if (!showDeleteBtn) {
                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.unsuccessred));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.unsuccessred)));
                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.black)));
                    }
                }

                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(true);

                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);

                        ///////////////  product veriation ///////////////

                        int quantit = maxQuantity.intValue();

                        ///////////// product veriations  ///////////////

                        quantityNo.setHint("Max " + String.valueOf(quantit));
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!TextUtils.isEmpty(quantityNo.getText())) {
                                    if (Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0) {
                                        if (itemView.getContext() instanceof Main2Activity) {
                                            Log.d("mytag", "instanseof Main Activiry: TRUE");
                                            // khi người dùng main Activity và thay đổi qty từ mmy cart fragment
                                            cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                        } else {
                                            if (DeliveryActivity.fromCart) {
                                                cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                            }
                                        }
                                        productQuantity.setText("Số lượng: " + quantityNo.getText());
                                        notifyItemChanged(cartItemModelList.size() - 1);
                                        notifyDataSetChanged();

                                    } else {
                                        DynamicToast.make(itemView.getContext(), "Số lượng tối đa :" + maxQuantity.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                quantityDialog.dismiss();

                            }
                        });
                        quantityDialog.show();
                    }
                });

                if (prefManager.getDiscountAvailable()) {
                    offersApplied.setVisibility(View.INVISIBLE);
                    String offerDiscountedAmt = String.valueOf(Integer.parseInt(cartItemModelList.get(position).getProductPrice()) - DBqueries.getproductPrice(itemView.getContext(), cartItemModelList.get(position).getProductPrice()));
                    offersApplied.setText("Đã áp dụng mã giảm " + offerDiscountedAmt + " đồng");
                } else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }
            } else {
                productPrice.setText("Hết hàng");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.unsuccessred));
                cuttedPrice.setText("");
                coupenReedemptionLayout.setVisibility(View.GONE);
                freeCoupens.setVisibility(View.INVISIBLE);
                productQuantity.setVisibility(View.INVISIBLE);
                coupensApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
                freeCoupenIcon.setVisibility(View.INVISIBLE);
            }
            if (showDeleteBtn) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }

            redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RewardModel rewardModel : DBqueries.rewardModelList) {
                        if (rewardModel.getCouenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                            rewardModel.setAlreadyUsed(false);
                        }
                    }
                    checkCoupenPriceDialog.show();

                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList) {
                            if (rewardModel.getCouenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(false);
                            }

                        }
                    }
                    if (!ProductDetailsActivity.running_cart_query) {
                        ProductDetailsActivity.running_cart_query = true;
                        DBqueries.removeFromCart(cartItemModelList.get(position).getProductID(), position, itemView.getContext(), cartTotalAmount);
                    }
                }
            });
        }

        private void showDialogRecyclerView() {
            if (coupensRecyclerView.getVisibility() == View.GONE) {
                coupensRecyclerView.setVisibility(View.VISIBLE);
                selectedCoupen.setVisibility(View.GONE);
            } else {
                coupensRecyclerView.setVisibility(View.GONE);
                selectedCoupen.setVisibility(View.VISIBLE);
            }
        }
    }

    class cartTotalAmountViewholder extends RecyclerView.ViewHolder {
        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public cartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }

        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText) {
            totalItems.setText("Giá (" + totalItemText + " món hàng)");
            totalItemPrice.setText(totalItemPriceText + " đồng");
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            } else {
                deliveryPrice.setText(deliveryPriceText + " đồng");
            }
            totalAmount.setText(totalAmountText + " đồng");
            cartTotalAmount.setText(totalAmountText + " đồng");
            savedAmount.setVisibility(View.GONE);
            savedAmount.setText("Bạn đã tiết kiệm " + savedAmountText + " đồng cho hóa đơn này.");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0) {
                if (DeliveryActivity.fromCart) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                    DeliveryActivity.cartItemModelList.remove(DeliveryActivity.cartItemModelList.size() - 1);
                }
                if (showDeleteBtn) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface TotalAmountinterface {
        void totalcartamount(int ammount);
    }
}
