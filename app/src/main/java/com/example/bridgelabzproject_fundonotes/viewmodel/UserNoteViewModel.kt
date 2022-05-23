package com.example.bridgelabzproject_fundonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bridgelabzproject_fundonotes.model.AuthListener
import com.example.bridgelabzproject_fundonotes.model.UserAuthService
import com.example.bridgelabzproject_fundonotes.model.UserNote
import com.example.bridgelabzproject_fundonotes.model.UserNoteOperation

class UserNoteViewModel(private val userNoteOperation: UserNoteOperation):ViewModel() {
    private var _UserNoteStatus = MutableLiveData<AuthListener>()
    val userNoteStatus = _UserNoteStatus as LiveData<AuthListener>

    fun addNote(userNote : UserNote) {
        userNoteOperation.addNote(userNote) {
            if (it.status) {
                _UserNoteStatus.value = it
            }

        }
    }
/*    fun getUserNote(uid:String){
        userNoteOperation.getUserNote(uid){
            if (it.status) {
                _UserNoteStatus.value = it
            }

        }
    }*/
}