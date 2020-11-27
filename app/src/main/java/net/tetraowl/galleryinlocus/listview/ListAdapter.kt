package net.tetraowl.galleryinlocus.listview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.tetraowl.galleryinlocus.LocusStuff
import net.tetraowl.galleryinlocus.R
import net.tetraowl.galleryinlocus.Storage

class ListAdapter(private var paths:MutableList<String>, val storgae:Storage) :RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var locus :LocusStuff? = null
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        locus = LocusStuff(parent.context)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textView = holder.view.findViewById<TextView>(R.id.text)
        textView.text = paths[position]
        val button = holder.view.findViewById<Button>(R.id.delete)
        button.setOnClickListener {
            paths.remove(textView.text)
            storgae.paths = paths
            locus?.imageRefresher()
            this.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
       return paths.size
    }

    public fun updateDataset(data:List<String>) {
        this.paths = data.toMutableList()
    }
}
