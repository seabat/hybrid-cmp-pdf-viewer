package dev.seabat.cmp.pdfviewer.screen.information

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import hypbridcmppdfviewer.composeapp.generated.resources.Res
import hypbridcmppdfviewer.composeapp.generated.resources.alert_ok
import hypbridcmppdfviewer.composeapp.generated.resources.bio_auth_subtitle
import hypbridcmppdfviewer.composeapp.generated.resources.bio_auth_title
import hypbridcmppdfviewer.composeapp.generated.resources.information_alert_message
import hypbridcmppdfviewer.composeapp.generated.resources.information_alert_title
import hypbridcmppdfviewer.composeapp.generated.resources.information_screen_lock_message
import hypbridcmppdfviewer.composeapp.generated.resources.information_screen_lock_title
import dev.seabat.cmp.pdfviewer.resource.getString
import org.jetbrains.compose.resources.stringResource

/**
 * Android 用のインフォメーションページ
 * Scaffold + TopAppBar で [InformationContent] をラップする
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(onNavigateBack: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var showScreenLockAlert by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as FragmentActivity
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(Res.string.information_alert_title)) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(Res.string.alert_ok))
                }
            },
            text = { Text(stringResource(Res.string.information_alert_message)) }
        )
    }

    if (showScreenLockAlert) {
        AlertDialog(
            onDismissRequest = { showScreenLockAlert = false },
            title = { Text(stringResource(Res.string.information_screen_lock_title)) },
            confirmButton = {
                TextButton(onClick = { showScreenLockAlert = false }) {
                    Text(stringResource(Res.string.alert_ok))
                }
            },
            text = { Text(stringResource(Res.string.information_screen_lock_message)) }
        )
    }

    InformationScaffold(
        onNavigateBack = onNavigateBack,
        onShowVersionAlert = { showDialog = true },
        onShowBioAuth = {
            showBiometricPrompt(
                activity = activity,
                onScreenLockNotSet = { showScreenLockAlert = true }
            )
        }
    )
}

private fun showBiometricPrompt(
    activity: FragmentActivity,
    onScreenLockNotSet: () -> Unit
) {
    val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    val canAuthenticate = BiometricManager.from(activity).canAuthenticate(authenticators)
    if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
        onScreenLockNotSet()
        return
    }

    val biometricPrompt = BiometricPrompt(
        activity,
        ContextCompat.getMainExecutor(activity),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {}
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {}
            override fun onAuthenticationFailed() {}
        }
    )
    // DEVICE_CREDENTIAL を含む場合は setNegativeButtonText 不可
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(getString(Res.string.bio_auth_title))
        .setSubtitle(getString(Res.string.bio_auth_subtitle))
        .setAllowedAuthenticators(authenticators)
        .build()
    biometricPrompt.authenticate(promptInfo)
}
