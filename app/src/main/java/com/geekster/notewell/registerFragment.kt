package com.geekster.notewell

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.geekster.notewell.databinding.FragmentRegisterBinding
import com.geekster.notewell.models.UserRequest
import com.geekster.notewell.utlis.Constants.TAG
import com.geekster.notewell.utlis.NetworkResult
import com.geekster.notewell.utlis.inputValidationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class registerFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupBtn.setOnClickListener{
            val validationResult = userValidation()
            if (validationResult.first){
                val userReq = getUserRequest()
                authViewModel.registerUser(userReq)
            }
            else{
                Toast.makeText(this.context, validationResult.second, Toast.LENGTH_SHORT).show()
            }
            //authViewModel.registerUser(UserRequest("chiragpc6@gmail.com","123456","chirag"))  -> Testing purpose

        }

        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        bindObserver()
    }

    private fun getUserRequest() : UserRequest{
        val username = binding.username.text.toString()
        val emailAdd = binding.email.text.toString()
        val pass = binding.pass.text.toString()
        return UserRequest(emailAdd,pass,username)
    }

    private fun userValidation(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return inputValidationHelper().userInputValidation(userRequest.username,userRequest.email,userRequest.password, false)
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    //token
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(this.context, "" + it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    Log.d(TAG, "Loading")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}