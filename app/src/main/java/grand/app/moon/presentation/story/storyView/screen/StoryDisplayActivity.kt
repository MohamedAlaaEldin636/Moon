package grand.app.moon.presentation.story.storyView.screen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import com.bolaware.viewstimerstory.Momentz
import com.bolaware.viewstimerstory.MomentzCallback
import com.bolaware.viewstimerstory.MomentzView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.denzcoskun.imageslider.interfaces.TouchListener
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.OnSwipeTouchListener
import grand.app.moon.databinding.ActivityStoryDisplayBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.generated.callback.OnClickListener
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.story.viewModels.StoryDisplayViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*

@AndroidEntryPoint
class StoryDisplayActivity : BaseActivity<ActivityStoryDisplayBinding>(),
  MomentzCallback{
  override
  fun getLayoutId() = R.layout.activity_story_display
  private val viewModel: StoryDisplayViewModel by viewModels()
  lateinit var momentz:Momentz
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.viewModel = viewModel
    observe()
    viewModel.stories.addAll(intent.getBundleExtra(Constants.BUNDLE)?.get(Constants.STORIES) as ArrayList<Store>)
    if(viewModel.stories.isNotEmpty()) {
      viewModel.pos = intent.getBundleExtra(Constants.BUNDLE)?.getInt(Constants.POSITION)!!
      viewModel.store.set(viewModel.stories[viewModel.pos])
      prepareStories()
    }
    startStory()
  }

  private fun prepareStories() {
    viewModel.store.get()!!.stories.forEach { story ->
      val locallyLoadedImageView = AppCompatImageView(this)
      locallyLoadedImageView.scaleType = ImageView.ScaleType.FIT_CENTER
      viewModel.listStories.add(MomentzView(locallyLoadedImageView,5))
    }
  }

  fun startStory(){
    if(this::momentz.isInitialized) momentz.removeAllViews()
    momentz = Momentz(this, viewModel.listStories, binding.container, this)
    momentz.start()
  }

  private fun observe() {
    viewModel.clickEvent.observe(this,{
      when(it){
        Constants.SHARE_IMAGE -> {
          viewModel.storyRequest.type = 3
          viewModel.callService()
          viewModel.share(this,viewModel.store.get()!!.name,"", binding.imgShare)
        }
        Constants.EXIT -> finish()
        Constants.STORE_DETAILS -> {

        }
      }
    })
  }

  private  val TAG = "StoryDisplayActivity"
  fun loadImage(view: AppCompatImageView?, momentz: Momentz?,index: Int) {
    if (index >= 0 && index < viewModel.store.get()!!.stories.size) {
      momentz?.pause(false)
      viewModel.startLoading()
      viewModel.image.set(viewModel.store.get()!!.stories[index].file)
      view?.let {
        Glide.with(applicationContext)
          .load(viewModel.store.get()!!.stories[index].file)
          .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
              e: GlideException?,
              model: Any?,
              target: Target<Drawable>?,
              isFirstResource: Boolean
            ): Boolean {
              momentz?.resume()
              viewModel.stopLoading()
              return false
            }

            override fun onResourceReady(
              resource: Drawable?,
              model: Any?,
              target: Target<Drawable>?,
              dataSource: com.bumptech.glide.load.DataSource?,
              isFirstResource: Boolean
            ): Boolean {
              viewModel.stopLoading()
              momentz?.resume()
              return false
            }

          })
          .into(it)
      }

    } else finish()
  }

  override fun onDestroy() {
    // Very important !
    super.onDestroy()
  }

  override fun done() {
    when(viewModel.isFinish()){
      true -> finish()
      else -> {
        prepareStories()
        startStory()
      }
    }
  }


  override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
    viewModel.index = index
    viewModel.storyRequest.story_id = viewModel.store.get()!!.stories[index].id
    viewModel.storyRequest.type = 1
    viewModel.callService()
    loadImage(view as AppCompatImageView,momentz,index)
  }

}