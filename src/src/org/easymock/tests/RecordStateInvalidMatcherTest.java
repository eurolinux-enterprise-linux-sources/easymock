/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class RecordStateInvalidMatcherTest extends TestCase {
    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testSetMatcherBeforeCallingMethods() {
        try {
            control.setMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting matcher",
                    expected.getMessage());
        }
    }

    public void testSetMatcherTwice() {
        mock.simpleMethod();
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.EQUALS_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "for method simpleMethod(), a matcher has already been set",
                    expected.getMessage());
        }
    }

    public void testSetMatcherTwice2() {
        mock.simpleMethodWithArgument("");
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.EQUALS_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "for method simpleMethodWithArgument(...), a matcher has already been set",
                    expected.getMessage());
        }
    }

    public void testSetSameMatcherTwice() {
        mock.simpleMethod();
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.ARRAY_MATCHER);
        } catch (IllegalStateException unexpected) {
            fail("setting the same matcher should work");
        }
    }
}
