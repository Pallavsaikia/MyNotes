package c.example.paul.mynotes.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import c.example.paul.mynotes.model.room.NotesDatabase
import c.example.paul.mynotes.pojo.ImagesList
import c.example.paul.mynotes.pojo.Notes
import org.jetbrains.anko.doAsync

class NotesViewModel(application: Application) : AndroidViewModel(application){

    private val db=NotesDatabase(this.getApplication())

    fun insertNotes(notes: Notes,imageList : List<String>?){

        Log.d("BookmarkVsM", "Here")
        doAsync {
            val row=db.notesDao().insertNote(notes)
            for(imageLink in imageList!!){
                val imageList=ImagesList(imageLink,row.toInt(),null,false)
                val r=db.notesDao().inserImage(imageList)
            }



        }


    }
    fun getnotes():LiveData<List<Notes>>{
        return db.notesDao().getNotes(true)

    }

    fun getImage(noteId:Int):LiveData<List<ImagesList>>{

        return db.notesDao().getImage(false,noteId)
    }

//
//
    fun updateNotes(id: Int , title: String, description :String){
        doAsync {
            val row=db.notesDao().updatenote(id,title,description)
        }

    }

    fun preDelete(id:Int){
        doAsync {
            val row=db.notesDao().preDelete(id)
        }

    }

    fun preDeleteImage(id:Int){
        doAsync {
            val row=db.notesDao().preDeleteImage(id)
        }
    }


}