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
import com.utrobin.luna.model.Achievement
import com.utrobin.luna.model.FeedItem
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

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
        holder.name.text = item.name
        holder.location.text = item.location

        Glide.with(context).load("http://hd.wallpaperswide.com/thumbs/megan_fox_new_look-t2.jpg").into(holder.avatar)
        Glide.with(context).load(getRandomImageUrl()).into(holder.image)

        holder.achievementsContainer.removeAllViews()
        for (achievement in item.achievements) {
            val drawable = when (achievement) {
                Achievement.CAREFUL -> ContextCompat.getDrawable(context, R.drawable.ic_notifications_black_24dp)
                Achievement.FRIENDLY -> ContextCompat.getDrawable(context, R.drawable.ic_cloud_black_24dp)
                Achievement.FAST -> ContextCompat.getDrawable(context, R.drawable.ic_chat_black_24dp)
            }

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

    private fun getRandomImageUrl(): String {
        val listUrl = ArrayList<String>()
        listUrl.add("https://i.pinimg.com/736x/8f/56/77/8f56773001b9590da809282d7bfa0cbe--classy-nails-pretty-nails.jpg")
        listUrl.add("https://i.pinimg.com/736x/06/cf/58/06cf5862dac2a181b70e81cd7efa9060--on-my-own-simple-nails.jpg")
        listUrl.add("https://i.pinimg.com/736x/28/81/bc/2881bcfcdfe55bb246b54a9b5ff02741--beautiful-manicure.jpg")
        listUrl.add("https://i.pinimg.com/originals/f0/57/84/f0578459c9a7d7bb4d5cf59005df9b3b.jpg")
        listUrl.add("https://i.ytimg.com/vi/5pWV0BpUyRA/maxresdefault.jpg")
        listUrl.add("https://i.ytimg.com/vi/Hplf6IeWiZg/maxresdefault.jpg")
        listUrl.add("https://i.pinimg.com/736x/78/15/b2/7815b2778c79899e581430119ec44eac--prom-nail-designs-red-amazing-nails-design.jpg")
        listUrl.add("https://i.pinimg.com/originals/e8/38/a2/e838a2abfc549a9ac005da8d2aa98bcc.jpg")
        return listUrl[Random().nextInt(listUrl.size)]
    }
}