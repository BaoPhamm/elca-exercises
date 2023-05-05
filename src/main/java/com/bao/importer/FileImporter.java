package com.bao.importer;

import com.bao.model.Company;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileImporter {
    List<Company> importFile(Path path) throws IOException;
    Integer getTotalCapitalOfHeadquartersLocatedInCH(Path path) throws IOException;
}
