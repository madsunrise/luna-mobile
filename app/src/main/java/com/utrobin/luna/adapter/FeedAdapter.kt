package com.utrobin.luna.adapter

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.utrobin.luna.R
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.utils.svg.GlideApp
import com.utrobin.luna.utils.svg.SvgSoftwareLayerSetter
import de.hdodenhof.circleimageview.CircleImageView

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
        holder.header.setOnClickListener { viewClickSubject.onNext(item) }

        holder.name.text = "${item.name} #$position"
        holder.location.text = item.address.description

        // Avatar
        item.avatar.path.takeIf { it.isNotBlank() }
                ?.let { Glide.with(context).load(it).into(holder.avatar) }
                ?: holder.avatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.noavatar))

        // Signs
        val requestBuilder = GlideApp.with(context)
                .`as`(PictureDrawable::class.java)
                .transition(withCrossFade())
                .listener(SvgSoftwareLayerSetter())

        holder.signsContainer.removeAllViews()
        for (sign in item.signs) {
            val image = ImageView(context)
            val signSize = context.resources.getDimension(R.dimen.feed_signs_size).toInt()
            val params = LinearLayout.LayoutParams(signSize, signSize)
            params.setMargins(0, 0, context.resources.getDimension(R.dimen.feed_signs_margin_right).toInt(), 0)
            image.layoutParams = params
            holder.signsContainer.addView(image)

            requestBuilder?.load(Uri.parse(sign.photo.path))?.into(image);
        }

        // Photos slider
        holder.viewPager.adapter = ViewPagerAdapter(context, item.photos)
        val totalPages = item.photos.size
        addBottomDots(context, holder.dotsContainer, 0, totalPages)
        holder.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(pagePosition: Int) {
                addBottomDots(context, holder.dotsContainer, pagePosition, totalPages)
            }
        })

        holder.rating.text = item.stars.toString()
    }

    private fun addBottomDots(context: Context, container: LinearLayout, currentPage: Int, totalPages: Int) {
        container.removeAllViews()
        if (totalPages < 2) {
            return
        }
        val dots = ArrayList<TextView>()
        for (i in 0 until totalPages) {
            val textView = TextView(container.context)
            textView.text =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY);
                    } else {
                        Html.fromHtml("&#8226;")
                    }
            textView.textSize = 35f

            val color = if (i != currentPage) {
                ContextCompat.getColor(context, R.color.view_pager_inactive_dot)
            } else {
                ContextCompat.getColor(context, R.color.view_pager_active_dot)
            }
            textView.setTextColor(color)

            container.addView(textView)
            dots.add(textView)
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
    }

    override fun getYourItemId(position: Int) = items[position].hashCode().toLong()
}