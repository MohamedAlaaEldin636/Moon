package app.grand.tafwak.presentation.subjects

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.backToPreviousScreen
import app.grand.tafwak.presentation.base.extensions.getMyString
import app.grand.tafwak.presentation.base.extensions.show
import com.structure.base_mvvm.databinding.FragmentStudySubjectsBinding
import app.grand.tafwak.presentation.subjects.viewModels.SubjectsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubjectsFragment : BaseFragment<FragmentStudySubjectsBinding>() {

  private val viewModel: SubjectsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_study_subjects

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setUpViews() {
    super.setUpViews()
    setUpToolBar()
  }

  private fun setUpToolBar() {
    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.privacy)
    binding.includedToolbar.backIv.show()
    binding.includedToolbar.backIv.setOnClickListener { backToPreviousScreen()}
  }
}