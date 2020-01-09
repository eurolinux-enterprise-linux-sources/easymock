/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.easymock.ArgumentsMatcher;

public class ResultListMap {

    private final List results = new ArrayList();

    private final ArgumentsMatcher matcher;

    private final Method method;

    private static class ArgumentsEntry {
        ExpectedMethodCall matchableArguments;

        ResultList resultList;

        ArgumentsEntry(ExpectedMethodCall key, ResultList value) {
            this.matchableArguments = key;
            this.resultList = value;
        }

        public ExpectedMethodCall getMatchableArguments() {
            return matchableArguments;
        }

        public ResultList getResultList() {
            return resultList;
        }
    }

    public ResultListMap(Method method, ArgumentsMatcher matcher) {
        this.method = method;
        this.matcher = matcher;
    }

    public void addExpected(Object[] expected, Result result, Range count) {
        ExpectedMethodCall matchableArguments = new ExpectedMethodCall(method,
                expected, matcher);

        for (Iterator iter = results.iterator(); iter.hasNext();) {
            ArgumentsEntry entry = (ArgumentsEntry) iter.next();
            if (entry.getMatchableArguments().equals(matchableArguments)) {
                entry.getResultList().add(result, count);
                return;
            }
        }
        ResultList list = new ResultList();
        list.add(result, count);
        results.add(new ArgumentsEntry(matchableArguments, list));
    }

    public Result addActual(Object[] arguments) {
        boolean matched = false;

        for (Iterator it = results.iterator(); it.hasNext();) {
            ArgumentsEntry entry = (ArgumentsEntry) it.next();
            ExpectedMethodCall matchableArguments = entry
                    .getMatchableArguments();

            if (matchableArguments.matches(method, arguments)) {
                matched = true;
                Result result = entry.getResultList().next();
                if (result != null) {
                    return result;
                }
            }
        }

        throw new AssertionFailedErrorWrapper(new AssertionFailedError(
                createFailureMessage(arguments, matched)));
    }

    private String createFailureMessage(Object[] actual, boolean matched) {
        StringBuffer result = new StringBuffer();

        if (!matched) {
            result.append("\n    ");
            result.append(new MethodCall(method, actual).toString(matcher));
            result.append(": ");
            result.append(new Range(7).expectedAndActual(1).replace('7', '0'));
        }

        for (Iterator it = results.iterator(); it.hasNext();) {
            ArgumentsEntry entry = (ArgumentsEntry) it.next();
            ExpectedMethodCall matchableArguments = entry
                    .getMatchableArguments();
            ResultList list = (ResultList) entry.getResultList();

            if (list.hasValidCallCount()
                    && !matchableArguments.matches(method, actual)) {
                continue;
            }

            int count = list.getCallCount();

            if (matched && matchableArguments.matches(method, actual)) {
                count += 1;
                matched = false;
            }
            result.append("\n    ");
            result.append(matchableArguments.toString());
            result.append(": ");
            result.append(list.getMessage(count));
        }

        return result.toString();
    }

    public void verify() {
        String failureMessage = "";
        boolean verifyFailed = false;

        for (Iterator it = results.iterator(); it.hasNext();) {
            ArgumentsEntry entry = (ArgumentsEntry) it.next();
            ExpectedMethodCall matchableArguments = entry
                    .getMatchableArguments();
            ResultList list = (ResultList) entry.getResultList();

            if (list.hasValidCallCount()) {
                continue;
            }
            verifyFailed = true;
            failureMessage += "\n    " + matchableArguments.toString() + ": "
                    + list.getMessage();
        }
        if (verifyFailed) {
            throw new AssertionFailedError(failureMessage);
        }
    }
}