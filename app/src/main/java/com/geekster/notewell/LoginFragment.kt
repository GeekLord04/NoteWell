package com.geekster.notewell

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.geekster.notewell.databinding.FragmentLoginBinding
import com.geekster.notewell.models.UserRequest
import com.geekster.notewell.utlis.Constants
import com.geekster.notewell.utlis.NetworkResult
import com.geekster.notewell.utlis.inputValidationHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater,container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtRegister.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.loginBtn.setOnClickListener{
            val validationResult = userValidation()
            if (validationResult.first){
                authViewModel.loginUser(getUserRequest())
            }
            else{
                Toast.makeText(this.context,validationResult.second,Toast.LENGTH_SHORT).show()
            }
        }
        bindObserver()
    }

    private fun getUserRequest() : UserRequest {                //get the user request from the UI
        val emailAdd = binding.loginEmail.text.toString()
        val pass = binding.loginPass.text.toString()
        return UserRequest(emailAdd,pass,"")
    }

    private fun userValidation(): Pair<Boolean, String> {       //validates the user request
        val userRequest = getUserRequest()
        return inputValidationHelper().userInputValidation(userRequest.username,userRequest.email,userRequest.password, true)
    }

    private fun bindObserver() {                                //Logs the user in if the credentials are correct
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    //token
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(this.context, "" + it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    Log.d(Constants.TAG, "Loading")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}