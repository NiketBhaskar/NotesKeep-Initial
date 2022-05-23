package com.example.bridgelabzproject_fundonotes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bridgelabzproject_fundonotes.R
import com.example.bridgelabzproject_fundonotes.databinding.FragmentAddNoteBinding
import com.example.bridgelabzproject_fundonotes.databinding.FragmentEditNoteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EditNoteFragment : Fragment() {
    lateinit var _binding: FragmentEditNoteBinding
    private val binding get() = _binding!!
    private lateinit var rootdatabase: DatabaseReference

    var inputPosition: Int? = null
    var inputUserId: String = ""
    var inputNoteId: String = ""
    val args: EditNoteFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        // Inflate the layout for this fragment

        val userId = args.userId
        val noteId = args.noteId
        readNote(userId, noteId)
        binding.btnEditDiscard.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_editNoteFragment_to_homeFragment)
        }
        binding.btnSaveEditNotes.setOnClickListener(){
            updateNote(userId, noteId)
            Navigation.findNavController(it).navigate(R.id.action_editNoteFragment_to_homeFragment)
        }

        return view
    }

    fun readNote(userId : String, noteId : String){
        rootdatabase = FirebaseDatabase.getInstance().getReference("Users")
        rootdatabase.child(userId).child("notes").child(noteId).get().addOnSuccessListener{
            if(it.exists()){
                val noteTitle = it.child("noteTitle").value
                val noteSubtitle = it.child("noteSubtitle").value
                var noteContent= it.child("noteContent").value
                val titleTv : TextView = binding.editEdtTitle
                val subTitleTv : TextView = binding.editEdtSubTitle
                val noteContentTv: TextView = binding.editEdtNotes

                titleTv.text = noteTitle.toString()
                subTitleTv.text = noteSubtitle.toString()
                noteContentTv.text = noteContent.toString()
            }
            else{
                Toast.makeText(context, "user does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun updateNote(userId : String, noteId : String){
        val titleTv : TextView = binding.editEdtTitle
        val subTitleTv : TextView = binding.editEdtSubTitle
        val noteContentTv: TextView = binding.editEdtNotes

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["noteTitle"] = titleTv.text.toString()
        hashMap["noteSubtitle"] = subTitleTv.text.toString()
        hashMap["noteContent"] = noteContentTv.text.toString()
        var calendar = Calendar.getInstance()
        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        hashMap["timeStamp"] = simpleDateFormat.format(calendar.time).toString()
        rootdatabase.child(userId).child("notes").child(noteId).updateChildren(hashMap).addOnSuccessListener {
            Toast.makeText(activity, "Database updated",Toast.LENGTH_LONG).show()
        }.addOnFailureListener(){
            Toast.makeText(activity, "Database updat failed",Toast.LENGTH_LONG).show()
        }


    }


}