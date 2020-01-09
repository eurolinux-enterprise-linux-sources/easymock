/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.util.Arrays;

import org.easymock.AbstractMatcher;

public class ArrayMatcher extends AbstractMatcher {
    public String argumentToString(Object argument) {
        if (argument instanceof boolean[]) {
            boolean[] array = (boolean[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += array[i];
            }
            return inBrackets(result);
        } else if (argument instanceof byte[]) {
            byte[] array = (byte[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += array[i];
            }
            return inBrackets(result);
        } else if (argument instanceof char[]) {
            char[] array = (char[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += array[i];
            }
            return inBrackets(result);
        } else if (argument instanceof double[]) {
            double[] array = (double[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += array[i];
            }
            return inBrackets(result);
        } else if (argument instanceof float[]) {
            float[] array = (float[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += array[i];
            }
            return inBrackets(result);
        } else if (argument instanceof int[]) {
            int[] array = (int[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += array[i];
            }
            return inBrackets(result);
        } else if (argument instanceof long[]) {
            long[] array = (long[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += array[i];
            }
            return inBrackets(result);
        } else if (argument instanceof short[]) {
            short[] array = (short[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += array[i];
            }
            return inBrackets(result);
        } else if (argument instanceof Object[]) {
            Object[] array = (Object[]) argument;
            String result = "";
            for (int i = 0; i < array.length; i++) {
                if (i > 0)
                    result += ", ";
                result += super.argumentToString(array[i]);
            }
            return inBrackets(result);
        } else {
            return super.argumentToString(argument);
        }
    }

    private String inBrackets(String result) {
        return "[" + result + "]";
    }

    public boolean argumentMatches(Object expected, Object actual) {
        if (expected instanceof boolean[]) {
            return Arrays.equals((boolean[]) expected, (boolean[]) actual);
        } else if (expected instanceof byte[]) {
            return Arrays.equals((byte[]) expected, (byte[]) actual);
        } else if (expected instanceof char[]) {
            return Arrays.equals((char[]) expected, (char[]) actual);
        } else if (expected instanceof double[]) {
            return Arrays.equals((double[]) expected, (double[]) actual);
        } else if (expected instanceof float[]) {
            return Arrays.equals((float[]) expected, (float[]) actual);
        } else if (expected instanceof int[]) {
            return Arrays.equals((int[]) expected, (int[]) actual);
        } else if (expected instanceof long[]) {
            return Arrays.equals((long[]) expected, (long[]) actual);
        } else if (expected instanceof short[]) {
            return Arrays.equals((short[]) expected, (short[]) actual);
        } else if (expected instanceof Object[]) {
            return Arrays.equals((Object[]) expected, (Object[]) actual);
        } else {
            return super.argumentMatches(expected, actual);
        }
    }
}