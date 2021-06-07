package com.example.clothersshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.clothersshopapp.MainActivity.showCart;

public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager productImagesViewPager;
    private TabLayout viewpagerIndicator;
    private Button coupenRedeemBtn;

    ///////////Coupen dialog////////////
    public static ImageView coupenIcon;
    public static TextView coupenTitle;
    public static TextView coupenDiscountPrice;
    public static TextView coupenBody;
    public static TextView coupenExpiryDate;

    private static RecyclerView coupensRecyclerView;
    private static LinearLayout selectedCoupen;
    ///////////Coupen dialog////////////

    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;

    /////////rating layout//////////
    private LinearLayout rateNowContainer;
    /////////rating layout//////////

    private Button buyNowBtn;

    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private FloatingActionButton btnAddToWishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        btnAddToWishlist = findViewById(R.id.btn_add_to_wishlist);
        productDetailsViewpager = findViewById(R.id.viewpager_product_details);
        productDetailsTablayout = findViewById(R.id.tablayout_product_details);
        buyNowBtn = findViewById(R.id.btn_buy_now);
        coupenRedeemBtn = findViewById(R.id.btn_coupon_redemption);

        List<Integer> productImages = new ArrayList<>();
        productImages.add(R.drawable.img_horizontal_item1);
        productImages.add(R.drawable.ic_profile);
        productImages.add(R.drawable.ic_add_profile);
        productImages.add(R.drawable.img_strip_ad_1);

        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
        productImagesViewPager.setAdapter(productImagesAdapter);

        viewpagerIndicator.setupWithViewPager(productImagesViewPager,true);

        btnAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ALREADY_ADDED_TO_WISHLIST) {
                    ALREADY_ADDED_TO_WISHLIST = false;
                    btnAddToWishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                } else {
                    ALREADY_ADDED_TO_WISHLIST = true;
                    btnAddToWishlist.setSupportImageTintList(getResources().getColorStateList(R.color.color_wishlist));
                }
            }
        });

        productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount()));

        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //---------------------rating layout------------------------------
        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(starPosition);
                }
            });
        }
        //---------------------rating layout------------------------------

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                startActivity(deliveryIntent);
            }
        });

        //------------------------coupen dialog---------------------------------
        Dialog checkCoupenPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.iv_toggle_recyclerview);
        coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen_container);

        coupenIcon = checkCoupenPriceDialog.findViewById(R.id.iv_icon_coupon_rewards_item);
        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.tv_coupen_title);
        coupenDiscountPrice = checkCoupenPriceDialog.findViewById(R.id.tv_coupen_discount_price);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.tv_coupen_body);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.tv_coupen_validity);

        TextView originalPrice = checkCoupenPriceDialog.findViewById(R.id.tv_original_price);
        TextView discountedPrice = checkCoupenPriceDialog.findViewById(R.id.tv_discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel(R.drawable.ic_coupen_fill2, "Mã giảm \n     giá","Giảm 100k", "Cho tất cả đơn hàng từ 1 triệu", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_free_ship, "Miễn phí \n   ship","Tối đa 15k", "Cho tất cả đơn hàng từ 100k", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_coupen_fill2, "Mã giảm \n     giá","Giảm 100k", "Cho tất cả đơn hàng từ 1 triệu", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_free_ship, "Miễn phí \n   ship","Tối đa 15k", "Cho tất cả đơn hàng từ 0 đồng", "HSD: 12/07/2001"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList, false);
        coupensRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });
        //------------------------coupen dialog---------------------------------

        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCoupenPriceDialog.show();
            }
        });
    }

    public static void showDialogRecyclerView() {
        if (coupensRecyclerView.getVisibility() == View.GONE) {
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        } else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    private void setRating(int starPosition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView btnStar = (ImageView)rateNowContainer.getChildAt(x);
            btnStar.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                btnStar.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle action bar item click here. The action bar will automatically handle clicks on the Home/Up button
        //, so long as you specify a parent activity in AndroidManifest.xml

        int id = item.getItemId();

        //noinspection SimlifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon){
            //todo: search
            return true;
        } else if (id == R.id.main_shopping_cart_icon) {
            Intent cartIntent = new Intent(ProductDetailsActivity.this,MainActivity.class);
            showCart = true;
            startActivity(cartIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}