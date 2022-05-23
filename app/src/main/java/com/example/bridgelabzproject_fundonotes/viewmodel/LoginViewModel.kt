package com.example.bridgelabzproject_fundonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bridgelabzproject_fundonotes.model.AuthListener
import com.example.bridgelabzproject_fundonotes.model.User
import com.example.bridgelabzproject_fundonotes.model.UserAuthService

class LoginViewModel(private val userAuthService: UserAuthService) : ViewModel() {
    private var _UserLoginStatus = MutableLiveData<AuthListener>()
    val userLoginStatus = _UserLoginStatus as LiveData<AuthListener>
    private var _ForgotPasswordStatus = MutableLiveData<AuthListener>()
    val ForgotPasswordStatus = _ForgotPasswordStatus as LiveData<AuthListener>

    fun loginUser(user: User){
        userAuthService.userLogin(user){
            if(it.status){
                _UserLoginStatus.value = it
            }
        }
    }

    fun forgotPassword(user: User){
        userAuthService.forgotPassword(user){
            if(it.status){
                _ForgotPasswordStatus.value = it
            }
        }
    }
}