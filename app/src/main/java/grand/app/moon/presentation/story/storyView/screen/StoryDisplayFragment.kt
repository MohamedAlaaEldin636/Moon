package grand.app.moon.presentation.story.storyView.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import grand.app.moon.presentation.story.storyView.data.Story
import grand.app.moon.presentation.story.storyView.data.StoryUser
import com.c2m.storyviewer.utils.OnSwipeTouchListener
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.core.MyApplication
import grand.app.moon.databinding.FragmentStoryDisplayBinding
import grand.app.moon.presentation.story.storyView.customview.StoriesProgressView
import grand.app.moon.presentation.story.viewModels.StoryDisplayViewModel
import kotlinx.android.synthetic.main.fragment_story_display.*
import java.util.*

@AndroidEntryPoint
class StoryDisplayFragment : BaseFragment<FragmentStoryDisplayBinding>(){
  override fun getLayoutId(): Int {
    TODO("Not yet implemented")
  }
//  StoriesProgressView.StoriesListener


//  private val position: Int by
//  lazy { arguments?.getInt(EXTRA_POSITION) ?: 0 }
//
//  private val storyUser: StoryUser by
//  lazy {
//    (arguments?.getParcelable<StoryUser>(
//      EXTRA_STORY_USER
//    ) as StoryUser)
//  }
//
//  private val stories: ArrayList<Story> by
//  lazy { storyUser.stories }
//
//  private var simpleExoPlayer: SimpleExoPlayer? = null
//  private lateinit var mediaDataSourceFactory: DataSource.Factory
//  private var pageViewOperator: PageViewOperator? = null
//  private var counter = 0
//  private var pressTime = 0L
//  private var limit = 500L
//  private var onResumeCalled = false
//  private var onVideoPrepared = false
//
//
//  private val viewModel: StoryDisplayViewModel by viewModels()
//
//  override
//  fun getLayoutId() = R.layout.fragment_story_display
//
//  override
//  fun setBindingVariables() {
//    binding.viewModel = viewModel
//  }
//
//  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    super.onViewCreated(view, savedInstanceState)
//    storyDisplayVideo.useController = false
//    updateStory()
////    setUpUi()
//  }
//
//
//  override
//  fun setUpViews() {
//  }
//
//  override fun onAttach(context: Context) {
//    super.onAttach(context)
//    this.pageViewOperator = context as PageViewOperator
//  }
//
//  override fun onStart() {
//    super.onStart()
//    counter = restorePosition()
//  }
//
//  override fun onResume() {
//    super.onResume()
//    onResumeCalled = true
//    if (stories[counter].isVideo() && !onVideoPrepared) {
//      simpleExoPlayer?.playWhenReady = false
//      return
//    }
//
//    simpleExoPlayer?.seekTo(5)
//    simpleExoPlayer?.playWhenReady = true
//    if (counter == 0) {
//      storiesProgressView?.startStories()
//    } else {
//      // restart animation
//      counter = StoryDisplayActivity.progressState.get(arguments?.getInt(EXTRA_POSITION) ?: 0)
//      storiesProgressView?.startStories(counter)
//    }
//  }
//
//  override fun setupObservers() {
//  }
//
//  override fun onPause() {
//    super.onPause()
//    simpleExoPlayer?.playWhenReady = false
//    binding.storiesProgressView.abandon()
//  }
//
//  override fun onComplete() {
//    simpleExoPlayer?.release()
//    pageViewOperator?.nextPageView()
//  }
//
//  override fun onPrev() {
//    if (counter - 1 < 0) return
//    --counter
//    savePosition(counter)
//    updateStory()
//  }
//
//  override fun onNext() {
//    if (stories.size <= counter + 1) {
//      return
//    }
//    ++counter
//    savePosition(counter)
//    updateStory()
//  }
//
//  override fun onDestroyView() {
//    super.onDestroyView()
//    simpleExoPlayer?.release()
//  }
//
//  private fun updateStory() {
//    simpleExoPlayer?.stop()
//    pauseCurrentStory()
//    if (stories[counter].isVideo()) {
//      binding.storyDisplayVideo.show()
//      binding.storyDisplayImage.hide()
//      binding.storyDisplayVideoProgress.show()
//      initializePlayer()
//    } else {
//      binding.storyDisplayVideo.hide()
//      binding.storyDisplayVideoProgress.hide()
//      binding.storyDisplayImage.show()
//      Glide.with(requireContext())
//        .load(stories[counter].url)
//        .listener(object : RequestListener<Drawable> {
//
//          override fun onResourceReady(
//            resource: Drawable?,
//            model: Any?,
//            target: Target<Drawable>?,
//            dataSource: com.bumptech.glide.load.DataSource?,
//            isFirstResource: Boolean
//          ): Boolean {
//            resumeCurrentStory()
//            return false
//          }
//
//          override fun onLoadFailed(
//            e: GlideException?,
//            model: Any?,
//            target: Target<Drawable>?,
//            isFirstResource: Boolean
//          ): Boolean {
//            resumeCurrentStory()
//            return false
//          }
//        })
//        .into(binding.storyDisplayImage)
//
//
////      Glide.with(this).load(stories[counter].url).into(binding.storyDisplayImage)
//    }
//
//    val cal: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
//      timeInMillis = stories[counter].storyDate
//    }
//    binding.storyDisplayTime.text = DateFormat.format("MM-dd-yyyy HH:mm:ss", cal).toString()
//  }
//
//  private fun initializePlayer() {
//    if (simpleExoPlayer == null) {
//      simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
//    } else {
//      simpleExoPlayer?.release()
//      simpleExoPlayer = null
//      simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
//    }
//
//    mediaDataSourceFactory = CacheDataSourceFactory(
//      MyApplication.simpleCache,
//      DefaultHttpDataSourceFactory(
//        Util.getUserAgent(
//          context,
//          Util.getUserAgent(requireContext(), getString(R.string.app_name))
//        )
//      )
//    )
//    val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
//      Uri.parse(stories[counter].url)
//    )
//    simpleExoPlayer?.prepare(mediaSource, false, false)
//    if (onResumeCalled) {
//      simpleExoPlayer?.playWhenReady = true
//    }
//
//    binding.storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
//    binding.storyDisplayVideo.player = simpleExoPlayer
//
//    simpleExoPlayer?.addListener(object : Player.EventListener {
//      override fun onPlayerError(error: ExoPlaybackException?) {
//        super.onPlayerError(error)
//        binding.storyDisplayVideoProgress.hide()
//        if (counter == stories.size.minus(1)) {
//          pageViewOperator?.nextPageView()
//        } else {
//          binding.storiesProgressView.skip()
//        }
//      }
//
//      override fun onLoadingChanged(isLoading: Boolean) {
//        super.onLoadingChanged(isLoading)
//        if (isLoading) {
//          binding.storyDisplayVideoProgress.show()
//          pressTime = System.currentTimeMillis()
//          pauseCurrentStory()
//        } else {
//          binding.storyDisplayVideoProgress.hide()
//          binding.storiesProgressView.getProgressWithIndex(counter)
//            .setDuration(simpleExoPlayer?.duration ?: 8000L)
//          onVideoPrepared = true
//          resumeCurrentStory()
//        }
//      }
//    })
//  }
//
//  private fun setUpUi() {
//    val touchListener = object : OnSwipeTouchListener(requireActivity()) {
//      override fun onSwipeTop() {
//        Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
//      }
//
//      override fun onSwipeBottom() {
//        Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
//      }
//
//      override fun onClick(view: View) {
//        when (view) {
//          binding.next -> {
//            if (counter == stories.size - 1) {
//              pageViewOperator?.nextPageView()
//            } else {
//              binding.storiesProgressView.skip()
//            }
//          }
//          binding.previous -> {
//            if (counter == 0) {
//              pageViewOperator?.backPageView()
//            } else {
//              binding.storiesProgressView.reverse()
//            }
//          }
//        }
//      }
//
//      override fun onLongClick() {
//        hideStoryOverlay()
//      }
//
//      override fun onTouchView(view: View, event: MotionEvent): Boolean {
//        super.onTouchView(view, event)
//        when (event.action) {
//          MotionEvent.ACTION_DOWN -> {
//            pressTime = System.currentTimeMillis()
//            pauseCurrentStory()
//            return false
//          }
//          MotionEvent.ACTION_UP -> {
//            showStoryOverlay()
//            resumeCurrentStory()
//            return limit < System.currentTimeMillis() - pressTime
//          }
//        }
//        return false
//      }
//    }
//    binding.previous.setOnTouchListener(touchListener)
//    binding.next.setOnTouchListener(touchListener)
//
//    binding.storiesProgressView.setStoriesCountDebug(
//      stories.size, position = arguments?.getInt(EXTRA_POSITION) ?: -1
//    )
//    binding.storiesProgressView.setAllStoryDuration(4000L)
//    binding.storiesProgressView.setStoriesListener(this)
//
//    Glide.with(this).load(storyUser.profilePicUrl).circleCrop()
//      .into(binding.storyDisplayProfilePicture)
//    binding.storyDisplayNick.text = storyUser.username
//  }
//
//  private fun showStoryOverlay() {
//    binding.storyOverlay.animate()
//      .setDuration(100)
//      .alpha(1F)
//      .start()
//  }
//
//  private fun hideStoryOverlay() {
//
//    binding.storyOverlay.animate()
//      .setDuration(200)
//      .alpha(0F)
//      .start()
//  }
//
//  private fun savePosition(pos: Int) {
//    StoryDisplayActivity.progressState.put(position, pos)
//  }
//
//  private fun restorePosition(): Int {
//    return StoryDisplayActivity.progressState.get(position)
//  }
//
//  fun pauseCurrentStory() {
//    simpleExoPlayer?.playWhenReady = false
//    binding.storiesProgressView.pause()
//  }
//
//  fun resumeCurrentStory() {
//    if (onResumeCalled) {
//      simpleExoPlayer?.playWhenReady = true
//      showStoryOverlay()
//      binding.storiesProgressView.resume()
//    }
//  }
//
//  companion object {
//    private const val EXTRA_POSITION = "EXTRA_POSITION"
//    private const val EXTRA_STORY_USER = "EXTRA_STORY_USER"
//    fun newInstance(position: Int, story: StoryUser): StoryDisplayFragment {
//      return StoryDisplayFragment().apply {
//        arguments = Bundle().apply {
//          putInt(EXTRA_POSITION, position)
//          putParcelable(EXTRA_STORY_USER, story)
//        }
//      }
//    }
//  }
}