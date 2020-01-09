/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class UsageOverloadedMethodTest extends TestCase {

    MockControl controller;

    IMethods mock;

    protected void setUp() {
        controller = MockControl.createControl(IMethods.class);
        mock = (IMethods) controller.getMock();
    }

    public void testOverloading() {

        mock.oneArgumentMethod(true);
        controller.setReturnValue("true");
        mock.oneArgumentMethod(false);
        controller.setReturnValue("false");

        mock.oneArgumentMethod((byte) 0);
        controller.setReturnValue("byte 0");
        mock.oneArgumentMethod((byte) 1);
        controller.setReturnValue("byte 1");

        mock.oneArgumentMethod((short) 0);
        controller.setReturnValue("short 0");
        mock.oneArgumentMethod((short) 1);
        controller.setReturnValue("short 1");

        mock.oneArgumentMethod((char) 0);
        controller.setReturnValue("char 0");
        mock.oneArgumentMethod((char) 1);
        controller.setReturnValue("char 1");

        mock.oneArgumentMethod(0);
        controller.setReturnValue("int 0");
        mock.oneArgumentMethod(1);
        controller.setReturnValue("int 1");

        mock.oneArgumentMethod((long) 0);
        controller.setReturnValue("long 0");
        mock.oneArgumentMethod((long) 1);
        controller.setReturnValue("long 1");

        mock.oneArgumentMethod((float) 0);
        controller.setReturnValue("float 0");
        mock.oneArgumentMethod((float) 1);
        controller.setReturnValue("float 1");

        mock.oneArgumentMethod(0.0);
        controller.setReturnValue("double 0");
        mock.oneArgumentMethod(1.0);
        controller.setReturnValue("double 1");

        mock.oneArgumentMethod("Object 0");
        controller.setReturnValue("1");
        mock.oneArgumentMethod("Object 1");
        controller.setReturnValue("2");

        controller.replay();

        assertEquals("true", mock.oneArgumentMethod(true));
        assertEquals("false", mock.oneArgumentMethod(false));

        assertEquals("byte 0", mock.oneArgumentMethod((byte) 0));
        assertEquals("byte 1", mock.oneArgumentMethod((byte) 1));

        assertEquals("short 0", mock.oneArgumentMethod((short) 0));
        assertEquals("short 1", mock.oneArgumentMethod((short) 1));

        assertEquals("char 0", mock.oneArgumentMethod((char) 0));
        assertEquals("char 1", mock.oneArgumentMethod((char) 1));

        assertEquals("int 0", mock.oneArgumentMethod(0));
        assertEquals("int 1", mock.oneArgumentMethod(1));

        assertEquals("long 0", mock.oneArgumentMethod((long) 0));
        assertEquals("long 1", mock.oneArgumentMethod((long) 1));

        assertEquals("float 0", mock.oneArgumentMethod((float) 0.0));
        assertEquals("float 1", mock.oneArgumentMethod((float) 1.0));

        assertEquals("double 1", mock.oneArgumentMethod(1.0));
        assertEquals("double 0", mock.oneArgumentMethod(0.0));

        assertEquals("1", mock.oneArgumentMethod("Object 0"));
        assertEquals("2", mock.oneArgumentMethod("Object 1"));

        controller.verify();
    }

    public void testNullReturnValue() {

        mock.oneArgumentMethod("Object");
        controller.setReturnValue(null);

        controller.replay();

        assertNull(mock.oneArgumentMethod("Object"));

    }

    public void testMoreThanOneResultAndOpenCallCount() {
        mock.oneArgumentMethod(true);
        controller.setReturnValue("First Result", 4);
        controller.setReturnValue("Second Result", 2);
        controller.setThrowable(new RuntimeException("Third Result"), 3);
        controller.setReturnValue("Following Result", MockControl.ONE_OR_MORE);

        controller.replay();

        assertEquals("First Result", mock.oneArgumentMethod(true));
        assertEquals("First Result", mock.oneArgumentMethod(true));
        assertEquals("First Result", mock.oneArgumentMethod(true));
        assertEquals("First Result", mock.oneArgumentMethod(true));

        assertEquals("Second Result", mock.oneArgumentMethod(true));
        assertEquals("Second Result", mock.oneArgumentMethod(true));

        try {
            mock.oneArgumentMethod(true);
            fail("expected exception");
        } catch (RuntimeException expected) {
            assertEquals("Third Result", expected.getMessage());
        }

        try {
            mock.oneArgumentMethod(true);
            fail("expected exception");
        } catch (RuntimeException expected) {
            assertEquals("Third Result", expected.getMessage());
        }

        try {
            mock.oneArgumentMethod(true);
            fail("expected exception");
        } catch (RuntimeException expected) {
            assertEquals("Third Result", expected.getMessage());
        }

        assertEquals("Following Result", mock.oneArgumentMethod(true));
        assertEquals("Following Result", mock.oneArgumentMethod(true));
        assertEquals("Following Result", mock.oneArgumentMethod(true));
        assertEquals("Following Result", mock.oneArgumentMethod(true));
        assertEquals("Following Result", mock.oneArgumentMethod(true));

        controller.verify();
    }
}