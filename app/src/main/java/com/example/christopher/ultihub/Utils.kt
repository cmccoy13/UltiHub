package com.example.christopher.ultihub

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Utils {
    private var mDatabase: FirebaseDatabase? = null
    private var uid: String? = null

    val database: FirebaseDatabase
        get() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance()
                mDatabase!!.setPersistenceEnabled(true)
            }
            return mDatabase!!
        }

    val userID: String
        get() {
            return FirebaseAuth.getInstance().currentUser?.uid!!
        }
}