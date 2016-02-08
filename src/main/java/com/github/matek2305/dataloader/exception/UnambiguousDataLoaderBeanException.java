package com.github.matek2305.dataloader.exception;

/**
 * Exception thrown when data loader bean marked as dependency for another loader cannot be resolved.
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class UnambiguousDataLoaderBeanException extends RuntimeException {

    public UnambiguousDataLoaderBeanException(String message) {
        super(message);
    }
}
