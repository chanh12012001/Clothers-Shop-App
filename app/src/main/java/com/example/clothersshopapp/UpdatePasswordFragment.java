package com.example.clothersshopapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.PowerManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class UpdatePasswordFragment extends AppCompatActivity {

    public UpdatePasswordFragment(){
        //Requires empty public constructor
    }

    private EditText oldPassword, newPassword, ConfirmNewPassword;
    private Button btnUpdate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_update_password_fragment, container, false);

        oldPassword = view.findViewById(R.id.old_password);
        newPassword = view.findViewById(R.id.new_password);
        ConfirmNewPassword = view.findViewById(R.id.confirm_new_password);
        btnUpdate = view.findViewById(R.id.btn_update);

        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ConfirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void checkInput() {
        if (!TextUtils.isEmpty(oldPassword.getText()) && oldPassword.length() >= 8) {
            if (!TextUtils.isEmpty(newPassword.getText()) && newPassword.length() >= 8) {
                if (!TextUtils.isEmpty(ConfirmNewPassword.getText()) && ConfirmNewPassword.length() >= 8) {
                    btnUpdate.setEnabled(true);
                    btnUpdate.setTextColor(Color.rgb(255, 255, 255));
                } else {
                    btnUpdate.setEnabled(false);
                    btnUpdate.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                btnUpdate.setEnabled(false);
                btnUpdate.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            btnUpdate.setEnabled(false);
            btnUpdate.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }


    public void checkEmailAndPassword() {
        if (newPassword.getText().toString().equals(ConfirmNewPassword.getText().toString())) {
            //update password
        } else {
            ConfirmNewPassword.setError(getResources().getString(R.string.check_confirm_password));
        }
    }
}
