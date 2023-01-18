package grand.app.moon.presentation.myStore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentCreateStoreBinding
import grand.app.moon.extensions.navUpThenSetResultInBackStackEntrySavedStateHandleViaGson
import grand.app.moon.extensions.setResultInPreviousBackStackEntrySavedStateHandleViaGson
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.viewModel.CreateStoreViewModel

@AndroidEntryPoint
class CreateStoreFragment : BaseFragment<FragmentCreateStoreBinding>() {

	private val viewModel by viewModels<CreateStoreViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_create_store

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


	}

}
