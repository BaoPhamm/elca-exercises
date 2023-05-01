package com.bao.exception;

import java.io.IOException;

public class NotSupportedFileExtensionException extends IOException {
    public NotSupportedFileExtensionException(String message) {
        super(message);
    }
}
