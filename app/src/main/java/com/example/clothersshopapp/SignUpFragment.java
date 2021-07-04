package com.example.clothersshopapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpFragment extends BaseFragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    private Button btnSignIn;
    private FrameLayout parentFrameLayout;

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    private Button btnSignUp;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableClosebtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        btnSignIn = view.findViewById(R.id.btnSave);
        parentFrameLayout = getActivity().findViewById(R.id.fragmentContainer);

        firstName = view.findViewById(R.id.edtFirstName);
        lastName = view.findViewById(R.id.edtLastName);
        email = view.findViewById(R.id.edtEmail);
        password = view.findViewById(R.id.edtPassword);
        confirmPassword = view.findViewById(R.id.edtConfirmPassword);

        btnSignUp = view.findViewById(R.id.btnSignUp);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore =  FirebaseFirestore.getInstance();

        if(disableClosebtn){
            btnSignIn.setVisibility(View.GONE);
        }else {
            btnSignIn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
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
        firstName.addTextChangedListener(new TextWatcher() {
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
        lastName.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
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
        confirmPassword.addTextChangedListener(new TextWatcher() {
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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                checkEmailAndPassword();
            }
        });
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    public void checkInput() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(firstName.getText())) {
                if (!TextUtils.isEmpty(lastName.getText())) {
                    if (!TextUtils.isEmpty(password.getText())) {
                        if (!TextUtils.isEmpty(confirmPassword.getText())) {
                            btnSignUp.setEnabled(true);
                            btnSignUp.setTextColor(Color.rgb(255, 255, 255));
                        } else {
                            btnSignUp.setEnabled(false);
                            btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
                        }
                    } else {
                        btnSignUp.setEnabled(false);
                        btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    btnSignUp.setEnabled(false);
                    btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                btnSignUp.setEnabled(false);
                btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            btnSignUp.setEnabled(false);
            btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    public void checkEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                showProgressDialog(getActivity());

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Map<String,Object> userData = new HashMap<>();
                                    userData.put("firstname", firstName.getText().toString());
                                    userData.put("lastname", lastName.getText().toString());

                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                        // MAPS
                                                        Map<String,Object> wishlistMap = new HashMap<>();
                                                        wishlistMap.put("list_size", (long) 0);

                                                        Map<String,Object> ratingsMap = new HashMap<>();
                                                        ratingsMap.put("list_size", (long) 0);

                                                        Map<String,Object> cartMap = new HashMap<>();
                                                        cartMap.put("list_size", (long) 0);

                                                        // MAPS

                                                        final List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");
                                                        documentNames.add("MY_CART");

                                                        List<Map<String,Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(wishlistMap);
                                                        documentFields.add(ratingsMap);
                                                        documentFields.add(cartMap);

                                                        for (int x = 0; x < documentNames.size(); x++) {

                                                            final int finalX = x;
                                                            userDataReference.document(documentNames.get(x))
                                                                    .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        if (finalX == documentNames.size() -1) {
                                                                            mainIntent();
                                            //                                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                            //                                startActivity(mainIntent);
                                            //                                getActivity().finish();
                                                                        }
                                                                    }else{
                                                                        hideProgressDialog();
                                                                        String Error = task.getException().getMessage();
                                                                        Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    } else {
                                                        String Error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    String Error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                confirmPassword.setError(getResources().getString(R.string.check_confirm_password));
            }
        } else {
            email.setError(getResources().getString(R.string.check_email));
        }
    }
    private void mainIntent(){
        if(disableClosebtn){
            disableClosebtn = false;
        }else {
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
        }
        getActivity().finish();
    }
}