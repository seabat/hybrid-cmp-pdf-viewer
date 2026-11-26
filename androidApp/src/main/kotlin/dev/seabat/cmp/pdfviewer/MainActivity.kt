package dev.seabat.cmp.pdfviewer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    /** 他アプリから「開く」「共有」で渡された PDF の Uri 文字列。Compose 側で処理したらクリアされる */
    private var incomingPdfUri by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // アプリ未起動の状態で PDF を渡された場合はこちらで受け取る
        handleIncomingIntent(intent)

        setContent {
            NavigationApp(
                incomingPdfUri = incomingPdfUri,
                onIncomingPdfConsumed = { incomingPdfUri = null }
            )
        }
    }

    /** 起動済みの状態で PDF を渡された場合はこちらで受け取る（launchMode="singleTask" のため） */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }

    /**
     * ACTION_VIEW / ACTION_SEND のインテントから PDF の Uri を取り出す
     *
     * Uri の読み取り権限はインテントに紐づく一時的なものなので、受け取り後は速やかに
     * 内部ストレージへコピーする（コピーは TopViewModel 経由で行われる）
     */
    private fun handleIncomingIntent(intent: Intent) {
        val uri = when (intent.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> intent.extraStreamUri()
            else -> null
        } ?: return
        incomingPdfUri = uri.toString()
    }

    private fun Intent.extraStreamUri(): Uri? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(Intent.EXTRA_STREAM)
        }
}

@Preview
@Composable
fun NavigationAppAndroidPreview() {
    NavigationApp()
}
