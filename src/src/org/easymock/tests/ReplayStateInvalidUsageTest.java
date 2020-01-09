/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.extensions.ExceptionTestCase;

import org.easymock.MockControl;

public class ReplayStateInvalidUsageTest extends ExceptionTestCase {

    private MockControl control;

    private Exception exception;

    public ReplayStateInvalidUsageTest(String name) {
        super(name, IllegalStateException.class);
    }

    protected void setUp() {
        exception = new Exception();
        control = MockControl.createControl(IMethods.class);
        control.replay();
    }

    public void testExpectAndThrowObjectWithMinMax() {
        control.expectAndThrow("", exception, 1, 2);
    }

    public void testExpectAndThrowDoubleWithMinMax() {
        control.expectAndThrow(0.0d, exception, 1, 2);
    }

    public void testExpectAndThrowFloatWithMinMax() {
        control.expectAndThrow(0.0f, exception, 1, 2);
    }

    public void testExpectAndThrowLongWithMinMax() {
        control.expectAndThrow(0, exception, 1, 2);
    }

    public void testExpectAndThrowBooleanWithMinMax() {
        control.expectAndThrow(false, exception, 1, 2);
    }

    public void testExpectAndThrowObjectWithCount() {
        control.expectAndThrow("", exception, 1);
    }

    public void testExpectAndThrowDoubleWithCount() {
        control.expectAndThrow(0.0d, exception, 1);
    }

    public void testExpectAndThrowFloatWithCount() {
        control.expectAndThrow(0.0f, exception, 1);
    }

    public void testExpectAndThrowLongWithCount() {
        control.expectAndThrow(0, exception, 1);
    }

    public void testExpectAndThrowBooleanWithCount() {
        control.expectAndThrow(false, exception, 1);
    }

    public void testExpectAndThrowObjectWithRange() {
        control.expectAndThrow("", exception, MockControl.ONE);
    }

    public void testExpectAndThrowDoubleWithRange() {
        control.expectAndThrow(0.0d, exception, MockControl.ONE);
    }

    public void testExpectAndThrowFloatWithRange() {
        control.expectAndThrow(0.0f, exception, MockControl.ONE);
    }

    public void testExpectAndThrowLongWithRange() {
        control.expectAndThrow(0, exception, MockControl.ONE);
    }

    public void testExpectAndThrowBooleanWithRange() {
        control.expectAndThrow(false, exception, MockControl.ONE);
    }

    public void testExpectAndThrowObject() {
        control.expectAndThrow("", exception);
    }

    public void testExpectAndThrowDouble() {
        control.expectAndThrow(0.0d, exception);
    }

    public void testExpectAndThrowFloat() {
        control.expectAndThrow(0.0f, exception);
    }

    public void testExpectAndThrowLong() {
        control.expectAndThrow(0, exception);
    }

    public void testExpectAndThrowBoolean() {
        control.expectAndThrow(false, exception);
    }

    public void testExpectAndReturnObjectWithMinMax() {
        control.expectAndReturn("", "", 1, 2);
    }

    public void testExpectAndReturnDoubleWithMinMax() {
        control.expectAndReturn(0.0d, 0.0d, 1, 2);
    }

    public void testExpectAndReturnFloatWithMinMax() {
        control.expectAndReturn(0.0f, 0.0f, 1, 2);
    }

    public void testExpectAndReturnLongWithMinMax() {
        control.expectAndReturn(0, 0, 1, 2);
    }

    public void testExpectAndReturnBooleanWithMinMax() {
        control.expectAndReturn(false, false, 1, 2);
    }

    public void testExpectAndReturnObjectWithCount() {
        control.expectAndReturn("", "", 1);
    }

    public void testExpectAndReturnDoubleWithCount() {
        control.expectAndReturn(0.0d, 0.0d, 1);
    }

    public void testExpectAndReturnFloatWithCount() {
        control.expectAndReturn(0.0f, 0.0f, 1);
    }

    public void testExpectAndReturnLongWithCount() {
        control.expectAndReturn(0, 0, 1);
    }

    public void testExpectAndReturnBooleanWithCount() {
        control.expectAndReturn(false, false, 1);
    }

    public void testExpectAndReturnObjectWithRange() {
        control.expectAndReturn("", "", MockControl.ONE);
    }

    public void testExpectAndReturnDoubleWithRange() {
        control.expectAndReturn(0.0d, 0.0d, MockControl.ONE);
    }

    public void testExpectAndReturnFloatWithRange() {
        control.expectAndReturn(0.0f, 0.0f, MockControl.ONE);
    }

