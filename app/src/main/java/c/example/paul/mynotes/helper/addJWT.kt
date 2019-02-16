package c.example.paul.mynotes.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.json.JSONException
import org.json.JSONObject

fun String.addJWT():String {
    return "JWT ".plus(this)
}

fun String.removeJWT():String{
    return this.removePrefix("JWT ")
}

fun String.addComma():String {
    return ",".plus(this)
}

fun String.addQuestionNumber(q_no : Int):String {
    return (q_no + 1).toString().plus(") ").plus(this)
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(this.context).inflate(layoutRes, this, false)
}

fun makeJSONObj(key: String, value: Any): JSONObject {

    val jsonObject = JSONObject()
    try {
        jsonObject.put(key, value)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    return jsonObject
}

fun isDeleted(deleted: Boolean): String{
    return if(deleted){
        "Deleted"
    }else{
        "Not Deleted"
    }
}