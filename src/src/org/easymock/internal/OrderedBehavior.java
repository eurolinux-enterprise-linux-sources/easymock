/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;

public class OrderedBehavior extends AbstractBehavior {

    private List methodCalls = new ArrayList();

    private List resultLists = new ArrayList();

    private int position = 0;

    public void addExpected(MethodCall methodCall, Result result, Range range) {
        ExpectedMethodCall matchableMethodCall = new ExpectedMethodCall(
                methodCall.getMethod(), methodCall.getArguments(),
                getMatcher(methodCall.getMethod()));

        if (!lastMethodCallEqualTo(matchableMethodCall)) {
            methodCalls.add(matchableMethodCall);
            resultLists.add(new ResultList());
        }
        ResultList resultList = (ResultList) resultLists
                .get(resultLists.size() - 1);
        resultList.add(result, range);
    }

    private boolean lastMethodCallEqualTo(ExpectedMethodCall matchableMethodCall) {
        if (methodCalls.isEmpty()) {
            return false;
        }
        ExpectedMethodCall lastMethodCall = ((ExpectedMethodCall) methodCalls
                .get(methodCalls.size() - 1));
        return lastMethodCall.equals(matchableMethodCall);
    }

    public Result doAddActual(MethodCall methodCall) {
        List matchedCalls = new ArrayList();
        List matchedResultLists = new ArrayList();

        int tooManyCallsPosition = -1;

        for (; position < methodCalls.size(); position++) {
            ExpectedMethodCall expectedCall = (ExpectedMethodCall) methodCalls
                    .get(position);
            ResultList resultList = (ResultList) resultLists.get(position);

            if (expectedCall.matches(methodCall)) {
                Result result = resultList.next();
                if (result != null) {
                    return result;
                }

                matchedCalls.add(expectedCall);
                matchedResultLists.add(resultList);
                tooManyCallsPosition = matchedCalls.size() - 1;
                continue;
            } else {
                if (resultList.hasValidCallCount()) {
                    continue;
                }

                matchedCalls.add(expectedCall);
                matchedResultLists.add(resultList);
                break;
            }
        }
        Result defaultBehavior = getDefaultResult(methodCall.getMethod());
        if (defaultBehavior != null) {
            return defaultBehavior;
        }
        throw new AssertionFailedErrorWrapper(new AssertionFailedError(
                createFailureMessage(methodCall, matchedCalls,
                        matchedResultLists, tooManyCallsPosition)));
    }

    private String createFailureMessage(MethodCall methodCall,
            List matchedCalls, List matchedResultLists, int tooManyCallsPosition) {
        String failureMessage = "";
        if (tooManyCallsPosition == -1) {
            failureMessage += "\n    "
                    + methodCall.toString(getMatcher(methodCall.getMethod()))
                    + ": "
                    + new Range(2).expectedAndActual(1).replace('2', '0');
        }
        for (int i = 0; i < matchedCalls.size(); i++) {
            ExpectedMethodCall call = (ExpectedMethodCall) matchedCalls.get(i);
            ResultList list = (ResultList) matchedResultLists.get(i);
            failureMessage += "\n    " + call.toString() + ": ";
            int count = list.getCallCount()
                    + (i == tooManyCallsPosition ? 1 : 0);
            failureMessage += list.getMessage(count);
        }
        return failureMessage;
    }

    public void doVerify() {
        String failureMessage = "";
        boolean verifyFailed = false;

        for (int i = position; i < methodCalls.size(); i++) {
            ExpectedMethodCall call = (ExpectedMethodCall) methodCalls.get(i);
            ResultList list = (ResultList) resultLists.get(i);
            failureMessage += "\n    " + call.toString() + ": "
                    + list.getMessage();

            if (list.hasValidCallCount()) {
                continue;
            }
            verifyFailed = true;
        }
        if (verifyFailed) {
            throw new AssertionFailedErrorWrapper(new AssertionFailedError(
                    failureMessage));
        }
    }
}
