/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;

public abstract class AbstractBehavior implements IBehavior {
    private ArgumentsMatcher defaultMatcher;

    private boolean defaultMatcherSet;

    private Map matchers = new HashMap();

    private Map defaultResults = new HashMap();

    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        if (defaultMatcherSet) {
            throw new RuntimeExceptionWrapper(
                    new IllegalStateException(
                            "default matcher can only be set once directly after creation of the MockControl"));
        }
        defaultMatcher = matcher;
        defaultMatcherSet = true;
    }

    public void setMatcher(Method method, ArgumentsMatcher matcher) {
        if (matchers.containsKey(method) && matchers.get(method) != matcher) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "for method "
                            + method.getName()
                            + "("
                            + (method.getParameterTypes().length == 0 ? ""
                                    : "...")
                            + "), a matcher has already been set"));
        }
        matchers.put(method, matcher);
    }

    public void setDefaultResult(Method method, Result result) {
        defaultResults.put(method, result);
    }

    public final Result addActual(MethodCall methodCall) {
        try {
            return doAddActual(methodCall);
        } catch (AssertionFailedErrorWrapper e) {
            throw new AssertionFailedErrorWrapper(new AssertionFailedError(
                    "\n  Unexpected method call "
                            + methodCall.toString(getMatcher(methodCall
                                    .getMethod())) + ":"
                            + e.getAssertionFailedError().getMessage()));
        }
    }

    public final void verify() {
        try {
            doVerify();
        } catch (AssertionFailedErrorWrapper e) {
            throw new AssertionFailedErrorWrapper(new AssertionFailedError(
                    "\n  Expectation failure on verify:"
                            + e.getAssertionFailedError().getMessage()));
        }
    }

    protected Result getDefaultResult(Method method) {
        return (Result) defaultResults.get(method);
    }

    protected ArgumentsMatcher getMatcher(Method method) {
        if (!matchers.containsKey(method)) {
            if (!defaultMatcherSet) {
                setDefaultMatcher(MockControl.EQUALS_MATCHER);
            }
            matchers.put(method, defaultMatcher);
        }
        return (ArgumentsMatcher) matchers.get(method);
    }

    protected abstract void doVerify();

    protected abstract Result doAddActual(MethodCall methodCall);
}
