package com.example.dischargediary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dischargediary.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


private const val LOG_TAG = "LoginCheck"

class LoginFragment : Fragment() {
    /**
     * [Part1] Get ref to Firebase Authentication Object
     **/
    lateinit var auth: FirebaseAuth
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // [Part2] Get instance of auth
        auth = FirebaseAuth.getInstance()

        val binding: FragmentLoginBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_login, container, false)

        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        binding.buttonRegister.setOnClickListener {
            registerUser(binding)
            Log.i(LOG_TAG, "Register Clicked")
        }
        binding.buttonLogin.setOnClickListener {
            loginUser(binding)
            Log.i(LOG_TAG, "Login Clicked")
        }

        loginViewModel.navigateToDiary.observe(viewLifecycleOwner) {
            if(it) {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDischargeDiaryFragment())
                loginViewModel.doneNavigating()
            } else {

            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.i(LOG_TAG, "Login Started!")
//        auth.signOut()
        checkLoggedInStateStart()
    }

    private fun registerUser(binding: FragmentLoginBinding) {
        val email = binding.newUserEmailInput.text.toString()
        val password = binding.newPasswordInput.text.toString()
        Log.i(LOG_TAG, "$email $password")
        val confirmPassword = binding.confirmNewPasswordInput.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState(binding)
                    }
                } catch (e:Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        Log.i(LOG_TAG, "$e")
                    }
                }
            }
        } else {
            Toast.makeText(context, "Invalid Input, Check and Try Again!", Toast.LENGTH_LONG).show()
        }
    }

    private fun loginUser(binding: FragmentLoginBinding) {
        val email = binding.usernameInput.text.toString()
        val password = binding.passwordInput.text.toString()
        Log.i(LOG_TAG, "$email $password")
        if(email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState(binding)
                    }
                } catch (e:Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        Log.i(LOG_TAG, "$e")
                    }
                }
            }
        } else {
            Toast.makeText(context, "Invalid Input, Check and Try Again!", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkLoggedInState(binding: FragmentLoginBinding) {
        if(auth.currentUser == null) {
            binding.loggedInTv.text = "You are NOT logged in"
        } else {
            binding.loggedInTv.text = "You are logged in"
            loginViewModel.loginValid()
        }
    }
    private fun checkLoggedInStateStart() {
        if(auth.currentUser != null) {
            loginViewModel.loginValid()
            Log.i(LOG_TAG, "Already Logged In!")
        }
    }
}