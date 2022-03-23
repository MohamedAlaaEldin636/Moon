package grand.app.moon.presentation.myAccount

import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentMyAccountBinding
import grand.app.moon.presentation.more.MoreItem
import java.util.ArrayList

@AndroidEntryPoint
class MyAccountFragment : BaseFragment<FragmentMyAccountBinding>() {


  private val viewModel: MyAccountViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_my_account

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    val moreItems = ArrayList<MoreItem>().also { list ->
      list.add(
        MoreItem(
          getString(R.string.log_in),
          getMyDrawable(R.drawable.ic_login),
          "https://google.com"
        )
      )
    }
    viewModel.moreAdapter.differ.submitList(moreItems)

  }

  private val TAG = "MoreFragment"

}