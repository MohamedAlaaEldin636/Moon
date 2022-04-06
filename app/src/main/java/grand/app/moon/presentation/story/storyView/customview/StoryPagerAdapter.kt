package grand.app.moon.presentation.story.storyView.customview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import grand.app.moon.presentation.story.storyView.data.StoryUser
import grand.app.moon.presentation.story.storyView.screen.StoryDisplayFragment

class StoryPagerAdapter constructor(fragmentManager: FragmentManager, private val storyList: ArrayList<StoryUser>)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  override fun getCount(): Int {
    TODO("Not yet implemented")
  }

  override fun getItem(position: Int): Fragment {
    TODO("Not yet implemented")
  }

//    override fun getItem(position: Int): Fragment = StoryDisplayFragment.newInstance(position, storyList[position])
//
//    override fun getCount(): Int {
//        return storyList.size
//    }
//
//    fun findFragmentByPosition(viewPager: ViewPager, position: Int): Fragment? {
//        try {
//            val f = instantiateItem(viewPager, position)
//            return f as? Fragment
//        } finally {
//            finishUpdate(viewPager)
//        }
//    }


}