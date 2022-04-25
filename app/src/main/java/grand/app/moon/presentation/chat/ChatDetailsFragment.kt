package grand.app.moon.presentation.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.cometchat.pro.constants.CometChatConstants.RECEIVER_TYPE_USER
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageList
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentChatDetailsBinding
import grand.app.moon.databinding.FragmentChatListBinding
import grand.app.moon.presentation.home.HomeFragment

@AndroidEntryPoint
class ChatDetailsFragment : BaseFragment<FragmentChatDetailsBinding>() {


  private val viewModel: ChatViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_chat_details

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
    val bundle = Bundle()
    bundle.putString(UIKitConstants.IntentStrings.AVATAR, "AvatarUrl")
    bundle.putString(UIKitConstants.IntentStrings.NAME,"Name")
    bundle.putString(UIKitConstants.IntentStrings.TYPE,"user")
    bundle.putString(UIKitConstants.IntentStrings.UID,"store_4")
    bundle.putString(UIKitConstants.IntentStrings.STATUS,"STATUS")
    val fragment = CometChatMessageList()
    fragment.arguments = bundle
    requireActivity().supportFragmentManager.putFragment(bundle,"chat_details",fragment)
    requireActivity().supportFragmentManager.commit {

    }
//    requireActivity().supportFragmentManager.commit {
//      setReorderingAllowed(true)
//      fragment
//    }
  }


  override fun setupObservers() {

  }


  private fun setRecyclerViewScrollListener() {

  }
}