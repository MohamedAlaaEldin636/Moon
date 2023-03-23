package ma.ya.cometchatintegration.helperClasses

import java.util.Timer
import java.util.TimerTask

class MATimer(
	private val periodInMs: Long,
	private val delayInMs: Long = 0,
	private val onRun: (currentTimeInMs: Long) -> Unit
) {

	private var timer: Timer? = null

	private var isPaused = false

	private var startingTimeInMs = 0L

	private var isStarted = false
	private var isStopped = false

	private var task: TimerTask? = null

	private fun setupTask() {
		task = object : TimerTask() {
			override fun run() {
				if (!isPaused) {
					startingTimeInMs += periodInMs

					onRun(startingTimeInMs)
				}

				MyLogger.e("CometChatIntegration paused $isPaused startingTimeInMs $startingTimeInMs")
			}
		}
	}

	fun stopOrReset() {
		timer?.cancel()
		timer = null
		task = null
		isStopped = true
		isStarted = false
		startingTimeInMs = 0L
	}

	fun pause() {
		isPaused = true
	}

	fun startOrResume() {
		isPaused = false
		if (isStarted.not()) {
			isStarted = true
			isStopped = false
			timer = Timer()
			setupTask()
			timer?.scheduleAtFixedRate(task, delayInMs, periodInMs)
		}
	}

}