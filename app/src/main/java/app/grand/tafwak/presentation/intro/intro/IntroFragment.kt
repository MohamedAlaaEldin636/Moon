package app.grand.tafwak.presentation.intro.intro

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView.BufferType.SPANNABLE
import androidx.fragment.app.viewModels
import com.structure.base_mvvm.R
import com.structure.base_mvvm.R.color
import app.grand.tafwak.presentation.auth.AuthActivity
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.getMyColor
import app.grand.tafwak.presentation.base.extensions.openActivityAndClearStack
import com.structure.base_mvvm.databinding.FragmentIntroBinding
import app.grand.tafwak.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>() {

  private val viewModel: IntroViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_intro

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
    setUpGuestButton()
  }

  private fun setUpGuestButton() {
    val finalMessage = "${resources.getString(R.string.explore_app)} ${resources.getString(R.string.continue_as_guest)}"

    val spanString = SpannableString(finalMessage)

    val clickableSpan = object : ClickableSpan() {
      override
      fun onClick(textView: View) {
        viewModel.setFirstTime(false)
        openHome()
      }
    }

    // Define my span
    spanString
      .setSpan(
        clickableSpan, finalMessage.indexOf(resources.getString(R.string.continue_as_guest)),
        finalMessage.indexOf(resources.getString(R.string.continue_as_guest)) + resources
          .getString(R.string.continue_as_guest).length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
      )

    // Set span color
    spanString.setSpan(
      ForegroundColorSpan(getMyColor(color.colorAccent)),
      finalMessage.indexOf(resources.getString(R.string.continue_as_guest)),
      finalMessage.indexOf(resources.getString(R.string.continue_as_guest)) + resources
        .getString(R.string.continue_as_guest).length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Set span style
    spanString.setSpan(
      StyleSpan(Typeface.BOLD),
      finalMessage.indexOf(resources.getString(R.string.continue_as_guest)),
      finalMessage.indexOf(resources.getString(R.string.continue_as_guest)) + resources
        .getString(R.string.continue_as_guest).length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Add underline to span
    spanString.setSpan(
      UnderlineSpan(),
      finalMessage.indexOf(resources.getString(R.string.continue_as_guest)),
      finalMessage.indexOf(resources.getString(R.string.continue_as_guest)) + resources
        .getString(R.string.continue_as_guest).length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    binding.btnGuestMode.movementMethod = LinkMovementMethod.getInstance()
    binding.btnGuestMode.setText(spanString, SPANNABLE)
    binding.btnGuestMode.isSelected = true
  }

  override
  fun setupObservers() {
    viewModel.openLogIn.observe(this) {
      viewModel.setFirstTime(false)
      openLogIn()
    }
  }

  private fun openLogIn() {
    openActivityAndClearStack(AuthActivity::class.java)
  }

  private fun openHome() {
    openActivityAndClearStack(HomeActivity::class.java)
  }
}