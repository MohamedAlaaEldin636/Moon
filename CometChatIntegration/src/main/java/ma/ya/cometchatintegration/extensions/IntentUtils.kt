package ma.ya.cometchatintegration.extensions

import android.content.Intent

fun Intent.createChooserMA(title: CharSequence): Intent = Intent.createChooser(this, title)
