/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class RecordStateMethodCallMissingTest extends TestCase {
    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testSetBooleanReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetLongReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetFloatReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue((float) 0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetDoubleReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetObjectReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetVoidCallableWithoutMethodCall() {
        try {
            control.setVoidCallable();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    public void testSetThrowableWithoutMethodCall() {
        try {
            control.setThrowable(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting Throwable",
                    expected.getMessage());
        }
    }

    public void testSetBooleanReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue(false, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetLongReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue(0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetFloatReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue((float) 0.0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetDoubleReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue(0.0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetObjectReturnValueCountWithoutMethodCall() {
        try {
            control.setReturnValue(null, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    public void testSetVoidCallableCountWithoutMethodCall() {
        try {
            control.setVoidCallable(3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    public void testSetThrowableCountWithoutMethodCall() {
        try {
            control.setThrowable(new RuntimeException(), 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting Throwable",
                    expected.getMessage());
        }
    }

    public void testSetBooleanDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    public void testSetLongDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    public void testSetFloatDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue((float) 0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    public void testSetDoubleDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(0.0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    public void testSetObjectDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    expected.getMessage());
        }
    }

    public void testSetDefaultVoidCallableWithoutMethodCall() {
        try {
            control.setDefaultVoidCallable();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default void callable",
                    expected.getMessage());
        }
    }

    public void testSetDefaultThrowableWithoutMethodCall() {
        try {
            control.setDefaultThrowable(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default Throwable",
                    expected.getMessage());
        }
    }

}
