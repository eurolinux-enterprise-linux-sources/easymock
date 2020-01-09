/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class RecordStateInvalidThrowableTest extends TestCase {

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
            control.setThrowable(null);
            fail("NullPointerException expected");
        } catch (NullPointerException expected) {
            assertEquals("null cannot be thrown", expected.getMessage());
        }

    }

    public void testThrowCheckedExceptionWhereNoCheckedExceptionIsThrown() {
        mock.throwsNothing(false);
        try {
            control.setThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + CheckedException.class.getName(), expected.getMessage());
        }
    }

    public void testThrowWrongCheckedException() throws IOException {
        mock.throwsIOException(0);
        try {
            control.setThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + CheckedException.class.getName(), expected.getMessage());
        }
    }

    public void testThrowAfterThrowable() throws IOException {
        mock.throwsIOException(0);
        control.setThrowable(new IOException(), MockControl.ONE_OR_MORE);
        try {
            control.setThrowable(new IOException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "last method called on mock already has a non-fixed count set.",
                    expected.getMessage());
        }
    }
}
