package c.example.paul.mynotes.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import c.example.paul.mynotes.model.room.NotesDatabase
import c.example.paul.mynotes.pojo.Notes
import org.jetbrains.anko.doAsync

class NotesViewModel(application: Application) : AndroidViewModel(application){

    private val db=NotesDatabase(this.getApplication())

    fun insertNotes(notes: Notes){
        Log.d("BookmarkVsM", "Here")
        doAsync {
            val row=db.notesDao().insertNote(notes)
            Log.d("BookmarkVM", "Inserted at row = $row ${notes.image}")
        }


    }
    fun getnotes():LiveData<List<Notes>>{
        return db.notesDao().getNotes(1)

    }

    fun deleteNotes(id : Int){
        doAsync {
            val row=db.notesDao().deletenote(id)
        }
    }

    fun preDelete(id: Int){
        doAsync {
            val row=db.notesDao().preDelete(id,0)
        }

    }

    fun updateNotes(id: Int , title: String, description :String){
        doAsync {
            val row=db.notesDao().updatenote(id,title,description)
        }

    }

}