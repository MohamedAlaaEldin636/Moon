package grand.app.moon.presentation.auth.language

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
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
import grand.app.moon.databinding.FragmentLanguageBinding
import grand.app.moon.presentation.auth.language.viewModels.LanguagesViewModel
import grand.app.moon.presentation.base.BaseActivity
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.splash.SplashActivity
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding>() {

  val languageFragmentArgs : LanguageFragmentArgs by navArgs()

  private val viewModel: LanguagesViewModel by viewModels()

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
    viewModel.adapter.changeEvent.observe(this, { language ->
      if(viewModel.lang != language.lang){
        viewModel.updateLanguage(language.lang)

        context?.let { (activity as BaseActivity<*>).changeLanguage(it,viewModel.lang) }
        (activity as BaseActivity<*>).changeLanguage(MyApplication.instance,viewModel.lang)

        context?.let { (activity as BaseActivity<*>).changeLanguage(it,viewModel.lang) }
        (activity as BaseActivity<*>).changeLanguage(MyApplication.instance,viewModel.lang)
//        viewModel.accountRepository.saveKeyToLocal(Constants.INTRO,"false")
        LanguagesHelper.changeLanguage(MyApplication.instance.applicationContext,viewModel.lang)
        LanguagesHelper.changeLanguage(MyApplication.instance,viewModel.lang)
        LanguagesHelper.changeLanguage(MyApplication.instance.baseContext,viewModel.lang)

        openActivityAndClearStack(SplashActivity::class.java)
      }
    })
    viewModel.clickEvent.observe(this,{
      Log.d(TAG, "setupObservers: ")
      findNavController().popBackStack()
    })
  }

}