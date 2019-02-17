package c.example.paul.mynotes.view.fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider

import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.FragmentTools

import c.example.paul.mynotes.pojo.Notes

import c.example.paul.mynotes.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.fragment_add_note.*
import org.jetbrains.anko.AnkoLogger

import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddNoteFragment : Fragment() ,AnkoLogger{

    companion object {


       fun instance()=AddNoteFragment()
        val TAKE_PICTURE=1
        var currentPath: String? = null
        var selectedPath:String?=null
        var bitmap: Bitmap? = null
        var f:File?=null
    }

    private val notesViewModel:NotesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity!!.toast("here")

        return inflater.inflate(R.layout.fragment_add_note, container, false)



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == requestCode && resultCode == RESULT_OK){
            if(selectedPath==null) {
                selectedPath = currentPath
            }
            else{
                selectedPath= selectedPath+","+ currentPath
            }
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        addimagebtn.setOnClickListener {
            dispatchCameraIntent()
        }


        addnotebtn.setOnClickListener {

            val title = title.text.toString()
            val description=description.text.toString()
            activity!!.toast("clicked $selectedPath")
            val time =System.currentTimeMillis()
            val notes=Notes(title,description,time, active = true, synced = false, isCanvas = false, serverId = null)
            if(selectedPath!=null) {
                val imageList = selectedPath!!.split(",")
                notesViewModel.insertNotes(notes, imageList)
            }
            else{
                notesViewModel.insertNotes(notes, null)
            }
                activity!!.toast(selectedPath.toString())
                selectedPath = null
                FragmentTools.replaceFragment(
                    DisplayNoteFragment.instance(),
                    activity!!.supportFragmentManager,
                    R.id.notesContainer
                )

        }




    }

    fun dispatchCameraIntent(){
        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(activity!!.packageManager)!= null){
            var photoFile: File?=null
            try{
                photoFile=createImage()

            }
            catch(e: IOException){
                e.printStackTrace()
            }
            if(photoFile!= null){
                var photoUri= FileProvider.getUriForFile(activity!!,"c.example.paul.mynotes.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TAKE_PICTURE)


            }
        }
    }

    private fun createImage(): File? {
        val timeStamp= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageName="JPEG_"+timeStamp+"_"
        var storeDir=activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image=File.createTempFile(imageName,".jpg",storeDir)
        currentPath = image.absolutePath
        return image

    }


}
