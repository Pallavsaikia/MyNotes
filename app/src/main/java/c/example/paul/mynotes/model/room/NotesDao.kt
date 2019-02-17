package c.example.paul.mynotes.model.room

import androidx.lifecycle.LiveData
import androidx.room.*
import c.example.paul.mynotes.pojo.ImagesList
import c.example.paul.mynotes.pojo.Notes

@Dao
interface NotesDao{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(notes:Notes):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserImage(imagesList: ImagesList):Long

    @Query("Select * from Notes  where active =:activated  order by timesaved desc")
    fun getNotes(activated: Boolean): LiveData<List<Notes>>

    @Query("Select * from ImagesList  where deleteStatus =:deleted and noteId=:noteID")
    fun getImage(deleted: Boolean,noteID : Int): LiveData<List<ImagesList>>


    @Query("Update Notes set title=:title ,description=:description WHERE id=:id")
    fun updatenote(id:Int,title: String, description:String)


    @Query("Update notes set active= 0 where id=:id")
    fun preDelete(id:Int)


    @Query("Update ImagesList set deleteStatus=1 where imageid=:id")
    fun preDeleteImage(id:Int)













}
