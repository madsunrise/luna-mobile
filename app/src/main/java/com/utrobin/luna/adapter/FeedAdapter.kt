package com.utrobin.luna.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.utrobin.luna.R
import com.utrobin.luna.model.MasterBase

/**
 * Created by ivan on 31.10.2017.
 */

class FeedAdapter(items: List<MasterBase>, private val screenWidthInPx: Int) : FooterLoaderAdapter<MasterBase, ViewPager>(ArrayList(items)) {

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val context = (holder as ItemViewHolder).itemView.context

        val adapter = ViewPagerAdapter(context, item.photos)
        adapter.imageClickSubject.subscribe { viewClickSubject.onNext(Pair(item, holder.viewPager)) } // A bit strange decision
        holder.itemView.setOnClickListener { viewClickSubject.onNext(Pair(item, holder.viewPager)) }

        holder.moreOptions.setOnClickListener {
            Toast.makeText(context, "Options!", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setTransitionName(holder.viewPager, getTransitionName(item))
        holder.viewPager.layoutParams.height = screenWidthInPx * 9 / 16


        holder.name.text = item.name
        holder.address.text = item.address.description

        if (item.address.metro.isNotEmpty()) {
            holder.address.compoundDrawables[0].colorFilter = PorterDuffColorFilter(
                    Color.parseColor('#' + item.address.metro[0].color), PorterDuff.Mode.MULTIPLY
            )
        } else {
            holder.address.compoundDrawables[0].colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(context, R.color.white),
                    PorterDuff.Mode.MULTIPLY
            )
        }
        // Avatar
        item.avatar.path.takeIf { it.isNotBlank() }
                ?.let { Glide.with(context).load(it).apply(RequestOptions.circleCropTransform()).into(holder.avatar) }
                ?: holder.avatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_avatar))

        // Signs
        holder.signsContainer.removeAllViews()
        if (item.signs.isNotEmpty()) {
            for (sign in item.signs) {
                val drawable = when (sign.name) {
                    "accuracy" -> ContextCompat.getDrawable(context, R.drawable.ic_fast)
                    "politeness" -> ContextCompat.getDrawable(context, R.drawable.ic_neatly)
                    "varnish resistance" -> ContextCompat.getDrawable(context, R.drawable.ic_pallet)
                    "painting" -> ContextCompat.getDrawable(context, R.drawable.ic_painting)
                    else -> null
                } ?: continue

                val container = RelativeLayout(context)
                val icon = ImageView(context)
                val background = ImageView(context)

                val params = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                params.setMargins(0, 0, context.resources.getDimension(R.dimen.feed_space_between_signs).toInt(), 0)
                container.layoutParams = params


                val iconParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                iconParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                icon.layoutParams = iconParams


                icon.setImageDrawable(drawable)
                icon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                background.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sign_circle))

                container.addView(background)
                container.addView(icon)

                holder.signsContainer.addView(container)
            }
        }

        holder.initialCost.text = "2500 - 3500 \u20BD"

        // Photos slider
        holder.viewPager.adapter = adapter
        holder.tabLayout.setupWithViewPager(holder.viewPager)

        val ratingsCount = 21
        holder.stars.text = context.resources.getQuantityString(R.plurals.ratings_count, ratingsCount, item.stars.toString(), ratingsCount)
        holder.comments.text = "12"
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: View = view.findViewById(R.id.header)
        val name: TextView = view.findViewById(R.id.name)
        val address: TextView = view.findViewById(R.id.address)
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val moreOptions: View = view.findViewById(R.id.more_options)
        val viewPager: ViewPager = view.findViewById(R.id.image_slider)
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
        val signsContainer: LinearLayout = view.findViewById(R.id.signs_container)
        val initialCost: TextView = view.findViewById(R.id.initial_cost)
        val stars: TextView = view.findViewById(R.id.stars)
        val comments: TextView = view.findViewById(R.id.comments)
    }

    override fun getYourItemId(position: Int) = items[position].hashCode().toLong()

    companion object {
        fun getTransitionName(item: MasterBase) = item.name + item.id
    }
}