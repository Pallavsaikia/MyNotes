package c.example.paul.mynotes.pojo.ImageResponse

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("my_note_image_id")
    val myNoteImageId: String
)