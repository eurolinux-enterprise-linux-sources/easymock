/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.util.ArrayList;
import java.util.List;

import org.easymock.ArgumentsMatcher;

public class MatchableArguments implements Comparable {

    private final Arguments arguments;

    private final ArgumentsMatcher matcher;

    private List isLessThan;

    public MatchableArguments(Arguments arguments, ArgumentsMatcher matcher) {
        this.arguments = arguments;
        this.matcher = matcher;
    }

    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;

        MatchableArguments other = (MatchableArguments) o;
        return this.matcher.equals(other.matcher)
                && this.arguments.matches(other.arguments, matcher);
    }

    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not implemented");
    }

    public Arguments getArguments() {
        return arguments;
    }

    public String toString() {
        return arguments.toString(matcher);
    }

    public int compareTo(Object o) {
        MatchableArguments other = (MatchableArguments) o;
        if (this.equals(other))
            return 0;

        int result = this.toString().compareTo(other.toString());
        if (result != 0) {
            return result;
        } else {
            if (!this.getIsLessThan().contains(other)
                    && !other.getIsLessThan().contains(this)) {
                this.getIsLessThan().add(other);
            }
            return (this.getIsLessThan().contains(other)) ? -1 : 1;
        }
    }

    private List getIsLessThan() {
        if (isLessThan == null) {
            isLessThan = new ArrayList();
        }
        return isLessThan;
    }
}
