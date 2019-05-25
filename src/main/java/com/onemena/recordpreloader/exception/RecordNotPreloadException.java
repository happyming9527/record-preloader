package com.onemena.recordpreloader.exception;

public class RecordNotPreloadException extends RuntimeException {
    public RecordNotPreloadException() {
        super("relation need preload first");
    }
}
