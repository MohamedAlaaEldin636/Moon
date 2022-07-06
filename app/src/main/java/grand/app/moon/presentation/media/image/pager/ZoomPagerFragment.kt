package grand.app.moon.presentation.media.image.pager

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentZoomPagerBinding
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.media.image.ZoomFragment
import grand.app.moon.presentation.media.image.ZoomViewModel
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ZoomPagerFragment : BaseFragment<FragmentZoomPagerBinding>() {

  private val viewModel: ZoomViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_zoom_pager

  override
  fun setBindingVariables() {
    if(requireArguments().containsKey("images")) {
      val gson = GsonBuilder().create()
      val list = gson.fromJson<ArrayList<String>>(requireArguments().getString("images"), object :
        TypeToken<ArrayList<String>>() {}.type)
      val adapter = ScreenSlidePagerAdapter(requireActivity(),list)
      binding.pager.adapter = adapter
      viewModel.setImages(list)
      lifecycleScope.launchWhenResumed {
        delay(100)
        binding.pager.currentItem = requireArguments().getInt("position")
      }
      binding.viewModel = viewModel
    }
  }

  private inner class ScreenSlidePagerAdapter(fa: FragmentActivity,val images: ArrayList<String>) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = images.size

    override fun createFragment(position: Int): Fragment {
      val fragment = ZoomFragment()
      fragment.arguments = bundleOf(Constants.IMAGE to images[position])
      return fragment
    }
  }

  override fun onStop() {
    super.onStop()
  }

  override fun onPause() {
    super.onPause()
  }

}