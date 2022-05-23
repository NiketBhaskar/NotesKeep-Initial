package com.example.bridgelabzproject_fundonotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bridgelabzproject_fundonotes.model.UserNoteOperation

class UserNoteViewModelFactory(private val userNoteOperation : UserNoteOperation) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserNoteViewModel(userNoteOperation) as T
    }
}