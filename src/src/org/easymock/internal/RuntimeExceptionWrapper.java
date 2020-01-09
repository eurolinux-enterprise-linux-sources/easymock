/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

public class RuntimeExceptionWrapper extends RuntimeException {
    private final RuntimeException runtimeException;

    public RuntimeExceptionWrapper(final RuntimeException runtimeException) {
        this.runtimeException = runtimeException;
    }

    public RuntimeException getRuntimeException() {
        return runtimeException;
    }
}
