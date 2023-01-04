package grand.app.moon.presentation.auth.completeLogin

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.fromJsonInlinedOrNull
import grand.app.moon.extensions.showSnackbarWithAction
import grand.app.moon.presentation.base.extensions.makeIntegrationWithRedirectHome
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class CompleteLoginViewModel @Inject constructor(
	args: CompleteLoginFragmentArgs,
	private val useCaseLogIn: LogInUseCase,
	accountRepository: AccountRepository
) : ViewModel() {

	val user = args.jsonUser.fromJsonInlinedOrNull<User>().apply {
		MyLogger.e("hello 12333 -> 2 -> $this")
	}

	val name = MutableLiveData("")

	/** Ex. +20 01016171926 */
	val phone = MutableLiveData("${user?.country_code} ${user?.phone}")

	val countryImage = MutableLiveData(accountRepository.getKeyFromLocal(Constants.COUNTRY_IMAGE))

	fun login(view: View) {
		val fragment = view.findFragmentOrNull<CompleteLoginFragment>() ?: return

		val user = user ?: return fragment.showMessage(fragment.getString(R.string.something_went_wrong_please_try_again))

		if (name.value.isNullOrEmpty()) {
			return fragment.showMessage(fragment.getString(R.string.fill_this_field))
		}

		viewModelScope.launch {
			when (val resource = useCaseLogIn.updateProfile(name.value.orEmpty(), user)) {
				is Resource.Success -> {
					if (resource.value.data == null) {
						return@launch fragment.showMessage(fragment.getString(R.string.something_went_wrong_please_try_again))
					}

					fragment.makeIntegrationWithRedirectHome(user.id)
				}
				is Resource.Failure -> {
					fragment.binding.root.showSnackbarWithAction(
						resource
					) {
						this@CompleteLoginViewModel.login(view)
					}
				}
				else -> {}
			}
		}
	}

}
