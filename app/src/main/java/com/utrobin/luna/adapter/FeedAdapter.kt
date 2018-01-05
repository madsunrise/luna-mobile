package com.utrobin.luna.adapter

import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter.Companion.addBottomDots
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.utils.svg.SvgModule
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.subjects.PublishSubject

/**
 * Created by ivan on 31.10.2017.
 */

class FeedAdapter(items: List<FeedItem>) : FooterLoaderAdapter(ArrayList(items)) {
    val bookmarkClickSubject: PublishSubject<FeedItem> = PublishSubject.create<FeedItem>()

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val context = (holder as ItemViewHolder).itemView.context

        holder.header.setOnClickListener { viewClickSubject.onNext(item) }

        setBookmarkDrawable(item, holder.bookmark)
        holder.bookmark.setOnClickListener {
            item.isFavorite = !item.isFavorite
            setBookmarkDrawable(item, holder.bookmark)
            bookmarkClickSubject.onNext(item)
        }

        holder.name.text = item.name
        holder.location.text = item.address.description

        // Avatar
        item.avatar.path.takeIf { it.isNotBlank() }
                ?.let { Glide.with(context).load(it).into(holder.avatar) }
                ?: holder.avatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_avatar))

        // Signs
        val requestBuilder = SvgModule.getGlideSvgRequestBuilder(context)
        holder.signsContainer.removeAllViews()
        if (item.signs.isEmpty()) {
            holder.signsContainer.visibility = View.GONE
        } else {
            for (sign in item.signs) {
                val image = ImageView(context)
                val signSize = context.resources.getDimension(R.dimen.feed_signs_size).toInt()
                val params = LinearLayout.LayoutParams(signSize, signSize)
                params.setMargins(0, 0, context.resources.getDimension(R.dimen.feed_space_between_signs).toInt(), 0)
                image.layoutParams = params
                holder.signsContainer.addView(image)

                requestBuilder?.load(Uri.parse(sign.icon))?.into(image);
            }
        }

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

        holder.rating.text = item.stars.toString()
    }

    private fun setBookmarkDrawable(item: FeedItem, image: ImageButton) {
        if (item.isFavorite) {
            image.setImageDrawable(ContextCompat.getDrawable(image.context, R.drawable.ic_bookmark_black_24dp))
        } else {
            image.setImageDrawable(ContextCompat.getDrawable(image.context, R.drawable.ic_bookmark_border_black_24dp))
        }
    }


    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: View = view.findViewById(R.id.header)
        val name: TextView = view.findViewById(R.id.name)
        val location: TextView = view.findViewById(R.id.location)
        val avatar: CircleImageView = view.findViewById(R.id.avatar)
        val viewPager: ViewPager = view.findViewById(R.id.pager)
        val dotsContainer: LinearLayout = view.findViewById(R.id.dots_container)
        val signsContainer: LinearLayout = view.findViewById(R.id.signs_container)
        val rating: TextView = view.findViewById(R.id.rating)
        val bookmark: ImageButton = view.findViewById(R.id.bookmark)
    }

    override fun getYourItemId(position: Int) = items[position].hashCode().toLong()
}