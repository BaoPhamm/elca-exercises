package com.bao;

import com.bao.exception.NotSupportedFileExtensionException;
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
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String FAILED_TO_IMPORT_FILE_MSG = "Failed to import file: ";

    public static void main(String[] args) {
        if (args.length < 2) {
            logger.info("Not enough args, please provide:\n1: file path\n2: country");
            return;
        }

        String filePath = args[0];
        String country = args[1];
        Predicate<Company> predicateCountry = company -> country.equals(company.getCountry());
        Predicate<Company> predicateIsHeadQuarter = Company::getHeadQuarter;
        Comparator<Company> comparatorCapitalDesc = Comparator.comparing(Company::getCapital).reversed();

        try (Stream<Company> companiesStream = importFile(filePath)) {
            printTotalCapitalOfHeadquartersLocatedInCountry(companiesStream, country, predicateCountry.and(predicateIsHeadQuarter));
        } catch (IOException e) {
            logger.severe(FAILED_TO_IMPORT_FILE_MSG + e.getMessage());
        }

        try (Stream<Company> secondCompaniesStream = importFile(filePath)) {
            printNameOfCompaniesInCountry(secondCompaniesStream, country, predicateCountry, comparatorCapitalDesc);
        } catch (IOException e) {
            logger.severe(FAILED_TO_IMPORT_FILE_MSG + e.getMessage());
        }

        Thread monitorThread = new Thread(() -> {
            try {
                monitorFolderImport(filePath, country, predicateCountry, predicateIsHeadQuarter, comparatorCapitalDesc);
            } catch (InterruptedException | IOException e) {
                logger.severe("InterruptedException: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        });
        monitorThread.start();
    }

    private static Stream<Company> importFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        String extension = getFileExtension(filePath)
                .orElseThrow(() -> new NotSupportedFileExtensionException(""));

        FileImporter fileImporter = ImporterFactory.getImporter(extension);
        return fileImporter.importFileAsTream(filePath);
    }

    private static Optional<String> getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return Optional.of(fileName.substring(dotIndex + 1));
        } else {
            return Optional.empty();
        }
    }

    /* print total capital of headquarters located in “CH” */
    private static void printTotalCapitalOfHeadquartersLocatedInCountry(Stream<Company> companies,
                                                                        String country,
                                                                        Predicate<Company> predicate) {
        logMemoryUsage("Before printTotalCapitalOfHeadquartersLocatedInCountry");
        int totalCapital = companies
                .filter(predicate)
                .map(Company::getCapital)
                .reduce(0, Integer::sum);
        System.out.println("Total capital of headquarters located in " + country + ": " + totalCapital);
        logMemoryUsage("After printTotalCapitalOfHeadquartersLocatedInCountry");
    }

    /* name of companies that the country is in “CH”. The list is sorted descending by capital */
    private static void printNameOfCompaniesInCountry(Stream<Company> companies,
                                                      String country,
                                                      Predicate<Company> predicate,
                                                      Comparator<Company> comparatorCapitalDesc) {
        logMemoryUsage("Before printNameOfCompaniesInCountry");
        System.out.println("Name of companies that the country is in " + country + ":");
        companies
                .filter(predicate)
                .sorted(comparatorCapitalDesc)
                .map(Company::getName)
                .forEach(System.out::println);
        logMemoryUsage("After printNameOfCompaniesInCountry");
    }

    private static void monitorFolderImport(String filePath,
                                            String country,
                                            Predicate<Company> predicateCountry,
                                            Predicate<Company> predicateIsHeadQuarter,
                                            Comparator<Company> comparatorCapitalDesc) throws IOException, InterruptedException {
        Path path = Paths.get(filePath);
        Path folderPath = path.getParent();
        String filename = path.getFileName().toString();

        // Create a WatchService and register the folder for events
        WatchService watchService = FileSystems.getDefault().newWatchService();
        folderPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            // Process all events for the key
            for (WatchEvent<?> event : key.pollEvents()) {

                // Check if the event is for the "companies.csv" file
                if (filename.equals(event.context().toString())) {
                    System.out.println(filename + " changed, reimporting....");

                    // Reimport the file
                    try (Stream<Company> companiesStream = importFile(filePath)) {
                        printTotalCapitalOfHeadquartersLocatedInCountry(companiesStream, country, predicateCountry.and(predicateIsHeadQuarter));
                    } catch (IOException e) {
                        logger.severe(FAILED_TO_IMPORT_FILE_MSG + e.getMessage());
                    }

                    try (Stream<Company> secondCompaniesStream = importFile(filePath)) {
                        printNameOfCompaniesInCountry(secondCompaniesStream, country, predicateCountry, comparatorCapitalDesc);
                    } catch (IOException e) {
                        logger.severe(FAILED_TO_IMPORT_FILE_MSG + e.getMessage());
                    }
                }
            }
            // Reset the key and continue processing events
            key.reset();
        }
    }

    private static void logMemoryUsage(String message) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        logger.info(message + ": Used Memory = " + usedMemory + " bytes");
    }

}
