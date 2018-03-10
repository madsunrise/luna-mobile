package com.utrobin.luna.ui.view

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
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

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        binding.imageSlider.layoutParams.height = displayMetrics.widthPixels * 9 / 16

        val adapter = ViewPagerAdapter(this, base.photos)
        binding.imageSlider.adapter = adapter
        adapter.imageClickSubject.subscribe {
            openImageViewer()
        }


        supportPostponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.imageSlider.transitionName = intent.extras.getString(TRANSITION_NAME)
            binding.imageSlider.currentItem = intent.extras.getInt(CURRENT_PHOTO)
        }
        supportStartPostponedEnterTransition()


        binding.buttonWhat.setOnClickListener { showServicesBlock(true) }
        binding.closeServicesBtn.setOnClickListener { showServicesBlock(false) }

        val totalPhotos = base.photos.size
        binding.imageSlider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                binding.photosCounter.text = getString(R.string.master_photos_counter_template, position + 1, totalPhotos)
            }

            override fun onPageSelected(position: Int) {

            }
        })
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

        constructServices()
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
                ContextCompat.getDrawable(this, R.drawable.ic_fast),
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
                ContextCompat.getDrawable(this, R.drawable.ic_painting),
                null,
                null,
                null)
        sign4.findViewById<TextView>(R.id.main_part).text = "Кисточка"
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

    override fun onBackPressed() {
        if (binding.servicesBlock.visibility == View.VISIBLE) {
            showServicesBlock(false)
            return
        }
        super.onBackPressed()
    }


    private fun showServicesBlock(show: Boolean) {
        val animation: Animation
        if (show) {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_up)
            binding.servicesBlock.visibility = View.VISIBLE
        } else {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_down)
            binding.servicesBlock.visibility = View.GONE
        }
        binding.servicesBlock.startAnimation(animation)
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


    private fun constructServices() {

        // Маникюр
        val manicureLayout = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_radio_buttons, binding.servicesContent, false)

        manicureLayout.findViewById<TextView>(R.id.title).text = "Маникюр"

        val radioGroup = manicureLayout.findViewById<LinearLayout>(R.id.radio_buttons_container)

        val params = RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        params.setMargins(0, 0, 0, resources.getDimension(R.dimen.services_block_with_radio_buttons_space_between_radio_buttons).toInt())

        val manicureOption1 = LayoutInflater.from(this)
                .inflate(R.layout.radio_button_with_price, radioGroup, false)
        val radioButtonManicure1 = manicureOption1.findViewById<RadioButton>(R.id.radio_button)
        radioButtonManicure1.text = "Обрезной/классический"
        manicureOption1.findViewById<TextView>(R.id.price).text = "1100-1500 \u20BD"
        manicureOption1.layoutParams = params
        radioGroup.addView(manicureOption1)

        val manicureOption2 = LayoutInflater.from(this)
                .inflate(R.layout.radio_button_with_price, radioGroup, false)
        manicureOption2.findViewById<RadioButton>(R.id.radio_button).text = "Аппаратный"
        manicureOption2.findViewById<TextView>(R.id.price).text = "1200-1800 \u20BD"
        manicureOption2.layoutParams = params
        radioGroup.addView(manicureOption2)

        val manicureOption3 = LayoutInflater.from(this)
                .inflate(R.layout.radio_button_with_price, radioGroup, false)
        manicureOption3.findViewById<RadioButton>(R.id.radio_button).text = "Комбинированный"
        manicureOption3.findViewById<TextView>(R.id.price).text = "1500-2000 \u20BD"
        manicureOption3.layoutParams = params
        radioGroup.addView(manicureOption3)

        val manicureOption4 = LayoutInflater.from(this)
                .inflate(R.layout.radio_button_with_price, radioGroup, false)
        manicureOption4.findViewById<RadioButton>(R.id.radio_button).text = "Европейский"
        manicureOption4.findViewById<TextView>(R.id.price).text = "1500-2000 \u20BD"
        radioGroup.addView(manicureOption4)

        binding.servicesContent.addView(manicureLayout)


        // Покрытие
        val coverLayout = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_radio_buttons, binding.servicesContent, false)

        coverLayout.findViewById<TextView>(R.id.title).text = "Покрытие"

        val coverRadioGroup = coverLayout.findViewById<LinearLayout>(R.id.radio_buttons_container)

        val coverOption1 = LayoutInflater.from(this)
                .inflate(R.layout.radio_button_with_price, coverRadioGroup, false)
        coverOption1.findViewById<RadioButton>(R.id.radio_button).text = "Шеллак"
        coverOption1.findViewById<TextView>(R.id.price).text = "800-1300 \u20BD"
        coverOption1.layoutParams = params
        coverRadioGroup.addView(coverOption1)

        val coverOption2 = LayoutInflater.from(this)
                .inflate(R.layout.radio_button_with_price, coverRadioGroup, false)
        coverOption2.findViewById<RadioButton>(R.id.radio_button).text = "Лак"
        coverOption2.findViewById<TextView>(R.id.price).text = "1200-1500 \u20BD"
        coverOption2.layoutParams = params
        coverRadioGroup.addView(coverOption2)

        val coverOption3 = LayoutInflater.from(this)
                .inflate(R.layout.radio_button_with_price, coverRadioGroup, false)
        coverOption3.findViewById<RadioButton>(R.id.radio_button).text = "Френч"
        coverOption3.findViewById<TextView>(R.id.price).text = "1500-1800 \u20BD"
        coverRadioGroup.addView(coverOption3)

        binding.servicesContent.addView(coverLayout)


        // Design
        val designLayout = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_counters, binding.servicesContent, false)

        designLayout.findViewById<TextView>(R.id.title).text = "Дизайн ногтя"

        val designContainer = designLayout.findViewById<LinearLayout>(R.id.container)

        val simple = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_counter, designContainer, false)
        simple.findViewById<TextView>(R.id.property).text = "Простой"
        simple.findViewById<TextView>(R.id.counter).text = "4 ногтя"
        simple.findViewById<TextView>(R.id.price).text = "600-800 \u20BD"
        designContainer.addView(simple)

        val complex = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_counter, designContainer, false)
        complex.findViewById<TextView>(R.id.property).text = "Сложный"
        complex.findViewById<TextView>(R.id.counter).text = "нет"
        complex.findViewById<TextView>(R.id.price).text = "0 \u20BD"
        designContainer.addView(complex)

        val ultraComplex = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_counter, designContainer, false)
        ultraComplex.findViewById<TextView>(R.id.property).text = "Ультра-сложный"
        ultraComplex.findViewById<TextView>(R.id.counter).text = "нет"
        ultraComplex.findViewById<TextView>(R.id.price).text = "0 \u20BD"
        designContainer.addView(ultraComplex)

        binding.servicesContent.addView(designLayout)
    }

    private fun openImageViewer() {
        val intent = Intent(this, ImageViewer::class.java)
        intent.apply {
            putExtra(TRANSITION_NAME, ViewCompat.getTransitionName(binding.imageSlider))
            putExtra(CURRENT_PHOTO, binding.imageSlider.currentItem)
            putParcelableArrayListExtra(ImageViewer.PHOTO_LIST_EXTRA, ArrayList(master.base.photos))
        }

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                binding.imageSlider,
                ViewCompat.getTransitionName(binding.imageSlider)
        )

        startActivity(intent, options.toBundle())
    }
}