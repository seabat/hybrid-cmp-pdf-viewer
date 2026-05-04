package dev.seabat.cmp.pdfviewer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform