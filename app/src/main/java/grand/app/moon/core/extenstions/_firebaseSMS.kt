package grand.app.moon.core.extenstions

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.presentation.base.extensions.enable
import java.util.concurrent.TimeUnit

fun Context.sendFirebaseSMS(activity :Activity,view: View, phone: String,msg: String) {
  val auth = FirebaseAuth.getInstance()
  var mAuthCredentials: PhoneAuthCredential? = null

  val verificationStateChangedCallbacks: OnVerificationStateChangedCallbacks =
    object : OnVerificationStateChangedCallbacks() {
      override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
        view.enable()
        mAuthCredentials = phoneAuthCredential
      }

      /* This one is never called: so i assume there's no problem on my part */
      override fun onVerificationFailed(e: FirebaseException) {
        view.enable()
        e.printStackTrace()
        showErrorToast(view.context.resources.getString(R.string.something_went_wrong_please_try_again))
      }

      /* This one is called */
      override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
        super.onCodeSent(s, forceResendingToken)
        //here
        showErrorToast(view.context.resources.getString(R.string.code_had_been_sent))
      }

      /* This one is also called */
      override fun onCodeAutoRetrievalTimeOut(s: String) {
        super.onCodeAutoRetrievalTimeOut(s)
      }
    }

  val options = PhoneAuthOptions.newBuilder(auth)
    .setPhoneNumber(phone)       // Phone number to verify
    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
    .setActivity(activity)                 // Activity (for callback binding)
    .setCallbacks(verificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
    .build()
  PhoneAuthProvider.verifyPhoneNumber(options)

}