    public void testExpectAndReturnLongWithRange() {
        control.expectAndReturn(0, 0, MockControl.ONE);
    }

    public void testExpectAndReturnBooleanWithRange() {
        control.expectAndReturn(false, false, MockControl.ONE);
    }

    public void testExpectAndReturnObject() {
        control.expectAndReturn("", "");
    }

    public void testExpectAndReturnDouble() {
        control.expectAndReturn(0.0d, 0.0d);
    }

    public void testExpectAndReturnFloat() {
        control.expectAndReturn(0.0f, 0.0f);
    }

    public void testExpectAndReturnLong() {
        control.expectAndReturn(0, 0);
    }

    public void testExpectAndReturnBoolean() {
        control.expectAndReturn(false, false);
    }

    public void testSetDefaultMatcher() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
    }

    public void testSetReturnValueObjectWithMinMax() {
        control.setReturnValue("", 1, 2);
    }

    public void testSetReturnValueDoubleWithMinMax() {
        control.setReturnValue(0.0d, 1, 2);
    }

    public void testSetReturnValueFloatWithMinMax() {
        control.setReturnValue(0.0f, 1, 2);
    }

    public void testSetReturnValueLongWithMinMax() {
        control.setReturnValue(0, 1, 2);
    }

    public void testSetReturnValueBooleanWithMinMax() {
        control.setReturnValue(false, 1, 2);
    }

    public void testSetThrowableWithMinMax() {
        control.setThrowable(exception, 1, 2);
    }

    public void testSetVoidCallableWithMinMax() {
        control.setVoidCallable(1, 2);
    }

    public void testSetMatcher() {
        control.setMatcher(MockControl.ARRAY_MATCHER);
    }

    public void testSetDefaultReturnValueObject() {
        control.setDefaultReturnValue("");
    }

    public void testSetDefaultReturnValueDouble() {
        control.setDefaultReturnValue(0.0d);
    }

    public void testSetDefaultReturnValueFloat() {
        control.setDefaultReturnValue(0.0f);
    }

    public void testSetDefaultReturnValueLong() {
        control.setDefaultReturnValue(0);
    }

    public void testSetDefaultReturnValueBoolean() {
        control.setDefaultReturnValue(false);
    }

    public void testSetDefaultThrowable() {
        control.setDefaultThrowable(exception);
    }

    public void testSetDefaultVoidCallable() {
        control.setDefaultVoidCallable();
    }

    public void testSetReturnValueObjectWithRange() {
        control.setReturnValue("", MockControl.ONE);
    }

    public void testSetReturnValueLongWithRange() {
        control.setReturnValue(0, MockControl.ONE);
    }

    public void testSetReturnValueFloatWithRange() {
        control.setReturnValue(0.0f, MockControl.ONE);
    }

    public void testSetReturnValueDoubleWithRange() {
        control.setReturnValue(0.0d, MockControl.ONE);
    }

    public void testSetReturnValueBooleanWithRange() {
        control.setReturnValue(false, MockControl.ONE);
    }

    public void testSetThrowableWithRange() {
        control.setThrowable(exception, MockControl.ONE);
    }

    public void testSetVoidCallableWithRange() {
        control.setVoidCallable(MockControl.ONE);
    }

    public void testSetReturnValueObjectWithCount() {
        control.setReturnValue("", 1);
    }

    public void testSetReturnValueLongWithCount() {
        control.setReturnValue(0, 1);
    }

    public void testSetReturnValueFloatWithCount() {
        control.setReturnValue(0.0f, 1);
    }

    public void testSetReturnValueDoubleWithCount() {
        control.setReturnValue(0.0d, 1);
    }

    public void testSetReturnValueBooleanWithCount() {
        control.setReturnValue(false, 1);
    }

    public void testSetThrowableWithCount() {
        control.setThrowable(exception, 1);
    }

    public void testSetVoidCallableWithCount() {
        control.setVoidCallable(1);
    }

    public void testSetReturnValueObject() {
        control.setReturnValue("");
    }

    public void testSetReturnValueDouble() {
        control.setReturnValue(0.0d);
    }

    public void testSetReturnValueFloat() {
        control.setReturnValue(0.0f);
    }

    public void testSetReturnValueLong() {
        control.setReturnValue(0);
    }

    public void testSetReturnValueBoolean() {
        control.setReturnValue(false);
    }

    public void testSetThrowable() {
        control.setThrowable(exception);
    }

    public void testSetVoidCallable() {
        control.setVoidCallable();
    }

    public void testReplay() {
        control.replay();
    }
}