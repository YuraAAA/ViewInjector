package com.aizenberg.support.viewinjector;

/**
 * Created by Yuriy Aizenberg
 */
public class InjectionException extends RuntimeException {

    public InjectionException(String detailMessage) {
        super(detailMessage);
    }

    public InjectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
