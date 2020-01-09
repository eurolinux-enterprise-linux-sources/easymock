/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class UsageDefaultReturnValueTest extends TestCase {
    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testDefaultReturnValue() {
        mock.threeArgumentMethod(7, "", "test");
        control.setReturnValue("test", 1);

        mock.threeArgumentMethod(8, null, "test2");
        control.setReturnValue("test2", 1);

        Object defaultValue = new Object();
        control.setDefaultReturnValue(defaultValue);

        control.replay();
        assertEquals("test", mock.threeArgumentMethod(7, "", "test"));
        assertEquals("test2", mock.threeArgumentMethod(8, null, "test2"));
        assertSame(defaultValue, mock.threeArgumentMethod(7, new Object(),
                "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(7, "", "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(8, null, "test"));
        assertSame(defaultValue, mock.threeArgumentMethod(9, null, "test"));

        control.verify();
    }

    public void testDefaultVoidCallable() {

        mock.twoArgumentMethod(1, 2);
        control.setDefaultVoidCallable();

        mock.twoArgumentMethod(1, 1);
        RuntimeException expected = new RuntimeException();
        control.setThrowable(expected);

        control.replay();
        mock.twoArgumentMethod(2, 1);
        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(3, 7);

        try {
            mock.twoArgumentMethod(1, 1);
            fail("RuntimeException expected");
        } catch (RuntimeException actual) {
            assertSame(expected, actual);
        }

    }

    public void testDefaultThrowable() {
        mock.twoArgumentMethod(1, 2);
        control.setVoidCallable();
        mock.twoArgumentMethod(1, 1);
        control.setVoidCallable();

        RuntimeException expected = new RuntimeException();
        control.setDefaultThrowable(expected);

        control.replay();

        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(1, 1);
        try {
            mock.twoArgumentMethod(2, 1);
            fail("RuntimeException expected");
        } catch (RuntimeException actual) {
            assertSame(expected, actual);
        }
    }

    public void testDefaultReturnValueBoolean() {
        mock.booleanReturningMethod(12);
        control.setReturnValue(true);
        control.setDefaultReturnValue(false);

        control.replay();

        assertFalse(mock.booleanReturningMethod(11));
        assertTrue(mock.booleanReturningMethod(12));
        assertFalse(mock.booleanReturningMethod(13));

        control.verify();
    }
}
