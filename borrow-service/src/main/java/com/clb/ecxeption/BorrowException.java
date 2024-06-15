package com.clb.ecxeption;

public class BorrowException extends RuntimeException {
    public BorrowException(String message) {
        super(message);
    }
}
