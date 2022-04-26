package grand.app.moon.presentation.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.cometchat.pro.models.Conversation
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentChatListBinding
import com.cometchat.pro.uikit.ui_components.users.user_list.CometChatUserList
import com.cometchat.pro.uikit.ui_components.users.user_list.CometChatUserList.Companion.setItemClickListener
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener
import com.google.gson.Gson
import grand.app.moon.core.extenstions.startChatPage





@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding>() {


  private val viewModel: ChatViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_chat_list

  private val TAG = "ChatListFragment"
  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.callService()
    CometChatConversationList.setItemClickListener(object : OnItemClickListener<Any>() {
      override fun OnItemClick(t: Any, position: Int) {
        val conversation = t as Conversation
        val user = conversation.conversationWith as User
        requireContext().startChatPage(requireView(),user)
      }
    })
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override
  fun setUpViews() {
  }
}