package grand.app.moon.presentation.auth.language

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.auth.countries.viewModels.CountriesViewModel
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.databinding.FragmentCountriesBinding
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.language.LanguagesHelper
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.getSelectedLangBeforeThenReset
import grand.app.moon.databinding.FragmentLanguageBinding
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.navigateSafely
import grand.app.moon.presentation.auth.language.viewModels.LanguagesViewModel
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.splash.SplashActivity
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding>() {

  val languageFragmentArgs : LanguageFragmentArgs by navArgs()

  private val viewModel: LanguagesViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		/*if (requireContext().getSelectedLangBeforeThenReset()) {
			if (viewModel.languageNavArgs?.type == Constants.SPLASH) {
				findNavController().navigateSafely(
					LanguageFragmentDirections.actionLanguageFragmentToCountriesFragment2()
				)
			}else {
				activity?.openActivityAndClearStack(SplashActivity::class.java)
			}
		}*/

		/*if (viewModel.accountRepository.getKeyFromLocal(Constants.LANGUAGE_SELECTED_ON_APP_LAUNCH_BEFORE).toBooleanStrictOrNull() == true) {
			viewModel.accountRepository.saveKeyToLocal(Constants.LANGUAGE_SELECTED_ON_APP_LAUNCH_BEFORE, false.toString())

			// Shows language bs ha3mel eh ya3ne... m7tag more debugging which will take time isa.
			binding.root.post {
				findNavController().navigate(LanguageFragmentDirections.actionLanguageFragmentToCountriesFragment2())
			}
		}*/
	}

  override
  fun getLayoutId() = R.layout.fragment_language

  override
  fun setBindingVariables() {
    viewModel.languageNavArgs = languageFragmentArgs
    binding.viewModel = viewModel
  }

  private  val TAG = "LanguageFragment"
  override
  fun setupObservers() {
    viewModel.adapter.changeEvent.observe(this) { language ->
	    if (viewModel.lang != language.lang) {
		    viewModel.updateLanguageWithoutLocalSave(language.lang)
	    }
    }
	  viewModel.clickEvent.observe(this) {
		  if (viewModel.lang != viewModel.oldSetLang) {
			  viewModel.updateLanguageLocalSave(viewModel.lang)

			  context?.let { (activity as BaseActivity<*>).changeLanguage(it, viewModel.lang) }
			  (activity as BaseActivity<*>).changeLanguage(MyApplication.instance, viewModel.lang)

			  context?.let { (activity as BaseActivity<*>).changeLanguage(it, viewModel.lang) }
			  (activity as BaseActivity<*>).changeLanguage(MyApplication.instance, viewModel.lang)
//        viewModel.accountRepository.saveKeyToLocal(Constants.INTRO,"false")
			  LanguagesHelper.changeLanguage(MyApplication.instance.applicationContext, viewModel.lang)
			  LanguagesHelper.changeLanguage(MyApplication.instance, viewModel.lang)
			  LanguagesHelper.changeLanguage(MyApplication.instance.baseContext, viewModel.lang)

			  openActivityAndClearStack(SplashActivity::class.java)

			  return@observe
		  }

		  Log.d(TAG, "setupObservers: ")
		  findNavController().popBackStack()
	  }
  }

}