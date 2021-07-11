package com.myclothershopapp.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myclothershopapp.PrefManager;
import com.myclothershopapp.R;
import com.myclothershopapp.activities.ProductDetailsActivity;
import com.myclothershopapp.activities.ViewAllActivity;
import com.myclothershopapp.model.HomePageModel;
import com.myclothershopapp.model.HorizontalProductScrollModel;
import com.myclothershopapp.model.SliderModel;
import com.myclothershopapp.model.WishlistModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int lastPosition = -1;
    FragmentActivity activity;
    PrefManager prefManager;

    public HomePageAdapter(List<HomePageModel> homePageModelList,FragmentActivity fragmentActivity) {
        this.homePageModelList = homePageModelList;
        this.activity =fragmentActivity;
        recycledViewPool = new RecyclerView.RecycledViewPool();
        prefManager = new PrefManager(fragmentActivity);
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {

            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;


            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View BannerSliderview= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sliding_ad_layout,viewGroup,false);
                return new BannerSliderViewHolder(BannerSliderview);
            case HomePageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_ad_layout, viewGroup, false);
                return new stripAdBannerViewholder(stripAdView);
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout, viewGroup, false);
                return new HorizontalProductViewholder(horizontalProductView);
            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_layout, viewGroup, false);
                return new GridProductViewholder(gridProductView);

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (homePageModelList.get(position).getType()== HomePageModel.BANNER_SLIDER){
            List<SliderModel> sliderModelList= homePageModelList.get(position).getSliderModelList();
            ((BannerSliderViewHolder) viewHolder).setBannerSliderViewPager(sliderModelList);
        }else if (homePageModelList.get(position).getType()== HomePageModel.STRIP_AD_BANNER){
            String resource = homePageModelList.get(position).getResource();
            String color = homePageModelList.get(position).getBackgroundColor();
            ((stripAdBannerViewholder) viewHolder).setStripAd(resource, color);

        }else if (homePageModelList.get(position).getType()== HomePageModel.HORIZONTAL_PRODUCT_VIEW){
            String layoutColor = homePageModelList.get(position).getBackgroundColor();
            String horizontalLayoutTitle = homePageModelList.get(position).getTitle();
            List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
            List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
            ((HorizontalProductViewholder) viewHolder).setHorizontalProductLayout(horizontalProductScrollModelList, horizontalLayoutTitle, layoutColor, viewAllProductList);

        }else if (homePageModelList.get(position).getType()== HomePageModel.GRID_PRODUCT_VIEW){
            String gridLayoutColor = homePageModelList.get(position).getBackgroundColor();
            String gridLayoutTitle = homePageModelList.get(position).getTitle();
            List<HorizontalProductScrollModel> gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
            ((GridProductViewholder) viewHolder).setGridProductLayout(gridProductScrollModelList, gridLayoutTitle, gridLayoutColor);
        }

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }


    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;
        final private long DELAY_TIME=3000;
        final private long PERIOD_TIME=3000;
        private List<SliderModel> arrangedList;


        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerSliderViewPager=itemView.findViewById(R.id.banner_slider_view_pager);

        }

        public void setBannerSliderViewPager(final List<SliderModel> sliderModelList){
            currentPage=2;
            if(timer!=null){
                timer.cancel();
            }
            arrangedList=new ArrayList<>();
            for(int x=0;x<sliderModelList.size();x++){
                arrangedList.add(x,sliderModelList.get(x));
            }
            arrangedList.add(0,sliderModelList.get(sliderModelList.size()-2));
            arrangedList.add(1,sliderModelList.get(sliderModelList.size()-1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));

            SliderAdapter sliderAdapter=new SliderAdapter(arrangedList);
            bannerSliderViewPager.setAdapter(sliderAdapter);

            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);

            bannerSliderViewPager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage=position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state== ViewPager.SCROLL_STATE_IDLE){
                        pageLooper(arrangedList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

            startBannerSlideShow(arrangedList);

            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper(arrangedList);
                    stopBannerSlideShow();

                    if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                        startBannerSlideShow(arrangedList);
                    }
                    return false;
                }
            });

        }

        private void pageLooper(List<SliderModel> sliderModelList){
            if(currentPage==sliderModelList.size()-2){
                currentPage=2;
                bannerSliderViewPager.setCurrentItem(currentPage,false);
            }

            if(currentPage==1){
                currentPage=sliderModelList.size()-3;
                bannerSliderViewPager.setCurrentItem(currentPage,false);
            }
        }

        private void startBannerSlideShow(final List<SliderModel> sliderModelList){
            final Handler handler=new Handler();
            final Runnable update=new Runnable() {
                @Override
                public void run() {
                    if(currentPage>=sliderModelList.size()){
                        currentPage=1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++,true);
                }
            };

            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },DELAY_TIME,PERIOD_TIME);
        }

        private void stopBannerSlideShow(){
            timer.cancel();
        }

    }

    public class stripAdBannerViewholder extends RecyclerView.ViewHolder {

        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;


        public stripAdBannerViewholder(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripAd(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder_image)).into(stripAdImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductViewholder extends RecyclerView.ViewHolder {

        private ConstraintLayout container;
        private TextView horizontalLayoutTitle;
        private Button horizontalLayoutViewAllBtn;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalLayoutViewAllBtn = itemView.findViewById(R.id.horizontal_scroll_view_all_btn);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
        }


        private void setHorizontalProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color, final List<WishlistModel> viewAllProductList) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontalLayoutTitle.setText(title);


            for (int x = 0; x < horizontalProductScrollModelList.size(); x++) {

                HorizontalProductScrollModel model = horizontalProductScrollModelList.get(x);

                if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()) {

                    int finalX = x;
                    FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
//                                 x[0]++;
                                DocumentSnapshot document = task.getResult();

                                //if ((boolean) document.get("PRODUCTISLIVE")) {
                                    model.setProductTitle(document.get("product_title").toString());
                                    model.setProduceImage(document.get("product_image_1").toString());
                                    model.setProductPrice(document.get("product_price").toString());
                                    if (document.get("product_subtitle") != null) {
                                        if (!document.get("product_subtitle").equals("")) {
                                            model.setProductDescription(document.get("product_subtitle").toString());
                                        }
                                    }

                                    WishlistModel wishlistModel = viewAllProductList.get(horizontalProductScrollModelList.indexOf(model));
                                    wishlistModel.setTotalRatings(document.getLong("total_rating"));
                                    wishlistModel.setRating(document.get("average_rating").toString());
                                    wishlistModel.setProductTitle(document.get("product_title").toString());
                                    wishlistModel.setProductPrice(document.get("product_price").toString());
                                    wishlistModel.setProductImage(document.get("product_image_1").toString());
                                    wishlistModel.setFreecoupens(document.getLong("free_coupens"));
                                    wishlistModel.setCuttedPrice(document.get("cutted_price").toString());
                                    wishlistModel.setCOD(document.getBoolean("COD"));
                                    wishlistModel.setInStock(document.getLong("stock_quantity") > 0);

                                if (horizontalRecyclerView.getAdapter() != null) {
                                    horizontalRecyclerView.getAdapter().notifyDataSetChanged();
                                }

                            }
                        }
                    });
                }
            }
            if (horizontalProductScrollModelList.size() > 6) {
                horizontalLayoutViewAllBtn.setVisibility(View.VISIBLE);

                horizontalLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishlistModelList = viewAllProductList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code", 0);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            } else {
                horizontalLayoutViewAllBtn.setVisibility(View.INVISIBLE);
            }
            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList,activity);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager);

            horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }
    }

    public class GridProductViewholder extends RecyclerView.ViewHolder {

        private ConstraintLayout container;
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private GridLayout gridProductLayout;

        public GridProductViewholder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutViewAllBtn = itemView.findViewById(R.id.grid_product_layout_viewall_btn);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);
        }

        private void setGridProductLayout(final List<HorizontalProductScrollModel> gridmodellist, final String title, String color) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);
            //old code
            for (HorizontalProductScrollModel model : gridmodellist) {
                if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()) {
                    FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                //      if ((boolean) document.get("PRODUCTISLIVE")) {
                                model.setProductTitle(document.get("product_title").toString());
                                model.setProduceImage(document.get("product_image_1").toString());
                                model.setProductPrice(document.get("product_price").toString());

                                if (document.get("product_subtitle") != null) {
                                    if (!document.get("product_subtitle").equals("")) {
                                        model.setProductDescription(document.get("product_subtitle").toString());
                                    }
                                }
                                setGridData(title, gridmodellist);

                            }
                        }
                    });
                }
            }


            gridLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewAllActivity.horizontalProductScrollModelList = gridmodellist;
                    Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra("layout_code", 1);
                    viewAllIntent.putExtra("title", title);
                    itemView.getContext().startActivity(viewAllIntent);
                }
            });

            setGridData(title, gridmodellist);
        }

        private void setGridData(String title, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
            for (int x = 0; x < 4; x++) {
                ImageView productImage = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_image);
                TextView productTitle = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_title);
                TextView productDiscription = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_description);

                TextView cuttedprice = gridProductLayout.getChildAt(x).findViewById(R.id.cuttedprice);
                TextView productPrice = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_price);
                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProduceImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder_image)).into(productImage);
                productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
                productDiscription.setText(horizontalProductScrollModelList.get(x).getProductDescription());
                cuttedprice.setVisibility(View.GONE);
                cuttedprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);


                if (prefManager.getDiscountAvailable()){
                    cuttedprice.setVisibility(View.GONE);
                    int percentage =  Integer.parseInt(prefManager.getPercentageValue());
                    cuttedprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    String price = horizontalProductScrollModelList.get(x).getProductPrice();
                    cuttedprice.setText(price+" đồng");

                    Double productpricecal = (double) percentage / (double) 100.00;
                    if (!price.equals("")) {
                        Double productrealprice = (Double) (productpricecal * Integer.parseInt(price));
                        int realpriceint = (int)Math.round(productrealprice);

                        productPrice.setText(( Integer.parseInt(price) - realpriceint) + " đồng");
                        Double p = Integer.parseInt(price) - productrealprice;
                        Double dif = Integer.parseInt(price) - p;
                        Double div = (double) dif / (double) Integer.parseInt(price);
                        int percentoff = (int) (div * 100);
                 //       discounttextview.setText(percentoff + "% off");
                   //     discounttextview.setVisibility(View.GONE);
                    }
                }else {
                    cuttedprice.setVisibility(View.GONE);
                   // discounttextview.setVisibility(View.GONE);
                    productPrice.setText(horizontalProductScrollModelList.get(x).getProductPrice()+" đồng");

                }
                    cuttedprice.setVisibility(View.GONE);
//                    cuttedprice.setText("" + horizontalProductScrollModelList.get(x).getCuttedPrice());
                    productPrice.setVisibility(View.VISIBLE);
//                    productPrice.setText("₹ " + horizontalProductScrollModelList.get(x).getProductPrice());


                gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#FFFFFF"));

                if (!title.equals("")) {
                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productDetailsIntent.putExtra("PRODUCT_ID", horizontalProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailsIntent);
                        }
                    });
                }

            }
        }

    }

}
