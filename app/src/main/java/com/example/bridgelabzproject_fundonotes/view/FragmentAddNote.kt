package com.example.bridgelabzproject_fundonotes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.bridgelabzproject_fundonotes.R
import com.example.bridgelabzproject_fundonotes.database.SQLiteDatabaseHandler
import com.example.bridgelabzproject_fundonotes.databinding.FragmentAddNoteBinding
import com.example.bridgelabzproject_fundonotes.model.UserNote
import com.example.bridgelabzproject_fundonotes.model.UserNoteOperation
import com.example.bridgelabzproject_fundonotes.viewmodel.*
import kotlinx.android.synthetic.main.fragment_add_note.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddNote : Fragment() {
    lateinit var _binding: FragmentAddNoteBinding
    private val binding get() = _binding!!
    private lateinit var userNoteViewModel: UserNoteViewModel
    var labelSelection = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        userNoteViewModel = ViewModelProvider(this, UserNoteViewModelFactory(UserNoteOperation())).get(UserNoteViewModel::class.java)

        binding.btnSaveDiscard.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragmentAddNote_to_homeFragment)
        }

        binding.btnSaveNotes.setOnClickListener(){
            addNote()
            Navigation.findNavController(it).navigate(R.id.action_fragmentAddNote_to_homeFragment)
        }
        var selectedRedFlag: Boolean = false
        var selectedGreenFlag: Boolean = false
        var selectedYellowFlag: Boolean = false

        binding.labelRed.setOnClickListener(){
            if(selectedRedFlag == false) {
                labelSelection = "High"
                labelRed.setImageResource(R.drawable.ic_done)
                labelGreen.setImageResource(R.drawable.ic_baseline_check_box_)
                labelYellow.setImageResource(R.drawable.ic_baseline_check_box_)
                selectedRedFlag = true
                selectedGreenFlag = false
                selectedYellowFlag = false
            }
            else if(selectedRedFlag == true) {
                labelSelection = ""
                labelRed.setImageResource(R.drawable.ic_baseline_check_box_)
                labelGreen.setImageResource(R.drawable.ic_baseline_check_box_)
                labelYellow.setImageResource(R.drawable.ic_baseline_check_box_)
                selectedRedFlag = false
                selectedGreenFlag = false
                selectedYellowFlag = false
            }
        }

        binding.labelGreen.setOnClickListener(){
            if(selectedGreenFlag == false) {
                labelSelection = "Medium"
                labelGreen.setImageResource(R.drawable.ic_done)
                labelRed.setImageResource(R.drawable.ic_baseline_check_box_)
                labelYellow.setImageResource(R.drawable.ic_baseline_check_box_)
                selectedGreenFlag = true
                selectedRedFlag = false
                selectedYellowFlag = false
            }
            else if(selectedGreenFlag == true) {
                labelSelection = ""
                labelRed.setImageResource(R.drawable.ic_baseline_check_box_)
                labelGreen.setImageResource(R.drawable.ic_baseline_check_box_)
                labelYellow.setImageResource(R.drawable.ic_baseline_check_box_)
                selectedGreenFlag = false
                selectedRedFlag = false
                selectedYellowFlag = false
            }
        }

        binding.labelYellow.setOnClickListener(){
            if(selectedYellowFlag == false) {
                labelSelection = "Low"
                labelGreen.setImageResource(R.drawable.ic_done)
                labelRed.setImageResource(R.drawable.ic_baseline_check_box_)
                labelYellow.setImageResource(R.drawable.ic_baseline_check_box_)
                selectedYellowFlag = true
                selectedRedFlag = false
                selectedGreenFlag = false
            }
            else if(selectedYellowFlag == true) {
                labelSelection = ""
                labelRed.setImageResource(R.drawable.ic_baseline_check_box_)
                labelGreen.setImageResource(R.drawable.ic_baseline_check_box_)
                labelYellow.setImageResource(R.drawable.ic_baseline_check_box_)
                selectedYellowFlag = false
                selectedRedFlag = false
                selectedGreenFlag = false
            }
        }

        // Inflate the layout for this fragment
        return view
    }

    companion object {
    }
    private fun addNote(){
        var title = ""
        var subTitle = ""
        var note = ""
        var label = ""
        var timeStamp = ""
        val db = SQLiteDatabaseHandler(this.requireContext())
        title = binding.editTitle.text.toString().trim()
        subTitle = binding.editSubTitle.text.toString().trim()
        note = binding.editNotes.text.toString().trim()
        label = labelSelection
        var calendar = Calendar.getInstance()
        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        timeStamp = simpleDateFormat.format(calendar.time).toString()

        val userNote = UserNote(noteTitle = title, noteSubtitle = subTitle, noteContent = note, noteLabel= label, timeStamp = timeStamp)
        db.insertData(userNote)
        userNoteViewModel.addNote(userNote)
        userNoteViewModel.userNoteStatus.observe(viewLifecycleOwner, Observer {
            if(it.status){
                Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                binding.editTitle.text.clear()
                binding.editSubTitle.text.clear()
                binding.editNotes.text.clear()
            }
            else{
                Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
            }
        })
    }
}