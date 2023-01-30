package grand.app.moon.presentation.story.storyView.screen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.ActivityStoryDisplayBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.story.viewModels.StoryDisplayViewModel
import pt.tornelas.segmentedprogressbar.SegmentedProgressBarListener

@AndroidEntryPoint
class StoryDisplayActivity : BaseActivity<ActivityStoryDisplayBinding>() ,
  SegmentedProgressBarListener {
  private val TAG = "StoryDisplayActivity"
  override
  fun getLayoutId() = R.layout.activity_story_display
  private val viewModel: StoryDisplayViewModel by viewModels()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.viewModel = viewModel
    observe()
    val bundle = intent.getBundleExtra(Constants.BUNDLE)
    viewModel.stories.addAll(bundle?.get(Constants.STORIES) as ArrayList<Store>)
    val position = bundle.getInt(Constants.POSITION)
    if(position < viewModel.stories.size)
    bundle.putSerializable(Constants.STORY,viewModel.stories[position])
    viewModel.store.set(viewModel.stories[viewModel.positionStoryAdapter])
    init()
  }


  private fun observe() {
    viewModel.clickEvent.observe(this,{
      when(it){
        Constants.EXIT -> finish()

      }
    })
  }

  override fun onDestroy() {
    // Very important !
    super.onDestroy()
  }

  fun pause() {
    viewModel.progress.set(true)
    binding.progress.pause()
  }

  fun resume() {
    Log.d(TAG, "resume: ${viewModel.isLoaded}")
    if(viewModel.isLoaded) {
      Log.d(TAG, "resume")
      viewModel.progress.set(false)
      binding.progress.start()
    }
  }

  fun init() {
    binding.progress.segmentCount = viewModel.store.get()?.stories?.size.orZero()
    binding.progress.listener = this
    binding.progress.start()
    binding.skip.setOnClickListener {
      Log.d(TAG, "init: YES")
      if(viewModel.allowNext())
        binding.progress.next()
    }
    binding.reverse.setOnClickListener {
      Log.d(TAG, "init: HEREEEEE")
      if(viewModel.allowPrev())
        binding.progress.previous()
    }

    binding.image.setOnTouchListener(object : View.OnTouchListener{
      override fun onTouch(p0: View?, event: MotionEvent?): Boolean {

        when(event?.action){
          MotionEvent.ACTION_DOWN -> pause()
          MotionEvent.ACTION_UP -> resume()
        }
        return true
      }

    })

    viewModel.clickEvent.observe(this,{
      when(it){
        Constants.EXIT ->  finish()
      }
    })
  }
  fun loadImage() {
    Log.d(TAG, "loadImage: here")

//    Log.d(TAG, "loadImage: ${viewModel.store.get()!!.stories[viewModel.pos].file}")
    viewModel.isLoaded = false
    pause()
    viewModel.image.set(viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.file.orEmpty())
    Glide.with(this)
      .load(viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.file.orEmpty())
      .listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<Drawable>?,
          isFirstResource: Boolean
        ): Boolean {
          Log.d(TAG, "onLoadFailed: ")
          viewModel.isLoaded = true
          resume()
          return false
        }

        override fun onResourceReady(
          resource: Drawable?,
          model: Any?,
          target: Target<Drawable>?,
          dataSource: com.bumptech.glide.load.DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          Log.d(TAG, "onResourceReady: READY")
          viewModel.isLoaded = true
          resume()
          return false
        }

      })
      .into(binding.image)

  }

  override fun onFinished() {
    viewModel.isFinish = true
    if(viewModel.nextStory()){
      reset()
    }else
      finish()

  }

  override fun onPage(oldPageIndex: Int, newPageIndex: Int) {
    Log.d(TAG, "onPageHere: $oldPageIndex , withNew $newPageIndex")
    viewModel.pos = newPageIndex
    viewModel.storyRequest.story_id = viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.id.orZero()
    Log.d(TAG, "onPage: ${viewModel.storyRequest.story_id}")
    viewModel.storyRequest.type = 1
    viewModel.callService()
    loadImage()
  }

  override fun onPause() {
    super.onPause()
    pause()
  }

  override fun onResume() {
    super.onResume()
    if(!viewModel.isFinish)
      resume()
    else {
      reset()
    }
  }

  fun reset(){
    binding.progress.reset()
    viewModel.pos = 0
    viewModel.isFinish = false
    binding.progress.segmentCount = viewModel.store.get()?.stories?.size.orZero()
    resume()
  }

}