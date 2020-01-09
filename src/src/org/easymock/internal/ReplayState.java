/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

import org.easymock.ArgumentsMatcher;

public class ReplayState implements IMockControlState {

    private IBehavior behavior;

    public ReplayState(IBehavior behavior) {
        this.behavior = behavior;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        return behavior.addActual(new MethodCall(method, args))
                .returnObjectOrThrowException();
    }

    public void verify() {
        behavior.verify();
    }

    public void replay() {
        throwWrappedIllegalStateException();
    }

    public void setVoidCallable(Range count) {
        throwWrappedIllegalStateException();
    }

    public void setThrowable(Throwable throwable, Range count) {
        throwWrappedIllegalStateException();
    }

    public void setReturnValue(boolean value, Range count) {
        throwWrappedIllegalStateException();
    }

    public void setReturnValue(long value, Range count) {
        throwWrappedIllegalStateException();
    }

    public void setReturnValue(float value, Range count) {
        throwWrappedIllegalStateException();
    }

    public void setReturnValue(double value, Range count) {
        throwWrappedIllegalStateException();
    }

    public void setReturnValue(Object value, Range count) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultVoidCallable() {
        throwWrappedIllegalStateException();
    }

    public void setDefaultThrowable(Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultReturnValue(float value) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultReturnValue(boolean value) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultReturnValue(long value) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultReturnValue(double value) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultReturnValue(Object value) {
        throwWrappedIllegalStateException();
    }

    public void setMatcher(ArgumentsMatcher matcher) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        throwWrappedIllegalStateException();
    }

    private void throwWrappedIllegalStateException() {
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "This method must not be called in replay state."));
    }
}