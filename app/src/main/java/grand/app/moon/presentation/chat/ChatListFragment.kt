package grand.app.moon.presentation.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentChatListBinding

@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding>() {


  private val viewModel: ChatViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_chat_list

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.callService()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override
  fun setUpViews() {
  }


  override fun setupObservers() {

  }


  private fun setRecyclerViewScrollListener() {

  }
}