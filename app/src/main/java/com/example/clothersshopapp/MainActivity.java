package com.example.clothersshopapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;

    private FrameLayout frameLayout;
    private static int currentFragment = 1;
    TextView actionbarLogo;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        actionbarLogo = findViewById(R.id.tv_actionbar_logo1);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_home_navigation);

        //navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.nav_host_fragment);

        setFragment(new HomeFragment(), HOME_FRAGMENT);

//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
//                .setDrawerLayout(drawer)
//                .build();
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView.OnNavigationItemSelectedListener navListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_mymall) {
                    actionbarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(),HOME_FRAGMENT);

                } else if (id == R.id.nav_order) {

                } else if (id == R.id.nav_reward) {

                } else if (id == R.id.nav_cart) {
                    myCart();
                } else if (id == R.id.nav_wishlist) {

                } else if (id == R.id.nav_profile) {

                } else if (id == R.id.nav_setting) {

                } else if (id == R.id.nav_logout) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_help) {

                } else if (id == R.id.nav_about_us) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        };
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle action bar item click here. The action bar will automatically handle clicks on the Home/Up button
        //, so long as you specify a parent activity in AndroidManifest.xml

        int id = item.getItemId();

        if (id == android.R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        //noinspection SimlifiableIfStatement
         else if (id == R.id.action_search) {
            //todo: search
            return true;
        } else if (id == R.id.action_notification){
            //todo: notification
            return true;
        } else if (id == R.id.action_shopping_cart) {
            myCart();
            return true;
        } else if (id == R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void myCart() {
        actionbarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Cart");
        invalidateOptionsMenu();
        setFragment(new MyCartFragment(), CART_FRAGMENT);
        navigationView.getMenu().getItem(3).setChecked(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }
}