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

         return Result.success()
    }

}