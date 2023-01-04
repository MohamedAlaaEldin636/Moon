package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.compose.BaseTheme
import grand.app.moon.compose.ExtendedTheme
import grand.app.moon.databinding.FragmentAddAdvFinalPageBinding
import grand.app.moon.databinding.FragmentAddAdvSubCategoriesListBinding
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.categories.viewModel.AddAdvSubCategoriesListViewModel
import grand.app.moon.presentation.myAds.viewModel.AddAdvFinalPageViewModel

@AndroidEntryPoint
class AddAdvFinalPageFragment : BaseFragment<FragmentAddAdvFinalPageBinding>()  {

	private val viewModel by viewModels<AddAdvFinalPageViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_add_adv_final_page

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
			setContent {
				BaseTheme {
					//AddAdvFinalPageScreen()
					Column(Modifier.fillMaxSize().background(ExtendedTheme.colors.borderTextField)) {

					}
				}
			}
		}
	}

	/*override fun setBindingVariables() {
		binding.viewModel = viewModel
	}*/

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		//super.onViewCreated(view, savedInstanceState)
	}

}
