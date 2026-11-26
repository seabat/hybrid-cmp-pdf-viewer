import UIKit
import SwiftUI
import UniformTypeIdentifiers

/// UIDocumentPickerViewController のラッパー
struct DocumentPickerView: UIViewControllerRepresentable {
    var onFilePicked: (PdfImportFile) -> Void

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
        var onFilePicked: (PdfImportFile) -> Void

        init(onFilePicked: @escaping (PdfImportFile) -> Void) {
            self.onFilePicked = onFilePicked
        }

        func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
            guard let url = urls.first, let file = preparePdfForImport(from: url) else { return }
            onFilePicked(file)
        }
    }
}
