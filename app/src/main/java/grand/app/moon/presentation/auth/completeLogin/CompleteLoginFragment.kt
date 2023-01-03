package grand.app.moon.presentation.auth.completeLogin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAddAdvSubCategoriesListBinding
import grand.app.moon.databinding.FragmentCompleteLoginBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.showSnackbarWithAction
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.categories.viewModel.AddAdvSubCategoriesListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompleteLoginFragment : BaseFragment<FragmentCompleteLoginBinding>()  {

	private val viewModel by viewModels<CompleteLoginViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_complete_login

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

}

