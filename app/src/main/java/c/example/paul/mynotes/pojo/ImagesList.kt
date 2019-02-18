package c.example.paul.mynotes.pojo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Notes::class,
        parentColumns = ["id"],
        childColumns = ["noteId"],
        onDelete = ForeignKey.CASCADE
    )]

)
data class ImagesList(val imageName:String,
                      val noteId:Int,
                      val serverId:String?,
                      val deleteStatus: Boolean,
                      val isCanvas:Boolean
                      ){
    @PrimaryKey(autoGenerate = true)

    var imageid: Int = 0
}