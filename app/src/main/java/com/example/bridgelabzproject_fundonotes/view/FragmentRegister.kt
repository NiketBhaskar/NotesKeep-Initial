package com.example.bridgelabzproject_fundonotes.view

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bridgelabzproject_fundonotes.R
import com.example.bridgelabzproject_fundonotes.databinding.FragmentRegisterBinding
import com.example.bridgelabzproject_fundonotes.model.User
import com.example.bridgelabzproject_fundonotes.model.UserAuthService
import com.example.bridgelabzproject_fundonotes.viewmodel.RegisterViewModel
import com.example.bridgelabzproject_fundonotes.viewmodel.RegisterViewModelFactory
import com.example.bridgelabzproject_fundonotes.viewmodel.SharedViewModel
import com.example.bridgelabzproject_fundonotes.viewmodel.SharedViewModelFactory

class FragmentRegister : Fragment(){
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(UserAuthService())).get(RegisterViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())).get(SharedViewModel::class.java)

        val registerBtn : Button = view.findViewById(R.id.btnRegister)

        registerBtn.setOnClickListener() {
            register()
        }
        return view
    }

    private fun register(){
        var name = ""
        var email = ""
        var password = ""
        var imageUrl = ""

        name = binding.etUserName.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        val cPassword = binding.etConfPassword.text.toString().trim()

        val user = User(name = name, email = email, password = password, image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/PICA.jpg/900px-PICA.jpg")
        if(name.isEmpty()){
            //empty name
            Toast.makeText(activity, "Enter Your Name", Toast.LENGTH_LONG).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid Email Pattern
            Toast.makeText(activity, "Invalid Email", Toast.LENGTH_LONG).show()
        }
        else if (password.isEmpty()){
            //empty password
            Toast.makeText(activity, "Enter Password", Toast.LENGTH_LONG).show()
        }
        else if (cPassword.isEmpty()){
            //empty password
            Toast.makeText(activity, "Enter Confirm Password", Toast.LENGTH_LONG).show()
        }
        else if (password != cPassword){
            //empty password
            Toast.makeText(activity, "Password mismatch", Toast.LENGTH_LONG).show()
        }
        else{
            registerViewModel.registerUser(user)
            registerViewModel.userRegisterStatus.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                    sharedViewModel.setGoToDashboardPage(true)
                }
                else{
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}