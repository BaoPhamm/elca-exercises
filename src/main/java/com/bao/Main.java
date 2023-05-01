package com.bao;

import com.bao.exception.NotSupportedFileExtensionException;
import com.bao.importer.CsvFileImporter;
import com.bao.importer.FileImporter;
import com.bao.importer.ImporterFactory;
import com.bao.importer.XmlFileImporter;
import com.bao.model.Company;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static String filePath = "src/main/resources/import/companies.csv";

    public static void main(String[] args) {
        System.out.println("Hello world!");
        List<Company> companies = new ArrayList<>();
        try {
            Path path = Paths.get(filePath);
            String extension = getFileExtension(path);
            FileImporter fileImporter = ImporterFactory.getImporter(extension);
            companies = fileImporter.importFile(path);

        } catch (NotSupportedFileExtensionException e) {
            System.out.println("Failed to import file: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed to import file: " + e.getMessage());
        }

        companies.stream().forEach(System.out::println);



    }

    private static String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "";
        }
    }

}
