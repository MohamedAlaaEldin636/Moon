package grand.app.moon.core.extenstions

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.presentation.base.extensions.disable
import grand.app.moon.presentation.base.extensions.enable
import java.util.concurrent.TimeUnit

private  val TAG = "_firebaseSMS"
fun Context.sendFirebaseSMS(activity :Activity,view: View, phone: String,callBack: (result: String) -> Unit) {
  view.disable()
  Log.d(TAG, "sendFirebaseSMS: $phone")
  
  val auth = FirebaseAuth.getInstance()

  val verificationStateChangedCallbacks: OnVerificationStateChangedCallbacks =
    object : OnVerificationStateChangedCallbacks() {
      override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
        view.enable()
        Log.d(TAG, "onVerificationCompleted: ")
      }

      /* This one is never called: so i assume there's no problem on my part */
      override fun onVerificationFailed(e: FirebaseException) {
        view.enable()
        e.printStackTrace()
        Log.d(TAG, "onVerificationFailed: ${e.message}")
//        showErrorToast(view.context.resources.getString(R.string.something_went_wrong_please_try_again))
      }

      /* This one is called */
      override fun onCodeSent(verificationId: String, forceResendingToken: ForceResendingToken) {
        super.onCodeSent(verificationId, forceResendingToken)
        view.enable()
        Log.d(TAG, "onCodeSent: ")
//        showErrorToast(view.context.resources.getString(R.string.code_had_been_sent))
        callBack(verificationId)
      }

      /* This one is also called */
      override fun onCodeAutoRetrievalTimeOut(s: String) {
        super.onCodeAutoRetrievalTimeOut(s)
        view.enable()
        Log.d(TAG, "onCodeAutoRetrievalTimeOut: E")
//        showErrorToast(view.context.resources.getString(R.string.something_went_wrong_please_try_again))
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

fun Context.verifyCode(v: View,verificationId : String, code: String,callBack: (result: Boolean) -> Unit) {
  val credential = PhoneAuthProvider.getCredential(verificationId, code)
  v.disable()
  FirebaseAuth.getInstance().signInWithCredential(credential)
    .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
      override fun onComplete(task: Task<AuthResult?>) {
        v.enable()
        when{
          task.isSuccessful -> callBack(true)
          else -> {
            showErrorToast(getString(R.string.verification_code_not_valid))
            callBack(false)
          }
        }
      }
    })
}