package c.example.paul.mynotes.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import c.example.paul.mynotes.pojo.Notes

@Dao
interface NotesDao{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(notes:Notes):Long

    @Query("Select * from Notes where active =:activated order by timesaved desc")
    fun getNotes(activated: Int): LiveData<List<Notes>>

    @Query("Delete from Notes WHERE id=:id")
    fun deletenote(id:Int)

    @Query("Update Notes set active=:deactive  WHERE id=:id")
    fun preDelete(id:Int,deactive :Int)


    @Query("Update Notes set title=:title ,description=:description WHERE id=:id")
    fun updatenote(id:Int,title: String, description:String)


}
