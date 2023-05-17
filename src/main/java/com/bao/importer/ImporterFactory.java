package com.bao.importer;

import com.bao.exception.NotSupportedFileExtensionException;

public class ImporterFactory {
    public static FileImporter getImporter(String fileExtension) throws NotSupportedFileExtensionException {
        switch (fileExtension) {
            case "csv":
                return CsvFileImporter.getInstance();
            case "xml":
                return XmlFileImporter.getInstance();
            default:
                throw new NotSupportedFileExtensionException(fileExtension);
        }
    }
}
