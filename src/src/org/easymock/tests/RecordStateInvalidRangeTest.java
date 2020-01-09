/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class RecordStateInvalidRangeTest extends TestCase {

    MockControl control;

    IMethods mock;

    public void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testSetOpenCallCountTwice() {
        mock.simpleMethod();
        control.setVoidCallable(MockControl.ONE_OR_MORE);
        try {
            control.setVoidCallable(MockControl.ONE_OR_MORE);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "last method called on mock already has a non-fixed count set.",
                    expected.getMessage());
        }
    }

    public void testSetIllegalMinimumCount() {
        mock.simpleMethod();
        int NEGATIVE = -1;
        try {
            control.setVoidCallable(NEGATIVE, 2);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("minimum must be >= 0", expected.getMessage());
        }
    }

    public void testSetIllegalMaximumCount() {
        mock.simpleMethod();
        int NON_POSITIVE = 0;
        try {
            control.setVoidCallable(0, NON_POSITIVE);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("maximum must be >= 1", expected.getMessage());
        }
    }

    public void testSetMinimumBiggerThanMaximum() {
        mock.simpleMethod();
        try {
            control.setVoidCallable(4, 3);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("minimum must be <= maximum", expected.getMessage());
        }
    }
}