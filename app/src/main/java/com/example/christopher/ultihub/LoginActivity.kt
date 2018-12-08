package com.example.christopher.ultihub

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.R
import kotlinx.android.synthetic.main.login.*


/*
The login fields should be pre-populated to allow you to login with a test user with some data.
The login credentials for that account are:
Email: test@test.com
Password: password

This app has open signups though, so feel free to create a new account and test it on that.

I would recommend running this on the Nexus 10 tablet emulator.
 */

class LoginActivity : AppCompatActivity() {
    var fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.christopher.ultihub.R.layout.login)

        loginButton.setOnClickListener {view ->
            signIn(view,emailText.text.toString(), passwordText.text.toString())
        }

        newUserButton.setOnClickListener {view ->
            createNewUser(view,emailText.text.toString(), passwordText.text.toString())
        }
    }

    fun createNewUser(view: View, email: String, password: String){
        showMessage(view, "Creating new account...")
        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                var intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        }
    }

    fun signIn(view: View, email: String, password: String){
        showMessage(view,"Authenticating...")
        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                var intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        }
    }
    fun showMessage(view:View, message: String){

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
