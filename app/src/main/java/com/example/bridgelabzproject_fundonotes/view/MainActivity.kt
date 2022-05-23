package com.example.bridgelabzproject_fundonotes.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bridgelabzproject_fundonotes.R
import com.example.bridgelabzproject_fundonotes.model.UserAuthService
import com.example.bridgelabzproject_fundonotes.viewmodel.SharedViewModel
import com.example.bridgelabzproject_fundonotes.viewmodel.SharedViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    //this maybe need to change to false
    var isFragmentOneLoaded = false
    var isFragmentTwoLoaded = false
    val manager = supportFragmentManager
    private val RC_SIGN_IN = 89
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedViewModel: SharedViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory(UserAuthService())).get(SharedViewModel::class.java)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("429069074656-36vnn74c9kp31cjgb1t3tmk2ue728lee.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val loginBtn : Button = findViewById(R.id.btnLogin)
        val registerBtn : Button = findViewById(R.id.btnRegister)

        ShowFragmentLogo()

        loginBtn.setOnClickListener(){
            ShowFragmentLogin()
            hideShowLoginBtn(loginBtn)
            hideShowRegBtn(registerBtn)
            registerBtn.visibility = View.VISIBLE
        }

        registerBtn.setOnClickListener(){
            ShowFragmentRegister()
            hideShowRegBtn(registerBtn)
            hideShowLoginBtn(loginBtn)
            loginBtn.visibility = View.VISIBLE
        }

       observeAppNav()
    }

    private fun observeAppNav(){
        sharedViewModel.goToDashboardPageStatus.observe(this, Observer {
            if(it == true){
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
        })
    }

    fun signIn(view: android.view.View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("Fire89", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Fire89", "signInWithCredential:success")
                    val user = auth.currentUser
                    startActivity(Intent(this, DashboardActivity::class.java))
                    Log.d("Fire89", "firebaseAuthWithGoogle: ${user?.displayName}")
                } else {
                    Log.w("Fire89", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun hideShowLoginBtn(loginBtn : Button){
        if (loginBtn.visibility == View.VISIBLE){
            loginBtn.visibility = View.GONE
        }else if (loginBtn.visibility == View.GONE){
            loginBtn.visibility = View.VISIBLE
        }
    }

    fun hideShowRegBtn(registerBtn : Button){
        if (registerBtn.visibility == View.VISIBLE){
            registerBtn.visibility = View.GONE
        }else if (registerBtn.visibility == View.GONE){
            registerBtn.visibility = View.VISIBLE
        }
    }

    fun ShowFragmentLogo() {
        val transaction = manager.beginTransaction()
        val fragment = FragmentLogo()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun ShowFragmentLogin() {
        val transaction = manager.beginTransaction()
        val fragment = FragmentLogin()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentOneLoaded = true
    }

    fun ShowFragmentRegister() {
        val transaction = manager.beginTransaction()
        val fragment = FragmentRegister()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentTwoLoaded = true
    }
}