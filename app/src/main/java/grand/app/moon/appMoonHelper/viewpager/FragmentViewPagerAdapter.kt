package grand.app.moon.appMoonHelper.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.story.view.StoryFragment

class FragmentViewPagerAdapter(fragment: Fragment ,val bundle: Bundle) :  FragmentStateAdapter(fragment) {

  override fun getItemCount(): Int = 100

  override fun createFragment(position: Int): Fragment {
    // Return a NEW fragment instance in createFragment(int)
    val fragment = StoryFragment()
    fragment.arguments = bundle
    return fragment
  }
}