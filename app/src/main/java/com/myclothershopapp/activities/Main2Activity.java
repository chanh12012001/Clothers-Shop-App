package com.myclothershopapp.activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myclothershopapp.DBqueries;
import com.myclothershopapp.fragmment.MyAccountFragment;
import com.myclothershopapp.fragmment.MyCartFragment;
import com.myclothershopapp.fragmment.MyOrdersFragment;
import com.myclothershopapp.fragmment.MyRewardsFragment;
import com.myclothershopapp.fragmment.MyWishlistFragment;
import com.myclothershopapp.PrefManager;
import com.myclothershopapp.R;
import com.myclothershopapp.fragmment.SignInFragment;
import com.myclothershopapp.fragmment.SignUpFragment;
import com.myclothershopapp.ui.home.HomeFragment;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    Dialog alertDialog = null;
    public static boolean showCart = false;
    public static Activity main2Activity;
    public static boolean resetMain2Activity = false;
    private FrameLayout frameLayout;
    private TextView actionBarLogo;
    private int currentFragment = -1;
    public static NavigationView navigationView;
    private Window window;
    private Toolbar toolbar;
    private Dialog signInDialog;
    private FirebaseUser currentuser;
    private TextView badgeCount;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    private CircleImageView profileView;
    private TextView fullname, email;
    public static DrawerLayout drawer;
    FusedLocationProviderClient mFusedLocationClient;
    Snackbar snackbar;
    Dialog disableLocationdialog;
    private PrefManager prefManager;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        prefManager = new PrefManager(this);
        alertDialog = new Dialog(Main2Activity.this);
        alertDialog.setCancelable(false);

        // disable location dialog
        disableLocationdialog = new Dialog(Main2Activity.this);
        disableLocationdialog.setTitle("Location permission Denied");
        disableLocationdialog.setCancelable(false);

        snackbar = Snackbar.make(drawer, getResources().getString(R.string.message_no_locaion_permission_snackbar), Snackbar.LENGTH_LONG);
        snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);


        profileView = navigationView.getHeaderView(0).findViewById(R.id.main2_profile_image);
        fullname = navigationView.getHeaderView(0).findViewById(R.id.main2_fullname);
        email = navigationView.getHeaderView(0).findViewById(R.id.main2_email);

        Timer timer = new Timer();

        signInDialog = new Dialog(Main2Activity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);


        if (showCart) {
            //  main2Activity = this;
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            frameLayout = findViewById(R.id.main2_framelayout);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {

            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(0).setChecked(true);

            frameLayout = findViewById(R.id.main2_framelayout);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(), HOME_FRAGMENT);

        }


        final Intent registerIntent = new Intent(Main2Activity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                RegisterActivity.setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
            if (DBqueries.email == null) {
                FirebaseFirestore.getInstance().collection("USERS").document(currentuser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {


                            DBqueries.fullname = task.getResult().getString("fullname");
                            DBqueries.email = task.getResult().getString("email");
                            DBqueries.profile = task.getResult().getString("profile");

                            PrefManager prefManager = new PrefManager(Main2Activity.this);
                            if (task.getResult().getDate("Last seen") != null) {
                                prefManager.setDate(task.getResult().getDate("Last seen").getTime());

                            }

                            fullname.setText(DBqueries.fullname);
                            email.setText(DBqueries.email);
                            if (DBqueries.profile.equals("")) {
                                //       addProfileIcon.setVisibility(View.VISIBLE);
                            } else {
                                //     addProfileIcon.setVisibility(View.INVISIBLE);
                                Glide.with(Main2Activity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile_icon)).into(profileView);
                            }
                        } else {
                            String error = task.getException().getMessage();
                            DynamicToast.make(Main2Activity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                fullname.setText(DBqueries.fullname);
                email.setText(DBqueries.email);
                if (DBqueries.profile.equals("")) {
                    profileView.setImageResource(R.drawable.profile_icon);
                } else {
                    Glide.with(Main2Activity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile_icon)).into(profileView);
                }
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);

        }
        if (resetMain2Activity) {
            resetMain2Activity = false;
            actionBarLogo.setVisibility(View.VISIBLE);
            setFragment(new HomeFragment(), HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (currentuser != null) {
            DBqueries.checkNotifications(true, null);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            } else {
                if (showCart) {
                    main2Activity = null;
                    showCart = false;
                    finish();
                } else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main2, menu);

            MenuItem cartItem = menu.findItem(R.id.main2_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.mipmap.cart_white);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
            if (currentuser != null) {
                if (DBqueries.cartList.size() == 0) {
                    DBqueries.loadCartList(Main2Activity.this, new Dialog(Main2Activity.this), false, badgeCount, new TextView(Main2Activity.this));
                } else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if (DBqueries.cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    } else {
                        badgeCount.setText("99");
                    }
                }
            }

            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentuser == null) {
                        signInDialog.show();
                    } else {
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                    }
                }
            });

            MenuItem notifyItem = menu.findItem(R.id.main2_notification_icon);
            notifyItem.setActionView(R.layout.badge_layout);
            ImageView notifyIcon = notifyItem.getActionView().findViewById(R.id.badge_icon);
            notifyIcon.setImageResource(R.mipmap.bell);
            TextView notifyCount = notifyItem.getActionView().findViewById(R.id.badge_count);
            if (currentuser != null) {
                DBqueries.checkNotifications(false, notifyCount);
            }

            notifyItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent notificationIntent = new Intent(Main2Activity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                }
            });

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main2_search_icon) {
            if (currentuser == null) {
                signInDialog.show();
            } else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
            return true;
        } else if (id == R.id.main2_notification_icon) {
            if (currentuser == null) {
                signInDialog.show();
            } else {
                Intent notificationIntent = new Intent(this, NotificationActivity.class);
                startActivity(notificationIntent);
            }
            return true;
        } else if (id == R.id.main2_cart_icon) {
            if (currentuser == null) {
                signInDialog.show();
            } else {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            }
            return true;
        } else if (id == android.R.id.home) {
            if (showCart) {
                main2Activity = null;
                showCart = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        if (fragmentNo == CART_FRAGMENT || showCart) {
            navigationView.getMenu().getItem(7).setChecked(true);
            params.setScrollFlags(0);
        } else {
            params.setScrollFlags(scrollFlags);
        }
    }

    MenuItem menuItem;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem = item;

        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                int id = menuItem.getItemId();

                if (id == R.id.nav_boomshopy) {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    drawer.removeDrawerListener(this);

                    return;
                }

                if (currentuser != null) {

                    if (id == R.id.nav_recharge) {
                        Intent rechargeIntent = new Intent(Main2Activity.this, RechargeActivity.class);
                        startActivity(rechargeIntent);
                        drawer.removeDrawerListener(this);

                        return;
                    } else if (id == R.id.nav_my_orders) {
                        gotoFragment("My orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
                        drawer.removeDrawerListener(this);

                        return;
                    } else if (id == R.id.nav_my_rewards) {
                        gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
                        drawer.removeDrawerListener(this);

                        return;
                    } else if (id == R.id.nav_my_cart) {
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                        drawer.removeDrawerListener(this);

                        return;
                    } else if (id == R.id.nav_my_wishlist) {
                        gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
                        drawer.removeDrawerListener(this);

                        return;
                    } else if (id == R.id.nav_my_account) {
                        gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                        drawer.removeDrawerListener(this);

                        return;
                    }
                    else if (id == R.id.nav_sign_out) {
                        FirebaseAuth.getInstance().signOut();
                        DBqueries.clearData();
                        Intent registerIntent = new Intent(Main2Activity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                        finish();
                    }


                }else{
                    signInDialog.show();
                    drawer.removeDrawerListener(this);
                    return;
                }

                drawer.removeDrawerListener(this);
            }
        });
        return true;

    }

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            if (fragmentNo == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#00BCD4"));
                toolbar.setBackgroundColor(Color.parseColor("#00BCD4"));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fede_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }

    }
}
