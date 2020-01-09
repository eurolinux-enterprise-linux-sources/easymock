/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.internal.ExpectedMethodCall;

public class MatchableArgumentsTest extends TestCase {

    private Object[] arguments;

    private Object[] arguments2;

    protected void setUp() {
        arguments = new Object[] { "" };
        arguments2 = new Object[] { "", "" };
    }

    public void testEquals() throws SecurityException, NoSuchMethodException {
        Method toPreventNullPointerExceptionm = Object.class.getMethod(
                "toString", new Class[] {});

        ExpectedMethodCall matchableArguments = new ExpectedMethodCall(
                toPreventNullPointerExceptionm, arguments,
                MockControl.EQUALS_MATCHER);
        ExpectedMethodCall nonEqualMatchableArguments = new ExpectedMethodCall(
                toPreventNullPointerExceptionm, arguments2,
                MockControl.EQUALS_MATCHER);

        assertFalse(matchableArguments.equals(null));
        assertFalse(matchableArguments.equals(nonEqualMatchableArguments));
    }
}