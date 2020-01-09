/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import junit.framework.AssertionFailedError;

public class AssertionFailedErrorWrapper extends RuntimeException {
    private final AssertionFailedError error;

    public AssertionFailedErrorWrapper(final AssertionFailedError error) {
        this.error = error;
    }

    public AssertionFailedError getAssertionFailedError() {
        return error;
    }
}
