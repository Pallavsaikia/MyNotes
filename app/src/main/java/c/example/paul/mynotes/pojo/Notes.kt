package c.example.paul.mynotes.pojo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [
    Index(value = arrayOf("id"), unique = true)
])
data class Notes(  val title: String,
                   val description: String?,
                   val timesaved: Long,
                   val active: Boolean,
                   val synced: Boolean,
                   val isCanvas: Boolean,
                   val serverId:String?,
                   val toBeUpdated: Boolean


){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}