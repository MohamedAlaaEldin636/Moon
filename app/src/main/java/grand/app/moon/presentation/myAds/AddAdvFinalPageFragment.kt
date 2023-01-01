package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAddAdvFinalPageBinding
import grand.app.moon.databinding.FragmentAddAdvSubCategoriesListBinding
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.categories.viewModel.AddAdvSubCategoriesListViewModel
import grand.app.moon.presentation.myAds.viewModel.AddAdvFinalPageViewModel

@AndroidEntryPoint
class AddAdvFinalPageFragment : BaseFragment<FragmentAddAdvFinalPageBinding>()  {

	private val viewModel by viewModels<AddAdvFinalPageViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_add_adv_final_page

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
	}

}
