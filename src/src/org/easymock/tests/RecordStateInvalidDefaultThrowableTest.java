/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class RecordStateInvalidDefaultThrowableTest extends TestCase {
    MockControl control;

    IMethods mock;

    private class CheckedException extends Exception {
    }

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testThrowNull() {
        mock.throwsNothing(false);
        try {
            control.setDefaultThrowable(null);
            fail("NullPointerException expected");
        } catch (NullPointerException expected) {
            assertEquals("null cannot be thrown", expected.getMessage());
        }

    }

    public void testThrowCheckedExceptionWhereNoCheckedExceptionIsThrown() {
        mock.throwsNothing(false);
        try {
            control.setDefaultThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + this.getClass().getName() + "$CheckedException", expected
                    .getMessage());
        }
    }

    public void testThrowWrongCheckedException() throws IOException {
        mock.throwsIOException(0);
        try {
            control.setDefaultThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + this.getClass().getName() + "$CheckedException", expected
                    .getMessage());
        }
    }
}
