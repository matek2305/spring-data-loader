package com.github.matek2305.dataloader.exception;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class DataDependencyCycleFoundException extends RuntimeException {

    public DataDependencyCycleFoundException(String message) {
        super(message);
    }
}
