package c.example.paul.mynotes.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_veiw_note_image.view.*

class ImageAdapter(val context: Context,val image:List<String>?): RecyclerView.Adapter<ImageAdapter.ViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = viewGroup.inflate(R.layout.card_veiw_note_image)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return image!!.size

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image=image!![position]
        holder.setData(image,position)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(image:String?,pos : Int){


            Glide.with(context).load(image!!).into(itemView.noteimage!!)

        }

    }
}
