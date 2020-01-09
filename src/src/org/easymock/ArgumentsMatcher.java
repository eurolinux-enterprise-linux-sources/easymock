/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock;

/**
 * A comparison function that is used to match arguments.
 * 
 * @see MockControl#setDefaultMatcher
 * @see MockControl#setMatcher
 * @see MockControl#EQUALS_MATCHER
 * @see MockControl#ARRAY_MATCHER
 * @see MockControl#ALWAYS_MATCHER
 */
public interface ArgumentsMatcher {

    /**
     * Matches two arrays of arguments.
     * 
     * @param expected
     *            the expected arguments.
     * @param actual
     *            the actual arguments.
     * @return true if the arguments match, false otherwise.
     */
    boolean matches(Object[] expected, Object[] actual);

    /**
     * Returns a string representation of the arguments.
     * 
     * @param arguments
     *            the arguments to be used in the string representation.
     * @return a string representation of the arguments.
     */
    String toString(Object[] arguments);
}
