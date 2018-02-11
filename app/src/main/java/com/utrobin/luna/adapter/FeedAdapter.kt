package com.utrobin.luna.adapter

import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter.Companion.addBottomDots
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.utils.svg.SvgModule

/**
 * Created by ivan on 31.10.2017.
 */

class FeedAdapter(items: List<FeedItem>) : FooterLoaderAdapter<FeedItem>(ArrayList(items)) {

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val context = (holder as ItemViewHolder).itemView.context

        holder.header.setOnClickListener { viewClickSubject.onNext(item) }

        holder.moreOptions.setOnClickListener {
            Toast.makeText(context, "Options!", Toast.LENGTH_SHORT).show()
        }

        holder.name.text = item.name
        holder.address.text = item.address

        // Avatar
        item.avatar.path.takeIf { it.isNotBlank() }
                ?.let { Glide.with(context).load(it).apply(RequestOptions.circleCropTransform()).into(holder.avatar) }
                ?: holder.avatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_avatar))

        // Signs
        holder.signsContainer.removeAllViews()
        if (item.signs.isNotEmpty()) {
            val requestBuilder = SvgModule.getGlideSvgRequestBuilder(context)
            for (sign in item.signs) {
                val image = ImageView(context)
                val signSize = context.resources.getDimension(R.dimen.feed_signs_size).toInt()
                val params = LinearLayout.LayoutParams(signSize, signSize)
                params.setMargins(0, 0, context.resources.getDimension(R.dimen.feed_space_between_signs).toInt(), 0)
                image.layoutParams = params
                holder.signsContainer.addView(image)
                requestBuilder.load(Uri.parse(sign.icon)).into(image);
            }
        }

        holder.initialCost.text = "от 2500 Р"

        // Photos slider
        holder.viewPager.adapter = ViewPagerAdapter(context, item.photos)
        val totalPages = item.photos.size
        addBottomDots(holder.dotsContainer, 0, totalPages)
        holder.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(pagePosition: Int) {
                addBottomDots(holder.dotsContainer, pagePosition, totalPages)
            }
        })

        holder.stars.text = item.stars.toString() + " | 21 оценка"
        holder.comments.text = "12"
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: View = view.findViewById(R.id.header)
        val name: TextView = view.findViewById(R.id.name)
        val address: TextView = view.findViewById(R.id.address)
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val moreOptions: View = view.findViewById(R.id.more_options)
        val viewPager: ViewPager = view.findViewById(R.id.pager)
        val dotsContainer: LinearLayout = view.findViewById(R.id.dots_container)
        val signsContainer: LinearLayout = view.findViewById(R.id.signs_container)
        val initialCost: TextView = view.findViewById(R.id.initial_cost)
        val stars: TextView = view.findViewById(R.id.stars)
        val comments: TextView = view.findViewById(R.id.comments)
    }

    override fun getYourItemId(position: Int) = items[position].hashCode().toLong()
}