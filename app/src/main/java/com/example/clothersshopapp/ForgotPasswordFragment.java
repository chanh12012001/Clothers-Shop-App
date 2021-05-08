package com.example.clothersshopapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends BaseFragment {

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    private EditText edtRegisterEmail;
    private Button btnResetPassword;
    private TextView tvGoBack;
    private FrameLayout parentFrameLayout;

    private  ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView tvEmailIcon;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        edtRegisterEmail = view.findViewById(R.id.edtEmailFogotPassword);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);
        tvGoBack = view.findViewById(R.id.tvGoback);

        emailIconContainer = view.findViewById(R.id.emailContaiter);
        emailIcon = view.findViewById(R.id.ivEmail);
        tvEmailIcon = view.findViewById(R.id.tv_sent_mail);
        progressBar = view.findViewById(R.id.tv_progress_sent_mail);

        parentFrameLayout = getActivity().findViewById(R.id.fragmentContainer);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            edtRegisterEmail.addTextChangedListener(new TextWatcher() {
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

            btnResetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    hideKeyboard(getActivity());

                    TransitionManager.beginDelayedTransition(emailIconContainer);

                    if (edtRegisterEmail.getText().length() > 0) {

                        emailIcon.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        firebaseAuth.sendPasswordResetEmail(edtRegisterEmail.getText().toString())

                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            tvEmailIcon.setText(getResources().getText(R.string.sent_email_successful));
                                            tvEmailIcon.setTextColor(getResources().getColor(R.color.text_send_email_successfully));
                                            TransitionManager.beginDelayedTransition(emailIconContainer);
                                            tvEmailIcon.setVisibility(View.VISIBLE);
                                        } else {
                                            String error = task.getException().getMessage();
                                            progressBar.setVisibility(View.GONE);
                                            tvEmailIcon.setText(error);
                                            tvEmailIcon.setTextColor(getResources().getColor(R.color.text_send_email_error));
                                            TransitionManager.beginDelayedTransition(emailIconContainer);
                                            tvEmailIcon.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                    } else {
                        showErrorSnackBar(getResources().getString(R.string.enter_email), false);
                    }

                }
            });

            tvGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFragment(new SignInFragment());
                }
            });
    }

    private void checkInput() {
        if (TextUtils.isEmpty(edtRegisterEmail.getText())) {
            btnResetPassword.setEnabled(false);
            Toast.makeText(getActivity(), getResources().getText(R.string.enter_email), Toast.LENGTH_SHORT).show();
        } else {
            btnResetPassword.setEnabled(true);
        }
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}