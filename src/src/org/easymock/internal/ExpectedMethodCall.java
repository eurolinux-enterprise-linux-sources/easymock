/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

import org.easymock.ArgumentsMatcher;

public class ExpectedMethodCall {

    private final MethodCall methodCall;

    private final ArgumentsMatcher matcher;

    public ExpectedMethodCall(Method method, Object[] arguments,
            ArgumentsMatcher matcher) {
        this.methodCall = new MethodCall(method, arguments);
        this.matcher = matcher;
    }

    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;

        ExpectedMethodCall other = (ExpectedMethodCall) o;
        return this.methodCall.equals(other.methodCall)
                && this.matcher.equals(other.matcher);
    }

    public int hashCode() {
        return methodCall.hashCode();
    }

    public boolean matches(MethodCall actualCall) {
        return this.methodCall.matches(actualCall, matcher);
    }

    public boolean matches(Method method, Object[] arguments) {
        return matches(new MethodCall(method, arguments));
    }

    public String toString() {
        return methodCall.toString(matcher);
    }
}
