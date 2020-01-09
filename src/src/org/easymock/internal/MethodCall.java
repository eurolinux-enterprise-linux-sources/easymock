/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

import org.easymock.ArgumentsMatcher;

public class MethodCall {
    private final Method method;

    private final Object[] arguments;

    public MethodCall(Method method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }

        MethodCall other = (MethodCall) o;
        return this.method.equals(other.method)
                && this.equalArguments(other.arguments);
    }

    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not implemented");
    }

    private boolean equalArguments(Object[] arguments) {
        if (this.arguments == null && arguments == null) {
            return true;
        }

        if (this.arguments.length != arguments.length) {
            return false;
        }
        for (int i = 0; i < this.arguments.length; i++) {
            Object myArgument = this.arguments[i];
            Object otherArgument = arguments[i];
            if (method.getParameterTypes()[i].isPrimitive()) {
                if (!myArgument.equals(otherArgument)) {
                    return false;
                }
            } else {
                if (myArgument != otherArgument) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean matches(MethodCall actual, ArgumentsMatcher matcher) {
        return this.method.equals(actual.method)
                && matcher.matches(this.arguments, actual.arguments);
    }

    public String toString(ArgumentsMatcher matcher) {
        return method.getName() + "(" + matcher.toString(arguments) + ")";
    }

}
