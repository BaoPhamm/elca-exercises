package com.bao.importer;

import com.bao.model.Company;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

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
    public List<Company> importFile(Path path) {
        // Implement XML file import logic
        System.out.println("Importing XML file: " + path.getFileName());
        return null;
    }

    @Override
    public Stream<Company> importFileAsTream(Path path) throws IOException {
        return null;
    }
}
