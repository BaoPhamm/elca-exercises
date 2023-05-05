package com.bao;

import com.bao.importer.FileImporter;
import com.bao.importer.ImporterFactory;
import com.bao.model.Company;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class Main {
    private static final String FILE_PATH = "src/main/resources/import/companies_bigdata.csv";
    private static final String FOLDER_PATH = "src/main/resources/import";

    public static void main(String[] args) throws IOException {

        List<Company> companies;

        try {
            companies = importFile(FILE_PATH);
            printTotalCapitalOfHeadquartersLocatedInCH(companies);
            printTotalCapitalOfHeadquartersLocatedInCHBigData(FILE_PATH);
//            printNameOfCompaniesInCHSortedByCapitalDesc(companies);
            monitorFolderImport(FOLDER_PATH);


        } catch (IOException e) {
            System.out.println("Failed to import file: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Company> importFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        String extension = getFileExtension(filePath);

        FileImporter fileImporter = ImporterFactory.getImporter(extension);
        return fileImporter.importFile(filePath);
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

    /* print total capital of headquarters located in “CH” */
    private static void printTotalCapitalOfHeadquartersLocatedInCH(List<Company> companies) {
        int totalCapital = companies.stream()
                .filter(company -> company.getCountry().equals("CH")
                        && company.getHeadQuarter())
                .map(Company::getCapital)
                .reduce(0, Integer::sum);
        System.out.println("Total capital of headquarters located in CH: " + totalCapital);
    }

    private static void printTotalCapitalOfHeadquartersLocatedInCHBigData(String path) throws IOException {
        Path filePath = Paths.get(path);
        String extension = getFileExtension(filePath);

        FileImporter fileImporter = ImporterFactory.getImporter(extension);
        System.out.println("Total capital of headquarters located in CH: " +
                fileImporter.getTotalCapitalOfHeadquartersLocatedInCH(filePath));
    }

    /* name of companies that the country is in “CH”. The list is sorted descending by capital */
    private static void printNameOfCompaniesInCHSortedByCapitalDesc(List<Company> companies) {
        System.out.println("Name of companies that the country is in CH (sorted descending by capital): ");
        companies.stream()
                .filter(company -> company.getCountry().equals("CH"))
                .sorted((o1, o2) -> o2.getCapital().compareTo(o1.getCapital()))
                .map(Company::getName)
                .forEach(System.out::println);
    }

    private static void monitorFolderImport(String folder) throws IOException, InterruptedException {
        List<Company> companies;
        Path folderPath = Paths.get(folder);

        // Create a WatchService and register the folder for events
        WatchService watchService = FileSystems.getDefault().newWatchService();
        folderPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            // Process all events for the key
            for (WatchEvent<?> event : key.pollEvents()) {

                // Check if the event is for the "companies.csv" file
                if (event.context().toString().equals("companies_bigdata.csv")) {
                    System.out.println("companies.csv changed, reimporting....");

                    // Reimport the file
                    companies = importFile(FILE_PATH);
                    printTotalCapitalOfHeadquartersLocatedInCH(companies);
                    printNameOfCompaniesInCHSortedByCapitalDesc(companies);
                }
            }
            // Reset the key and continue processing events
            key.reset();
        }
    }

}
