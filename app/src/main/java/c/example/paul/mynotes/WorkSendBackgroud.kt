package c.example.paul.mynotes

import android.content.Context
import android.util.Log

import androidx.work.Worker
import androidx.work.WorkerParameters
import c.example.paul.mynotes.model.room.NotesDatabase


class WorkSendBackgroud(context : Context, params : WorkerParameters)
    : Worker(context, params) {
    private val db= NotesDatabase(context)
    override fun doWork(): Result {

        db.notesDao().deleteDataNotSynced()
        val unpdatedList=db.notesDao().getSynchData()
        for(i in unpdatedList){
            db.notesDao().deletePicNotSynced(i!!.id)
        }
        val syncList=db.notesDao().getSynchData()
        Log.d("sync list size",syncList.size.toString())
        return Result.success()
    }

}