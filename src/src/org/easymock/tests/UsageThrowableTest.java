/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class UsageThrowableTest extends TestCase {

    public void testNoUpperLimit() {
        mock.simpleMethodWithArgument("1");
        control.setVoidCallable(MockControl.ONE_OR_MORE);
        mock.simpleMethodWithArgument("2");
        control.replay();
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("1");
        control.verify();
    }

    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testThrowRuntimeException() {
        testThrowUncheckedException(new RuntimeException());
    }

    public void testThrowSubclassOfRuntimeException() {
        testThrowUncheckedException(new RuntimeException() {
        });
    }

    public void testThrowError() {
        testThrowUncheckedException(new Error());
    }

    public void testThrowSubclassOfError() {
        testThrowUncheckedException(new Error() {
        });
    }

    private void testThrowUncheckedException(Throwable throwable) {
        mock.throwsNothing(true);
        control.setReturnValue("true");
        mock.throwsNothing(false);
        control.setThrowable(throwable);

        control.replay();

        try {
            mock.throwsNothing(false);
            fail("Trowable expected");
        } catch (Throwable expected) {
            assertSame(throwable, expected);
        }

        assertEquals("true", mock.throwsNothing(true));
    }

    public void testThrowCheckedException() throws IOException {
        testThrowCheckedException(new IOException());
    }

    public void testThrowSubclassOfCheckedException() throws IOException {
        testThrowCheckedException(new IOException() {
        });
    }

    private void testThrowCheckedException(IOException expected)
            throws IOException {
        try {
            mock.throwsIOException(0);
            control.setReturnValue("Value 0");
            mock.throwsIOException(1);
            control.setThrowable(expected);
            mock.throwsIOException(2);
            control.setReturnValue("Value 2");
        } catch (IOException e) {
            fail("Unexpected Exception");
        }

        control.replay();

        assertEquals("Value 0", mock.throwsIOException(0));
        assertEquals("Value 2", mock.throwsIOException(2));

        try {
            mock.throwsIOException(1);
            fail("IOException expected");
        } catch (IOException expectedException) {
            assertSame(expectedException, expected);
        }
    }

    public void testThrowAfterReturnValue() {
        mock.throwsNothing(false);
        control.setReturnValue("");
        RuntimeException expectedException = new RuntimeException();
        control.setThrowable(expectedException);

        control.replay();

        assertEquals("", mock.throwsNothing(false));

        try {
            mock.throwsNothing(false);
            fail("RuntimeException expected");
        } catch (RuntimeException actualException) {
            assertSame(expectedException, actualException);
        }

        control.verify();
    }

}