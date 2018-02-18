package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.databinding.MasterActivityBinding
import com.utrobin.luna.model.MasterBase
import com.utrobin.luna.model.MasterExtended
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.ui.presenter.MasterPresenter


/**
 * Created by ivan on 01.11.2017.
 */
class MasterActivity : AppCompatActivity(), MasterContract.View {
    private lateinit var master: MasterExtended

    lateinit var binding: MasterActivityBinding

    private val presenter = MasterPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.master_activity)

        presenter.attachView(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val base = intent.extras.getParcelable<MasterBase>(MASTER_BASE)
        title = base.name

        setState(State.LOADING)

        presenter.loadData(base)
//        binding.errorContainer!!.repeat_btn.setOnClickListener {
//            setState(State.LOADING)
//            presenter.loadData(base)
//        }

        binding.pager.adapter = ViewPagerAdapter(this, base.photos)


        supportPostponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.pager.transitionName = intent.extras.getString(TRANSITION_NAME)
            binding.pager.currentItem = intent.extras.getInt(CURRENT_PHOTO)
        }
        supportStartPostponedEnterTransition();
    }

    override fun dataLoaded(master: MasterExtended) {
        setState(State.CONTENT)
        this.master = master

        fillViews()
    }

    override fun dataLoadingFailed(reason: NetworkError) {
        setState(State.ERROR)
    }

    private fun fillViews() {
        binding.title.text = master.base.name

        drawStars()
//        binding.ratingMainPart.text = resources.getQuantityString(R.plurals.ratings_count,
//                reviewsCount, master.base.stars.toString(), reviewsCount)
        binding.ratingMainPart.text = master.base.stars.toString()
        binding.ratingSecondaryPart.text = " | 124 оценки"

        binding.masterDescription.text = "Мы легко впишемся в ваш график, а все наши услуги" +
                " не займут у вас много времени."

        binding.addressMetro.text = "Улица 1905 года"
        binding.addressDescription.text = master.base.address.description

        fillSigns()
        fillReviews()
        fillSuitableMasters()

        binding.buttonWhen.text = "20 янв, 19:15"
        binding.buttonWhere.text = "Пресненский"
        binding.buttonWhat.text = "3 услуги"

        binding.appointmentPrice.text = "2500 \u20BD"
        binding.appointmentDuration.text = "1ч 20м"
        binding.appointmentMasterDescription.text = "Алия Агиповна, мастер-стилист"
    }

    private fun drawStars() {
        val fullStarsCount = master.base.stars.toInt()
        val halfStarPresented = master.base.stars - fullStarsCount >= 0.5

        val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.setMargins(0, 0, resources.getDimension(R.dimen.master_space_between_stars).toInt(), 0)

        for (i in 0 until fullStarsCount) {
            val fullStar = ImageView(this)
            fullStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp))
            fullStar.layoutParams = params
            binding.starsContainer.addView(fullStar)
        }

        if (halfStarPresented) {
            val halfStar = ImageView(this)
            halfStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_half_black_24dp))
            halfStar.layoutParams = params
            binding.starsContainer.addView(halfStar)
        }


        for (i in 4 downTo binding.starsContainer.childCount) {
            val emptyStar = ImageView(this)
            emptyStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp))
            emptyStar.layoutParams = params
            binding.starsContainer.addView(emptyStar)
        }
    }


    private fun fillSigns() {
        val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.setMargins(0, 0, resources.getDimension(R.dimen.master_space_between_signs).toInt(), 0)


        val sign1 = LayoutInflater
                .from(this)
                .inflate(R.layout.master_sign, binding.signsContainer, false)
        sign1.layoutParams = params

        sign1.findViewById<TextView>(R.id.main_part).setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.ic_cute_rocket_launching),
                null,
                null,
                null)
        sign1.findViewById<TextView>(R.id.main_part).text = "Быстро"
        sign1.findViewById<TextView>(R.id.secondary_part).text = " | 15"

        binding.signsContainer.addView(sign1)


        val sign2 = LayoutInflater
                .from(this)
                .inflate(R.layout.master_sign, binding.signsContainer, false)
        sign2.layoutParams = params

        sign2.findViewById<TextView>(R.id.main_part).setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.ic_neatly),
                null,
                null,
                null)
        sign2.findViewById<TextView>(R.id.main_part).text = "Аккуратно"
        sign2.findViewById<TextView>(R.id.secondary_part).text = " | 25"

        binding.signsContainer.addView(sign2)


        val sign3 = LayoutInflater
                .from(this)
                .inflate(R.layout.master_sign, binding.signsContainer, false)
        sign3.layoutParams = params

        sign3.findViewById<TextView>(R.id.main_part).setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.ic_pallet),
                null,
                null,
                null)
        sign3.findViewById<TextView>(R.id.main_part).text = "Большой выбор"
        sign3.findViewById<TextView>(R.id.secondary_part).text = " | 9"

        binding.signsContainer.addView(sign3)


        val sign4 = LayoutInflater
                .from(this)
                .inflate(R.layout.master_sign, binding.signsContainer, false)
        sign4.layoutParams = params

        sign4.findViewById<TextView>(R.id.main_part).setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.ic_neatly),
                null,
                null,
                null)
        sign4.findViewById<TextView>(R.id.main_part).text = "Красивые девушки"
        sign4.findViewById<TextView>(R.id.secondary_part).text = " | 107"
        if (System.currentTimeMillis() % 2 == 0L) {
            binding.signsContainer.addView(sign4)
        }
    }

    private fun fillReviews() {
        binding.seeAllNReviews.text = "Посмотреть все 34 отзыва"

        val params = LinearLayout.LayoutParams(
                resources.getDimension(R.dimen.master_review_width).toInt(),
                resources.getDimension(R.dimen.master_review_height).toInt()
        )
        params.setMargins(0, 0, resources.getDimension(R.dimen.master_space_between_reviews).toInt(), 0)

        val reviewsCount = 4

        val names = ArrayList<String>().apply {
            add("Псевдопродольная Алефтина")
            add("Каролина Яцунова")
            add("Ксения Попова")
            add("Элла Орбельни")
        }

        val dates = ArrayList<String>().apply {
            add("2 дн. назад. Мастер Пенсильвана В.")
            add("3 дн. назад. Мастер Агафья К.")
            add("3 дн. назад. Мастер Пенсильвана В.")
            add("5 дн. назад. Мастер Алия А.")
        }

        val texts = ArrayList<String>().apply {
            add("Аккуратный и опрятный мастер, приятный персонал," +
                    " чистое оборудование и вообще мне все понравилось))")
            add("Каждый раз когда я посещаю эту клинику первое что бросается мне в лицо это приятные девушки на ресепшне, которые всегда улыбчивы и вежливы. Это настраивает на позитивный лад перед началом любой процедуры. Мне не страшно отдавать себя в руки специалистов Силуэта, поскольку уровень их квалификации не вызывает у меня сомнений.")
            add("Проблемы с волосами меня преследуют давно, но я никак не могу прекратить их красить ежемесячно, из-за чего постоянно сжигаю. Но в последнее время на ощупь они вообще стали как солома, а на концы и взглянуть было страшно. А в довес ко всему, еще и сожгла кожу головы краской. По совету хорошей подружки решила обратиться к проверенному трихологу в медицинский центр красоты и здоровья. Инна Павловна меня отлично приняла и рассказала, как можно избежать подобных проблем. Более того, за два сеанса она привела кожу в порядок, а с волосами немножко поколдовала и их состояние улучшилось. Так что походом на Весковский я очень довольна.")
            add("Спасибо большое Виктории за сложную, но на совесть выполненную работу! Особенно за индивидуальный подход, быстроту и после всего перенесенного волосами за их качество. Выровнять темные корни и рыжую полосу по середине волос с белоснежными концами нелегко, но она справились! Очень всем рекомендую этого суперского мастера и чудесного человека!!!! Обязательно приду к Вам еще не раз!")
        }

        for (i in 0 until reviewsCount) {
            val review = LayoutInflater
                    .from(this)
                    .inflate(R.layout.master_review, binding.reviewsContainer, false)

            if (i != reviewsCount - 1) {
                review.layoutParams = params    // Don't apply it to last element
            }

            binding.reviewsContainer.addView(review)

            review.findViewById<TextView>(R.id.review_author).text = names[i]
            review.findViewById<TextView>(R.id.review_date).text = dates[i]
            review.findViewById<TextView>(R.id.review_text).text = texts[i]
        }
    }

    private fun fillSuitableMasters() {
        val mastersCount = 3
        binding.nMastersSuitableForYou.text = resources.getQuantityString(
                R.plurals.n_masters_suitable_for_you, mastersCount, mastersCount)


        // Avatar
//        item.avatar.path.takeIf { it.isNotBlank() }
//                ?.let { Glide.with(this).load(it).apply(RequestOptions.circleCropTransform()).into(master1.findViewById<ImageView>(R.id.avatar)) }
//                ?: master1.findViewById<ImageView>(R.id.avatar).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_avatar))

        val master1 = LayoutInflater
                .from(this)
                .inflate(R.layout.suitable_master, binding.suitableMastersContainer, false)
        Glide.with(this)
                .load("http://www.garmoniazhizni.com/wp-content/uploads/2015/04/odinokaya-prichiny.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(master1.findViewById<ImageView>(R.id.avatar))
        master1.findViewById<TextView>(R.id.name).text = "Алия Агиповна"
        master1.findViewById<TextView>(R.id.role).text = "Мастер-стилист"
        master1.findViewById<TextView>(R.id.price).text = "2500 \u20BD"
        var ratingsCount = 124
        var rating = 4.7 //item.stars.toString()
        master1.findViewById<TextView>(R.id.rating_main_part).text = "4.7"
        master1.findViewById<TextView>(R.id.rating_secondary_part).text = " | 124 оценки"
        binding.suitableMastersContainer.addView(master1)


        val master2 = LayoutInflater
                .from(this)
                .inflate(R.layout.suitable_master, binding.suitableMastersContainer, false)
        Glide.with(this)
                .load("https://wallpaperlayer.com/img/2015/9/miranda-kerr-8591-8917-hd-wallpapers.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(master2.findViewById<ImageView>(R.id.avatar))
        master2.findViewById<TextView>(R.id.name).text = "Агафья Карамыслова"
        master2.findViewById<TextView>(R.id.role).text = "Ведущий стилист"
        master2.findViewById<TextView>(R.id.price).text = "2800 \u20BD"
        ratingsCount = 155
        rating = 4.8 //item.stars.toString()
        master2.findViewById<TextView>(R.id.rating_main_part).text = "4.8"
        master2.findViewById<TextView>(R.id.rating_secondary_part).text = " | 155 оценок"
        binding.suitableMastersContainer.addView(master2)


        val master3 = LayoutInflater
                .from(this)
                .inflate(R.layout.suitable_master, binding.suitableMastersContainer, false)
        Glide.with(this)
                .load("http://download.loveradio.ru/pub/380448.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(master3.findViewById<ImageView>(R.id.avatar))
        master3.findViewById<TextView>(R.id.name).text = "Пенсильвана Воздушная"
        master3.findViewById<TextView>(R.id.role).text = "Арт-директор"
        master3.findViewById<TextView>(R.id.price).text = "3500 \u20BD"
        ratingsCount = 107
        rating = 4.5 //item.stars.toString()
        master3.findViewById<TextView>(R.id.rating_main_part).text = "4.5"
        master3.findViewById<TextView>(R.id.rating_secondary_part).text = " | 107 оценок"
        binding.suitableMastersContainer.addView(master3)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    // TODO make loading and error state
    private fun setState(state: State) {
//        when (state) {
//            State.CONTENT -> {
//                with(binding) {
//                    downloadableContent.visibility = View.VISIBLE
//                    errorContainer.visibility = View.GONE
//                    progressBar.visibility = View.GONE
//                }
//            }
//            State.ERROR -> {
//                with(binding) {
//                    downloadableContent.visibility = View.GONE
//                    errorContainer.visibility = View.VISIBLE
//                    progressBar.visibility = View.GONE
//                }
//            }
//            State.LOADING -> {
//                with(binding) {
//                    downloadableContent.visibility = View.GONE
//                    errorContainer.visibility = View.GONE
//                    progressBar.visibility = View.VISIBLE
//                }
//            }
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val MASTER_BASE = "MASTER_BASE_EXTRA"
        const val TRANSITION_NAME = "TRANSITION_NAME_EXTRA"
        const val CURRENT_PHOTO = "CURRENT_PHOTO_EXTRA"
    }

    private enum class State {
        CONTENT, ERROR, LOADING
    }
}