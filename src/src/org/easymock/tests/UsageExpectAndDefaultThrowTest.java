/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.MockControl;

/**
 * Same as UsageExpectAndThrowTest except that each mocked method is called
 * twice to make sure the defaulting works fine.
 * 
 * @author Henri Tremblay
 */
public class UsageExpectAndDefaultThrowTest extends TestCase {
    private MockControl control;

    private IMethods mock;

    private static RuntimeException EXCEPTION = new RuntimeException();

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testBoolean() {
        control
                .expectAndDefaultThrow(mock.booleanReturningMethod(4),
                        EXCEPTION);
        control.replay();
        try {
            mock.booleanReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.booleanReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testLong() {
        control.expectAndDefaultThrow(mock.longReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.longReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.longReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testFloat() {
        control.expectAndDefaultThrow(mock.floatReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.floatReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.floatReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testDouble() {
        control.expectAndDefaultThrow(mock.doubleReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.doubleReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.doubleReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testObject() {
        control.expectAndDefaultThrow(mock.objectReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.objectReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.objectReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

}
