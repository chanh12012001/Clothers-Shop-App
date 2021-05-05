package com.example.myclothersshopapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.myclothersshopapp.R
import com.example.myclothersshopapp.firestore.FireStoreClass
import com.example.myclothersshopapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseFragment(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        btnSignIninSignIn.setOnClickListener(this)
        btnSignUpinSignIn.setOnClickListener(this)
        tvForgot.setOnClickListener(this)
    }

    /**
     * A function to notify user that logged in success and get the user details from the FireStore database after authentication.
     */
    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        // Print the user details in the log as of now.
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tvForgot -> {
                    val intent : Intent = Intent(this@SignInActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent);
                }

                R.id.btnSignIninSignIn -> {
                    loginUser()
                }

                R.id.btnSignUpinSignIn -> {
                    //Launch the register screen when the user clicks on the button.
                    val intent : Intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent);

                }
            }
        }
    }

    private fun validateSignInDetails() : Boolean {
        return when {
            TextUtils.isEmpty(edtEmailSignIn.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(edtPasswordSignIn.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loginUser() {
        //Check with vadidate funtion if the entries are valid or not.
        if (validateSignInDetails())  {

            showProgressDialog(resources.getString(R.string.lbl_loading))

            val email: String = edtEmailSignIn.text.toString().trim() {it <= ' '}
            val password: String = edtPasswordSignIn.text.toString().trim() {it <= ' '}

            //Create an instance and create a register a user with email and password
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->

                        //if the registration is successfully done
                        if (task.isSuccessful) {

                            FireStoreClass().getUserDetails(this@SignInActivity)

                        } else {
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
        }
    }
}