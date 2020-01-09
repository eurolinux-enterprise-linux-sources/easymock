/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.TestCase;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;

public class ArrayMatcherTest extends TestCase {

    private static ArgumentsMatcher MATCHER = MockControl.ARRAY_MATCHER;

    public void testBooleanArray() {
        assertTrue(equals(new boolean[] { true }, new boolean[] { true }));
        assertFalse(equals(new boolean[] { true }, new boolean[] { false }));
    }

    public void testByteArray() {
        assertTrue(equals(new byte[] { 6 }, new byte[] { 6 }));
        assertFalse(equals(new byte[] { 6 }, new byte[] { 7 }));
    }

    public void testCharArray() {
        assertTrue(equals(new char[] { 'x' }, new char[] { 'x' }));
        assertFalse(equals(new char[] { 'x' }, new char[] { 'y' }));
    }

    public void testDoubleArray() {
        assertTrue(equals(new double[] { 6.0 }, new double[] { 6.0 }));
        assertFalse(equals(new double[] { 6.0 }, new double[] { 7.0 }));
    }

    public void testFloatArray() {
        assertTrue(equals(new float[] { 6.0F }, new float[] { 6.0F }));
        assertFalse(equals(new float[] { 6.0F }, new float[] { 7.0F }));
    }

    public void testIntArray() {
        assertTrue(equals(new int[] { 6 }, new int[] { 6 }));
        assertFalse(equals(new int[] { 6 }, new int[] { 7 }));
    }

    public void testLongArray() {
        assertTrue(equals(new long[] { 6 }, new long[] { 6 }));
        assertFalse(equals(new long[] { 6 }, new long[] { 7 }));
    }

    public void testShortArray() {
        assertTrue(equals(new short[] { 6 }, new short[] { 6 }));
        assertFalse(equals(new short[] { 6 }, new short[] { 7 }));
    }

    public void testObjectArray() {
        assertTrue(equals(new String[] { "1", "2" }, new String[] { "1", "2" }));
        assertFalse(equals(new String[] { "1", "2" }, new String[] { "2", "2" }));
    }

    public void testNonArray() {
        assertTrue(equals("1", "1"));
        assertFalse(equals("1", "2"));
    }

    public void testToString() {
        assertEquals("[true, false]", stringFor(new boolean[] { true, false }));
        assertEquals("[6, 7]", stringFor(new byte[] { 6, 7 }));
        assertEquals("[x, y]", stringFor(new char[] { 'x', 'y' }));
        assertEquals("[6.0, 7.0]", stringFor(new double[] { 6, 7 }));
        assertEquals("[6.0, 7.0]", stringFor(new float[] { 6, 7 }));
        assertEquals("[6, 7]", stringFor(new int[] { 6, 7 }));
        assertEquals("[6, 7]", stringFor(new long[] { 6, 7 }));
        assertEquals("[6, 7]", stringFor(new short[] { 6, 7 }));
        assertEquals("[\"1\", \"2\"]", stringFor(new String[] { "1", "2" }));
        assertEquals("[\"1\", \"2\"]", stringFor(new Object[] { "1", "2" }));
    }

    private String stringFor(Object argument) {
        return MATCHER.toString(new Object[] { argument });
    }

    public void testToStringMixed() {
        assertEquals("3, [\"1\", 2.0], \"Test\"", MATCHER
                .toString(new Object[] { new Integer(3),
                        new Object[] { "1", new Float(2.0) }, "Test" }));
    }

    private boolean equals(Object o1, Object o2) {
        Object[] expected = new Object[] { o1 };
        Object[] actual = new Object[] { o2 };
        return MATCHER.matches(expected, actual);
    }
}
