package grand.app.moon.core

import android.graphics.Typeface
import company.tap.gosellapi.GoSellSDK
import company.tap.gosellapi.open.controllers.SDKSession
import company.tap.gosellapi.open.controllers.ThemeObject
import company.tap.gosellapi.open.enums.AppearanceMode
import grand.app.moon.core.GoSellSDKUtils.performBeforeAnyUsageSetups

@Suppress("UsePropertyAccessSyntax")
object GoSellSDKUtils {

	private var sdkSession: SDKSession? = null

	fun beforeAnyLaunchSetups(application: MyApplication, language: String = "en") {
		// TODO shel el if condition da isa.
		if (true) return
		application.performBeforeAnyUsageSetups(language)
	}

	fun changeLanguageAfterSetupsAndBeforeUsage(language: String) {
		GoSellSDK.setLocale(language)

		ThemeObject.getInstance()
			.setSdkLanguage(language)
	}

	/**
	 * Setups here [Link](https://github.com/Tap-Payments/goSellSDK-androidX)
	 */
	private fun MyApplication.performBeforeAnyUsageSetups(language: String) {
		/**
		 * Required step.
		 * Configure SDK with your Secret API key and App Bundle name registered with tap company.
		 */
		configureApp(language)

		/**
		 * Optional step
		 * Here you can configure your app theme (Look and Feel).
		 */
		configureSDKThemeObject(language)

		/**
		 * Required step.
		 * Configure SDK Session with all required data.
		 */
		configureSDKSession()

		/**
		 * Required step.
		 * Choose between different SDK modes
		 */
		//configureSDKMode()
		// TODO LESSAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

		/**
		 * If you included Tap Pay Button then configure it first, if not then ignore this step.
		 */
		//initPayButton()
		// TODO LESSAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
	}

	private fun MyApplication.configureApp(language: String) {
		//sk_test_jeAIVETRHKpdgu6lOraPtsXm -> Android -> sk_live_5JpXIsZ2CuKrBv67nV1wF9EO
		//sk_test_gdfGYQrI158pKJSmPEV4CtvN -> IOS
		GoSellSDK.init(this, "sk_test_jeAIVETRHKpdgu6lOraPtsXm", packageName)
		GoSellSDK.setLocale(language) //  if you dont pass locale then default locale EN will be used
		// todo above init change above authToken(Secret API Key) with what will be given from Alaa
		// todo setLocale before you enter any payment afdal bythaya2le isa.
	}

	private fun configureSDKThemeObject(language: String) {
		ThemeObject.getInstance()
			// set Appearance mode [Full Screen Mode - Windowed Mode]
			.setAppearanceMode(AppearanceMode.WINDOWED_MODE) // **Required**
			.setSdkLanguage(language) //if you dont pass locale then default locale EN will be used

			// Setup header font type face **Make sure that you already have asset folder with required fonts**
			//.setHeaderFont(Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf"))//**Optional**

			//Setup header text color
			//.setHeaderTextColor(getResources().getColor(R.color.black1))  // **Optional**

			// Setup header text size
			//.setHeaderTextSize(17) // **Optional**

			// setup header background
			//.setHeaderBackgroundColor(getResources().getColor(R.color.french_gray_new))//**Optional**

			// setup card form input font type
			//.setCardInputFont(Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf"))//**Optional**

			// setup card input field text color
			//.setCardInputTextColor(getResources().getColor(R.color.black))//**Optional**

			// setup card input field text color in case of invalid input
			//.setCardInputInvalidTextColor(getResources().getColor(R.color.red))//**Optional**

			// setup card input hint text color
			//.setCardInputPlaceholderTextColor(getResources().getColor(R.color.black))//**Optional**

			// setup Switch button Thumb Tint Color in case of Off State
			//.setSaveCardSwitchOffThumbTint(getResources().getColor(R.color.gray)) // **Optional**

			// setup Switch button Thumb Tint Color in case of On State
			//.setSaveCardSwitchOnThumbTint(getResources().getColor(R.color.vibrant_green)) // **Optional**

			// setup Switch button Track Tint Color in case of Off State
			//.setSaveCardSwitchOffTrackTint(getResources().getColor(R.color.gray)) // **Optional**

			// setup Switch button Track Tint Color in case of On State
			//.setSaveCardSwitchOnTrackTint(getResources().getColor(R.color.green)) // **Optional**

			// change scan icon
			//.setScanIconDrawable(getResources().getDrawable(R.drawable.btn_card_scanner_normal)) // **Optional**

			// setup pay button selector [ background - round corner ]
			//.setPayButtonResourceId(R.drawable.btn_pay_selector)

			// setup pay button font type face
			//.setPayButtonFont(Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf")) // **Optional**

			// setup pay button disable title color
			//.setPayButtonDisabledTitleColor(getResources().getColor(R.color.black)) // **Optional**

			// setup pay button enable title color
			//.setPayButtonEnabledTitleColor(getResources().getColor(R.color.White)) // **Optional**

			//setup pay button text size
			//.setPayButtonTextSize(14) // **Optional**

			// show/hide pay button loader
			.setPayButtonLoaderVisible(true) // **Optional**

			// show/hide pay button security icon
			.setPayButtonSecurityIconVisible(true) // **Optional**

			// set the text on pay button
			//.setPayButtonText("PAY BTN CAN BE VERY VERY VERY  LONGGGG LONGGGGG") // **Optional**


			// setup dialog textcolor and textsize
			//.setDialogTextColor(getResources().getColor(R.color.black1))     // **Optional**
			//.setDialogTextSize(17)                // **Optional**

	}

