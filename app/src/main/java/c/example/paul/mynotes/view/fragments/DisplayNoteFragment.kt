package c.example.paul.mynotes.view.fragments



import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.FragmentTools
import c.example.paul.mynotes.helper.PassValue
import c.example.paul.mynotes.helper.RecyclerViewOnItemClickListener
import c.example.paul.mynotes.pojo.Notes
import c.example.paul.mynotes.view.adapter.MyNotesAdapter
import c.example.paul.mynotes.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.fragment_display_note.*
import org.jetbrains.anko.AnkoLogger

import org.koin.android.viewmodel.ext.android.viewModel


class DisplayNoteFragment : Fragment(),RecyclerViewOnItemClickListener,AnkoLogger {
    override fun recyclerViewOnItemClick(view: View?, position: Int?) {
        val note= view!!.tag as Notes
        PassValue.notes=note
        FragmentTools.replaceFragment(ViewNoteFragMent.instance(), activity!!.supportFragmentManager, R.id.notesContainer)

    }

    companion object {
        fun instance()=DisplayNoteFragment()
    }

    private val notesViewModel: NotesViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_display_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity!!)
        notesRecyclerView.layoutManager=layoutManager
        notesRecyclerView.setHasFixedSize(true)
        notesViewModel.getnotes().observe(this, Observer {

            val adapter=MyNotesAdapter(activity!!,it!!,this)
            notesRecyclerView.adapter=adapter

        })
    }


}
