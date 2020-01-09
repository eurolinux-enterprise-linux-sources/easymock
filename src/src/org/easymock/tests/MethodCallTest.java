/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.internal.MethodCall;

public class MethodCallTest extends TestCase {

    private MethodCall call;

    private MethodCall equalCall;

    private MethodCall nonEqualCall;

    protected void setUp() throws SecurityException, NoSuchMethodException {
        Object[] arguments1 = new Object[] { "" };
        Object[] arguments2 = new Object[] { "" };
        Object[] arguments3 = new Object[] { "X" };
        Method m = Object.class.getMethod("equals",
                new Class[] { Object.class });
        call = new MethodCall(m, arguments1);
        equalCall = new MethodCall(m, arguments2);
        nonEqualCall = new MethodCall(m, arguments3);
    }

    public void testEquals() {
        assertFalse(call.equals(null));
        assertFalse(call.equals(""));
        assertTrue(call.equals(equalCall));
        assertFalse(call.equals(nonEqualCall));
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