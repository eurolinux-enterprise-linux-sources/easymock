/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.internal.ReplayState;

public class UsageStrictMockTest extends TestCase {
    private MockControl control;

    private IMethods mock;

    protected void setUp() {
        control = MockControl.createStrictControl(IMethods.class);
        mock = (IMethods) control.getMock();

        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        control.replay();
    }

    public void testOrderedCallsSucces() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        control.verify();
    }

    public void testUnorderedCallsFailure() {
        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("2");
        } catch (AssertionFailedError expected) {
            failed = true;
        }
        if (!failed) {
            fail("unordered calls accepted");
        }
    }

    public void testTooManyCallsFailure() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("2");
        } catch (AssertionFailedError expected) {
            failed = true;
        }
        if (!failed) {
            fail("too many calls accepted");
        }
    }

    public void testTooFewCallsFailure() {
        mock.simpleMethodWithArgument("1");
        boolean failed = false;
        try {
            control.verify();
        } catch (AssertionFailedError expected) {
            failed = true;
            assertTrue("stack trace must be filled in", Util.getStackTrace(
                    expected).indexOf(ReplayState.class.getName()) == -1);
        }
        if (!failed) {
            fail("too few calls accepted");
        }
    }

    public void testDifferentMethods() {

        control.reset();

        mock.booleanReturningMethod(0);
        control.setReturnValue(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        control.setReturnValue(false, 2, 3);
        mock.simpleMethod();
        control.setVoidCallable(MockControl.ONE_OR_MORE);

        control.replay();
        assertEquals(true, mock.booleanReturningMethod(0));
        mock.simpleMethod();

        boolean failed = false;
        try {
            control.verify();
        } catch (AssertionFailedError expected) {
            failed = true;
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    simpleMethod(): expected: 1, actual: 1"
                            + "\n    booleanReturningMethod(1): expected: between 2 and 3, actual: 0"
                            + "\n    simpleMethod(): expected: at least 1, actual: 0",
                    expected.getMessage());
        }
        if (!failed) {
            fail("too few calls accepted");
        }

        assertEquals(false, mock.booleanReturningMethod(1));

        failed = false;
        try {
            mock.simpleMethod();
        } catch (AssertionFailedError expected) {
            failed = true;
            assertEquals(
                    "\n  Unexpected method call simpleMethod():"
                            + "\n    simpleMethod(): expected: 0, actual: 1"
                            + "\n    booleanReturningMethod(1): expected: between 2 and 3, actual: 1",
                    expected.getMessage());
        }
        if (!failed) {
            fail("wrong call accepted");
        }
    }

    public void testRange() {

        control.reset();

        mock.booleanReturningMethod(0);
        control.setReturnValue(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        control.setReturnValue(false, 2, 3);
        mock.simpleMethod();
        control.setVoidCallable(MockControl.ONE_OR_MORE);
        mock.booleanReturningMethod(1);
        control.setReturnValue(false);

        control.replay();

        mock.booleanReturningMethod(0);
        mock.simpleMethod();

        mock.booleanReturningMethod(1);
        mock.booleanReturningMethod(1);
        mock.booleanReturningMethod(1);

        boolean failed = false;

        try {
            mock.booleanReturningMethod(1);
        } catch (AssertionFailedError expected) {
            failed = true;
            assertEquals(
                    "\n  Unexpected method call booleanReturningMethod(1):"
                            + "\n    booleanReturningMethod(1): expected: between 2 and 3, actual: 4"
                            + "\n    simpleMethod(): expected: at least 1, actual: 0",
                    expected.getMessage());
        }
        if (!failed) {
            fail("too many calls accepted");
        }
    }

    public void testDefaultBehavior() {
        control.reset();

        mock.booleanReturningMethod(1);
        control.setReturnValue(true);
        control.setReturnValue(false);
        control.setReturnValue(true);
        control.setDefaultReturnValue(true);

        control.replay();

        assertEquals(true, mock.booleanReturningMethod(2));
        assertEquals(true, mock.booleanReturningMethod(3));
        assertEquals(true, mock.booleanReturningMethod(1));
        assertEquals(false, mock.booleanReturningMethod(1));
        assertEquals(true, mock.booleanReturningMethod(3));

        boolean failed = false;
        try {
            control.verify();
        } catch (AssertionFailedError expected) {
            failed = true;
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    booleanReturningMethod(1): expected: 3, actual: 2",
                    expected.getMessage());
        }
        if (!failed) {
            fail("too few calls accepted");
        }
    }

    public void testUnexpectedCallWithArray() {
        control.reset();
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        mock.arrayMethod(new String[] { "Test", "Test 2" });
        control.replay();
        boolean failed = false;
        String[] strings = new String[] { "Test" };
        try {
            mock.arrayMethod(strings);
        } catch (AssertionFailedError expected) {
            failed = true;
            assertEquals(
                    "\n  Unexpected method call arrayMethod([\"Test\"]):"
                            + "\n    arrayMethod([\"Test\"]): expected: 0, actual: 1"
                            + "\n    arrayMethod([\"Test\", \"Test 2\"]): expected: 1, actual: 0",
                    expected.getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }

    }
}
