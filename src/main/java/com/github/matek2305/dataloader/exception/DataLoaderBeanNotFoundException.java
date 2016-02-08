package com.github.matek2305.dataloader.exception;

/**
 * Exception thrown when data loader bean marked as dependency for another loader is not found in spring context.
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class DataLoaderBeanNotFoundException extends RuntimeException {

    public DataLoaderBeanNotFoundException(String message) {
        super(message);
    }
}
