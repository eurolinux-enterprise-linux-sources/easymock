/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.easymock.tests");
        suite.addTestSuite(ArgumentsMatcherTest.class);
        suite.addTestSuite(ArrayMatcherTest.class);
        suite.addTestSuite(DefaultMatcherTest.class);
        suite.addTestSuite(EqualsMatcherTest.class);
        suite.addTestSuite(ExpectedMethodCallTest.class);
        suite.addTestSuite(LegacyBehaviorTests.class);
        suite.addTestSuite(MatchableArgumentsTest.class);
        suite.addTestSuite(MethodCallTest.class);
        suite.addTestSuite(MockNameTest.class);
        suite.addTestSuite(NiceMockControlTest.class);
        suite.addTestSuite(NiceMockControlLongCompatibleReturnValueTest.class);
        suite.addTestSuite(ObjectMethodsTest.class);
        suite.addTestSuite(RecordStateInvalidDefaultReturnValueTest.class);
        suite.addTestSuite(RecordStateInvalidDefaultThrowableTest.class);
        suite.addTestSuite(RecordStateInvalidMatcherTest.class);
        suite.addTestSuite(RecordStateInvalidRangeTest.class);
        suite.addTestSuite(RecordStateInvalidReturnValueTest.class);
        suite.addTestSuite(RecordStateInvalidStateChangeTest.class);
        suite.addTestSuite(RecordStateInvalidThrowableTest.class);
        suite.addTestSuite(RecordStateInvalidUsageTest.class);
        suite.addTestSuite(RecordStateMethodCallMissingTest.class);
        suite.addTestSuite(ReplayStateInvalidUsageTest.class);
        suite.addTestSuite(StacktraceTest.class);
        suite.addTestSuite(UsageCallCountTest.class);
        suite.addTestSuite(UsageDefaultReturnValueTest.class);
        suite.addTestSuite(UsageExpectAndDefaultReturnTest.class);        
        suite.addTestSuite(UsageExpectAndDefaultThrowTest.class);
        suite.addTestSuite(UsageExpectAndReturnTest.class);
        suite.addTestSuite(UsageExpectAndThrowTest.class);
        suite.addTestSuite(UsageFloatingPointReturnValueTest.class);
        suite.addTestSuite(UsageLongCompatibleReturnValueTest.class);
        suite.addTestSuite(UsageOverloadedDefaultValueTest.class);
        suite.addTestSuite(UsageOverloadedMethodTest.class);
        suite.addTestSuite(UsageUnorderedTest.class);
        suite.addTestSuite(UsageRangeTest.class);
        suite.addTestSuite(UsageStrictMockTest.class);
        suite.addTestSuite(UsageTest.class);
        suite.addTestSuite(UsageThrowableTest.class);
        suite.addTestSuite(UsageVerifyTest.class);
        return suite;
    }
}
