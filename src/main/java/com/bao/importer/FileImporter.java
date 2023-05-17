package com.bao.importer;

import com.bao.model.Company;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileImporter {
    List<Company> importFile(Path path) throws IOException;
    Stream<Company> importFileAsTream(Path path) throws IOException;
}
