package grand.app.moon.presentation.myAds

import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentExploreBinding
import grand.app.moon.databinding.FragmentMyAdsBinding
import grand.app.moon.presentation.base.BaseFragment

@AndroidEntryPoint
class MyAdsFragment : BaseFragment<FragmentMyAdsBinding>()  {

	override fun getLayoutId(): Int = R.layout.fragment_my_ads

}
