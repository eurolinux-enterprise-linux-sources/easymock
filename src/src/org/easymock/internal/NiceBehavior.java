/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

public class NiceBehavior extends UnorderedBehavior {
    protected Result returnValueForUnexpected(Method method) {
        return Result.createReturnResult(RecordState.emptyReturnValueFor(method
                .getReturnType()));
    }
}
