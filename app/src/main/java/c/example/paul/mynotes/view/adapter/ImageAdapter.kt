package c.example.paul.mynotes.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.RecyclerViewOnItemClickListener
import c.example.paul.mynotes.helper.inflate
import c.example.paul.mynotes.pojo.ImagesList
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_veiw_note_image.view.*

class ImageAdapter(val context: Context,var image:MutableList<ImagesList>?,val clickListener: RecyclerViewOnItemClickListener): RecyclerView.Adapter<ImageAdapter.ViewHolder>(){
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
        holder.itemView.deleteImage.tag=image


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        override fun onClick(v: View) {
            clickListener.recyclerViewOnItemClick(v,this.layoutPosition)
            image!!.removeAt(this.layoutPosition)
        }
        init{
            itemView.deleteImage.setOnClickListener(this)


        }
        fun setData(image:ImagesList?,pos : Int){


            Glide.with(context).load(image!!.imageName!!).into(itemView.noteimage!!)

        }

    }
}
