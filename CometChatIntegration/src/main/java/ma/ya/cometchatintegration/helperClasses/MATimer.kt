package ma.ya.cometchatintegration.helperClasses

import java.util.Timer
import java.util.TimerTask

class MATimer(
	private val periodInMs: Long,
	private val delayInMs: Long = 0,
	private val onRun: (currentTimeInMs: Long) -> Unit
) {

	private val timer = Timer()

	private var isPaused = false

	private var startingTimeInMs = 0L

	private var isStopped = false

	private val task = object : TimerTask() {
		override fun run() {
			if (!isPaused) {
				startingTimeInMs += periodInMs

				onRun(startingTimeInMs)
			}

			MyLogger.e("CometChatIntegration paused $isPaused startingTimeInMs $startingTimeInMs")
		}
	}

	fun start() {
		timer.scheduleAtFixedRate(task, delayInMs, periodInMs)
	}

	fun stop() {
		timer.cancel()
		isStopped = true
	}

	fun pause() {
		isPaused = true
	}

	fun resume() {
		isPaused = false
	}

}