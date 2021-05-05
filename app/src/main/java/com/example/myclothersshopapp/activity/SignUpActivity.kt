package com.example.myclothersshopapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.myclothersshopapp.R
import com.example.myclothersshopapp.firestore.FireStoreClass
import com.example.myclothersshopapp.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        btnSignIninSignUp.setOnClickListener{
            //Launch the register screen when the user clicks on the button.
            val intent : Intent = Intent(this, SignInActivity::class.java)
            startActivity(intent);
        }

        btnSignUpinSignUp.setOnClickListener{
            registerUser()
        }
    }

    private fun validateSignUpDetails() : Boolean {
        return when {
            TextUtils.isEmpty(edtFirstName.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(edtLastName.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(edtEmailSignUp.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(edtPasswordSignUp.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(edtConfirmPassword.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }
            else -> {
                showErrorSnackBar(resources.getString(R.string.check_information_sign_up), false)
                true
            }
        }
    }

    private fun registerUser() {
        //Check with vadidate funtion if the entries are valid or not.
        if (validateSignUpDetails())  {

            showProgressDialog(resources.getString(R.string.lbl_loading))

            val email: String = edtEmailSignUp.text.toString().trim() {it <= ' '}
            val password: String = edtPasswordSignUp.text.toString().trim() {it <= ' '}

            //Create an instance and create a register a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                OnCompleteListener<AuthResult> {task ->

                    //if the registration is successfully done
                    if (task.isSuccessful) {
                        //Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                                firebaseUser.uid,
                                edtFirstName.text.toString().trim {it <= ' '},
                                edtLastName.text.toString().trim {it <= ' '},
                                edtEmailSignUp.text.toString().trim {it <= ' '},
                        )

                        FireStoreClass().registerUser(this@SignUpActivity, user)
                        //FirebaseAuth.getInstance().signOut()
                        //finish()

                    } else {
                        hideProgressDialog()
                        //if the registering is not successful then show error message
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            )
        }
    }

    fun userRegistrationSuccess() {

        hideProgressDialog()

        Toast.makeText(
                this@SignUpActivity,
                resources.getString(R.string.register_success),
                Toast.LENGTH_SHORT
        ).show()
    }
}