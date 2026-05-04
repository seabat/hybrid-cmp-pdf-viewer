package dev.seabat.cmp.pdfviewr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform