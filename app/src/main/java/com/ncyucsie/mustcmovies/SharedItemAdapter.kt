package com.ncyucsie.mustcmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ncyucsie.mustcmovies.MVVM.Movie

class SharedItemAdapter(private var data: MutableList<Movie>, private var mode: String) :
    RecyclerView.Adapter<SharedItemAdapter.ViewHolder>() {

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val textTitle: TextView = v.findViewById(R.id.textTitle)
        val textOrder: TextView = v.findViewById(R.id.textOrder)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int):
            ViewHolder {

        var itemLayoutType = when (mode) {
            "Lofi" ->  R.layout.lo_fi_item
            "Neon" -> R.layout.neon_item
            else -> R.layout.totoro_item
        }

        val v = LayoutInflater.from(viewGroup.context)
            .inflate(itemLayoutType, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textTitle.text = data[position].title
        holder.textOrder.text = (position + 1).toString()
    }
}