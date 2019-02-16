package c.example.paul.mynotes.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import c.example.paul.mynotes.pojo.Notes

@Database(
    entities =[Notes::class],
    version = 1
)

abstract class NotesDatabase : RoomDatabase(){

        abstract fun notesDao():NotesDao

        companion object {

            @Volatile
            private var instance:NotesDatabase?=null
            private var LOCK= Any()
            operator fun invoke(context: Context)=instance?: synchronized(LOCK){
                instance?:buildDatabase(context).also{ instance=it}
            }

            private fun buildDatabase(context: Context)=
                    Room.databaseBuilder( context.applicationContext,
                        NotesDatabase::class.java, "mynotes.db"
                    ).build()
        }
}