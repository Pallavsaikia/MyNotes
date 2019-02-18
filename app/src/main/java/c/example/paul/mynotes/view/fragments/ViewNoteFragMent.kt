package c.example.paul.mynotes.view.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.DeleteImage
import c.example.paul.mynotes.helper.FragmentTools
import c.example.paul.mynotes.helper.PassValue
import c.example.paul.mynotes.helper.RecyclerViewOnItemClickListener
import c.example.paul.mynotes.pojo.ImagesList
import c.example.paul.mynotes.pojo.Notes
import c.example.paul.mynotes.view.adapter.ImageAdapter
import c.example.paul.mynotes.viewmodel.NotesViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_display_note.*
import kotlinx.android.synthetic.main.fragment_view_note_frag_ment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import androidx.recyclerview.widget.DefaultItemAnimator





class ViewNoteFragMent : Fragment(),AnkoLogger, RecyclerViewOnItemClickListener {
    override fun recyclerViewOnItemClick(view: View?, position: Int?) {

        val item=view!!.tag as ImagesList
        val id= item!!.imageid

        DeleteImage.deleteimage(item!!.imageName)


        if(item!!.isCanvas){
            notesViewModel!!.deleteCanvas(note!!.id)
            FragmentTools.replaceFragment(DisplayNoteFragment.instance(), activity!!.supportFragmentManager, R.id.notesContainer)

        }else {
            notesViewModel.preDeleteImage(id!!)
            context!!.toast("deleted")
        }


        adapter!!.notifyDataSetChanged()
//        imageRecycleView.removeViewAt(position!!)
//        adapter!!.notifyItemRemoved(position!!)

    }

    companion object {
        fun instance()=ViewNoteFragMent()
    }
    private var adapter:ImageAdapter?=null

    private val notesViewModel: NotesViewModel by viewModel()
    private var note:Notes?=null
    private var listSize:Int=0

    private var image: List<ImagesList>?=null


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
        val layoutManager = LinearLayoutManager(activity!!,LinearLayout.HORIZONTAL,false)

        imageRecycleView.layoutManager = layoutManager

        imageRecycleView.setHasFixedSize(true)
        imageRecycleView.itemAnimator=null


        populate()

    }


    fun populate(){
        notesViewModel.getImage(note!!.id).observe(this, Observer {

            if(it!!.isNotEmpty()) {
                image=it!!

                var imagelist:MutableList<ImagesList> = it!!.toMutableList()
                adapter = ImageAdapter(activity!!, imagelist!!,this)
                adapter!!.setHasStableIds(true)
                listSize=it.size!!
                imageRecycleView.adapter = adapter
            }

        })
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
                note=PassValue!!.notes
                image?.let {
                    for (images in image!!) {
                        DeleteImage.deleteimage(images!!.imageName)
                    }
                }
                notesViewModel.preDelete(note!!.id)
                FragmentTools.replaceFragment(DisplayNoteFragment.instance(), activity!!.supportFragmentManager, R.id.notesContainer)

                return true
            }
            else -> {
            }
        }

        return false
    }


}

