package com.example.clothersshopapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelList;
    private FirebaseFirestore firebaseFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view .findViewById(R.id.category_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager((getActivity()));
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryModelList = new ArrayList<CategoryModel>();
        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),documentSnapshot.get("categoryName").toString()));
                            }
                            categoryAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //------------------Banner slider-----------------

        //-------------------Banner slider---------------------

        //-------------------Horizontal Product Layout---------
//        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo Sơ mi", "200000d","TP. Hà Nội"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo khoác nam", "400000d","Bình Định"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.img_horizontal_item1,"Áo thun", "100000d","TP. Hồ Chí Minh"));

        //-------------------Horizontal Product Layout---------

        ////////////////////TESTING//////////////////////////
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        adapter = new HomePageAdapter(homePageModelList);
        homePageRecyclerView.setAdapter(adapter);

        firebaseFirestore.collection("CATEGORIES")
                .document("HOME")
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task){
                       if(task.isSuccessful()){
                           homePageModelList.add(new HomePageModel(categoryModelList,4));
                           for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                               if((long) documentSnapshot.get("view_type") == 0){

                                   List<SliderModel> sliderModelList = new ArrayList<>();
                                   long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                   for(int x = 1; x < no_of_banners + 1; x++)
                                   {
                                       sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + x).toString(),
                                               documentSnapshot.get("banner_"+ x+ "_background").toString()));
                                   }
                                   homePageModelList.add(new HomePageModel(0,sliderModelList));

                               }else if((long) documentSnapshot.get("view_type") == 1) {

                                   homePageModelList.add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString()
                                           , documentSnapshot.get("background").toString()));
                               }
                           }
                           adapter.notifyDataSetChanged();
                       }
                       else {
                           String error = task.getException().getMessage();
                           Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
                       }
                   }
                });


        ////////////////////TESTING//////////////////////////
        return view;
    }
}