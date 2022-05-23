package com.example.bridgelabzproject_fundonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bridgelabzproject_fundonotes.model.AuthListener
import com.example.bridgelabzproject_fundonotes.model.User
import com.example.bridgelabzproject_fundonotes.model.UserAuthService

class RegisterViewModel(private val userAuthService:UserAuthService) : ViewModel() {
        private var _UserRegisterStatus = MutableLiveData<AuthListener>()
        val userRegisterStatus = _UserRegisterStatus as LiveData<AuthListener>

        fun registerUser(user:User){
                userAuthService.userRegister(user){
                        if(it.status){
                                _UserRegisterStatus.value = it
                        }
                }
        }
}