package grand.app.moon.presentation.story.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.story.view.StoryFragment

class ViewPagerAdapter(fragment: FragmentActivity, val size : Int, val bundle: Bundle) :  FragmentStateAdapter(fragment) {

  override fun getItemCount(): Int = size

  override fun createFragment(position: Int): Fragment {
    // Return a NEW fragment instance in createFragment(int)
    val fragment = StoryFragment()
    fragment.arguments = bundle
    return fragment
  }
}