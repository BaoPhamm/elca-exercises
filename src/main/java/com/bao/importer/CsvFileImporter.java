package com.bao.importer;

import com.bao.model.Company;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvFileImporter implements FileImporter {

    private static CsvFileImporter instance;

    private CsvFileImporter() {
    }

    public static CsvFileImporter getInstance() {
        if (instance == null) {
            instance = new CsvFileImporter();
        }
        return instance;
    }

    @Override
    public List<Company> importFile(Path path) throws IOException {

        System.out.println("Importing CSV file: " + path.getFileName());
        List<Company> companies = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] values = line.split(",");
            Company company = new Company();
            company.setId(Integer.valueOf(values[0]));
            company.setName(values[1]);
            company.setFoundationDate(values[2]);
            company.setCapital(Integer.valueOf(values[3]));
            company.setCountry(values[4]);
            if (values.length == 6) {
                company.setHeadQuarter(convertToBoolean(values[5]));
            } else {
                company.setHeadQuarter(Boolean.FALSE);
            }
            companies.add(company);
        }
        return companies;
    }

    @Override
    public Integer getTotalCapitalOfHeadquartersLocatedInCH(Path path) throws IOException {
        int totalCapital = 0;

        try (FileInputStream inputStream = new FileInputStream(path.toFile());
                Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] values = line.split(",");
                if (values[4].equals("CH") && (values.length == 6) && (values[5].equals("1"))) {
                    totalCapital += Integer.valueOf(values[3]);
                }
            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        }
        return totalCapital;
    }

    private Boolean convertToBoolean(String str) {
        return str.equals("1") ? Boolean.TRUE : Boolean.FALSE;
    }
}
