package grand.app.moon.presentation.home

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

/**
 * https://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
 */
class OnSwipeTouchListener(context: Context) : OnTouchListener {

	companion object {
		private const val SWIPE_THRESHOLD = 100
		private const val SWIPE_VELOCITY_THRESHOLD = 100
	}

	private val gestureDetector = GestureDetector(context, object  : GestureDetector.SimpleOnGestureListener() {
		override fun onDown(e: MotionEvent): Boolean {
			return true
		}

		override fun onFling(
			e1: MotionEvent,
			e2: MotionEvent,
			velocityX: Float,
			velocityY: Float
		): Boolean {
			return super.onFling(e1, e2, velocityX, velocityY)
		}

// todo on up event w hakaza isa.
	})

	override fun onTouch(v: View?, event: MotionEvent?): Boolean {
		return gestureDetector.onTouchEvent(event ?: return false)
	}
}

/*
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
 */
