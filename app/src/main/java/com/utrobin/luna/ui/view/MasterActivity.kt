package com.utrobin.luna.ui.view

import android.content.Context
import android.content.Intent
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
import com.utrobin.luna.model.MasterBase
import com.utrobin.luna.model.MasterExtended
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.ui.presenter.MasterPresenter
import kotlinx.android.synthetic.main.master_activity.*


/**
 * Created by ivan on 01.11.2017.
 */
class MasterActivity : AppCompatActivity(), MasterContract.View {
    private lateinit var master: MasterExtended

    private val presenter = MasterPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.master_activity)

        presenter.attachView(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val base = intent.extras.getParcelable<MasterBase>(MASTER_BASE)
        title = base.name

        setState(State.LOADING)

        presenter.loadData(base)
//        errorContainer!!.repeat_btn.setOnClickListener {
//            setState(State.LOADING)
//            presenter.loadData(base)
//        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        imageSlider.layoutParams.height = displayMetrics.widthPixels * 9 / 16

        val adapter = ViewPagerAdapter(this, base.photos)
        imageSlider.adapter = adapter
        adapter.imageClickSubject.subscribe {
            openImageViewer()
        }


        supportPostponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageSlider.transitionName = intent.extras.getString(TRANSITION_NAME)
            imageSlider.currentItem = intent.extras.getInt(CURRENT_PHOTO)
        }
        supportStartPostponedEnterTransition()


        buttonWhat.setOnClickListener { showServicesBlock(true) }
        closeServicesBtn.setOnClickListener { showServicesBlock(false) }

        val totalPhotos = base.photos.size
        imageSlider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                photosCounter.text = getString(R.string.master_photos_counter_template, position + 1, totalPhotos)
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
        title = master.base.name

        drawStars()
