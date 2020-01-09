/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.internal.Arguments;

public class ArgumentsTest extends TestCase {

    private Arguments arguments;

    private Arguments equalArguments;

    private Arguments nonEqualArguments;

    protected void setUp() {
        arguments = new Arguments(new Object[] { "" });
        equalArguments = new Arguments(new Object[] { "" });
        nonEqualArguments = new Arguments(new Object[] { "X" });
    }

    public void testEquals() {
        assertFalse(arguments.equals(null));
        assertFalse(arguments.equals(""));
        assertTrue(arguments.equals(equalArguments));
        assertFalse(arguments.equals(nonEqualArguments));
    }

    public void testHashCode() {
        try {
            arguments.hashCode();
            fail();
        } catch (UnsupportedOperationException expected) {
            assertEquals("hashCode() is not implemented", expected.getMessage());
        }
    }
}