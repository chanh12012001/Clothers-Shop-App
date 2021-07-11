package com.myclothershopapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidstudy.networkmanager.Tovuti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myclothershopapp.PrefManager;
import com.myclothershopapp.R;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
//        SystemClock.sleep(3000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser== null){
            Intent registerIntent =new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(registerIntent);
            finish();

        }else {

                        FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).update("Last seen", FieldValue.serverTimestamp())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            FirebaseFirestore.getInstance().collection("SETTINGS").document("SETTINGS").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    PrefManager prefManager = new PrefManager(MainActivity.this);
                                                    prefManager.setDiscountAvailable(documentSnapshot.getBoolean("isPercentageAvailable"));
                                                    prefManager.setIsShopClose(documentSnapshot.getBoolean("isshopClosed"));

                                                    prefManager.setPercentageValue(String.valueOf(documentSnapshot.getLong("percentageValue")));
                                                    Intent main2Intent =new Intent(MainActivity.this,Main2Activity.class);
                                                    startActivity(main2Intent);
                                                    finish();


                                                }
                                            });
                                        }else {
                                            String error = task.getException().getMessage();
                                            DynamicToast.make(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tovuti.from(this).stop();
    }
}