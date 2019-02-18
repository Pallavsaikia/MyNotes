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


    @Query("Update Notes set title=:title , description=:description, toBeUpdated=1  WHERE id=:id")
    fun updatenote(id:Int,title: String, description:String)


    @Query("Update notes set active= 0 where id=:id")
    fun preDelete(id:Int)


    @Query("Select * from notes where synced = 0")
    fun getSynchData():List<Notes>


    @Query("Update ImagesList set deleteStatus=1 where imageid=:id")
    fun preDeleteImage(id:Int)

    @Query("Delete from Notes where active=0 and synced=0")
    fun deleteDataNotSynced()

    @Query("Delete From ImagesList where deleteStatus=1 and noteId=:id")
    fun deletePicNotSynced(id:Int)

    @Query("Delete from Notes where id=:id")
    fun deleteCanvas(id:Int)

    @Query("update Notes set synced=1 , serverId=:serId where id=:noteID")
    fun synchNote(noteID: Int,serId:Int)



}
