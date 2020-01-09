/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class NiceMockControlLongCompatibleReturnValueTest extends TestCase {

    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createNiceControl(IMethods.class);
        mock = (IMethods) control.getMock();
        control.replay();
    }

    public void testByteReturningValue() {
        assertEquals(0, mock.byteReturningMethod(12));
        control.verify();
    }

    public void testShortReturningValue() {
        assertEquals(0, mock.shortReturningMethod(12));
        control.verify();
    }

    public void testCharReturningValue() {
        assertEquals(0, mock.charReturningMethod(12));
        control.verify();
    }

    public void testIntReturningValue() {
        assertEquals(0, mock.intReturningMethod(12));
        control.verify();
    }

    public void testLongReturningValue() {
        assertEquals(0, mock.longReturningMethod(12));
        control.verify();
    }
}