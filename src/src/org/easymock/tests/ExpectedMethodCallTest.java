/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.internal.ExpectedMethodCall;

public class ExpectedMethodCallTest extends TestCase {

    private ExpectedMethodCall call;

    protected void setUp() throws SecurityException, NoSuchMethodException {
        Object[] arguments1 = new Object[] { "" };
        Method m = Object.class.getMethod("equals",
                new Class[] { Object.class });
        call = new ExpectedMethodCall(m, arguments1, MockControl.EQUALS_MATCHER);
    }

    public void testHashCode() {
        try {
            call.hashCode();
            fail();
        } catch (UnsupportedOperationException expected) {
            assertEquals("hashCode() is not implemented", expected.getMessage());
        }
    }
}