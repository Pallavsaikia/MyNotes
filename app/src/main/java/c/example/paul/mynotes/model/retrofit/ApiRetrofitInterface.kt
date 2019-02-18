package c.example.paul.mynotes.model.retrofit

import c.example.paul.mynotes.pojo.NoteResponse.NoteSyncResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiRetrofitInterface{

    @POST("my-note/")
    @FormUrlEncoded
    fun sendNotes(@Header("Authorization") token: String,
                  @Field("title") title:String,
                  @Field("description") description:String,
                  @Field("saved_time") saved_time:Long,
                  @Field("isCanvas") isCanvas:Boolean):Call<NoteSyncResponse>

}