package com.utrobin.luna.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.utrobin.luna.R
import io.reactivex.subjects.PublishSubject


/**
 * Created by ivan on 29.10.2017.
 */


abstract class FooterLoaderAdapter<T, V>(internal val items: ArrayList<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val viewClickSubject: PublishSubject<Pair<T, V>> = PublishSubject.create<Pair<T, V>>()
    var loading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADER -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.loader_item, parent, false)

                // Your LoaderViewHolder class
                return LoaderViewHolder(view)
            }
            VIEW_TYPE_ITEM ->
                getYourItemViewHolder(parent);

            else ->
                throw IllegalArgumentException("Invalid ViewType: " + viewType);
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoaderViewHolder) {
            holder.progressBar.visibility =
                    if (loading)
                        View.VISIBLE
                    else
                        View.GONE
            return
        }

        bindYourViewHolder(holder, position);
    }

    override fun getItemCount() = if (items.isEmpty()) 0 else items.size + 1

    override fun getItemId(position: Int): Long {
        return if (position != 0 && position == itemCount - 1) {
            -1
        } else {
            getYourItemId(position)
        }
    }

    fun addItems(data: List<T>) {
        val firstInsertedItemPosition = items.size
        this.items.addAll(data)
        notifyItemRangeInserted(firstInsertedItemPosition, data.size)
    }

    fun setItems(data: List<T>) {
        this.items.clear()
        this.items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position != 0 && position == itemCount - 1) {
            return VIEW_TYPE_LOADER;
        }
        return VIEW_TYPE_ITEM;
    }

    class LoaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
    }

    abstract fun getYourItemId(position: Int): Long
    abstract fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    companion object {
        private val VIEW_TYPE_LOADER = 0;
        private val VIEW_TYPE_ITEM = 1;
    }
}