/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.io.IOException;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.easymock.MockControl;

public class LegacyBehaviorTests extends TestCase {

    public void testThrowAfterThrowable() throws IOException {

        MockControl control = MockControl.createControl(IMethods.class);
        IMethods mock = (IMethods) control.getMock();

        mock.throwsIOException(0);
        control.setThrowable(new IOException());
        control.setThrowable(new IOException(), MockControl.ONE_OR_MORE);

        control.replay();

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        boolean exceptionOccured = true;
        try {
            control.verify();
            exceptionOccured = false;
        } catch (AssertionFailedError expected) {
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    throwsIOException(0): expected: at least 2, actual: 1",
                    expected.getMessage());
        }

        if (!exceptionOccured)
            fail("exception expected");

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        control.verify();

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        control.verify();
    }
}
