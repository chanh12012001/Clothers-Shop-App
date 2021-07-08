package com.myclothershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.myclothershopapp.ui.my_account.MyAccountFragment;
import com.myclothershopapp.ui.my_cart.MyCartFragment;
import com.myclothershopapp.ui.my_mall.MyMallFragment;
import com.myclothershopapp.ui.my_orders.MyOrdersFragment;
import com.myclothershopapp.ui.my_rewards.MyRewardsFragment;
import com.myclothershopapp.ui.my_wishlist.MyWishlistFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.myclothershopapp.DBquerries.categoryModelList;
import static com.myclothershopapp.DBquerries.lists;
import static com.myclothershopapp.DBquerries.loadCategories;
import static com.myclothershopapp.DBquerries.loadFragmentData;
import static com.myclothershopapp.DBquerries.loadedCategoriesNames;
import static com.myclothershopapp.RegisterActivity.setsignUpFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ConstraintLayout frameLayout;
    public static Activity mainActivity;
    private static final int MyMallFragment=0,CART_FRAGMENT=1,ORDERS_FRAGMENT=2,WISHLIST_FRAGMENT=3,REWARDS_FRAGMENT=4,MYACCOUNT_FRAGMENT=5;
    private int currentFragment=-1;
    private ImageView noInternet;
    private TextView actionbarLogo;
    private Window window;
    private Toolbar toolbar;
    public static Boolean showCart=false;
    private Dialog signInDialog;
    public static DrawerLayout drawer;
    private FirebaseUser currentUser;
    private TextView badgeCount;
    private int scrollFlags;
    public static  boolean resetMainActivity=false;
    private AppBarLayout.LayoutParams params;
    private CircularImageView addProfileIcon;
    private CircleImageView profileView;
    private TextView fullname,email;

    NavigationView navigationView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        actionbarLogo=findViewById(R.id.action_bar_logo);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params= (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags=params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_my_mall,
                R.id.nav_my_orders, R.id.nav_my_rewards, R.id.nav_my_cart,
                R.id.nav_my_wishlist, R.id.nav_my_account, R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_1);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().getItem(0).setChecked(true);



        fullname=navigationView.getHeaderView(0).findViewById(R.id.main_name);
        email=navigationView.getHeaderView(0).findViewById(R.id.main_email);
        addProfileIcon=navigationView.getHeaderView(0).findViewById(R.id.add_profile_image_icon);
        profileView=navigationView.getHeaderView(0).findViewById(R.id.main_profile_pic);

        if(showCart){
            //mainActivity=this;
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            frameLayout=findViewById(R.id.main_frame_layout);
            //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            gotoFragment("My Cart",new MyCartFragment(),-2);
        }
        else {
            //navigationView.setNavigationItemSelectedListener(MainActivity.this);
            navigationView.getMenu().getItem(0).setChecked(true);

            frameLayout=findViewById(R.id.main_frame_layout);

            setFragment(new MyMallFragment(), MyMallFragment);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        signInDialog=new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button signInDialogBtn=signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpDialogBtn=signInDialog.findViewById(R.id.sign_up_btn);

        signInDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninFragment.disableCloseBtn=true;
                SignupFragment.disableCloseBtn=true;
                signInDialog.dismiss();
                setsignUpFragment=false;
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        signUpDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninFragment.disableCloseBtn=true;
                SignupFragment.disableCloseBtn=true;
                signInDialog.dismiss();
                setsignUpFragment=true;
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            MenuItem menuItem;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawers();
                menuItem=item;
                if(currentUser != null) {
                    drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);
                            int id = menuItem.getItemId();
                            if (id == R.id.nav_my_mall) {
                                actionbarLogo.setVisibility(View.VISIBLE);
                                invalidateOptionsMenu();
                                setFragment(new MyMallFragment(), MyMallFragment);
                                //navigationView.getMenu().getItem(0).setChecked(true);
                                drawer.removeDrawerListener(this);
                            } else if (id == R.id.nav_my_orders) {
                                gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
                            } else if (id == R.id.nav_my_rewards) {
                                gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
                            } else if (id == R.id.nav_my_cart) {
                                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                            } else if (id == R.id.nav_my_wishlist) {
                                gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
                            } else if (id == R.id.nav_my_account) {
                                gotoFragment("My Account", new MyAccountFragment(), MYACCOUNT_FRAGMENT);
                            } else if (id == R.id.nav_sign_out) {
                                FirebaseAuth.getInstance().signOut();
                                DBquerries.clearData();
                                DBquerries.email=null;  //my code
                                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                                finish();
                            }
                            drawer.removeDrawerListener(this);
                        }
                    });
                    return true;
                }
                else {
                    signInDialog.show();
                    return false;
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);
        }
        else {
            if(DBquerries.email == null) {
                FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DBquerries.fullname = task.getResult().getString("name");
                            DBquerries.email = task.getResult().getString("email");
                            DBquerries.profile = task.getResult().getString("profile");

                            fullname.setText(DBquerries.fullname);
                            email.setText(DBquerries.email);
                            if (DBquerries.profile.equals("")) {
                                addProfileIcon.setVisibility(View.VISIBLE);
                            } else {
                                addProfileIcon.setVisibility(View.INVISIBLE);
                                Glide.with(MainActivity.this).load(DBquerries.profile).apply(new RequestOptions().placeholder(R.mipmap.user_blue)).into(profileView);
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                fullname.setText(DBquerries.fullname);
                email.setText(DBquerries.email);
                if (DBquerries.profile.equals("")) {
                    profileView.setImageResource(R.mipmap.user_blue);
                    addProfileIcon.setVisibility(View.VISIBLE);
                } else {
                    addProfileIcon.setVisibility(View.INVISIBLE);
                    Glide.with(MainActivity.this).load(DBquerries.profile).apply(new RequestOptions().placeholder(R.mipmap.user_blue)).into(profileView);
                }
            }

            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);
        }
        if(resetMainActivity){
            actionbarLogo.setVisibility(View.VISIBLE);
            resetMainActivity=false;
            setFragment(new MyMallFragment(), MyMallFragment);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(currentUser!=null) {  ///my code
            DBquerries.checkNotifications(true, null);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(currentFragment== MyMallFragment){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem cartItem = menu.findItem(R.id.main_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon=cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.shopping_cart);
            badgeCount=cartItem.getActionView().findViewById(R.id.badge_count);


            MenuItem notificationItem = menu.findItem(R.id.main_notification_icon);
            notificationItem.setActionView(R.layout.badge_layout);
            ImageView badgeeIcon=notificationItem.getActionView().findViewById(R.id.badge_icon);
            badgeeIcon.setImageResource(R.drawable.notification);
            TextView notifyCount=notificationItem.getActionView().findViewById(R.id.badge_count);

            if(currentUser!=null){
                DBquerries.checkNotifications(false,notifyCount);
            }
            notificationItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentUser == null){
                        signInDialog.show();
                    }else {
                        startActivity(new Intent(MainActivity.this,NotificationActivity.class));
                    }
                }
            });

            if(currentUser!=null){
                if(DBquerries.cartList.size() == 0){
                    DBquerries.loadCartList(MainActivity.this,new Dialog(MainActivity.this),false,badgeCount,new TextView(MainActivity.this));
                }else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if(DBquerries.cartList.size()<99) {
                        badgeCount.setText(String.valueOf(DBquerries.cartList.size()));
                    }else {
                        badgeCount.setText("99");
                    }
                }

            }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentUser == null){
                        signInDialog.show();
                    }else {
                        gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
                    }
                }
            });

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.main_search_icon){
            startActivity(new Intent(MainActivity.this,SearchActivity.class));
            return true;
        }else if(id==R.id.main_notification_icon){
            startActivity(new Intent(MainActivity.this,NotificationActivity.class));
            return true;
        }else if(id==R.id.main_cart_icon){
            if(currentUser == null){
                signInDialog.show();
            }else {
                gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
            }

            return true;
        }else if(id==android.R.id.home){
            if(showCart==true) {
                mainActivity=null;
                showCart = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragementNo) {
        actionbarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragementNo);
        if(fragementNo==CART_FRAGMENT || showCart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else {
            params.setScrollFlags(scrollFlags);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_1);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setFragment(Fragment fragment,int fragementNo){
        if(fragementNo!=currentFragment) {

            if(fragementNo==REWARDS_FRAGMENT){
                window.setStatusBarColor(Color.parseColor("#5b04b1"));
                toolbar.setBackgroundColor(Color.parseColor("#5b04b1"));
            }else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            currentFragment = fragementNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            if(currentFragment==MyMallFragment){
                currentFragment=-1;
                super.onBackPressed();
            }else {
                if(showCart == true){
                    mainActivity=null;
                    showCart=false;
                    finish();
                }else {
                    actionbarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new MyMallFragment(),MyMallFragment);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }
}