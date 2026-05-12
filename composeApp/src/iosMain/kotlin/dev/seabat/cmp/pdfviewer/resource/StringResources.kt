package dev.seabat.cmp.pdfviewer.resource

import hypbridcmppdfviewer.composeapp.generated.resources.Res
import hypbridcmppdfviewer.composeapp.generated.resources.alert_ok
import hypbridcmppdfviewer.composeapp.generated.resources.information_alert_message
import hypbridcmppdfviewer.composeapp.generated.resources.information_alert_title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString as getStringResource

enum class StringResourceKey {
    INFORMATION_ALERT_TITLE,
    INFORMATION_ALERT_MESSAGE,
    ALERT_OK
}

fun getString(key: StringResourceKey): String = runBlocking(Dispatchers.Default) {
    when (key) {
        StringResourceKey.INFORMATION_ALERT_TITLE -> getStringResource(Res.string.information_alert_title)
        StringResourceKey.INFORMATION_ALERT_MESSAGE -> getStringResource(Res.string.information_alert_message)
        StringResourceKey.ALERT_OK -> getStringResource(Res.string.alert_ok)
    }
}