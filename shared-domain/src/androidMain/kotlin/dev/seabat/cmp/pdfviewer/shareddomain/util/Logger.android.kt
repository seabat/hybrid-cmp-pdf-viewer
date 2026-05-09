package dev.seabat.cmp.pdfviewer.shareddomain.util

actual fun log(message: String) {
    android.util.Log.d("KmpMultiModule", message)
}