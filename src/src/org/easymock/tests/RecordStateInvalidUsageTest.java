/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class RecordStateInvalidUsageTest extends TestCase {

    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testSetReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetExpectedVoidCallCountWithoutMethodCall() {
        try {
            control.setVoidCallable(3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    public void testOpenVoidCallCountWithoutMethodCall() {
        try {
            control.setVoidCallable();
            fail("IllegalStateException expected");
        } catch (Exception expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    public void testSetWrongReturnValueBoolean() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    public void testSetWrongReturnValueShort() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((short) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    public void testSetWrongReturnValueChar() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((char) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    public void testSetWrongReturnValueInt() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((int) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    public void testSetWrongReturnValueLong() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    public void testSetWrongReturnValueFloat() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    public void testSetWrongReturnValueDouble() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    public void testSetWrongReturnValueObject() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }
}