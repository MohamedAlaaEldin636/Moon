package codes.grand.app_tutorial

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.structure.base_mvvm.domain.intro.entity.AppTutorial
import grand.app_tutorial.R
import kotlinx.coroutines.delay

class AppTutorialHelper private constructor(builder: Builder) : LifecycleObserver {

  companion object {
    const val SLIDER_DELAY: Long = 2500
  }

  private var activity: Activity = builder.activity
  private var lifecycle: Lifecycle = builder.lifecycle

  private var tutorialData: List<AppTutorial>
  private var sliderContainer: FrameLayout? = null

  @IdRes
  private var sliderContainerResourceID: Int = 0

  @ColorRes
  private var titleColor: Int = R.color.colorDarkGray

  @ColorRes
  private var contentColor: Int = R.color.colorGray

  @ColorRes
  private var activeIndicatorColor: Int = R.color.colorBlack

  @ColorRes
  private var inActiveIndicatorColor: Int = R.color.colorWhite
  private var autoScrolling: Boolean = false

  @ColorRes
  private var btnNextTextColor: Int = R.color.colorDarkGray

  @ColorRes
  private var btnPreviousTextColor: Int = R.color.colorBlack

  private var btnNextTextBackground: Int? = null
  private var btnNextIcon: Int? = null

  private var currentItem = 0
  private lateinit var tutorialViewsContainer: RelativeLayout
  private lateinit var viewPager: ViewPager2
  private lateinit var indicatorsContainer: LinearLayout
  private var viewPagerIndicators: ArrayList<TextView>
  private lateinit var btnNext: MaterialButton
  private lateinit var btnPrevious: TextView
  private lateinit var tutorialAdapter: TutorialAdapter
  private lateinit var pageChangedCallback: ViewPager2.OnPageChangeCallback
  private var firstTimeCallListener: Boolean = true

  private var skipTutorialClick: (() -> Unit)? = null

  init {
    tutorialData = builder.tutorialData
    viewPagerIndicators = ArrayList(tutorialData.size)
    sliderContainer = builder.sliderContainer
    sliderContainerResourceID = builder.sliderContainerResourceID
    titleColor = builder.titleColor
    contentColor = builder.contentColor
    activeIndicatorColor = builder.activeIndicatorColor
    inActiveIndicatorColor = builder.inActiveIndicatorColor
    autoScrolling = builder.autoScrolling
    btnNextTextColor = builder.btnNextTextColor
    btnPreviousTextColor = builder.btnPreviousTextColor
    btnNextTextBackground = builder.btnNextTextBackground
    btnNextIcon = builder.btnNextIcon
    skipTutorialClick = builder.skipTutorialClick

    setUpSliderContainer()

    setUpTutorialViewsContainer()

    setUpNextButton()
    setUpPreviousButton()
    setViewPagerIndicator()

    addViewPagerIndicator()

    addViewPager()

    setUpViewPagerAdapter()

    setUpViewPagerListener()

    setUpAutoScrolling()

    handleNextButtonClickListener()
    handlePreviousButtonClickListener()
    lifecycle.addObserver(this)
  }

  private fun setUpSliderContainer() {
    if (sliderContainerResourceID != 0 && sliderContainer == null) {
      sliderContainer = activity.findViewById(sliderContainerResourceID)
    }

    sliderContainer?.removeAllViewsInLayout()
  }

  private fun setUpTutorialViewsContainer() {
    tutorialViewsContainer = RelativeLayout(activity)

    tutorialViewsContainer.apply {
      val lp = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
      layoutParams = lp
    }

    sliderContainer?.addView(tutorialViewsContainer)
  }

  private fun setUpNextButton() {
    btnNext = MaterialButton(activity)

    btnNext.apply {
      val lp = RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )
      lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
      lp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
      lp.setMargins(5, 16, 5, 16)
      layoutParams = lp
      gravity = Gravity.CENTER
      id = View.generateViewId()
      text = activity.resources.getString(R.string.next)
//      setPadding(180, 36, 180, 36)
      setTextColor(ContextCompat.getColor(activity, btnNextTextColor))

      btnNextTextBackground?.let {
        setBackgroundColor(ContextCompat.getColor(activity, it))
      }
      btnNextIcon?.let {
        icon = ContextCompat.getDrawable(activity, it)
//        iconGravity = Gravity.END
      }

    }

