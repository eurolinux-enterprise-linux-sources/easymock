/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.util.Iterator;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.easymock.MockControl;

public class UsageRangeTest extends TestCase {

    private Iterator mock;

    private MockControl control;

    protected void setUp() {
        control = MockControl.createStrictControl(Iterator.class);
        mock = (Iterator) control.getMock();
    }

    public void testZeroOrMoreNoCalls() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        control.verify();
    }

    public void testZeroOrMoreOneCall() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        assertFalse(mock.hasNext());
        control.verify();
    }

    public void testZeroOrMoreThreeCalls() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        assertFalse(mock.hasNext());
        assertFalse(mock.hasNext());
        assertFalse(mock.hasNext());
        control.verify();
    }

    public void testCombination() {
        mock.hasNext();
        control.setReturnValue(true, MockControl.ONE_OR_MORE);
        mock.next();
        control.setReturnValue("1");

        mock.hasNext();
        control.setReturnValue(true, MockControl.ONE_OR_MORE);
        mock.next();
        control.setReturnValue("2");

        mock.hasNext();
        control.setReturnValue(false, MockControl.ONE_OR_MORE);

        control.replay();

        assertTrue(mock.hasNext());
        assertTrue(mock.hasNext());
        assertTrue(mock.hasNext());

        assertEquals("1", mock.next());

        try {
            mock.next();
            fail();
        } catch (AssertionFailedError expected) {
        }

        assertTrue(mock.hasNext());

        assertEquals("2", mock.next());

        assertFalse(mock.hasNext());

        control.verify();

    }
}
