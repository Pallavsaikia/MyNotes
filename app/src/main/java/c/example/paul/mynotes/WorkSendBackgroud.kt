package c.example.paul.mynotes

import android.content.Context
import android.util.Log

import androidx.work.Worker
import androidx.work.WorkerParameters
import c.example.paul.mynotes.helper.Constants
import c.example.paul.mynotes.model.retrofit.ApiRetrofitBase
import c.example.paul.mynotes.model.retrofit.ApiRetrofitInterface
import c.example.paul.mynotes.model.room.NotesDatabase


class WorkSendBackgroud(context : Context, params : WorkerParameters)
    : Worker(context, params) {
    private val db= NotesDatabase(context)

    private  val apiService=ApiRetrofitBase().getReply(context)!!.create(ApiRetrofitInterface::class.java)
    override fun doWork(): Result {

        db.notesDao().deleteDataNotSynced()
        val unpdatedList=db.notesDao().getSynchData()
        for(i in unpdatedList){
            db.notesDao().deletePicNotSynced(i!!.id)
        }
        val syncList=db.notesDao().getSynchData()
        Log.d("sync list size",syncList.size.toString())


        syncList?.let {
            for (notes in syncList){
                val response=apiService.sendNotes(Constants.token,
                    notes!!.title,
                    notes!!.description!!,
                    notes!!.timesaved,
                    notes!!.isCanvas)
                    .execute()

                if(response.code()==200){
                    val responseGot=response.body()
                    if(responseGot!!.status=="success") {
                        val serverID=responseGot.response.data.myNoteId!!
                        serverID?.let {
                            Log.d("response", response.toString())
                            db.notesDao().synchNote(notes!!.id,serverID!!)


                        }
                    }


                }else{

                }

            }
        }
        return Result.success()
    }

}