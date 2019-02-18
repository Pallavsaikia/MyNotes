package c.example.paul.mynotes.view.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.FragmentTools
import c.example.paul.mynotes.helper.PassValue
import c.example.paul.mynotes.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.fragment_edit_note.*
import org.jetbrains.anko.AnkoLogger
import org.koin.android.viewmodel.ext.android.viewModel


class EditNoteFragment : Fragment(),AnkoLogger {

   companion object {
       fun instance()=EditNoteFragment()
   }

    private val notesViewModel: NotesViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_edit_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.savenote -> {
                notesViewModel.updateNotes(PassValue.notes!!.id,titleEdit.text.toString(),descriptionEdit.text.toString())
                FragmentTools.replaceFragment(DisplayNoteFragment.instance(), activity!!.supportFragmentManager, R.id.notesContainer)
                return true
            }
            else -> {
            }
        }

        return false
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        titleEdit.setText(PassValue.notes!!.title)
        descriptionEdit.setText(PassValue.notes!!.description)
    }



}
