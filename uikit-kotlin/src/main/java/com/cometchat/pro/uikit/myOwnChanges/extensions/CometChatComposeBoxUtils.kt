package com.cometchat.pro.uikit.myOwnChanges.extensions

import android.view.View
import android.widget.RelativeLayout
import com.cometchat.pro.uikit.R
import com.cometchat.pro.uikit.ui_components.shared.cometchatComposeBox.CometChatComposeBox

fun CometChatComposeBox.sendAudioMsg(fileName: String) {
	composeActionListener.onVoiceNoteComplete(fileName)
	audioFileNameWithPath = ""
	voiceMessageLayout!!.visibility = View.GONE
	etComposeBox!!.visibility = View.VISIBLE
	ivDelete!!.visibility = RelativeLayout.GONE
	ivSend!!.visibility = View.GONE
	ivArrow!!.visibility = View.VISIBLE
  //ivMic!!.visibility = View.VISIBLE
	isRecording = false
	isPlaying = false
	voiceMessage = false
	ivMic!!.setImageResource(R.drawable.ic_microphone_circle)
}
