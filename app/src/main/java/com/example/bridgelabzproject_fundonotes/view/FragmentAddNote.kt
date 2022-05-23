package com.example.bridgelabzproject_fundonotes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.bridgelabzproject_fundonotes.R
import com.example.bridgelabzproject_fundonotes.database.SQLiteDatabaseHandler
import com.example.bridgelabzproject_fundonotes.databinding.FragmentAddNoteBinding
import com.example.bridgelabzproject_fundonotes.model.UserNote
import com.example.bridgelabzproject_fundonotes.model.UserNoteOperation
import com.example.bridgelabzproject_fundonotes.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddNote : Fragment() {
    lateinit var _binding: FragmentAddNoteBinding
    private val binding get() = _binding!!
    private lateinit var userNoteViewModel: UserNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        userNoteViewModel = ViewModelProvider(this, UserNoteViewModelFactory(UserNoteOperation())).get(UserNoteViewModel::class.java)

        binding.btnSaveDiscard.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragmentAddNote_to_homeFragment)
        }

        binding.btnSaveNotes.setOnClickListener(){
            addNote()
            Navigation.findNavController(it).navigate(R.id.action_fragmentAddNote_to_homeFragment)
        }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
    }
    private fun addNote(){
        var title = ""
        var subTitle = ""
        var note = ""
        var label = ""
        var timeStamp = ""
        val db = SQLiteDatabaseHandler(this.requireContext())

        title = binding.editTitle.text.toString().trim()
        subTitle = binding.editSubTitle.text.toString().trim()
        note = binding.editNotes.text.toString().trim()
        var calendar = Calendar.getInstance()
        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        timeStamp = simpleDateFormat.format(calendar.time).toString()

        val userNote = UserNote(noteTitle = title, noteSubtitle = subTitle, noteContent = note, timeStamp = timeStamp)
        db.insertData(userNote)
        userNoteViewModel.addNote(userNote)
        userNoteViewModel.userNoteStatus.observe(viewLifecycleOwner, Observer {
            if(it.status){
                Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                binding.editTitle.text.clear()
                binding.editSubTitle.text.clear()
                binding.editNotes.text.clear()
            }
            else{
                Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
            }
        })
    }
}