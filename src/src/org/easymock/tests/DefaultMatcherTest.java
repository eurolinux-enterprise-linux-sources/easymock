/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class DefaultMatcherTest extends TestCase {

    public static interface ArrayInterface {
        void methodA(int[] argument);

        void methodB(int[] argument);
    }

    private MockControl control;

    private ArrayInterface mock;

    protected void setUp() {
        control = MockControl.createControl(ArrayInterface.class);
        mock = (ArrayInterface) control.getMock();
    }

    public void testDefaultMatcher() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);

        mock.methodA(new int[] { 1, 1 });
        mock.methodB(new int[] { 2, 2 });

        control.replay();

        mock.methodA(new int[] { 1, 1 });
        mock.methodB(new int[] { 2, 2 });

        control.verify();
    }

    public void testFailInReplayState() {
        control.replay();
        try {
            control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    public void testFailIfDefaultMatcherSetTwice() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "default matcher can only be set once directly after creation of the MockControl",
                    expected.getMessage());
        }
    }

    public void testFailIfDefaultMatcherSetTooLate() {
        mock.methodA(new int[] { 1, 1 });
        control.setVoidCallable();
        try {
            control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "default matcher can only be set once directly after creation of the MockControl",
                    expected.getMessage());
        }
    }
}
