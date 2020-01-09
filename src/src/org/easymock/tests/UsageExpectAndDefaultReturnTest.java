/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 *
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

/**
 * Same as UsageExpectAndReturnTest except that each mocked method is called
 * twice to make sure the defaulting works fine.
 * 
 * @author Henri Tremblay
 */
public class UsageExpectAndDefaultReturnTest extends TestCase {
    private MockControl control;

    private IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testBoolean() {
        control.expectAndDefaultReturn(mock.booleanReturningMethod(4), true);
        control.replay();
        assertEquals(true, mock.booleanReturningMethod(4));
        assertEquals(true, mock.booleanReturningMethod(4));
        control.verify();
    }

    public void testLong() {
        control.expectAndDefaultReturn(mock.longReturningMethod(4), 12);
        control.replay();
        assertEquals(12, mock.longReturningMethod(4));
        assertEquals(12, mock.longReturningMethod(4));
        control.verify();
    }

    public void testFloat() {
        control.expectAndDefaultReturn(mock.floatReturningMethod(4), 12f);
        control.replay();
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        control.verify();
    }

    public void testDouble() {
        control.expectAndDefaultReturn(mock.doubleReturningMethod(4), 12.0);
        control.replay();
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        control.verify();
    }

    public void testObject() {
        control.expectAndDefaultReturn(mock.objectReturningMethod(4), "12");
        control.replay();
        assertEquals("12", mock.objectReturningMethod(4));
        assertEquals("12", mock.objectReturningMethod(4));
        control.verify();
    }

}
