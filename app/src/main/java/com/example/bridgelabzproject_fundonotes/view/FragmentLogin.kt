package com.example.bridgelabzproject_fundonotes.view
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.bridgelabzproject_fundonotes.databinding.FragmentLoginBinding
import com.example.bridgelabzproject_fundonotes.model.User
import com.example.bridgelabzproject_fundonotes.model.UserAuthService
import com.example.bridgelabzproject_fundonotes.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

class FragmentLogin : Fragment(){
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService())).get(LoginViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())).get(SharedViewModel::class.java)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click, begin login
        binding.btnLogin.setOnClickListener{
            loginUser()
        }

        //handle click, forget password
        binding.tvForgotPass.setOnClickListener{
            sendForgotPassEmail()
        }
        return view
    }
    private fun loginUser() {
        var email = ""
        var password = ""
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        val user = User(email = email, password = password)

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid Email Pattern
            Toast.makeText(activity, "Invalid Email", Toast.LENGTH_LONG).show()
        }
        else if (password.isEmpty()){
            //empty password
            Toast.makeText(activity, "Enter Password", Toast.LENGTH_LONG).show()
        }
        else{
            loginViewModel.loginUser(user)
            loginViewModel.userLoginStatus.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                    sharedViewModel.setGoToDashboardPage(true)
                }
                else{
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                    sharedViewModel.setGoToDashboardPage(false)
                    sharedViewModel.setGoToLoginPage(true)
                }
            })
        }

    }

    private fun sendForgotPassEmail() {
        var email = ""
        var password = ""
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        val user = User(email = email, password = password)

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid Email Pattern
            Toast.makeText(activity, "Invalid Email", Toast.LENGTH_LONG).show()
        }
        else if (email.isEmpty()){
            //empty password
            Toast.makeText(activity, "Enter Email", Toast.LENGTH_LONG).show()
        }
        else{
            loginViewModel.forgotPassword(user)
            loginViewModel.ForgotPasswordStatus.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}