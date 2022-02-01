package app.grand.tafwak.presentation.teachers.studentRequests

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentStudentRequestDetailsBinding
import app.grand.tafwak.presentation.teachers.studentRequests.viewModels.TeacherStudentRequestDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherStudentRequestDetailsFragment : BaseFragment<FragmentStudentRequestDetailsBinding>() {

  private val viewModel: TeacherStudentRequestDetailsViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_student_request_details

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
//    viewModel.clickEvent.observeForever {
//      if (it == Constants.REVIEWS)
//
//    }
  }
}