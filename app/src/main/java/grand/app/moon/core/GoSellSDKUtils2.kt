package grand.app.moon.core

import androidx.fragment.app.Fragment
import company.tap.gosellapi.internal.api.callbacks.GoSellError
import company.tap.gosellapi.internal.api.models.Authorize
import company.tap.gosellapi.internal.api.models.Charge
import company.tap.gosellapi.internal.api.models.Token
import company.tap.gosellapi.open.delegate.SessionDelegate
import company.tap.gosellapi.open.enums.CardType
import company.tap.gosellapi.open.models.CardsList
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.MyLogger
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import java.lang.ref.WeakReference
import java.math.BigDecimal

object GoSellSDKUtils2 {

	fun startPayment(
		fragment: BaseFragment<*>,
		appPreferences: AppPreferences,
		currencyIsoCode: String,
		amount: BigDecimal,
		userLocalUseCase: UserLocalUseCase,
		metaData: HashMap<String, String>,
		cardType: CardType = CardType.ALL,
		onPaymentFailed: (() -> Unit)? = null,
		onPaymentSuccess: (onSuccess: () -> Unit) -> Unit,
	) {
		val activity = fragment.activity ?: return

		fragment.showLoading()

		val session = ISessionDelegate(
			fragment,
			appPreferences,
			onPaymentFailed,
			onPaymentSuccess
		)

		GoSellSDKUtils.configureSDKSessionAndStartIt(
			activity,
			session,
			currencyIsoCode,
			amount,
			cardType,
			metaData,
			userLocalUseCase
		)
	}

	private class ISessionDelegate(
		fragment: BaseFragment<*>,
		private val appPreferences: AppPreferences,
		private val onPaymentFailed: (() -> Unit)? = null,
		private val onPaymentSuccess: (onSuccess: () -> Unit) -> Unit,
	) : SessionDelegate {
		private val weakRefFragment = WeakReference(fragment)

		@Suppress("unused")
		private fun WeakReference<BaseFragment<*>>.showErrorMsg(msg: String) {
			weakRefFragment.get()?.showMessage(msg)
		}

		override fun paymentSucceed(charge: Charge) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> paymentSucceed $charge")

			appPreferences.savePaymentSuccess(charge.id.orEmpty(), charge.metadata.toMap())

			onPaymentSuccess {
				appPreferences.clearPaymentSuccess()
			}
		}

		override fun paymentFailed(charge: Charge?) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> paymentFailed $charge")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("payment failed")
		}

		override fun authorizationSucceed(authorize: Authorize) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> authorizationSucceed $authorize")
		}

		override fun authorizationFailed(authorize: Authorize?) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> authorizationFailed $authorize")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("authorization failed")
		}

		override fun cardSaved(charge: Charge) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> cardSaved $charge")
		}

		override fun cardSavingFailed(charge: Charge) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> cardSavingFailed $charge")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("card saving failed")
		}

		override fun cardTokenizedSuccessfully(token: Token) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> cardTokenizedSuccessfully $token")
		}

		override fun cardTokenizedSuccessfully(token: Token, saveCardEnabled: Boolean) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> cardTokenizedSuccessfully 2 params $token $saveCardEnabled")
		}

		override fun savedCardsList(cardsList: CardsList) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> savedCardsList $cardsList")
		}

		override fun sdkError(goSellError: GoSellError?) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> sdkError ${goSellError?.toSpecialString()}")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("sdk error ${goSellError.toSpecialString()}")
		}

		private fun GoSellError?.toSpecialString(): String {
			return if (this == null) "null" else "$errorCode $errorMessage $errorBody"
		}

		override fun sessionIsStarting() {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> sessionIsStarting")
		}

		override fun sessionHasStarted() {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> sessionHasStarted")

			weakRefFragment.get()?.also { fragment ->
				kotlin.runCatching {
					fragment.view?.post {
						fragment.hideLoading()
					}
				}
			}
		}

		override fun sessionCancelled() {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> sessionCancelled")
		}

		override fun sessionFailedToStart() {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> sessionFailedToStart")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("session failed to start")
		}

		override fun invalidCardDetails() {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> invalidCardDetails")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("invalid card details")
		}

		override fun backendUnknownError(message: String?) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> backendUnknownError $message")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("backend unknown error $message")
		}

		override fun invalidTransactionMode() {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> invalidTransactionMode")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("invalid transaction mode")
		}

		override fun invalidCustomerID() {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> invalidCustomerID")

			onPaymentFailed?.invoke() ?: weakRefFragment.showErrorMsg("invalid customer ID")
		}

		override fun userEnabledSaveCardOption(saveCardEnabled: Boolean) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> userEnabledSaveCardOption $saveCardEnabled")
		}

		override fun asyncPaymentStarted(charge: Charge) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> asyncPaymentStarted $charge")
		}

		override fun paymentInitiated(charge: Charge?) {
			MyLogger.e("TAP PAYMENT GO SELL SDK -> paymentInitiated $charge")
		}
	}

}
