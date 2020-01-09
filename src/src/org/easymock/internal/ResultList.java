/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ResultList {

    private int callCount;

    private LinkedList ranges = new LinkedList();

    private List results = new ArrayList();

    void add(Result result, Range range) {
        if (!ranges.isEmpty()) {
            Range lastRange = (Range) ranges.getLast();
            if (!lastRange.hasFixedCount())
                throw new RuntimeExceptionWrapper(
                        new IllegalStateException(
                                "last method called on mock already has a non-fixed count set."));
        }
        ranges.add(range);
        results.add(result);
    }

    Result next() {
        int currentPosition = 0;
        for (int i = 0; i < ranges.size(); i++) {
            Range interval = (Range) ranges.get(i);
            if (interval.hasOpenCount()) {
                callCount += 1;
                return getResult(i);
            }
            currentPosition += interval.getMaximum();
            if (currentPosition > callCount) {
                callCount += 1;
                return getResult(i);
            }
        }
        return null;
    }

    private Result getResult(int i) {
        return (Result) results.get(i);
    }

    boolean hasValidCallCount() {
        return getMainInterval().contains(getCallCount());
    }

    String getMessage() {
        return getMessage(getCallCount());
    }

    String getMessage(int count) {
        return getMainInterval().expectedAndActual(count);
    }

    private Range getMainInterval() {
        int min = 0;
        int max = 0;

        for (int i = 0; i < ranges.size(); i++) {
            Range interval = (Range) ranges.get(i);
            min += interval.getMinimum();
            if (interval.hasOpenCount() || max == Integer.MAX_VALUE) {
                max = Integer.MAX_VALUE;
            } else {
                max += interval.getMaximum();
            }
        }

        return new Range(min, max);
    }

    public int getCallCount() {
        return callCount;
    }
}
