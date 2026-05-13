package dev.seabat.cmp.pdfviewer.resource

import hypbridcmppdfviewer.composeapp.generated.resources.Res
import hypbridcmppdfviewer.composeapp.generated.resources.alert_ok
import hypbridcmppdfviewer.composeapp.generated.resources.auth_success_message
import hypbridcmppdfviewer.composeapp.generated.resources.auth_success_title
import hypbridcmppdfviewer.composeapp.generated.resources.bio_auth_subtitle
import hypbridcmppdfviewer.composeapp.generated.resources.information_alert_message
import hypbridcmppdfviewer.composeapp.generated.resources.information_alert_title
import hypbridcmppdfviewer.composeapp.generated.resources.information_screen_lock_message
import hypbridcmppdfviewer.composeapp.generated.resources.information_screen_lock_title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString as getStringResource

enum class StringResourceKey {
    INFORMATION_ALERT_TITLE,
    INFORMATION_ALERT_MESSAGE,
    INFORMATION_SCREEN_LOCK_TITLE,
    INFORMATION_SCREEN_LOCK_MESSAGE,
    BIO_AUTH_SUBTITLE,
    AUTH_SUCCESS_TITLE,
    AUTH_SUCCESS_MESSAGE,
    ALERT_OK
}

fun getString(key: StringResourceKey): String = runBlocking(Dispatchers.Default) {
    when (key) {
        StringResourceKey.INFORMATION_ALERT_TITLE -> getStringResource(Res.string.information_alert_title)
        StringResourceKey.INFORMATION_ALERT_MESSAGE -> getStringResource(Res.string.information_alert_message)
        StringResourceKey.INFORMATION_SCREEN_LOCK_TITLE -> getStringResource(Res.string.information_screen_lock_title)
        StringResourceKey.INFORMATION_SCREEN_LOCK_MESSAGE -> getStringResource(Res.string.information_screen_lock_message)
        StringResourceKey.BIO_AUTH_SUBTITLE -> getStringResource(Res.string.bio_auth_subtitle)
        StringResourceKey.AUTH_SUCCESS_TITLE -> getStringResource(Res.string.auth_success_title)
        StringResourceKey.AUTH_SUCCESS_MESSAGE -> getStringResource(Res.string.auth_success_message)
        StringResourceKey.ALERT_OK -> getStringResource(Res.string.alert_ok)
    }
}