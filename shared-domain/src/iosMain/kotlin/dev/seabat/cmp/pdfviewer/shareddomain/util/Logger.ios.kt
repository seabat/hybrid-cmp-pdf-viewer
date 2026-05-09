package dev.seabat.cmp.pdfviewer.shareddomain.util

import platform.Foundation.NSLog

actual fun log(message: String) {
    NSLog(message)
}