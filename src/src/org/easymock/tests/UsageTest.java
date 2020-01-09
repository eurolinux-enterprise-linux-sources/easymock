/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.easymock.MockControl;

public class UsageTest extends TestCase {

    MockControl control;

    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testExactCallCountByLastCall() {
        mock.oneArgumentMethod(false);
        control.setReturnValue("Test");
        control.setReturnValue("Test2");

        control.replay();

        assertEquals("Test", mock.oneArgumentMethod(false));
        assertEquals("Test2", mock.oneArgumentMethod(false));

        boolean failed = false;
        try {
            mock.oneArgumentMethod(false);
        } catch (AssertionFailedError expected) {
            failed = true;
        }
        if (!failed)
            fail("expected AssertionFailedError");
    }

    public void testOpenCallCountByLastCall() {
        mock.oneArgumentMethod(false);
        control.setReturnValue("Test");
        control.setReturnValue("Test2", MockControl.ONE_OR_MORE);

        control.replay();

        assertEquals("Test", mock.oneArgumentMethod(false));
        assertEquals("Test2", mock.oneArgumentMethod(false));
        assertEquals("Test2", mock.oneArgumentMethod(false));
    }

    public void testExactCallCountByLastThrowable() {
        mock.oneArgumentMethod(false);
        control.setReturnValue("Test");
        control.setReturnValue("Test2");
        control.setThrowable(new IndexOutOfBoundsException(), 1);

        control.replay();

        assertEquals("Test", mock.oneArgumentMethod(false));
        assertEquals("Test2", mock.oneArgumentMethod(false));

        try {
            mock.oneArgumentMethod(false);
        } catch (IndexOutOfBoundsException expected) {
        }

        boolean failed = true;
        try {
            try {
                mock.oneArgumentMethod(false);
            } catch (IndexOutOfBoundsException expected) {
            }
            failed = false;
        } catch (AssertionFailedError expected) {
        }
        if (!failed)
            fail("expected AssertionFailedError");
    }

    public void testOpenCallCountByLastThrowable() {
        mock.oneArgumentMethod(false);
        control.setReturnValue("Test");
        control.setReturnValue("Test2");
        control.setThrowable(new IndexOutOfBoundsException(),
                MockControl.ONE_OR_MORE);

        control.replay();

        assertEquals("Test", mock.oneArgumentMethod(false));
        assertEquals("Test2", mock.oneArgumentMethod(false));

        try {
            mock.oneArgumentMethod(false);
        } catch (IndexOutOfBoundsException expected) {
        }
        try {
            mock.oneArgumentMethod(false);
        } catch (IndexOutOfBoundsException expected) {
        }
    }

    public void testMoreThanOneArgument() {
        mock.threeArgumentMethod(1, "2", "3");
        control.setReturnValue("Test", 2);

        control.replay();

        assertEquals("Test", mock.threeArgumentMethod(1, "2", "3"));

        boolean failed = true;
        try {
            control.verify();
            failed = false;
        } catch (AssertionFailedError expected) {
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    threeArgumentMethod(1, \"2\", \"3\"): expected: 2, actual: 1",
                    expected.getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }
    }

    public void testUnexpectedCallWithArray() {
        control.reset();
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        control.replay();
        boolean failed = false;
        String[] strings = new String[] { "Test" };
        try {
            mock.arrayMethod(strings);
        } catch (AssertionFailedError expected) {
            failed = true;
            assertEquals("\n  Unexpected method call arrayMethod([\"Test\"]):"
                    + "\n    arrayMethod([\"Test\"]): expected: 0, actual: 1",
                    expected.getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }

    }

    public void testWrongArguments() {
        mock.simpleMethodWithArgument("3");
        control.replay();

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (AssertionFailedError expected) {
            assertEquals(
                    "\n  Unexpected method call simpleMethodWithArgument(\"5\"):"
                            + "\n    simpleMethodWithArgument(\"5\"): expected: 0, actual: 1"
                            + "\n    simpleMethodWithArgument(\"3\"): expected: 1, actual: 0",
                    expected.getMessage());
        }

    }

    public void testSummarizeSameObjectArguments() {
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");
        control.replay();

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (AssertionFailedError expected) {
            assertEquals(
                    "\n  Unexpected method call simpleMethodWithArgument(\"5\"):"
                            + "\n    simpleMethodWithArgument(\"5\"): expected: 0, actual: 1"
                            + "\n    simpleMethodWithArgument(\"3\"): expected: 2, actual: 0",
                    expected.getMessage());
        }

    }

    public void testArgumentsOrdered() {
        mock.simpleMethodWithArgument("4");
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("0");
        mock.simpleMethodWithArgument("1");
        control.replay();

        try {
            mock.simpleMethodWithArgument("5");
            fail();
        } catch (AssertionFailedError expected) {
            assertEquals(
                    "\n  Unexpected method call simpleMethodWithArgument(\"5\"):"
                            + "\n    simpleMethodWithArgument(\"5\"): expected: 0, actual: 1"
                            + "\n    simpleMethodWithArgument(\"4\"): expected: 1, actual: 0"
                            + "\n    simpleMethodWithArgument(\"3\"): expected: 1, actual: 0"
                            + "\n    simpleMethodWithArgument(\"2\"): expected: 1, actual: 0"
                            + "\n    simpleMethodWithArgument(\"0\"): expected: 1, actual: 0"
                            + "\n    simpleMethodWithArgument(\"1\"): expected: 1, actual: 0",
                    expected.getMessage());
        }

    }

}