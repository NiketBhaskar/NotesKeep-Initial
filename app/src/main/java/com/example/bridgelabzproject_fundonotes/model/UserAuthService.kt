package com.example.bridgelabzproject_fundonotes.model

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UserAuthService {
    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var shared : SharedPreferences

    init {
        initService()
    }

    private fun initService() {
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun userRegister(user: User, listener: (AuthListener) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    listener(AuthListener(true, "User registration successful."))

                    storeUserDetails(user,listener)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure")
                    listener(AuthListener(false, "User registration failed."))
                }
            }
    }

    fun storeUserDetails(user: User, listener: (AuthListener) -> Unit) {
        val uid = firebaseAuth.uid
    //   val hashMap: HashMap<String, Any?> = HashMap()
      //  hashMap["uid"] = uid

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(user)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    listener(AuthListener(true, "User data saved"))
                } else {
                    listener(AuthListener(false, "User data save failed."))
                }
            }
    }
    fun userLogin(user: User, listener: (AuthListener) -> Unit){
        firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(){
                if(it.isSuccessful){
                    listener(AuthListener(true, "User login successful."))
                }
                else{
                    listener(AuthListener(false, "User login failed."))
                }
            }
    }
    fun forgotPassword(user: User, listener: (AuthListener) -> Unit){
        firebaseAuth.sendPasswordResetEmail(user.email)
            .addOnCompleteListener(){
                if(it.isSuccessful){
                    listener(AuthListener(true, "Password Reset mail sent."))
                }
                else{
                    listener(AuthListener(false, "User password reset failed."))
                }
            }
    }

}