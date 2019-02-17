package c.example.paul.mynotes.helper

import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.io.File

object DeleteImage{
        fun deleteimage(image: String?) {

            if (image != null) {
                Log.d("deleted", "here")
                var imgarray = image!!.split(",")
                for (i in imgarray) {
                    val target = File(i)
                    if (target.exists() && target.isFile && target.canWrite()) {
                        target.delete()
                        Log.d("d_file", "" + target.name)
                    }
                }
            }

    }
}

//private fun deleteimage(image: String?) {
//    if(image!=null) {
//        var imgarray = image!!.split(",")
//        for(i in imgarray){
//            val target= File(i)
//            if (target.exists() && target.isFile && target.canWrite()) {
//                target.delete()
//                Log.d("d_file", "" + target.getName())
//            }
//        }
//    }
