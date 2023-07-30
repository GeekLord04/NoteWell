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

        binding.signupBtn.setOnClickListener{

            authViewModel.signupUser(UserRequest("test@gmail.com","125456","test123"))
            //findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

        }

        binding.txtLogin.setOnClickListener {
            authViewModel.loginUser(UserRequest("test123@gmail.com","125456","test123"))
            //findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    //token
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this.context, ""+it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Log.d(TAG,"Loading")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}