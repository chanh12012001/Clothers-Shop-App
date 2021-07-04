package com.example.clothersshopapp;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.clothersshopapp.MainActivity.showCart;
import static com.example.clothersshopapp.R.id.btn_add_to_cart;
import static com.example.clothersshopapp.R.id.start;
import static com.example.clothersshopapp.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;

    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewpagerIndicator;
    private Button coupenRedeemBtn;

    private LinearLayout coupenRedemption;
    private TextView rewardTitle;
    private TextView rewardBody;

    /////////Product description//////////
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;
    private TextView productOnlyDescriptionBody;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String productDescription;
    private String productOtherDetails;
    /////////Product description//////////

    /////////rating layout//////////
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    /////////rating layout//////////

    private Button buyNowBtn;
    private ImageView addtoCartBtn;

    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static FloatingActionButton btnAddToWishlist;

    private FirebaseFirestore firebaseFirestore;

    ///////////Coupen dialog////////////
    public static TextView coupenTitle;
    public static TextView coupenBody;
    public static TextView coupenExpiryDate;

    private static RecyclerView coupensRecyclerView;
    private static LinearLayout selectedCoupen;
    ///////////Coupen dialog////////////

    private Dialog signInDialog;
    private FirebaseUser currentUser;
    private Dialog loadingDialog;
    public static String productID;

    private DocumentSnapshot documentSnapshot;

    @SuppressLint("WrongViewCast")
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

        productTitle = findViewById(R.id.tv_product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_rating_miniview);
        productPrice = findViewById(R.id.tv_product_price);
        cuttedPrice = findViewById(R.id.tv_product_cutted_price);

        rewardBody = findViewById(R.id.reward_body);
        rewardTitle = findViewById(R.id.reward_title);

        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productOnlyDescriptionBody = findViewById(R.id.tv_product_details_body);

        totalRatings = findViewById(R.id.tv_total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_number_container);
        totalRatingsFigure = findViewById(R.id.tv_sum_rating_number);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.tv_average_rating);
        addtoCartBtn = findViewById(R.id.btn_add_to_cart);
        coupenRedemption = findViewById(R.id.coupen_redemption_layout);

        initialRating = -1;

        ////loading dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////loading dialog


        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("Products").document(productID)
                .get() .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    for (long x = 1; x < (long) documentSnapshot.get("no_of_products_images") + 1; x++){
                        productImages.add(documentSnapshot.get("products_image_"+x).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product_title").toString());
                    averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                    totalRatingMiniView.setText("("+(long)documentSnapshot.get("total_ratings")+") ratings");
                    productPrice.setText(documentSnapshot.get("product_price").toString()+" đồng");
                    cuttedPrice.setText(documentSnapshot.get("cutted_price").toString()+" đồng");

                    rewardTitle.setText((long)documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
                    rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                    if((boolean)documentSnapshot.get("use_tab_layout")){
                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        productDetailsOnlyContainer.setVisibility(View.GONE);
                        productDescription = documentSnapshot.get("product_description").toString();

                        productOtherDetails = documentSnapshot.get("product_other_details").toString();

                        for(long x = 1; x < (long)documentSnapshot.get("total_spec_titles") + 1; x++){
                            productSpecificationModelList.add(new ProductSpecificationModel(0,documentSnapshot.get("spec_title_"+x).toString()));
                            for(long y = 1; y < (long)documentSnapshot.get("spec_title_"+x+"_total_field") + 1; y++){
                                productSpecificationModelList.add(new ProductSpecificationModel(1,documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(), documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString()));
                            }
                        }
                    } else {
                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                    }

                    totalRatings.setText((long)documentSnapshot.get("total_ratings")+"ratings");

                    for (int x = 0; x < 5; x++) {
                        TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                        rating.setText(String.valueOf((long)documentSnapshot.get(5-x+"_star")));

                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                        int maxProgress = Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-x)+"_star"))));
                    }
                    totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                    averageRating.setText(documentSnapshot.get("average_rating").toString());
                    productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));

                    if (currentUser != null){
                        if (DBqueries.myRating.size() == 0) {
                            DBqueries.loadRatingList(ProductDetailsActivity.this);
                        }
                        if (DBqueries.cartList.size() == 0){
                            DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false);
                        }
                        if (DBqueries.wishList.size() == 0){
                            DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                        } else {
                            loadingDialog.dismiss();
                        }
                    } else {
                        loadingDialog.dismiss();
                    }

                    if (DBqueries.myRatedIds.contains(productID)) {
                        int index = DBqueries.myRatedIds.indexOf(productID);
                        initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) -1;
                        setRating(initialRating);
                    }

                    if(DBqueries.cartList.contains(productID)){
                        ALREADY_ADDED_TO_CART = true;
                    }else{
                        ALREADY_ADDED_TO_CART = false;
                    }

                    if(DBqueries.wishList.contains(productID)){
                        ALREADY_ADDED_TO_WISHLIST = true;
                        btnAddToWishlist.setSupportImageTintList(getResources().getColorStateList(R.color.color_wishlist));
                    }else{
                        btnAddToWishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        ALREADY_ADDED_TO_WISHLIST = false;
                    }

                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerIndicator.setupWithViewPager(productImagesViewPager,true);

        btnAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishList(index, ProductDetailsActivity.this);
                            btnAddToWishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            btnAddToWishlist.setSupportImageTintList(getResources().getColorStateList(R.color.color_wishlist));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                            addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));
                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DBqueries.wishlistModelList.size() != 0) {
                                            DBqueries.wishlistModelList.add(new WishlistModel(productID, documentSnapshot.get("products_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString()
                                                    , (long) documentSnapshot.get("free_coupens")
                                                    , documentSnapshot.get("average_rating").toString()
                                                    , (long) documentSnapshot.get("total_ratings")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , documentSnapshot.get("cutted_price").toString()
                                                    , (boolean) documentSnapshot.get("COD")));
                                        }
                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        btnAddToWishlist.setSupportImageTintList(getResources().getColorStateList(R.color.color_wishlist));
                                        DBqueries.wishList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Added to wishlist successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        btnAddToWishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;
                                }
                            });
                        }
                    }
                }
            }
        });

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
                        if(currentUser == null) {
                        signInDialog.show();
                    } else {
                            if (starPosition != initialRating) {
                                if (!running_rating_query) {
                                    running_rating_query = true;

                                    setRating(starPosition);
                                    Map<String, Object> updateRating = new HashMap<>();
                                    if (DBqueries.myRatedIds.contains(productID)) {

                                        TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                        TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                        updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                        updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                        updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                                    } else {
                                        updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                        updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                        updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                    }
                                    firebaseFirestore.collection("Products").document(productID)
                                            .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Map<String, Object> myRating = new HashMap<>();
                                                if (DBqueries.myRatedIds.contains(productID)) {
                                                    myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                                } else {
                                                    myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                    myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                    myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                                                }

                                                firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                        .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            if (DBqueries.myRatedIds.contains(productID)) {
                                                                DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                                TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                                TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                                oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                                finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));
                                                            } else {
                                                                DBqueries.myRatedIds.add(productID);
                                                                DBqueries.myRating.add((long) starPosition + 1);

                                                                TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                                rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                                totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ") ratings");
                                                                totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                                totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));
                                                                Toast.makeText(ProductDetailsActivity.this, "Thank you for rating! ", Toast.LENGTH_SHORT).show();
                                                            }

                                                            for (int x = 0; x < 5; x++) {
                                                                TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                                ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                                int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                                progressBar.setMax(maxProgress);
                                                                progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                            }
                                                            initialRating = starPosition;
                                                            averageRating.setText(calculateAverageRating(0, true));
                                                            averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                            if (DBqueries.wishList.contains(productID) && DBqueries.wishlistModelList.size() != 0) {
                                                                int index = DBqueries.wishList.indexOf(productID);
                                                                DBqueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                                DBqueries.wishlistModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));
                                                            }

                                                        } else {
                                                            setRating(initialRating);
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                        }
                                                        running_rating_query = false;
                                                    }
                                                });

                                            } else {
                                                running_rating_query = false;
                                                setRating(initialRating);
                                                String error = task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                    }
                }
            });
        }
        //---------------------rating layout------------------------------

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null)
                {
                    signInDialog.show();
                }
                else {
                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

        addtoCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null)
                {
                    signInDialog.show();
                }
                else {
                    if (!running_cart_query) {
                        running_cart_query = true;
                        if (ALREADY_ADDED_TO_CART) {
                            running_cart_query = false;
                            Toast.makeText(ProductDetailsActivity.this,"Already added to cart!",Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                            addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        if (DBqueries.cartItemModelList.size() != 0) {
                                            DBqueries.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM,productID, documentSnapshot.get("products_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString()
                                                    , (long) documentSnapshot.get("free_coupens")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , documentSnapshot.get("cutted_price").toString()
                                                    , (long) 1
                                                    , (long) 0
                                                    , (long) 0));
                                        }
                                        ALREADY_ADDED_TO_CART = true;
                                        DBqueries.cartList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
                                        running_cart_query = false;
                                    } else {
                                        running_cart_query = false;
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
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

        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.reward_title);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.reward_body);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.tv_coupen_validity);

        TextView originalPrice = checkCoupenPriceDialog.findViewById(R.id.tv_original_price);
        TextView discountedPrice = checkCoupenPriceDialog.findViewById(R.id.tv_discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel(R.drawable.ic_coupen_fill2, "Mã giảm \n     giá","Giảm 100k cho tất cả đơn hàng từ 1 triệu", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_free_ship, "Miễn phí \n   ship","Tối đa 15k cho tất cả đơn hàng từ 100k", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_coupen_fill2, "Mã giảm \n     giá","Giảm 100k cho tất cả đơn hàng từ 1 triệu", "HSD: 12/07/2001"));
        rewardModelList.add(new RewardModel(R.drawable.ic_free_ship, "Miễn phí \n   ship","Tối đa 15k cho tất cả đơn hàng từ 0 đồng", "HSD: 12/07/2001"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList, true);
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

        ////signinDialog///
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignIn = signInDialog.findViewById(R.id.btn_sign_in_dialog);
        Button dialogSignUp = signInDialog.findViewById(R.id.btn_sign_up_dialog);
        Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);

        dialogSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableClosebtn = true;
                SignUpFragment.disableClosebtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        dialogSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableClosebtn = true;
                SignUpFragment.disableClosebtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        ////signinDiaolog///

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
            coupenRedemption.setVisibility(View.GONE);
        }else{
            coupenRedemption.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DBqueries.cartList.size() == 0){
                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false);
            }
            if (DBqueries.wishList.size() == 0){
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog,false);
            } else {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }

        if (DBqueries.myRatedIds.contains(productID)) {
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
            setRating(initialRating);
        }

         if(DBqueries.cartList.contains(productID)){
             ALREADY_ADDED_TO_CART = true;
         }else{
             ALREADY_ADDED_TO_CART = false;
         }

        if(DBqueries.wishList.contains(productID)){
            ALREADY_ADDED_TO_WISHLIST = true;
            btnAddToWishlist.setSupportImageTintList(getResources().getColorStateList(R.color.color_wishlist));
        }else{
            btnAddToWishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }

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

    public static void setRating(int starPosition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView btnStar = (ImageView)rateNowContainer.getChildAt(x);
            btnStar.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                btnStar.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    private String calculateAverageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString())*x);
        }
        totalStars = totalStars + currentUserRating;
        if (update){
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0,3);
        }else{
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0,3);
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
            if(currentUser == null) {
                signInDialog.show();
            }
            else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}