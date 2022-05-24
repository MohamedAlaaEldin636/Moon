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
import grand.app.moon.presentation.story.view.ViewPagerAdapter
import grand.app.moon.presentation.story.viewModels.StoryDisplayViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*

@AndroidEntryPoint
class StoryDisplayActivity : BaseActivity<ActivityStoryDisplayBinding>(){
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
//    if(viewModel.stories.isNotEmpty()) {
//      viewModel.pos = intent.getBundleExtra(Constants.BUNDLE)?.getInt(Constants.POSITION)!!
//      viewModel.store.set(viewModel.stories[viewModel.pos])
//    }

    binding.pager.adapter  = ViewPagerAdapter(this,viewModel.stories.size,bundle)

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

  fun nextStory() {
    if(binding.pager.currentItem < viewModel.stories.size - 1)
      binding.pager.currentItem = binding.pager.currentItem + 1
    else
      finish()
  }

//  override fun done() {
//    when(viewModel.isFinish()){
//      true -> finish()
//      else -> {
//        prepareStories()
//        startStory()
//      }
//    }
//  }
//
//
//  override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
//    viewModel.index = index
//    viewModel.storyRequest.story_id = viewModel.store.get()!!.stories[index].id
//    viewModel.storyRequest.type = 1
//    viewModel.callService()
//    loadImage(view as AppCompatImageView,momentz,index)
//  }

}