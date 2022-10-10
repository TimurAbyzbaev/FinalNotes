package ru.abyzbaev.finalnotes

//import ru.abyzbaev.finalnotes.NodeListSource.nodeList
//import ru.abyzbaev.finalnotes.Node.title
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null

    private var dataSource: NodeListSource? = null
    fun setItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    fun setDataSource(dataSource: NodeListSource?) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nodeList = dataSource?.getNodeList()
        holder.title.text = nodeList!![position]!!.title.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSource!!.size()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView

        init {
            //title = (TextView) view.findViewById(R.id.note_title);
            title = view.findViewById(R.id.note_title)
            title.setOnClickListener { v ->
                val position = adapterPosition
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(v, position)
                }
            }
            title.setOnLongClickListener { v ->
                val position = adapterPosition
                if (itemClickListener != null) {
                    itemClickListener!!.onItemLongClick(v, position)
                }
                true
            }
        }
    }
}