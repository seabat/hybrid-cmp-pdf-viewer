import Foundation

/// 一覧へ追加する PDF の情報
struct PdfImportFile {
    /// 一時ディレクトリへコピーした PDF の URL 文字列（Kotlin 側がここから内部ストレージへコピーする）
    let sourceUrl: String
    let name: String
    let createdAt: String
    let size: String
}

/// PDF の URL からメタデータを取得し、一時ディレクトリへコピーして [PdfImportFile] を返す
///
/// ドキュメントピッカーで選択された URL と、他アプリから `onOpenURL` で渡された URL の双方で使う。
/// セキュリティスコープが有効な間に同期コピーするのが要点で、
/// スコープが切れた後に Kotlin のコルーチンから元 URL を読むと失敗する
func preparePdfForImport(from url: URL) -> PdfImportFile? {
    let isScoped = url.startAccessingSecurityScopedResource()
    defer { if isScoped { url.stopAccessingSecurityScopedResource() } }

    let name = url.lastPathComponent
    let sizeBytes = (try? url.resourceValues(forKeys: [.fileSizeKey]).fileSize) ?? 0
    let sizeFormatted: String = {
        switch sizeBytes {
        case 1_048_576...: return String(format: "%.1f MB", Double(sizeBytes) / 1_048_576)
        case 1_024...: return String(format: "%.1f KB", Double(sizeBytes) / 1_024)
        default: return "\(sizeBytes) B"
        }
    }()

    let creationDate = (try? url.resourceValues(forKeys: [.creationDateKey]).creationDate) ?? Date()
    let formatter = DateFormatter()

    // 固定フォーマット向けに locale と calendar を明示することで、
    // 日本語ロケールでも和暦（令和）にならず西暦で出力される
    formatter.locale = Locale(identifier: "en_US_POSIX")
    formatter.calendar = Calendar(identifier: .gregorian)

    formatter.dateFormat = "yyyy-MM-dd HH:mm"
    let createdAt = formatter.string(from: creationDate)

    let tempUrl = FileManager.default.temporaryDirectory
        .appendingPathComponent(UUID().uuidString)
        .appendingPathExtension("pdf")
    do {
        try FileManager.default.copyItem(at: url, to: tempUrl)
    } catch {
        return nil
    }

    // LSSupportsOpeningDocumentsInPlace が効かない経路では Documents/Inbox/ へ
    // コピーされたファイルが渡されるため、取り込み後に削除してゴミを残さない
    if url.path.contains("/Documents/Inbox/") {
        try? FileManager.default.removeItem(at: url)
    }

    return PdfImportFile(
        sourceUrl: tempUrl.absoluteString,
        name: name,
        createdAt: createdAt,
        size: sizeFormatted
    )
}
