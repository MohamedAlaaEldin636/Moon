package grand.app.moon.extensions

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Boolean>.toggleValueIfNotNull() {
	val value = value ?: return
	this.value = value.not()
}

/** If [MutableLiveData.getValue] is `null` treat as `false` so will be changed to `true` isa. */
fun MutableLiveData<Boolean>.toggleValue() {
	value = value.orFalse().not()
}
