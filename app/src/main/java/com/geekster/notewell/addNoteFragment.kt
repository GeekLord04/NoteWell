package com.geekster.notewell

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.geekster.notewell.databinding.FragmentAddNoteBinding
import com.geekster.notewell.models.NoteRequest
import com.geekster.notewell.models.NoteResponse
import com.geekster.notewell.utlis.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class addNoteFragment : Fragment() {

    private var _binding : FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private var note: NoteResponse? =null

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            _binding = FragmentAddNoteBinding.inflate(inflater,container,false)
            return binding.root

        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitalData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                    
                }
            }
        })
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener{
            note?.let { noteViewModel.deleteNote(it!!._id) }
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val desc = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(desc,title);
            if (note == null) {
                noteViewModel.createNote(noteRequest)
            }
            else{
                noteViewModel.updateNote(note!!._id,noteRequest)
            }
        }
    }

    private fun setInitalData() {   //update Note
        val jsonNote = arguments?.getString("note")
        if(jsonNote != null){
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title);
                binding.txtDescription.setText(it.description)
            }
        }
        else{
            binding.addEditText.text = "Add Note"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }
}