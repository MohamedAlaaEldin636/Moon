package grand.app.moon.presentation.department

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentDepartmentBinding
import grand.app.moon.presentation.department.viewModels.DepartmentListViewModel

@AndroidEntryPoint
class DepartmentListFragment : BaseFragment<FragmentDepartmentBinding>() {

  private val viewModel: DepartmentListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_department

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    binding.edtSearchDepartment.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
      }
      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
      }
      override fun afterTextChanged(text: Editable?) {
        viewModel.search(text.toString())
      }

    })
  }


  override fun setupObservers() {
  }
}