//        ratingMainPart.text = resources.getQuantityString(R.plurals.ratings_count,
//                reviewsCount, master.base.stars.toString(), reviewsCount)
        ratingMainPart.text = master.base.stars.toString()
        ratingSecondaryPart.text = " | 124 оценки"

        masterDescription.text = "Мы легко впишемся в ваш график, а все наши услуги" +
                " не займут у вас много времени."

        addressMetro.text = "Улица 1905 года"
        addressDescription.text = master.base.address?.description

        fillSigns()
        fillReviews()
        fillSuitableMasters()

        buttonWhen.text = "20 янв, 19:15"
        buttonWhere.text = "Пресненский"
        buttonWhat.text = "3 услуги"

        appointmentPrice.text = "2500 \u20BD"
        appointmentDuration.text = "1ч 20м"
        appointmentMasterDescription.text = "Алия Агиповна, мастер-стилист"

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
            starsContainer.addView(fullStar)
        }

        if (halfStarPresented) {
            val halfStar = ImageView(this)
            halfStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_half_black_24dp))
            halfStar.layoutParams = params
            starsContainer.addView(halfStar)
        }


        for (i in 4 downTo starsContainer.childCount) {
            val emptyStar = ImageView(this)
            emptyStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp))
            emptyStar.layoutParams = params
            starsContainer.addView(emptyStar)
        }
    }


    private fun fillSigns() {
        val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.setMargins(0, 0, resources.getDimension(R.dimen.master_space_between_signs).toInt(), 0)


        val sign1 = LayoutInflater
                .from(this)
                .inflate(R.layout.master_sign, signsContainer, false)
        sign1.layoutParams = params

        sign1.findViewById<TextView>(R.id.mainPart).setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.ic_fast),
                null,
                null,
                null)
        sign1.findViewById<TextView>(R.id.mainPart).text = "Быстро"
        sign1.findViewById<TextView>(R.id.secondaryPart).text = " | 15"

        signsContainer.addView(sign1)


        val sign2 = LayoutInflater
                .from(this)
                .inflate(R.layout.master_sign, signsContainer, false)
        sign2.layoutParams = params

        sign2.findViewById<TextView>(R.id.mainPart).setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.ic_neatly),
                null,
                null,
                null)
        sign2.findViewById<TextView>(R.id.mainPart).text = "Аккуратно"
        sign2.findViewById<TextView>(R.id.secondaryPart).text = " | 25"

        signsContainer.addView(sign2)


        val sign3 = LayoutInflater
                .from(this)
                .inflate(R.layout.master_sign, signsContainer, false)
        sign3.layoutParams = params

        sign3.findViewById<TextView>(R.id.mainPart).setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.ic_pallet),
                null,
                null,
                null)
        sign3.findViewById<TextView>(R.id.mainPart).text = "Большой выбор"
        sign3.findViewById<TextView>(R.id.secondaryPart).text = " | 9"

        signsContainer.addView(sign3)


        val sign4 = LayoutInflater
                .from(this)
                .inflate(R.layout.master_sign, signsContainer, false)
        sign4.layoutParams = params

        sign4.findViewById<TextView>(R.id.mainPart).setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.drawable.ic_painting),
                null,
                null,
                null)
        sign4.findViewById<TextView>(R.id.mainPart).text = "Кисточка"
        sign4.findViewById<TextView>(R.id.secondaryPart).text = " | 107"
        if (System.currentTimeMillis() % 2 == 0L) {
            signsContainer.addView(sign4)
        }
    }

    private fun fillReviews() {
        seeAllReviews.text = "Посмотреть все 34 отзыва"

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
                    .inflate(R.layout.master_review, reviewsContainer, false)

            if (i != reviewsCount - 1) {
                review.layoutParams = params    // Don't apply it to last element
            }

            reviewsContainer.addView(review)

            review.findViewById<TextView>(R.id.reviewAuthor).text = names[i]
            review.findViewById<TextView>(R.id.reviewDate).text = dates[i]
            review.findViewById<TextView>(R.id.reviewText).text = texts[i]
        }
    }

    private fun fillSuitableMasters() {
        val mastersCount = 3
        mastersSuitableForYou.text = resources.getQuantityString(
                R.plurals.n_masters_suitable_for_you, mastersCount, mastersCount)


        // Avatar
//        item.avatar.path.takeIf { it.isNotBlank() }
//                ?.let { Glide.with(this).load(it).apply(RequestOptions.circleCropTransform()).into(master1.findViewById<ImageView>(R.id.avatar)) }
//                ?: master1.findViewById<ImageView>(R.id.avatar).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_avatar))

        val master1 = LayoutInflater
                .from(this)
                .inflate(R.layout.suitable_master, suitableMastersContainer, false)
        Glide.with(this)
                .load("http://www.garmoniazhizni.com/wp-content/uploads/2015/04/odinokaya-prichiny.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(master1.findViewById<ImageView>(R.id.avatar))
        master1.findViewById<TextView>(R.id.name).text = "Алия Агиповна"
        master1.findViewById<TextView>(R.id.role).text = "Мастер-стилист"
        master1.findViewById<TextView>(R.id.price).text = "2500 \u20BD"
        var ratingsCount = 124
        var rating = 4.7 //item.stars.toString()
        master1.findViewById<TextView>(R.id.ratingMainPart).text = "4.7"
        master1.findViewById<TextView>(R.id.ratingSecondaryPart).text = " | 124 оценки"
        suitableMastersContainer.addView(master1)


        val master2 = LayoutInflater
                .from(this)
                .inflate(R.layout.suitable_master, suitableMastersContainer, false)
        Glide.with(this)
                .load("https://wallpaperlayer.com/img/2015/9/miranda-kerr-8591-8917-hd-wallpapers.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(master2.findViewById<ImageView>(R.id.avatar))
        master2.findViewById<TextView>(R.id.name).text = "Агафья Карамыслова"
        master2.findViewById<TextView>(R.id.role).text = "Ведущий стилист"
        master2.findViewById<TextView>(R.id.price).text = "2800 \u20BD"
        ratingsCount = 155
        rating = 4.8 //item.stars.toString()
        master2.findViewById<TextView>(R.id.ratingMainPart).text = "4.8"
        master2.findViewById<TextView>(R.id.ratingSecondaryPart).text = " | 155 оценок"
        suitableMastersContainer.addView(master2)


        val master3 = LayoutInflater
                .from(this)
                .inflate(R.layout.suitable_master, suitableMastersContainer, false)
        Glide.with(this)
                .load("http://download.loveradio.ru/pub/380448.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(master3.findViewById<ImageView>(R.id.avatar))
        master3.findViewById<TextView>(R.id.name).text = "Пенсильвана Воздушная"
        master3.findViewById<TextView>(R.id.role).text = "Арт-директор"
        master3.findViewById<TextView>(R.id.price).text = "3500 \u20BD"
        ratingsCount = 107
        rating = 4.5 //item.stars.toString()
        master3.findViewById<TextView>(R.id.ratingMainPart).text = "4.5"
        master3.findViewById<TextView>(R.id.ratingSecondaryPart).text = " | 107 оценок"
        suitableMastersContainer.addView(master3)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    // TODO make loading and error state
    private fun setState(state: State) {
//        when (state) {
//            State.CONTENT -> {
//                with( {
//                    downloadableContent.visibility = View.VISIBLE
//                    errorContainer.visibility = View.GONE
//                    progressBar.visibility = View.GONE
//                }
//            }
//            State.ERROR -> {
//                with( {
//                    downloadableContent.visibility = View.GONE
//                    errorContainer.visibility = View.VISIBLE
//                    progressBar.visibility = View.GONE
//                }
//            }
//            State.LOADING -> {
//                with( {
//                    downloadableContent.visibility = View.GONE
//                    errorContainer.visibility = View.GONE
//                    progressBar.visibility = View.VISIBLE
//                }
//            }
//        }
    }

    override fun onBackPressed() {
        if (servicesBlock.visibility == View.VISIBLE) {
            showServicesBlock(false)
            return
        }
        super.onBackPressed()
    }


    private fun showServicesBlock(show: Boolean) {
        val animation: Animation
        if (show) {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_up)
            servicesBlock.visibility = View.VISIBLE
        } else {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_down)
            servicesBlock.visibility = View.GONE
        }
        servicesBlock.startAnimation(animation)
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
                .inflate(R.layout.service_block_with_radio_buttons, servicesContent, false)

        manicureLayout.findViewById<TextView>(R.id.title).text = "Маникюр"

        val radioGroup = manicureLayout.findViewById<LinearLayout>(R.id.radioButtonsContainer)

        val params = RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        params.setMargins(0, 0, 0, resources.getDimension(R.dimen.services_block_with_radio_buttons_space_between_radio_buttons).toInt())


        val manicureOption1 = CustomRadioButton(this);
        manicureOption1.setButtonText("Обрезной/классический")
        manicureOption1.setPrice("1100-1500 \u20BD")
        manicureOption1.setLayoutParams(params)
        radioGroup.addView(manicureOption1.view)

        val manicureOption2 = CustomRadioButton(this);
        manicureOption2.setButtonText("Аппаратный")
        manicureOption2.setPrice("1200-1800 \u20BD")
        manicureOption2.setLayoutParams(params)
        radioGroup.addView(manicureOption2.view)

        val manicureOption3 = CustomRadioButton(this);
        manicureOption3.setButtonText("Комбинированный")
        manicureOption3.setPrice("1500-2000 \u20BD")
        manicureOption3.setLayoutParams(params)
        radioGroup.addView(manicureOption3.view)

        // Last one hasn't params
        val manicureOption4 = CustomRadioButton(this);
        manicureOption4.setButtonText("Европейский")
        manicureOption4.setPrice("1500-2000 \u20BD")
        radioGroup.addView(manicureOption4.view)

        servicesContent.addView(manicureLayout)


        // Покрытие
        val coverLayout = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_radio_buttons, servicesContent, false)

        coverLayout.findViewById<TextView>(R.id.title).text = "Покрытие"

        val coverRadioGroup = coverLayout.findViewById<LinearLayout>(R.id.radioButtonsContainer)

        val coverOption1 = CustomRadioButton(this);
        coverOption1.setButtonText("Шеллак")
        coverOption1.setPrice("800-1300 \u20BD")
        coverOption1.setLayoutParams(params)
        coverRadioGroup.addView(coverOption1.view)

        val coverOption2 = CustomRadioButton(this);
        coverOption2.setButtonText("Лак")
        coverOption2.setPrice("1200-1500 \u20BD")
        coverOption2.setLayoutParams(params)
        coverRadioGroup.addView(coverOption2.view)

        val coverOption3 = CustomRadioButton(this);
        coverOption3.setButtonText("Френч")
        coverOption3.setPrice("1500-1800 \u20BD")
        coverRadioGroup.addView(coverOption3.view)

        servicesContent.addView(coverLayout)


        // Design
        val designLayout = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_counters, servicesContent, false)

        designLayout.findViewById<TextView>(R.id.title).text = "Дизайн ногтя"

        val designContainer = designLayout.findViewById<LinearLayout>(R.id.container)

        var simpleCounter = 4
        var complexCounter = 0

        val simple = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_counter, designContainer, false)
        val counterTV = simple.findViewById<TextView>(R.id.counter)

        simple.findViewById<TextView>(R.id.property).text = "Простой рисунок"

        val low = 600
        val high = 800
        simple.findViewById<TextView>(R.id.price).text = "${low * simpleCounter}-${high * simpleCounter} \u20BD"

        counterTV.text = resources.getQuantityString(
                R.plurals.nails, simpleCounter, simpleCounter)

        simple.findViewById<ImageView>(R.id.lessBtn).setOnClickListener {
            if (simpleCounter == 0) {
                return@setOnClickListener
            }
            simpleCounter--
            if (simpleCounter == 0) {
                counterTV.text = "нет"
                simple.findViewById<TextView>(R.id.price).text = "0 \u20BD"
            } else {
                counterTV.text = resources.getQuantityString(
                        R.plurals.nails, simpleCounter, simpleCounter)
                simple.findViewById<TextView>(R.id.price).text = "${low * simpleCounter}-${high * simpleCounter} \u20BD"
            }
        }

        simple.findViewById<ImageView>(R.id.moreBtn).setOnClickListener {
            if (simpleCounter == 10 - complexCounter) {
                return@setOnClickListener
            }
            simpleCounter++
            counterTV.text = resources.getQuantityString(
                    R.plurals.nails, simpleCounter, simpleCounter)
            simple.findViewById<TextView>(R.id.price).text = "${low * simpleCounter}-${high * simpleCounter} \u20BD"
        }

        designContainer.addView(simple)


        val complex = LayoutInflater
                .from(this)
                .inflate(R.layout.service_block_with_counter, designContainer, false)

        val complexCounterTV = complex.findViewById<TextView>(R.id.counter)

        val complexPrice = 1000

        complex.findViewById<TextView>(R.id.property).text = "Сложный рисунок"
        complex.findViewById<TextView>(R.id.counter).text = "нет"
        complex.findViewById<TextView>(R.id.price).text = "0 \u20BD"

        complex.findViewById<ImageView>(R.id.lessBtn).setOnClickListener {
            if (complexCounter == 0) {
                return@setOnClickListener
            }
            complexCounter--
            if (complexCounter == 0) {
                complexCounterTV.text = "нет"
                complex.findViewById<TextView>(R.id.price).text = "0 \u20BD"
            } else {
                complexCounterTV.text = resources.getQuantityString(
                        R.plurals.nails, complexCounter, complexCounter)
                complex.findViewById<TextView>(R.id.price).text = "${complexPrice * complexCounter} \u20BD"
            }
        }

        complex.findViewById<ImageView>(R.id.moreBtn).setOnClickListener {
            if (complexCounter == 10 - simpleCounter) {
                return@setOnClickListener
            }
            complexCounter++
            complexCounterTV.text = resources.getQuantityString(
                    R.plurals.nails, complexCounter, complexCounter)
            complex.findViewById<TextView>(R.id.price).text = "${complexPrice * complexCounter} \u20BD"
        }

        complexCounterTV.text = resources.getQuantityString(
                R.plurals.nails, complexCounter, complexCounter)

        designContainer.addView(complex)

        servicesContent.addView(designLayout)
    }

    private fun openImageViewer() {
        val intent = Intent(this, ImageViewer::class.java)
        intent.apply {
            putExtra(TRANSITION_NAME, ViewCompat.getTransitionName(imageSlider))
            putExtra(CURRENT_PHOTO, imageSlider.currentItem)
            putParcelableArrayListExtra(ImageViewer.PHOTO_LIST_EXTRA, ArrayList(master.base.photos))
        }

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageSlider,
                ViewCompat.getTransitionName(imageSlider)
        )

        startActivity(intent, options.toBundle())
    }


    private class CustomRadioButton(context: Context) : View.OnClickListener {

        private val text: TextView
        private val radioButton: RadioButton
        val view: View = View.inflate(context, R.layout.radio_button_with_price, null)

        init {
            text = view.findViewById(R.id.price) as TextView
            radioButton = view.findViewById(R.id.radioButton) as RadioButton
            radioButton.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            val nextState = radioButton.isChecked

            val parent = view.parent as LinearLayout
            val size = parent.childCount

            for (i in 0 until size) {
                (parent.getChildAt(i).findViewById(R.id.radioButton) as RadioButton).isChecked = false
            }

            radioButton.isChecked = nextState
        }


        fun setButtonText(text: String) {
            radioButton.text = text
        }

        fun setPrice(price: String) {
            text.text = price
        }

        fun setChecked(isChecked: Boolean) {
            radioButton.isChecked = isChecked
        }

        fun setLayoutParams(params: LinearLayout.LayoutParams) {
            view.layoutParams = params
        }
    }
}