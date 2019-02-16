package c.example.paul.mynotes.view.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.FragmentTools
import c.example.paul.mynotes.helper.PassValue
import c.example.paul.mynotes.pojo.Notes
import c.example.paul.mynotes.view.adapter.ImageAdapter
import c.example.paul.mynotes.viewmodel.NotesViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_display_note.*
import kotlinx.android.synthetic.main.fragment_view_note_frag_ment.*
import org.jetbrains.anko.AnkoLogger
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class ViewNoteFragMent : Fragment(),AnkoLogger {

    companion object {
        fun instance()=ViewNoteFragMent()
    }

    private val notesViewModel: NotesViewModel by viewModel()
    private var note:Notes?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_view_note_frag_ment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        note=PassValue!!.notes
        noteTitle.text=note!!.title
        noteDescription.text=note!!.description!!

        val image=note!!.image
        if(image!=null) {
            var imagearray = image!!.split(",")
            val layoutManager = LinearLayoutManager(activity!!,LinearLayout.HORIZONTAL,false)

            imageRecycleView.layoutManager = layoutManager
            imageRecycleView.setHasFixedSize(true)

            val adapter = ImageAdapter(activity!!, imagearray)

            imageRecycleView.adapter = adapter
        }
//        Glide.with(this).load(imagearray[0]).into(noteimage)


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notemenu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.Edit -> {
                FragmentTools.replaceFragment(EditNoteFragment.instance(), activity!!.supportFragmentManager, R.id.notesContainer)
                return true
            }

            R.id.Delete -> {
                notesViewModel.preDelete(note!!.id)
                deleteimage(note!!.image)
                FragmentTools.replaceFragment(DisplayNoteFragment.instance(), activity!!.supportFragmentManager, R.id.notesContainer)

                return true
            }
            else -> {
            }
        }

        return false
    }

    private fun deleteimage(image: String?) {
        if(image!=null) {
            var imgarray = image!!.split(",")
            for(i in imgarray){
                val target= File(i)
                if (target.exists() && target.isFile && target.canWrite()) {
                    target.delete()
                    Log.d("d_file", "" + target.getName())
                }
            }
        }

    }
}
