package c.example.paul.mynotes.pojo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class Notes(  val title: String,
                   val description: String?,
                   val timesaved: Long,
                   val active: Boolean,
                   val synced: Boolean,
                   val isCanvas: Boolean,
                   val serverId:Int,
                   val toBeUpdated: Boolean


){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}