/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.io.IOException;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.internal.ReplayState;

public class UsageVerifyTest extends TestCase {
    private MockControl control;

    private IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testTwoReturns() {
        mock.throwsNothing(true);
        control.setReturnValue("Test");
        control.setReturnValue("Test2");

        control.replay();

        assertEquals("Test", mock.throwsNothing(true));

        boolean failed = true;

        try {
            control.verify();
            failed = false;
        } catch (AssertionFailedError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    throwsNothing(true): expected: 2, actual: 1",
                    expected.getMessage());
            assertTrue("stack trace must be filled in", Util.getStackTrace(
                    expected).indexOf(ReplayState.class.getName()) == -1);
        }

        if (!failed)
            fail("AssertionFailedError expected");

        assertEquals("Test2", mock.throwsNothing(true));

        control.verify();

        try {
            mock.throwsNothing(true);
            fail("AssertionFailedError expected");
        } catch (AssertionFailedError expected) {
            assertEquals("\n  Unexpected method call throwsNothing(true):"
                    + "\n    throwsNothing(true): expected: 2, actual: 3",
                    expected.getMessage());
        }
    }

    public void testAtLeastTwoReturns() {
        mock.throwsNothing(true);
        control.setReturnValue("Test");
        control.setReturnValue("Test2", MockControl.ONE_OR_MORE);

        control.replay();

        assertEquals("Test", mock.throwsNothing(true));

        try {
            control.verify();
            fail("AssertionFailedError expected");
        } catch (AssertionFailedError expected) {

            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    throwsNothing(true): expected: at least 2, actual: 1",
                    expected.getMessage());
        }

        assertEquals("Test2", mock.throwsNothing(true));
        assertEquals("Test2", mock.throwsNothing(true));

        control.verify();
    }

    public void testTwoThrows() throws IOException {
        mock.throwsIOException(0);
        control.setThrowable(new IOException());
        control.setThrowable(new IOException());
        mock.throwsIOException(1);
        control.setThrowable(new IOException());

        control.replay();

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        try {
            control.verify();
            fail("AssertionFailedError expected");
        } catch (AssertionFailedError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    throwsIOException(0): expected: 2, actual: 1"
                    + "\n    throwsIOException(1): expected: 1, actual: 0",
                    expected.getMessage());
        }

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        try {
            control.verify();
            fail("AssertionFailedError expected");
        } catch (AssertionFailedError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    throwsIOException(1): expected: 1, actual: 0",
                    expected.getMessage());
        }

        try {
            mock.throwsIOException(1);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        control.verify();

        try {
            mock.throwsIOException(0);
            fail("AssertionFailedError expected");
        } catch (AssertionFailedError expected) {
            assertEquals("\n  Unexpected method call throwsIOException(0):"
                    + "\n    throwsIOException(0): expected: 2, actual: 3",
                    expected.getMessage());
        }
    }
}
