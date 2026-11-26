package dev.seabat.cmp.pdfviewer.screen.top

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

/** PDF の Uri から取得した一覧表示用のメタデータ */
data class PdfUriMetadata(
    val name: String,
    val createdAt: String,
    val size: String
)

/**
 * PDF の Uri から表示名・作成日時・ファイルサイズを取得する
 *
 * ドキュメントピッカーで選択された Uri と、他アプリから渡された Uri の双方で使う。
 * 他アプリ由来の Uri は content:// / file:// のどちらもあり得るうえ、
 * content:// でも OpenableColumns を返さない提供元があるため、取得できない項目は代替値で補う
 */
fun readPdfUriMetadata(context: Context, uri: Uri): PdfUriMetadata {
    var name: String? = null
    var sizeBytes = 0L
    var lastModifiedMillis = 0L

    if (uri.scheme == ContentResolver.SCHEME_FILE) {
        // file:// は ContentResolver へ問い合わせできないため File から直接取得する
        uri.path?.let(::File)?.let { file ->
            name = file.name
            sizeBytes = file.length()
            lastModifiedMillis = file.lastModified()
        }
    } else {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIdx = cursor.getColumnIndex(OpenableColumns.SIZE)
                val modifiedIdx = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
                if (nameIdx >= 0) name = cursor.getString(nameIdx)
                if (sizeIdx >= 0) sizeBytes = cursor.getLong(sizeIdx)
                if (modifiedIdx >= 0) lastModifiedMillis = cursor.getLong(modifiedIdx)
            }
        }
    }

    val dateSource = if (lastModifiedMillis > 0) Date(lastModifiedMillis) else Date()
    // 固定フォーマット向けに Locale.US と GregorianCalendar を指定することで、
    // 日本語ロケールでも和暦にならず西暦で出力される
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    formatter.calendar = GregorianCalendar()
    return PdfUriMetadata(
        name = name?.takeIf { it.isNotEmpty() } ?: fallbackName(uri),
        createdAt = formatter.format(dateSource),
        size = formatFileSize(sizeBytes)
    )
}

private const val DEFAULT_FILE_NAME = "unknown.pdf"

/** 表示名が取得できなかった場合は Uri の末尾を使い、それも使えなければ既定値にする */
private fun fallbackName(uri: Uri): String =
    uri.lastPathSegment
        ?.substringAfterLast('/')
        ?.takeIf { it.isNotEmpty() }
        ?: DEFAULT_FILE_NAME

private fun formatFileSize(bytes: Long): String = when {
    bytes >= 1_048_576 -> "%.1f MB".format(bytes / 1_048_576.0)
    bytes >= 1_024 -> "%.1f KB".format(bytes / 1_024.0)
    else -> "$bytes B"
}
