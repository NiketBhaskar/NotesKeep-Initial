package com.example.bridgelabzproject_fundonotes.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.bridgelabzproject_fundonotes.R
import com.example.bridgelabzproject_fundonotes.model.UserNote
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList
import android.content.Context as Context1
import com.example.bridgelabzproject_fundonotes.view.HomeFragment as HomeFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var shared : SharedPreferences
    lateinit var mDrawerLayout : DrawerLayout
    lateinit var mNavigationView :NavigationView
    lateinit var userRecyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //init fire base auth
        firebaseAuth = FirebaseAuth.getInstance()
        shared = getSharedPreferences("Test" , Context1.MODE_PRIVATE)
        setSupportActionBar(findViewById(R.id.appBar))
        mDrawerLayout = findViewById(R.id.mainDrawer)
        mNavigationView = findViewById<View>(R.id.nav_view) as NavigationView

        setupDrawerLayout()
     }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.rightcorner_menu, menu)
        return true
    }
    fun setupDrawerLayout() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this, findViewById(R.id.mainDrawer),
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        mNavigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navLogout -> {
                    firebaseAuth.signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            return@OnNavigationItemSelectedListener true
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var menuItem = item.itemId

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        when(menuItem){
//            R.id.dashboardSearchBtn -> Toast.makeText(this, "You clicked on search button.", Toast.LENGTH_SHORT).show()
            R.id.dashboardProfileBtn -> FragmentProfileDialog().show(supportFragmentManager,
                FragmentProfileDialog.TAG
            )
        }
        return super.onOptionsItemSelected(item)
    }

}

