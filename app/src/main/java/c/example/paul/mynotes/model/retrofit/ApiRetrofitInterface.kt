package c.example.paul.mynotes.model.retrofit

import c.example.paul.mynotes.pojo.ImageResponse.ImageResponseSync
import c.example.paul.mynotes.pojo.NoteResponse.NoteSyncResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiRetrofitInterface{

    @POST("my-note/")
    @FormUrlEncoded
    fun sendNotes(@Header("Authorization") token: String,
                  @Field("title") title:String,
                  @Field("description") description:String,
                  @Field("saved_time") saved_time:Long,
                  @Field("isCanvas") isCanvas:Boolean):Call<NoteSyncResponse>


    @POST("my-note-image/")
    @Multipart
    fun sendNoteImage(@Header("Authorization") token: String,
                      @Part("my_note_id") title:Int,
                      @Part file: MultipartBody.Part):Call<ImageResponseSync>


    @PUT("my-note/")
    @FormUrlEncoded
    fun updateNote(@Header("Authorization") token: String,
                  @Field("title") title:String,
                  @Field("description") description:String,
                  @Field("saved_time") saved_time:Long,
                  @Field("isCanvas") isCanvas:Boolean,
                   @Field("my_note_id") noteId:Int):Call<NoteSyncResponse>


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "my-note/",hasBody = true)
    fun deleteNote(@Header("Authorization") token: String,
                   @Field("my_note_id") noteId:Int):Call<NoteSyncResponse>


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "my-note-image/",hasBody = true)
    fun deleteImage(@Header("Authorization") token: String,
                   @Field("my_note_image_id") noteId:Int):Call<NoteSyncResponse>





}