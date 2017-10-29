package com.utrobin.luna.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.utrobin.luna.R
import com.utrobin.luna.model.FeedItem
import io.reactivex.subjects.PublishSubject


/**
 * Created by ivan on 29.10.2017.
 */


class FeedAdapter(val items: List<FeedItem>) : RecyclerView.Adapter<FeedAdapter.MyViewHolder>() {

    val viewClickSubject: PublishSubject<FeedItem> = PublishSubject.create<FeedItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.location.text = item.location
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.item_name)
        var location = view.findViewById<TextView>(R.id.item_location)

        init {
            itemView.setOnClickListener { viewClickSubject.onNext(items[layoutPosition]) }
        }
    }
}