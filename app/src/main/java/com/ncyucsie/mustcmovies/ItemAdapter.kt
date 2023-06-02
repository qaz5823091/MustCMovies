package com.ncyucsie.mustcmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ncyucsie.mustcmovies.MVVM.Movie
import java.util.*

class ItemAdapter(private var data: MutableList<Movie>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var onItemClick: ((Movie) -> Unit)? = null

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val textTitle: TextView = v.findViewById(R.id.textTitle)
        val textOrder: TextView = v.findViewById(R.id.textOrder)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int):
            ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_view, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textTitle.text = data[position].title
        holder.textOrder.text = (position + 1).toString()

        // marquee effect
        holder.textTitle.postDelayed( {
            holder.textTitle.isSelected = true
        }, 1500)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(data[position])
        }

    }

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(data, fromPosition, toPosition)

        var temp = data[fromPosition].order
        data[fromPosition].order = data[toPosition].order
        data[toPosition].order = temp

        //Log.d("movieeeeeeeeeeeeee", data.toString())

        notifyItemMoved(fromPosition, toPosition)
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)

        return true
    }
}