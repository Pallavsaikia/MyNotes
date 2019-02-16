package c.example.paul.mynotes.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(  val title: String,
                   val description: String?,
                   val image: String?,
                   val timesaved: Long,
                   val active: Boolean,
                   val synched: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}