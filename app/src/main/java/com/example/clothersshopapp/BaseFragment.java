package com.example.clothersshopapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment {

    ProgressDialog progressDialog;

    public void showErrorSnackBar(String message, Boolean errorMessage) {

        if (errorMessage) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .setBackgroundTint(getResources().getColor(android.R.color.holo_green_dark))
                    .show();
        } else {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(android.R.color.holo_green_light))
                    .setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark))
                    .show();
        }

    }

    public void showProgressDialog(Activity activity) {

        //initialize Progress Dialog
        progressDialog = new ProgressDialog(activity);

        //show Dialog
        progressDialog.show();

        //set content view
        progressDialog.setContentView(R.layout.dialog_progress);

        //set Transparent Background
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}