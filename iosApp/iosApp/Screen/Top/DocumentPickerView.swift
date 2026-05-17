import UIKit
import SwiftUI
import UniformTypeIdentifiers

/// UIDocumentPickerViewController のラッパー
struct DocumentPickerView: UIViewControllerRepresentable {
    var onFilePicked: (String, String, String) -> Void

    func makeCoordinator() -> Coordinator {
        Coordinator(onFilePicked: onFilePicked)
    }

    func makeUIViewController(context: Context) -> UIDocumentPickerViewController {
        let picker = UIDocumentPickerViewController(forOpeningContentTypes: [UTType.pdf])
        picker.delegate = context.coordinator
        picker.allowsMultipleSelection = false
        return picker
    }

    func updateUIViewController(_ uiViewController: UIDocumentPickerViewController, context: Context) {}

    class Coordinator: NSObject, UIDocumentPickerDelegate {
        var onFilePicked: (String, String, String) -> Void

        init(onFilePicked: @escaping (String, String, String) -> Void) {
            self.onFilePicked = onFilePicked
        }

        func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
            guard let url = urls.first else { return }

            _ = url.startAccessingSecurityScopedResource()
            defer { url.stopAccessingSecurityScopedResource() }

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

            onFilePicked(name, createdAt, sizeFormatted)
        }
    }
}
