/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

import org.easymock.ArgumentsMatcher;

public interface IBehavior {

    void setDefaultMatcher(ArgumentsMatcher defaultMatcher);

    void setMatcher(Method method, ArgumentsMatcher matcher);

    void addExpected(MethodCall methodCall, Result result, Range count);

    void setDefaultResult(Method method, Result result);

    Result addActual(MethodCall methodCall);

    void verify();
}
