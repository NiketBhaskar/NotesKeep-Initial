package com.example.bridgelabzproject_fundonotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bridgelabzproject_fundonotes.model.UserAuthService

class LoginViewModelFactory(private val userAuthService: UserAuthService) : ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userAuthService) as T
    }
}