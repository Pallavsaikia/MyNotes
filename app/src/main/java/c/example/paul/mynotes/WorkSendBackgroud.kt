package c.example.paul.mynotes

import android.content.Context
import android.net.Uri
import android.util.Log

import androidx.work.Worker
import androidx.work.WorkerParameters
import c.example.paul.mynotes.helper.Constants
import c.example.paul.mynotes.helper.GetImageUri
import c.example.paul.mynotes.model.retrofit.ApiRetrofitBase
import c.example.paul.mynotes.model.retrofit.ApiRetrofitInterface
import c.example.paul.mynotes.model.room.NotesDatabase
import c.example.paul.mynotes.pojo.ImagesList
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception


class WorkSendBackgroud(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val db = NotesDatabase(context)

    private val apiService = ApiRetrofitBase().getReply(context)!!.create(ApiRetrofitInterface::class.java)
    override fun doWork(): Result {

        //deleting data not synched and deleted
        db.notesDao().deleteDataNotSynced()
        val unpdatedList = db.notesDao().getSynchData()
        for (i in unpdatedList) {
            db.notesDao().deletePicNotSynced(i!!.id)
        }

        //----------------------


        //delete individual image

        val individualImageNotDeleted=db.notesDao().getIndividualDeleteImage()
        Log.d("imagesize ",individualImageNotDeleted.size.toString())
        for ( images in individualImageNotDeleted){
            try {
                val response = apiService.deleteImage(Constants.token, images.serverId!!).execute()
                if (response.code() == 200) {

                    val res=response.body()
                    if (res!!.status == "success") {
//                        db.notesDao().deleteSyncNotes(notes!!.id)
                        Log.d("deleted","this is running")

                        db.notesDao().deleteImage(images.imageid)

                    } else {

                    }
                } else {

                }
            }catch (e: Exception) {

            }
        }

        //---------------------

        //delete synch


        val notesToDelete = db.notesDao().getToBeDeleted()

        for (notes in notesToDelete) {
            val serverId = notes.serverId
            try {
                val response = apiService.deleteNote(Constants.token, serverId!!).execute()
                if (response.code() == 200) {
                    Log.d("updated ", response.body().toString())
                    if (response.body()!!.status == "success") {
                        db.notesDao().deleteSyncNotes(notes!!.id)

                    } else {

                    }
                } else {

                }
            } catch (e: Exception) {

            }

        }


        //------------------------------------

        //update

        val noteList = db.notesDao().updateNoteListSync()

        for (notes in noteList) {
            try {
                val response = apiService.updateNote(
                    Constants.token,
                    notes.title,
                    notes.description!!,
                    notes.timesaved,
                    notes.isCanvas,
                    notes.serverId!!
                ).execute()
                if (response.code() == 200) {
                    Log.d("updated ", response.body().toString())
                    if (response.body()!!.status == "success") {

                        db.notesDao().updateNoteSync(notes.id)
                    } else {

                    }
                } else {

                }
            } catch (e: Exception) {

            }
        }


        //-------------------------------


        //------------------------------Insert note
        val syncList = db.notesDao().getSynchData()


        Log.d("sync list size", syncList.size.toString())

        //insert synch
        syncList?.let {
            for (notes in syncList) {
                try {
                    val response = apiService.sendNotes(
                        Constants.token,
                        notes!!.title,
                        notes!!.description!!,
                        notes!!.timesaved,
                        notes!!.isCanvas
                    )
                        .execute()

                    if (response.code() == 200) {
                        val responseGot = response.body()
                        if (responseGot!!.status == "success") {
                            val serverID = responseGot.response.data.myNoteId!!
                            serverID?.let {
                                Log.d("response", response.toString())
                                db.notesDao().synchNote(notes!!.id, serverID!!)
//                                sendImage(notes!!.id, serverID)
                            }
                        }


                    } else {

                    }
                } catch (e: Exception) {

                }

            }
        }

        //-------------------------------


        //retry image not sent
        val notsyncImg=db.notesDao().getAll()
//        Log.d("trying",notsyncImg[0].noteServ.toString())

        for(images in notsyncImg){

            sendImage(images.noteID,images.noteServ)
        }


        //---------------------------------






        return Result.success()
    }

    private fun sendImage(id: Int, serverId: String) {
        val imagesList = db.notesDao().getImageSync(false, id, false)
//        Log.d("Image", imagesList[0].serverId.toString())

        for (images in imagesList) {
            try {
                sendMultiPart(images!!, serverId)
            } catch (e: Exception) {

            }
        }

    }

    fun sendMultiPart(images: ImagesList, serverId: String) {
        var body: MultipartBody.Part? = null
        var photoPath: String?


        if (images.isCanvas) {
            photoPath = GetImageUri.getCanvasUri(images.imageName)
        } else {
            photoPath = GetImageUri.getImageUri(images.imageName)
        }



        if (photoPath != null) {
            val file = File(photoPath)
            val contentUri = Uri.fromFile(file)
            Log.d("here", "imageNote: contentUri=$contentUri")
            // create RequestBody instance from file
            val requestFile = RequestBody.create(
                MediaType.parse("image/jpg"),
                file
            )

            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            Log.d("here body", serverId)
            val servId= RequestBody.create(
            okhttp3.MultipartBody.FORM, serverId)
            val response = apiService.sendNoteImage(
                Constants.token,
                servId, body
            )
                .execute()
            if (response.code() == 200) {
                val responseGot = response.body()
                if (responseGot!!.status == "success") {
                    val serverID = responseGot.response.data.myNoteImageId
                    Log.d("serverid",serverID)
                    serverID?.let {

                        db.notesDao().setNoteImageSynced(serverID, images!!.imageid)

                    }
                }
            } else {
            }
        }
    }


}

