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
The login fields should be pre-populated, but the only login user/password at the moment is:
Email: test@test.com
Password: password

Ideally, this app will be run on a tablet.  The most important part of the app, the live
play-by-play recording, is too complicated to fit comfortably on a phone screen.  Because of
that, I have only tested/run my app on a tablet so far.  I recommend the Nexus 10.  If you
run it on a phone, it will look terrible (I will fix that later).  Also, I currently have a problem
when using the back button in the top left corner of the app, so it is usually best to use the
system back button for now.

One notable thing that is missing from my app (besides the obvious parts) is a home button
in the top right that will allow you to be taken back to the HomeActivity from any screen.
The app has a pretty linear flow to it, which is why I implemented it the way I did, but I
still want there to be an easy way to get to the home screen at all times.

Final note about the SDKs/APIs:

Originally I planned on using a firebase database along with the Google Maps and Weather APIs.
I thought it would be cool to use Google Maps to record exactly where your games/tournaments
were taking place and to use the Weather API to track wind speeds.  I decided that, if anything,
including these things would just make the app more of a hassle to use and they would detract
from what the app is supposed to be: a play-by-play and stat tracking app for teams and players.
I talked with a lot of teammates about my development of this app and they said that maps and weather
data is unneccessary.  They also suggested that you should be able to log into an account to get
into the app so that you can access your team stats from anywhere (we currently use a similar app
that stores everything locally, so you can only view your stats if you have the one specific iPad).
Because of all this feedback, I decided to add firebase authentication instead of Google Maps and
Weather.
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
