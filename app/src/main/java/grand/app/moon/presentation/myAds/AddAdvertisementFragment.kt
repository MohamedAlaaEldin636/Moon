package grand.app.moon.presentation.myAds

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAddAdvertisementBinding
import grand.app.moon.databinding.FragmentMyAdsBinding
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.viewModel.AddAdvertisementViewModel

@AndroidEntryPoint
class AddAdvertisementFragment : BaseFragment<FragmentAddAdvertisementBinding>()  {

	private val viewModel by viewModels<AddAdvertisementViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_add_advertisement

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}
