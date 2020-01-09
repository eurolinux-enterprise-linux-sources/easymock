/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.samples;

import org.easymock.AbstractMatcher;

public class FirstCharMatcher extends AbstractMatcher {
    protected boolean argumentMatches(Object expected, Object actual) {
        if (expected instanceof String) {
            expected = firstCharacterIfString(expected);
        }
        if (actual instanceof String) {
            actual = firstCharacterIfString(actual);
        }
        return super.argumentMatches(expected, actual);
    }

    protected String argumentToString(Object argument) {
        if (argument instanceof String) {
            argument = firstCharacterIfString(argument) + "...";
        }
        return super.argumentToString(argument);
    }

    private Object firstCharacterIfString(Object object) {
        if (!(object instanceof String)) {
            return object;
        }
        String string = (String) object;
        return string.length() > 0 ? string.substring(0, 1) : string;
    }
}
