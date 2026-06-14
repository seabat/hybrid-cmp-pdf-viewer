package dev.seabat.cmp.pdfviewer.resource

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString as getStringResource

fun getString(resource: StringResource): String = runBlocking {
    getStringResource(resource)
}
