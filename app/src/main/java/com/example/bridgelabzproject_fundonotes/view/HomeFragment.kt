package com.example.bridgelabzproject_fundonotes.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bridgelabzproject_fundonotes.R
import com.example.bridgelabzproject_fundonotes.database.SQLiteDatabaseHandler
import com.example.bridgelabzproject_fundonotes.databinding.FragmentHomeBinding
import com.example.bridgelabzproject_fundonotes.model.Communicater
import com.example.bridgelabzproject_fundonotes.model.UserNote
import com.example.bridgelabzproject_fundonotes.model.Utility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var userRecyclerView : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userNoteArrayList: ArrayList<UserNote>
    private lateinit var userNoteArrayListOffline: ArrayList<UserNote>
    var arrayNotes = ArrayList<UserNote>()

    var linearlayout: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        val view = binding.root

        setHasOptionsMenu(true)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        val uid:String = firebaseAuth.currentUser?.uid.toString()
        // getting the recyclerview by its id
        userRecyclerView = binding.rcvAllNotes
        // this creates a layout Manager
        userRecyclerView.layoutManager = LinearLayoutManager(activity)
        linearlayout = true
        userRecyclerView.setHasFixedSize(true)
        userNoteArrayList = arrayListOf<UserNote>()
        val db = SQLiteDatabaseHandler(this.requireContext())
        userNoteArrayListOffline = db.readData()
        getUserNote(uid)

        binding.addNoteBtn.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_fragmentAddNote)
        }

        var highPriorityArray = ArrayList<UserNote>()
        var mediumPriorityArray = ArrayList<UserNote>()
        var lowPriorityArray = ArrayList<UserNote>()

        binding.labelRed.setOnClickListener(){
            highPriorityArray.clear()
            for(item in arrayNotes){
                if(item.noteLabel!!.toLowerCase(Locale.getDefault()).contains("high")){
                    highPriorityArray.add(item)
                }
            }
            userRecyclerView.adapter = RecyclerViewAdaptor(highPriorityArray)
        }
        binding.labelGreen.setOnClickListener(){
            mediumPriorityArray.clear()
            for(item in arrayNotes){
                if(item.noteLabel!!.toLowerCase(Locale.getDefault()).contains("medium")){
                    mediumPriorityArray.add(item)
                }
            }
            userRecyclerView.adapter = RecyclerViewAdaptor(mediumPriorityArray)
        }
        binding.labelYellow.setOnClickListener(){
            lowPriorityArray.clear()
            for(item in arrayNotes){
                if(item.noteLabel!!.toLowerCase(Locale.getDefault()).contains("low")){
                    lowPriorityArray.add(item)
                }
            }
            userRecyclerView.adapter = RecyclerViewAdaptor(lowPriorityArray)
        }

        binding.labelAll.setOnClickListener() {
            userRecyclerView.adapter = RecyclerViewAdaptor(userNoteArrayList)
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater){
        super.onCreateOptionsMenu(menu, inflater)
        val search = menu!!.findItem(R.id.dashboardSearchBtn)
        val adapter = userRecyclerView.adapter
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        val context = this.requireContext()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "You clicked on search button.", Toast.LENGTH_SHORT).show()
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                Toast.makeText(context, "You clicked on search button text change.", Toast.LENGTH_SHORT).show()
                var tempArr = ArrayList<UserNote>()

                for(arr in arrayNotes){
                    if(arr.noteTitle!!.toLowerCase(Locale.getDefault()).contains(query.toString()) || arr.noteSubtitle!!.toLowerCase(Locale.getDefault()).contains(query.toString()) || arr.noteContent!!.toLowerCase(Locale.getDefault()).contains(query.toString()) ){
                        tempArr.add(arr)
                    }
                }
                userRecyclerView.adapter = RecyclerViewAdaptor(tempArr)
                return true
            }
        })
        return
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var menuItem = item.itemId
        when(menuItem){
            R.id.dashboardGridLinearToggleBtn -> toggleLayoutManager()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getUserNote(uid:String) {
        var utility = Utility()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if(utility.isOnline(this.requireContext())) {
            databaseReference.child(uid).child("notes")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        userNoteArrayList.clear()
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                val userNote = userSnapshot.getValue(UserNote::class.java)
                                userNoteArrayList.add(userNote!!)
                                Log.d(ContentValues.TAG, "usernote display:success")
                                arrayNotes.add(userNote)
                            }
                            userRecyclerView.adapter = RecyclerViewAdaptor(userNoteArrayList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                        Log.d(ContentValues.TAG, "usernote display:failure")
                    }
                })
        }
        else{
        }
    }
    private fun toggleLayoutManager(){
        if(linearlayout){
            userRecyclerView.layoutManager = GridLayoutManager(activity,2)
            linearlayout = false
        }
        else{
            userRecyclerView.layoutManager = LinearLayoutManager(activity)
            linearlayout = true
        }
    }
    /*fun highPriorityFilter(){
        var highPriorityArray = ArrayList<UserNote>()

        for(item in highPriorityArray){
            if(item.noteLabel!!.toLowerCase(Locale.getDefault()).contains("high")){
                highPriorityArray.add(item)
            }
        }
        userRecyclerView.adapter = RecyclerViewAdaptor(highPriorityArray)
    }*/
}