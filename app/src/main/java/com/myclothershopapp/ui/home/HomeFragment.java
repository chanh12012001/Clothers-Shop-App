package com.myclothershopapp.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.myclothershopapp.PrefManager;
import com.myclothershopapp.adapter.CategoryAdapter;
import com.myclothershopapp.model.CategoryModel;
import com.myclothershopapp.DBqueries;
import com.myclothershopapp.adapter.HomePageAdapter;
import com.myclothershopapp.model.HorizontalProductScrollModel;
import com.myclothershopapp.model.SliderModel;
import com.myclothershopapp.model.WishlistModel;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.myclothershopapp.model.HomePageModel;
import com.myclothershopapp.activities.Main2Activity;
import com.myclothershopapp.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private List<CategoryModel> categoryModelFakeList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;
    private LottieAnimationView noInternetConnection;
    private Button retryBtn;
    PrefManager prefManager;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        prefManager = new PrefManager(getContext());
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection= view.findViewById(R.id.no_internet_connection);
        categoryRecyclerView =view.findViewById(R.id.category_recyclerView);
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        retryBtn = view.findViewById(R.id.retry_btn);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.successGreen),getContext().getResources().getColor(R.color.unsuccessred),getContext().getResources().getColor(R.color.successGreen));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);


        //////////////// Categories Fake List Start
        categoryModelFakeList.add(new CategoryModel("null",""));
         categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        //////////////// Categories Fake List End


        //////////////// HomePage Fake List Start
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("#C3C3C3",""));
        sliderModelFakeList.add(new SliderModel("#C3C3C3",""));
        sliderModelFakeList.add(new SliderModel("#C3C3C3",""));
        sliderModelFakeList.add(new SliderModel("#C3C3C3",""));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#C3C3C3"));
        homePageModelFakeList.add(new HomePageModel(2,"","#C3C3C3",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#C3C3C3",horizontalProductScrollModelFakeList));


        //////////////// HomePage Fake List End
        categoryAdapter = new CategoryAdapter(categoryModelFakeList);
        adapter = new HomePageAdapter(homePageModelFakeList,getActivity());
        connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            Main2Activity.drawer.setDrawerLockMode(0);
            Main2Activity.navigationView.setVisibility(View.VISIBLE);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            if (DBqueries.categoryModelList.size() == 0){
                DBqueries.loadCategories(categoryRecyclerView,getContext());
            }else {
                categoryAdapter = new CategoryAdapter(DBqueries.categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);

            DBqueries.lists.clear();
            DBqueries.loadedCategoriesNames.clear();
            if (DBqueries.lists.size() == 0){
                DBqueries.loadedCategoriesNames.add("HOME");
                  DBqueries.lists.add(new ArrayList<HomePageModel>());

                DBqueries.loadFragmentData(homePageRecyclerView,getContext(),0,"Home",getActivity());
            }else {
                adapter = new HomePageAdapter(DBqueries.lists.get(0),getActivity());
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);
        }else {
            Main2Activity.navigationView.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
        }
        ///////////////////// refresh Layout Start
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();

            }
        });
        ///////////////////// refresh Layout End
         retryBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 reloadPage();
             }
         });
        return view;
    }

    @SuppressLint("WrongConstant")
    private void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();
        DBqueries.clearData();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            Main2Activity.drawer.setDrawerLockMode(0);
            Main2Activity.navigationView.setVisibility(View.VISIBLE);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            categoryAdapter = new CategoryAdapter(categoryModelFakeList);
            adapter = new HomePageAdapter(homePageModelFakeList,getActivity());
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerView.setAdapter(adapter);
            DBqueries.loadCategories(categoryRecyclerView,getContext());
            DBqueries.loadedCategoriesNames.add("HOME");
            DBqueries.lists.add(new ArrayList<HomePageModel>());
            DBqueries.loadFragmentData(homePageRecyclerView,getContext(),0,"Home", getActivity());

        }else {
            Main2Activity.drawer.setDrawerLockMode(1);
            Main2Activity.navigationView.setVisibility(View.GONE);
            DynamicToast.make(getContext(),"No internet connection.",Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefManager.getShopClose()){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.alerttheme);
                        alertDialog.setTitle("Cửa hàng đã đóng cửa");
                        alertDialog.setMessage("Chúng tôi không cung cấp dịch vụ tại thời điểm này. vui lòng quay lại sau ");
                        alertDialog.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                                System.exit(0);
                            }
                        });

                        alertDialog.setCancelable(false);
                        alertDialog.create();
                        alertDialog.show();
        }
    }
}
