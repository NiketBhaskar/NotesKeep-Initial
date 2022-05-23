package com.example.bridgelabzproject_fundonotes.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import com.example.bridgelabzproject_fundonotes.model.UserNote
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.example.bridgelabzproject_fundonotes.R
import com.example.bridgelabzproject_fundonotes.model.Communicater
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RecyclerViewAdaptor (private val mList: ArrayList<UserNote>) : RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder>() {
    lateinit var database: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth



    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        firebaseAuth = FirebaseAuth.getInstance()
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notes, parent, false)

        return ViewHolder(view)
    }
    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.title.text = currentItem.noteTitle
        holder.subTitle.text = currentItem.noteSubtitle
        holder.notes.text = currentItem.noteContent
        holder.date.text = currentItem.timeStamp
        holder.popupBtn.setOnClickListener(){

            val popupMenu: PopupMenu = PopupMenu(it.context,holder.popupBtn)
            popupMenu.menuInflater.inflate(R.menu.note_card_options,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.editNoteBtn -> {
                        //val userId = currentItem.userId.toString()
                        val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentItem.userId.toString(), currentItem.noteId.toString())
                        it.findNavController().navigate(action)
                    }
                    R.id.deleteNOtesBtn ->{
                        mList.removeAt(position)
                        deleteNote(currentItem.noteId)
                    }
                }
                true
            })
            popupMenu.show()
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){

        val title: TextView = itemView.findViewById(R.id.titleTV)
        val subTitle: TextView = itemView.findViewById(R.id.subTitleTV)
        val notes: TextView = itemView.findViewById(R.id.noteTV)
        val date: TextView = itemView.findViewById(R.id.date)
        val popupBtn: ImageView = itemView.findViewById(R.id.popupIV)
    }

    fun deleteNote(noteId: String){
        database = FirebaseDatabase.getInstance().getReference("Users")
        val uid = firebaseAuth.uid

        database.child(uid!!).child("notes").child(noteId).removeValue().addOnCompleteListener {
        }
    }
}