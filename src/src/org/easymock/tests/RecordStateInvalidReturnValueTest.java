/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class RecordStateInvalidReturnValueTest extends TestCase {
    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testSetInvalidBooleanReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }

    }

    public void testSetInvalidLongReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidFloatReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidDoubleReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidObjectReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidBooleanReturnValueCount() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue(false, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }

    }

    public void testSetInvalidLongReturnValueCount() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((long) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidFloatReturnValueCount() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((float) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidDoubleReturnValueCount() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue((double) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidObjectReturnValueCount() {
        mock.oneArgumentMethod(false);
        try {
            control.setReturnValue(new Object(), 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetReturnValueForVoidMethod() {
        mock.simpleMethod();
        try {
            control.setReturnValue(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("void method cannot return a value", e.getMessage());
        }
    }
}
