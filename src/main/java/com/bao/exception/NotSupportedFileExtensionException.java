package com.bao.exception;

import java.io.IOException;

public class NotSupportedFileExtensionException extends IOException {
    private final String fileExtension;

    public NotSupportedFileExtensionException(String fileExtension) {
        super("Unsupported file extension: " + fileExtension);
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
