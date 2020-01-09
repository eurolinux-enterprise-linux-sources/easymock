/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.AssertionFailedError;

public class UnorderedBehavior extends AbstractBehavior {

    private Map methodBehaviors = new HashMap();

    private ResultListMap getMethodBehavior(Method method) {
        if (!methodBehaviors.containsKey(method)) {
            methodBehaviors.put(method, new ResultListMap(method,
                    getMatcher(method)));
        }
        return (ResultListMap) methodBehaviors.get(method);
    }

    public void addExpected(MethodCall call, Result returnValue, Range range) {
        ResultListMap behaviors = getMethodBehavior(call.getMethod());
        behaviors.addExpected(call.getArguments(), returnValue, range);
    }

    protected Result returnValueForUnexpected(Method method) {
        return null;
    }

    public Result doAddActual(MethodCall methodCall) {
        ResultListMap behavior = getMethodBehavior(methodCall.getMethod());
        try {
            return behavior.addActual(methodCall.getArguments());
        } catch (AssertionFailedErrorWrapper e) {
            Result defaultBehavior = getDefaultResult(methodCall.getMethod());
            if (defaultBehavior != null) {
                return defaultBehavior;
            }
            Result niceBehavior = returnValueForUnexpected(methodCall
                    .getMethod());
            if (niceBehavior != null) {
                return niceBehavior;
            }
            throw e;
        }

    }

    public void doVerify() {
        String failureMessage = "";
        boolean verifyFailed = false;

        for (Iterator it = methodBehaviors.keySet().iterator(); it.hasNext();) {
            Method method = (Method) it.next();
            try {
                getMethodBehavior(method).verify();
            } catch (AssertionFailedError e) {
                verifyFailed = true;
                failureMessage += e.getMessage();
            }
        }

        if (!verifyFailed)
            return;
        throw new AssertionFailedErrorWrapper(new AssertionFailedError(
                failureMessage));
    }
}