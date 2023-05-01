package com.bao.importer;

import com.bao.exception.NotSupportedFileExtensionException;
import com.bao.model.Company;

import java.nio.file.Path;
import java.util.List;

public class XmlFileImporter implements FileImporter {
    private static XmlFileImporter instance;

    private XmlFileImporter() {
    }

    public static XmlFileImporter getInstance() {
        if (instance == null) {
            instance = new XmlFileImporter();
        }
        return instance;
    }

    @Override
    public List<Company> importFile(Path path) throws NotSupportedFileExtensionException {
        // Implement XML file import logic
        System.out.println("Importing XML file: " + path.getFileName());
        return null;
    }
}
