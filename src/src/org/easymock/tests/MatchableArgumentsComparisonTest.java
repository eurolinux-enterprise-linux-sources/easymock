/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.AbstractMatcher;
import org.easymock.ArgumentsMatcher;
import org.easymock.internal.Arguments;
import org.easymock.internal.MatchableArguments;

public class MatchableArgumentsComparisonTest extends TestCase {

    private MatchableArguments first;

    private MatchableArguments second;

    private final Object firstObject = new String("1");

    private final Object equalToFirstEqualToString = new String("1");

    private final Object equalToFirstDifferentToString = new String("1");

    private final Object differentToFirstEqualToString = new String("2");

    private final Object differentToFirstDifferentToString = new String("2");

    private final ArgumentsMatcher matcher = new AbstractMatcher() {
        protected String argumentToString(Object argument) {
            if (argument == firstObject) {
                return "1";
            } else if (argument == equalToFirstEqualToString) {
                return "1";
            } else if (argument == equalToFirstDifferentToString) {
                return "2";
            } else if (argument == differentToFirstEqualToString) {
                return "1";
            } else if (argument == differentToFirstDifferentToString) {
                return "0";
            } else {
                throw new IllegalArgumentException(
                        "Unexpected argumentToString()");
            }
        }
    };

    public void testEqualObjectsEqualToString() {
        first = createMatchableArguments(firstObject);
        second = createMatchableArguments(equalToFirstEqualToString);
        assertEquals(first, second);
        assertEquals(0, first.compareTo(second));
    }

    public void testEqualObjectsDifferentToString() {
        first = createMatchableArguments(firstObject);
        second = createMatchableArguments(equalToFirstDifferentToString);
        assertEquals(first, second);
        assertEquals(0, first.compareTo(second));
    }

    public void testDifferentObjectsEqualToString() {
        first = createMatchableArguments(firstObject);
        second = createMatchableArguments(differentToFirstEqualToString);
        assertFalse(first.equals(second));
        assertEquals(-1, first.compareTo(second));
        assertEquals(1, second.compareTo(first));
        assertEquals(-1, first.compareTo(second));
        assertEquals(1, second.compareTo(first));
    }

    public void testDifferentObjectsDifferentToString() {
        first = createMatchableArguments(firstObject);
        second = createMatchableArguments(differentToFirstDifferentToString);
        assertFalse(first.equals(second));
        assertEquals(1, first.compareTo(second));
        assertEquals(-1, second.compareTo(first));
    }

    private MatchableArguments createMatchableArguments(Object o) {
        return new MatchableArguments(new Arguments(new Object[] { o }),
                matcher);
    }
}