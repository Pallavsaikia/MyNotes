package c.example.paul.mynotes.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.RecyclerViewOnItemClickListener
import c.example.paul.mynotes.helper.inflate
import c.example.paul.mynotes.pojo.Notes
import kotlinx.android.synthetic.main.notes_adapter_item.view.*

class MyNotesAdapter(val context: Context,val notes:List<Notes>,val clickListener: RecyclerViewOnItemClickListener):RecyclerView.Adapter<MyNotesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = viewGroup.inflate(R.layout.notes_adapter_item)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notes=notes[position]
        holder.setData(notes,position)
        holder.itemView.tag=notes

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        override fun onClick(v: View) {
            clickListener.recyclerViewOnItemClick(v,this.layoutPosition)
        }
        init{
            itemView.setOnClickListener(this)
        }
        fun setData(plan:Notes?,pos : Int){
            itemView.titleView.text=plan!!.title



        }

    }
}