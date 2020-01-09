/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

public class Result {
    private Object value;

    private boolean doThrow;

    private Result(Object value, boolean doThrow) {
        this.value = value;
        this.doThrow = doThrow;
    }

    public static Result createThrowResult(Throwable throwable) {
        return new Result(throwable, true);
    }

    public static Result createReturnResult(Object value) {
        return new Result(value, false);
    }

    public Object returnObjectOrThrowException() throws Throwable {
        if (doThrow) {
            throw new ThrowableWrapper((Throwable) value);
        } else {
            return value;
        }
    }
}
