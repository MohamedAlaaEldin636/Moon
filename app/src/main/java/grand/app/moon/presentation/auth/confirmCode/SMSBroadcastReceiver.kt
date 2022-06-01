package grand.app.moon.presentation.auth.confirmCode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSBroadcastReceiver : BroadcastReceiver() {

  private  val TAG = "SMSBroadcastReceiver"
  private var otpReceiver: OTPListener? = null

  fun injectOTPListener(receiver: OTPListener?) {
    Log.d(TAG, "injectOTPListener is empty : ${receiver == null}")
    this.otpReceiver = receiver
  }

  override fun onReceive(context: Context, intent: Intent) {
    if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
      val extras = intent.extras
      val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

      when (status.statusCode) {

        CommonStatusCodes.SUCCESS -> {
          Log.d(TAG, "CommonStatusCodes.SUCCESS")
          Toast.makeText(context, "CommonStatusCodes.SUCCESS", Toast.LENGTH_LONG).show()

          val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String

          val pattern = Pattern.compile("\\d{6}")
          val matcher = pattern.matcher(message)

          if (matcher.find()) {
            otpReceiver?.onOTPReceived(matcher.group(0))
            return
          }
        }
        CommonStatusCodes.TIMEOUT -> {
          Log.d(TAG, "CommonStatusCodes.TIMEOUT")
          Toast.makeText(context, "CommonStatusCodes.TIMEOUT", Toast.LENGTH_LONG).show()
          otpReceiver?.onOTPTimeOut()
        }
      }
    }
  }

  interface OTPListener {


    fun onOTPReceived(otp: String)

    fun onOTPTimeOut()
  }
}