package grand.app.moon.presentation.myAds.utils

import company.tap.gosellapi.internal.api.callbacks.GoSellError
import company.tap.gosellapi.internal.api.models.Authorize
import company.tap.gosellapi.internal.api.models.Charge
import company.tap.gosellapi.internal.api.models.Token
import company.tap.gosellapi.open.delegate.SessionDelegate
import company.tap.gosellapi.open.enums.CardType
import company.tap.gosellapi.open.models.CardsList
import grand.app.moon.core.GoSellSDKUtils
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.toHashMap
import grand.app.moon.presentation.myAds.MakeAdvOrStorePremiumFragment
import grand.app.moon.presentation.myAds.viewModel.MakeAdvOrStorePremiumViewModel
import java.math.BigDecimal

private const val META_DATA_PACKAGE_ID = "META_DATA_PACKAGE_ID"
private const val META_DATA_PACKAGE_IS_AD_NOT_STORE = "META_DATA_PACKAGE_IS_AD_NOT_STORE"
private const val META_DATA_PACKAGE_AD_ID_OR_EMPTY = "META_DATA_PACKAGE_AD_ID_OR_EMPTY"

fun MakeAdvOrStorePremiumViewModel.startPayment(
	fragment: MakeAdvOrStorePremiumFragment,
	sessionDelegate: SessionDelegate,
	amount: BigDecimal,
	packageId: Int,
	isAdNotStore: Boolean,
	adIdOrNullIfStore: Int?,
) {
	val activity = fragment.activity ?: return

	val currency = repoShop.getSelectedCountry()?.englishCurrencyIsoCode ?: return
	if (currency.isEmpty()) return

	GoSellSDKUtils.configureSDKSessionAndStartIt(
		activity,
		sessionDelegate,
		currency,
		amount,
		CardType.ALL,
		mutableMapOf(
			META_DATA_PACKAGE_ID to packageId.toString(),
			META_DATA_PACKAGE_IS_AD_NOT_STORE to isAdNotStore.toString(),
			META_DATA_PACKAGE_AD_ID_OR_EMPTY to adIdOrNullIfStore.toString(),
		).toHashMap(),
		userLocalUseCase
	)
}

class ISessionDelegate(
	private val appPreferences: AppPreferences,
	private val onPaymentSuccess: () -> Unit,
	private val onPaymentFailed: () -> Unit,
) : SessionDelegate {
	override fun paymentSucceed(charge: Charge) {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> paymentSucceed $charge")

		appPreferences.savePaymentSuccess(charge.id.orEmpty(), charge.metadata.toMap())

		onPaymentSuccess()
	}

	override fun paymentFailed(charge: Charge?) {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> paymentFailed $charge")

		onPaymentFailed()
	}

	override fun authorizationSucceed(authorize: Authorize) {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> authorizationSucceed $authorize")
	}

	override fun authorizationFailed(authorize: Authorize?) {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> authorizationFailed $authorize")
	}

	override fun cardSaved(charge: Charge) {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> cardSaved $charge")
	}

	override fun cardSavingFailed(charge: Charge) {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> cardSavingFailed $charge")
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
	}

	private fun GoSellError?.toSpecialString(): String {
		return if (this == null) "null" else "$errorCode $errorMessage $errorBody"
	}

	override fun sessionIsStarting() {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> sessionIsStarting")
	}

	override fun sessionHasStarted() {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> sessionHasStarted")
	}

	override fun sessionCancelled() {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> sessionCancelled")
	}

	override fun sessionFailedToStart() {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> sessionFailedToStart")
	}

	override fun invalidCardDetails() {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> invalidCardDetails")
	}

	override fun backendUnknownError(message: String?) {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> backendUnknownError $message")

		onPaymentFailed()
	}

	override fun invalidTransactionMode() {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> invalidTransactionMode")
	}

	override fun invalidCustomerID() {
		MyLogger.e("TAP PAYMENT GO SELL SDK -> invalidCustomerID")
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