	/**
	 * Configure SDK Session
	 */
	private fun configureSDKSession() {
		// Instantiate SDK Session
		if (sdkSession == null) {
			sdkSession = SDKSession() //** Required **
		}

		// TODO LESSAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

		// pass your activity as a session delegate to listen to SDK internal payment process follow
//		sdkSession?.addSessionDelegate(this) //** Required **

		// initiate PaymentDataSource
//		sdkSession.instantiatePaymentDataSource() //** Required **
//
//		// set transaction currency associated to your account
//		sdkSession.setTransactionCurrency(TapCurrency("KWD")) //** Required **
//
//		// Using static CustomerBuilder method available inside TAP Customer Class you can populate TAP Customer object and pass it to SDK
//		sdkSession.setCustomer(customer) //** Required **
//
//		// Set Total Amount. The Total amount will be recalculated according to provided Taxes and Shipping
//		sdkSession.setAmount(BigDecimal(1)) //** Required **
//
//		// Set Payment Items array list
//		sdkSession.setPaymentItems(ArrayList()) // ** Optional ** you can pass empty array list
//
//
//		sdkSession.setPaymentType("CARD");   //** Merchant can pass paymentType
//
//		// Set Taxes array list
//		sdkSession.setTaxes(ArrayList()) // ** Optional ** you can pass empty array list
//
//		// Set Shipping array list
//		sdkSession.setShipping(ArrayList()) // ** Optional ** you can pass empty array list
//
//		// Post URL
//		sdkSession.setPostURL("") // ** Optional **
//
//		// Payment Description
//		sdkSession.setPaymentDescription("") //** Optional **
//
//		// Payment Extra Info
//		sdkSession.setPaymentMetadata(HashMap()) // ** Optional ** you can pass empty array hash map
//
//		// Payment Reference
//		sdkSession.setPaymentReference(null) // ** Optional ** you can pass null
//
//		// Payment Statement Descriptor
//		sdkSession.setPaymentStatementDescriptor("") // ** Optional **
//
//		// Enable or Disable Saving Card
//		sdkSession.isUserAllowedToSaveCard(true) //  ** Required ** you can pass boolean
//
//		// Enable or Disable 3DSecure
//		sdkSession.isRequires3DSecure(true)
//
//		//Set Receipt Settings [SMS - Email ]
//		sdkSession.setReceiptSettings(Receipt(false, false)) // ** Optional ** you can pass Receipt object or null
//
//		// Set Authorize Action
//		sdkSession.setAuthorizeAction(null) // ** Optional ** you can pass AuthorizeAction object or null
//		sdkSession.setDestination(null) // ** Optional ** you can pass Destinations object or null
//		sdkSession.setMerchantID(null) // ** Optional ** you can pass merchant id or null
//		sdkSession.setCardType(CardType.CREDIT) // ** Optional ** you can pass which cardType[CREDIT/DEBIT] you want.By default it loads all available cards for Merchant.
//
//		sdkSession.setDefaultCardHolderName("TEST TAP") // ** Optional ** you can pass default CardHolderName of the user .So you don't need to type it.
//		sdkSession.isUserAllowedToEnableCardHolderName(false) // ** Optional ** you can enable/ disable  default CardHolderName .
//		/**
//		 * Use this method where ever you want to show TAP SDK Main Screen.
//		 * This method must be called after you configured SDK as above
//		 * This method will be used in case of you are not using TAP PayButton in your activity.
//		 */
//		sdkSession.start(this)
	}



}
