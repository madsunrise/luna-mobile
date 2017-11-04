package com.utrobin.luna.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.utrobin.luna.R
import com.utrobin.luna.model.FeedItem
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
 * Created by ivan on 31.10.2017.
 */

class FeedAdapter(items: List<FeedItem>) : FooterLoaderAdapter(ArrayList(items)) {

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val context = (holder as ItemViewHolder).itemView.context
        holder.itemView.setOnClickListener { viewClickSubject.onNext(item) }

        holder.name.text = item.name
        holder.location.text = item.location

        Glide.with(context).load("http://hd.wallpaperswide.com/thumbs/megan_fox_new_look-t2.jpg").into(holder.avatar)
        Glide.with(context).load("http://www.thainarak.net/uploads/4/8/5/0/48500987/b8804bdd65917509ca47fd51999a3b24_orig.jpg").into(holder.image)

        holder.achievementsContainer.removeAllViews()
        for (achievement in item.achievements) {
            val drawable = ContextCompat.getDrawable(context, icons[achievement.ordinal])
            val image = CircleImageView(context)
            image.setImageDrawable(drawable)
            val achievementSize = context.resources.getDimension(R.dimen.achievement_size).toInt()
            val params = LinearLayout.LayoutParams(achievementSize, achievementSize)
            params.setMargins(0, 0, context.resources.getDimension(R.dimen.achievement_margin_right).toInt(), 0)
            image.layoutParams = params
            holder.achievementsContainer.addView(image)
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val location: TextView = view.findViewById(R.id.location)
        val avatar: CircleImageView = view.findViewById(R.id.avatar)
        val image: ImageView = view.findViewById(R.id.image)
        val achievementsContainer: LinearLayout = view.findViewById(R.id.achievements_container)
    }

    override fun getYourItemId(position: Int) = items[position].hashCode().toLong()

    private val icons = arrayOf(
            R.drawable.ic_notifications_black_24dp,
            R.drawable.ic_cloud_black_24dp,
            R.drawable.ic_chat_black_24dp,
            R.drawable.ic_favorite_black_24dp,
            R.drawable.ic_star_black_24dp,
            R.drawable.ic_audiotrack_black_24dp
    )
}