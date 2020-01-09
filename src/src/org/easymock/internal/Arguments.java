/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import org.easymock.ArgumentsMatcher;

public class Arguments {
    private Object[] arguments;

    public Arguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;
        Arguments other = (Arguments) o;
        return java.util.Arrays.equals(this.arguments, other.arguments);
    }

    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not implemented");
    }

    public String toString(ArgumentsMatcher matcher) {
        return matcher.toString(arguments);
    }

    public boolean matches(Arguments other, ArgumentsMatcher matcher) {
        return matcher.matches(this.arguments, other.arguments);
    }
}
