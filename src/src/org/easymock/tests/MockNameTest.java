/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class MockNameTest extends TestCase {

    private MockControl control;

    public void testDefaultName() {
        control = MockControl.createControl(IMethods.class);
        String expected = "EasyMock for " + IMethods.class.toString();
        String actual = control.getMock().toString();
        assertEquals(expected, actual);
    }
}
