/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.InvocationHandler;

import org.easymock.ArgumentsMatcher;

public interface IMockControlState extends InvocationHandler {

    void replay();

    void verify();

    void setDefaultMatcher(ArgumentsMatcher matcher);

    void setMatcher(ArgumentsMatcher matcher);

    void setVoidCallable(Range range);

    void setThrowable(Throwable throwable, Range range);

    void setReturnValue(boolean value, Range range);

    void setReturnValue(long value, Range range);

    void setReturnValue(float value, Range range);

    void setReturnValue(double value, Range range);

    void setReturnValue(Object object, Range range);

    void setDefaultVoidCallable();

    void setDefaultThrowable(Throwable throwable);

    void setDefaultReturnValue(boolean value);

    void setDefaultReturnValue(long value);

    void setDefaultReturnValue(float value);

    void setDefaultReturnValue(double value);

    void setDefaultReturnValue(Object value);
}