    tutorialViewsContainer.addView(btnNext)
  }

  private fun setUpPreviousButton() {
    btnPrevious = TextView(activity.applicationContext)

    btnPrevious.apply {
      val lp = RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )
      lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
      lp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
      lp.setMargins(5, 16, 5, 16)
      layoutParams = lp
      gravity = Gravity.CENTER
      id = View.generateViewId()
      text = activity.resources.getString(R.string.previous_app)
      setTextColor(ContextCompat.getColor(activity, btnPreviousTextColor))
      btnPrevious.setTextAppearance(android.R.style.TextAppearance_Medium)
    }
    tutorialViewsContainer.addView(btnPrevious)
    btnPrevious.visibility = View.GONE
  }

  private fun setViewPagerIndicator() {
    if (!::indicatorsContainer.isInitialized) {
      indicatorsContainer = LinearLayout(activity)
    }
    indicatorsContainer.removeAllViews()

    for (i in tutorialData.indices) {
      val tv = TextView(activity)
      tv.apply {
        val lp = FrameLayout.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(5, 0, 5, 7)
        layoutParams = lp
        setText(R.string.dot)
        textSize = if (i == currentItem) 45f else 28f
        includeFontPadding = false
        setTextColor(
          if (i == currentItem)
            ContextCompat.getColor(context, activeIndicatorColor)
          else
            ContextCompat.getColor(context, inActiveIndicatorColor)
        )
      }

      viewPagerIndicators.add(tv)
      indicatorsContainer.addView(tv)
      indicatorsContainer.bringToFront()
    }
  }

  private fun addViewPagerIndicator() {
    indicatorsContainer.apply {
      val lp = RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )
      lp.addRule(RelativeLayout.ABOVE, btnNext.id)
      layoutParams = lp
      orientation = LinearLayout.HORIZONTAL
      id = View.generateViewId()
      gravity = Gravity.CENTER
    }

    tutorialViewsContainer.addView(indicatorsContainer)
  }

  private fun addViewPager() {
    viewPager = ViewPager2(activity)

    viewPager.apply {
      val lp = RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
      lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
      lp.addRule(RelativeLayout.ABOVE, indicatorsContainer.id)
      layoutParams = lp
      clipChildren = false
      clipToPadding = false
      getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    tutorialViewsContainer.addView(viewPager)
  }

  private fun setUpViewPagerAdapter() {
    tutorialAdapter = TutorialAdapter(
      ContextCompat.getColor(activity, titleColor),
      ContextCompat.getColor(activity, contentColor)
    )

    viewPager.adapter = tutorialAdapter.apply { submitList(tutorialData) }
  }

  private fun setUpViewPagerListener() {
    pageChangedCallback = object : ViewPager2.OnPageChangeCallback() {
      override
      fun onPageSelected(position: Int) {
        currentItem = position

        if (firstTimeCallListener) {
          firstTimeCallListener = false
          return
        }
        setViewPagerIndicator()

        if (position != tutorialData.size - 1) {
          btnNext.text = activity.resources.getString(R.string.next)
        }
//        else {
//          btnNext.text = activity.resources.getString(R.string.open_app)
//        }
        if (position == 0)
          btnPrevious.visibility = View.GONE
        else
          btnPrevious.visibility = View.VISIBLE
      }
    }
  }

  private fun setUpAutoScrolling() {
    if (autoScrolling) {
      lifecycle.coroutineScope.launchWhenResumed {
        while (true) {
          delay(SLIDER_DELAY)

          if (tutorialData.size - 1 == currentItem) {
            currentItem = 0
          } else {
            currentItem++
          }

          if (currentItem == 0) {
            viewPager.setCurrentItem(currentItem, false)
          } else {
            viewPager.setCurrentItem(currentItem, true)
          }
        }
      }
    }
  }

  private fun handleNextButtonClickListener() {
    btnNext.setOnClickListener {
      if (tutorialData.size - 1 == currentItem) {
        skipTutorialClick?.invoke()
      } else {
        currentItem++
      }

      viewPager.setCurrentItem(currentItem, true)
    }
  }

  private fun handlePreviousButtonClickListener() {
    btnPrevious.setOnClickListener {
//      if (tutorialData.size - 1 == currentItem) {
      currentItem--
//      } else {
//        currentItem++
//      }

      viewPager.setCurrentItem(currentItem, true)
    }
  }

  class Builder(internal var activity: Activity, internal var lifecycle: Lifecycle) {

    internal lateinit var tutorialData: List<AppTutorial>
    internal var sliderContainer: FrameLayout? = null

    @IdRes
    internal var sliderContainerResourceID: Int = 0

    @ColorRes
    internal var titleColor: Int = R.color.colorDarkGray

    @ColorRes
    internal var contentColor: Int = R.color.colorGray

    @ColorRes
    internal var activeIndicatorColor: Int = R.color.colorBlack

    @ColorRes
    internal var inActiveIndicatorColor: Int = R.color.colorWhite

    internal var autoScrolling: Boolean = false

    @ColorRes
    internal var btnNextTextColor: Int = R.color.colorDarkGray

    @ColorRes
    internal var btnPreviousTextColor: Int = R.color.colorBlack

    internal var btnNextTextBackground: Int? = null
    internal var btnNextIcon: Int? = null

    internal var skipTutorialClick: (() -> Unit)? = null

    fun setTutorialData(tutorialData: List<AppTutorial>): Builder {
      this.tutorialData = tutorialData
      return this
    }

    fun setSliderContainer(sliderContainer: FrameLayout): Builder {
      this.sliderContainer = sliderContainer
      return this
    }

    fun setSliderContainerResourceID(@IdRes sliderContainerResourceID: Int): Builder {
      this.sliderContainerResourceID = sliderContainerResourceID
      return this
    }

    fun setTitleColor(@ColorRes titleColor: Int): Builder {
      this.titleColor = titleColor
      return this
    }

    fun setContentColor(@ColorRes contentColor: Int): Builder {
      this.contentColor = contentColor
      return this
    }

    fun setActiveIndicatorColor(@ColorRes activeIndicatorColor: Int): Builder {
      this.activeIndicatorColor = activeIndicatorColor
      return this
    }

    fun setInActiveIndicatorColor(@ColorRes inActiveIndicatorColor: Int): Builder {
      this.inActiveIndicatorColor = inActiveIndicatorColor
      return this
    }

    fun setAutoScrolling(autoScrolling: Boolean): Builder {
      this.autoScrolling = autoScrolling
      return this
    }

    fun setNextButtonTextColor(@ColorRes btnNextTextColor: Int): Builder {
      this.btnNextTextColor = btnNextTextColor
      return this
    }

    fun setPreviousTextColor(@ColorRes btnPreviousTextColor: Int): Builder {
      this.btnPreviousTextColor = btnPreviousTextColor
      return this
    }

    fun setNextButtonBackground(btnNextTextBackground: Int): Builder {
      this.btnNextTextBackground = btnNextTextBackground
      return this
    }

    fun setNextButtonIcon(btnNextIcon: Int): Builder {
      this.btnNextIcon = btnNextIcon
      return this
    }

    fun setSkipTutorialClick(skipTutorialClick: (() -> Unit)): Builder {
      this.skipTutorialClick = skipTutorialClick
      return this
    }

    fun create(): AppTutorialHelper {
      return AppTutorialHelper(this)
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  private fun onPause() {
    unregisterViewPagerCallback()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  private fun onResume() {
    registerViewPagerCallback()
  }

  private fun registerViewPagerCallback() {
    viewPager.registerOnPageChangeCallback(pageChangedCallback)
  }

  private fun unregisterViewPagerCallback() {
    viewPager.unregisterOnPageChangeCallback(pageChangedCallback)
  }
}