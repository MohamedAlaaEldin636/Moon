package app.grand.tafwak.presentation.exams.studentArrangements

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentStudentsArrangementsBinding
import app.grand.tafwak.presentation.exams.studentArrangements.viewModels.StudentArrangementsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentsArrangementsFragment : BaseFragment<FragmentStudentsArrangementsBinding>() {

  private val viewModel: StudentArrangementsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_students_arrangements

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun setUpViews() {
    super.setUpViews()
    setUpToolBar()
  }

  private fun setUpToolBar() {
//    binding.includedToolbar.toolbarTitle.text = getMyString(R.string.privacy)
//    binding.includedToolbar.backIv.show()
//    binding.includedToolbar.backIv.setOnClickListener { backToPreviousScreen()}
  }
}