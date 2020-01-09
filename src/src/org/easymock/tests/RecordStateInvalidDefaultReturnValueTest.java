/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class RecordStateInvalidDefaultReturnValueTest extends TestCase {
    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testSetInvalidDefaultBooleanReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setDefaultReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidDefaultLongReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setDefaultReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidDefaultFloatReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setDefaultReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidDefaultDoubleReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setDefaultReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    public void testSetInvalidObjectDefaultReturnValue() {
        mock.oneArgumentMethod(false);
        try {
            control.setDefaultReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }
}
