package c.example.paul.mynotes.pojo.NoteResponse

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("my_note_id")
    val myNoteId: String
)