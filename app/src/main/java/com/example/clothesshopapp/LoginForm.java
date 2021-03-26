package com.example.clothesshopapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.PersistableBundle;
import android.text.NoCopySpan;
import android.view.Window;

import android.os.Bundle;

public class LoginForm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_form);
    }

}