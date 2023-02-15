package grand.app.moon.extensions

import android.content.Context
import grand.app.moon.core.MyApplication

val Context?.applicationScope get() = (this?.applicationContext as? MyApplication)?.applicationScope
