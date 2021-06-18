package com.example.clothersshopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static boolean setSignUpFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout = findViewById(R.id.fragmentContainer);

        if (setSignUpFragment) {
            setSignUpFragment = false;
            setFragment(new SignUpFragment());
        } else {
            setFragment(new SignInFragment());
        }

        setFragment(new SignInFragment());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            SignUpFragment.disableClosebtn = false;
            SignInFragment.disableClosebtn = false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //Thay thế bất cứ thứ gì có trong fragment_container bằng fragment
        fragmentTransaction.replace(frameLayout.getId(), fragment);

        fragmentTransaction.commit(); //Commit the transaction
    }
}