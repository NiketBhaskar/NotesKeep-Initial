package com.example.bridgelabzproject_fundonotes.model

import android.content.Context
import android.content.SharedPreferences
import com.example.bridgelabzproject_fundonotes.database.SQLiteDatabaseHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap

class UserNoteOperation {
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var shared : SharedPreferences
    lateinit var databaseReference: DatabaseReference
    lateinit var userNoteArrayList: ArrayList<UserNote>

    init {
        initService()

    }

    private fun initService() {
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
    }
    fun addNote(userNote: UserNote, listener: (AuthListener) -> Unit) {
        val uid = firebaseAuth.uid
        val notes = "notes"
        val noteId = "${uid}${UUID.randomUUID()}"
        /*    val hashMap: HashMap<String, Any?> = HashMap()
          hashMap["userId"] = uid
          hashMap["noteId"] = noteId
          hashMap["noteTitle"] = userNote.noteTitle
          hashMap["noteSubTitle"] = userNote.noteSubtitle
          hashMap["noteContent"] = userNote.noteContent
          hashMap["noteLabel"] = userNote.noteLabel
          hashMap["timeStamp"] = userNote.timeStamp*/

        val ref = FirebaseDatabase.getInstance().getReference("Users")


        ref.child(uid!!).child(notes).child(noteId).setValue(userNote)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    it.addOnSuccessListener {
                        val hashMap: HashMap<String, Any?> = HashMap()
                        hashMap["userId"] = uid
                        hashMap["noteId"] = noteId
                        ref.child(uid).child(notes).child(noteId).updateChildren(hashMap).addOnSuccessListener {
                            listener(AuthListener(true, "User note saved"))
                        }
                    }
                } else {
                    listener(AuthListener(false, "User note save failed."))
                }
            }



    }
    /*fun readOfflineNote(){
        val data = db.readData()
        //tvResult.text = ""
        for (i in 0 until data.size) {
            tvResult.append(
                data[i].id.toString() + " " + data[i].name + " " + data[i].age + "\n"
            )
        }
    }*/
}

