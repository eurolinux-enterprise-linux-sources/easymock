/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock;

/**
 * A convenience implementation of {@link ArgumentsMatcher}. A subclass that
 * does not redefine any method will behave like
 * {@link MockControl#EQUALS_MATCHER}.
 */
public abstract class AbstractMatcher implements ArgumentsMatcher {

    /**
     * Compares two arguments; used by
     * {@link AbstractMatcher#matches(Object[], Object[])}. The arguments
     * provided to this method are always not <code>null</code>.
     * 
     * @param expected
     *            the expected argument.
     * @param actual
     *            the actual argument.
     * @return true if the arguments match, false otherwise.
     */
    protected boolean argumentMatches(Object expected, Object actual) {
        return expected.equals(actual);
    }

    /**
     * Converts an argument to a String, used by
     * {@link AbstractMatcher#toString(Object[])}.
     * 
     * @param argument
     *            the argument to convert to a String.
     * @return a <code>String</code> representation of the argument.
     */
    protected String argumentToString(Object argument) {
        if (argument instanceof String) {
            return "\"" + argument + "\"";
        }
        return "" + argument;
    }

    /**
     * Matches two arrays of arguments. This convenience implementation uses
     * <code>argumentMatches(Object, Object)</code> to check whether arguments
     * pairs match. If all the arguments match, true is returned, otherwise
     * false. In two cases, <code>argumentMatches(Object, Object)</code> is
     * not called: If both argument arrays are null, they match; if one and only
     * one is null, they do not match.
     * 
     * @param expected
     *            the expected arguments.
     * @param actual
     *            the actual arguments.
     * @return true if the arguments match, false otherwise.
     */
    public boolean matches(Object[] expected, Object[] actual) {
        if (expected == actual) {
            return true;
        }
        if (expected == null || actual == null) {
            return false;
        }
        if (expected.length != actual.length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            Object expectedObject = expected[i];
            Object actualObject = actual[i];

            if (expectedObject == null && actualObject == null) {
                continue;
            }

            if (expectedObject == null && actualObject != null) {
                return false;
            }

            if (expectedObject != null && actualObject == null) {
                return false;
            }

            if (!argumentMatches(expectedObject, actualObject)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a string representation of the matcher. This convenience
     * implementation calls {@link AbstractMatcher#argumentToString(Object)}for
     * every argument in the given array and returns the string representations
     * of the arguments separated by commas.
     * 
     * @param arguments
     *            the arguments to be used in the string representation.
     * @return a string representation of the matcher.
     */
    public String toString(Object[] arguments) {
        if (arguments == null)
            arguments = new Object[0];

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < arguments.length; i++) {
            if (i > 0)
                result.append(", ");
            result.append(argumentToString(arguments[i]));
        }
        return result.toString();
    }
}
