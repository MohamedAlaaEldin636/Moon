package app.grand.tafwak.presentation.teachers.studentProfile

import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.BaseFragment
import com.structure.base_mvvm.databinding.FragmentGroupStudentProfileBinding
import app.grand.tafwak.presentation.teachers.studentProfile.viewModels.GroupStudentProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupStudentProfileFragment : BaseFragment<FragmentGroupStudentProfileBinding>() {

  private val viewModel: GroupStudentProfileViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_group_student_profile

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
}