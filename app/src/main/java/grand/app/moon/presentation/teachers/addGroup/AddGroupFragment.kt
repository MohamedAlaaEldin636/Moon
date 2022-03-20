package grand.app.moon.presentation.teachers.addGroup

import androidx.fragment.app.viewModels
import grand.app.moon.presentation.base.utils.Constants
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentAddGroupBinding
import grand.app.moon.presentation.teachers.addGroup.viewModels.AddGroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupFragment : BaseFragment<FragmentAddGroupBinding>() {

  private val viewModel: AddGroupViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_add_group

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
    setUpToolBar()
  }

  private fun setUpToolBar() {
//    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.settings)
//    binding.includedToolbar.backIv.hide()
  }

  override fun setupObservers() {
    viewModel.clickEvent.observeForever {
      if (it == Constants.REVIEWS)
        toReviews()
    }
  }

  fun toReviews() {
//    navigateSafe(TeacherProfileFragmentDirections.actionToReviewsFragment())
  }
}