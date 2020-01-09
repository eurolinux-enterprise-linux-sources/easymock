/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import junit.framework.AssertionFailedError;

import org.easymock.internal.AlwaysMatcher;
import org.easymock.internal.ArrayMatcher;
import org.easymock.internal.AssertionFailedErrorWrapper;
import org.easymock.internal.EqualsMatcher;
import org.easymock.internal.IBehavior;
import org.easymock.internal.IBehaviorFactory;
import org.easymock.internal.IMockControlState;
import org.easymock.internal.IProxyFactory;
import org.easymock.internal.JavaProxyFactory;
import org.easymock.internal.NiceBehavior;
import org.easymock.internal.ObjectMethodsFilter;
import org.easymock.internal.OrderedBehavior;
import org.easymock.internal.Range;
import org.easymock.internal.RecordState;
import org.easymock.internal.ReplayState;
import org.easymock.internal.RuntimeExceptionWrapper;
import org.easymock.internal.ThrowableWrapper;
import org.easymock.internal.UnorderedBehavior;

/**
 * A <code>MockControl</code> object controls the behavior of its associated
 * mock object. For more information, see the EasyMock documentation.
 */
public class MockControl {
    private IMockControlState state;

    private Object mock;

    private IBehavior behavior;

    private IBehaviorFactory behaviorFactory;

    /**
     * internal constant with <code>protected</code> visibility to allow
     * access for extensions.
     */
    protected static final IBehaviorFactory NICE_BEHAVIOR_FACTORY = new IBehaviorFactory() {
        public IBehavior createBehavior() {
            return new NiceBehavior();
        }
    };

    /**
     * internal constant with <code>protected</code> visibility to allow
     * access for extensions.
     */
    protected static final IBehaviorFactory ORDERED_BEHAVIOR_FACTORY = new IBehaviorFactory() {
        public IBehavior createBehavior() {
            return new OrderedBehavior();
        }
    };

    /**
     * internal constant with <code>protected</code> visibility to allow
     * access for extensions.
     */
    protected static final IBehaviorFactory UNORDERED_BEHAVIOR_FACTORY = new IBehaviorFactory() {
        public IBehavior createBehavior() {
            return new UnorderedBehavior();
        }
    };

    private static final JavaProxyFactory PROXY_FACTORY = new JavaProxyFactory();

    /**
     * Creates a mock control object for the specified interface. The
     * <code>MockControl</code> and its associated mock object will not check
     * the order of expected method calls. An unexpected method call on the mock
     * object will lead to an <code>AssertionFailedError</code>.
     * 
     * @param toMock
     *            the class of the interface to mock.
     * @return the mock control.
     */
    public static MockControl createControl(Class toMock) {
        return new MockControl(toMock, PROXY_FACTORY,
                UNORDERED_BEHAVIOR_FACTORY);
    }

    /**
     * Creates a mock control object for the specified interface. The
     * <code>MockControl</code> and its associated mock object will check the
     * order of expected method calls. An unexpected method call on the mock
     * object will lead to an <code>AssertionFailedError</code>.
     * 
     * @param toMock
     *            the class of the interface to mock.
     * @return the mock control.
     */
    public static MockControl createStrictControl(Class toMock) {
        return new MockControl(toMock, PROXY_FACTORY, ORDERED_BEHAVIOR_FACTORY);
    }

    /**
     * Creates a mock control object for the specified interface. The
     * <code>MockControl</code> and its associated mock object will not check
     * the order of expected method calls. An unexpected method call on the mock
     * object will return an empty value (0, null, false).
     * 
     * @param toMock
     *            the class of the interface to mock.
     * @return the mock control.
     */
    public static MockControl createNiceControl(Class toMock) {
        return new MockControl(toMock, PROXY_FACTORY, NICE_BEHAVIOR_FACTORY);
    }

    /**
     * Creates a new mock control object using the provided proxy and behavior
     * factory - this is an internal constructor with <code>protected</code>
     * visibility to allow access for extensions.
     * 
     * @param toMock
     *            the class of the interface to mock.
     * @param proxyFactory
     *            the proxy factory.
     * @param behaviorFactory
     *            the behavior factory.
     */
    protected MockControl(Class toMock, IProxyFactory proxyFactory,
            IBehaviorFactory behaviorFactory) {
        mock = proxyFactory.createProxy(toMock, new ObjectMethodsFilter(
                createDelegator()));
        this.behaviorFactory = behaviorFactory;
        reset();
    }

    private InvocationHandler createDelegator() {
        return new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                try {
                    return state.invoke(proxy, method, args);
                } catch (RuntimeExceptionWrapper e) {
                    throw e.getRuntimeException().fillInStackTrace();
                } catch (AssertionFailedErrorWrapper e) {
                    throw e.getAssertionFailedError().fillInStackTrace();
                } catch (ThrowableWrapper t) {
                    throw t.getThrowable().fillInStackTrace();
                }
            }
        };
    }

    /**
     * Returns the mock object.
     * 
     * @return the mock object of this control
     */
    public Object getMock() {
        return mock;
    }

    /**
     * Resets the mock control and the mock object to the state directly after
     * creation.
     */
    public final void reset() {
        behavior = behaviorFactory.createBehavior();
        state = new RecordState(behavior);
    }

    /**
     * Switches the mock object from record state to replay state. For more
     * information, see the EasyMock documentation.
     * 
     * @throws IllegalStateException
     *             if the mock object already is in replay state.
     */
    public void replay() {
        try {
            state.replay();
            state = new ReplayState(behavior);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Verifies that all expectations have been met. For more information, see
     * the EasyMock documentation.
     * 
     * @throws IllegalStateException
     *             if the mock object is in record state.
     * @throws AssertionFailedError
     *             if any expectation has not been met.
     */
    public void verify() {
        try {
            state.verify();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        } catch (AssertionFailedErrorWrapper e) {
            throw (AssertionFailedError) e.getAssertionFailedError()
                    .fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by returning silently.
     * 
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setVoidCallable() {
        try {
            state.setVoidCallable(MockControl.ONE);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by throwing the provided Throwable.
     * 
     * @param throwable
     *            the Throwable to throw.
     * @exception IllegalStateException
     *                if the mock object is in replay state or if no method was
     *                called on the mock object before.
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     */
    public void setThrowable(Throwable throwable) {
        try {
            state.setThrowable(throwable, MockControl.ONE);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setReturnValue(boolean value) {
        try {
            state.setReturnValue(value, MockControl.ONE);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return
     *             <code>byte, short, char, int, or long</code>.
     */
    public void setReturnValue(long value) {
        try {
            state.setReturnValue(value, MockControl.ONE);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>float</code>.
     */
    public void setReturnValue(float value) {
        try {
            state.setReturnValue(value, MockControl.ONE);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>double</code>.
     */
    public void setReturnValue(double value) {
        try {
            state.setReturnValue(value, MockControl.ONE);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return an object.
     * @throws IllegalArgumentException
     *             if the provided return value is not compatible to the return
     *             value of the last method called on the mock object.
     */
    public void setReturnValue(Object value) {
        try {
            state.setReturnValue(value, MockControl.ONE);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning silently.
     * 
     * @param times
     *            the number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setVoidCallable(int times) {
        try {
            state.setVoidCallable(new Range(times));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by throwing the provided Throwable.
     * 
     * @param throwable
     *            the Throwable to throw.
     * @param times
     *            the number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state or if no method was
     *                called on the mock object before.
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     */
    public void setThrowable(Throwable throwable, int times) {
        try {
            state.setThrowable(throwable, new Range(times));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param times
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setReturnValue(boolean value, int times) {
        try {
            state.setReturnValue(value, new Range(times));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param times
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>double</code>.
     */
    public void setReturnValue(double value, int times) {
        try {
            state.setReturnValue(value, new Range(times));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param times
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>float</code>.
     */
    public void setReturnValue(float value, int times) {
        try {
            state.setReturnValue(value, new Range(times));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param times
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return
     *             <code>byte, short, char, int, or long</code>.
     */
    public void setReturnValue(long value, int times) {
        try {
            state.setReturnValue(value, new Range(times));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param times
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return an object.
     * @throws IllegalArgumentException
     *             if the provided return value is not compatible to the return
     *             value of the last method called on the mock object.
     */
    public void setReturnValue(Object value, int times) {
        try {
            state.setReturnValue(value, new Range(times));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call the number
     * of times specified by the range argument, and will react by returning
     * silently. Available range arguments are:
     * <ul>
     * <li>{@link MockControl#ZERO_OR_MORE}</li>
     * <li>{@link MockControl#ONE}</li>
     * <li>{@link MockControl#ONE_OR_MORE}</li>
     * </ul>
     * 
     * @param range
     *            the number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setVoidCallable(Range range) {
        try {
            state.setVoidCallable(range);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call the number
     * of times specified by the range argument, and will react by throwing the
     * provided Throwable. Available range arguments are:
     * <ul>
     * <li>{@link MockControl#ZERO_OR_MORE}</li>
     * <li>{@link MockControl#ONE}</li>
     * <li>{@link MockControl#ONE_OR_MORE}</li>
     * </ul>
     * 
     * @param throwable
     *            the Throwable to throw.
     * @param range
     *            the number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state or if no method was
     *                called on the mock object before.
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     */
    public void setThrowable(Throwable throwable, Range range) {
        try {
            state.setThrowable(throwable, range);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call the number
     * of times specified by the range argument, and will react by returning the
     * provided return value. Available range arguments are:
     * <ul>
     * <li>{@link MockControl#ZERO_OR_MORE}</li>
     * <li>{@link MockControl#ONE}</li>
     * <li>{@link MockControl#ONE_OR_MORE}</li>
     * </ul>
     * 
     * @param value
     *            the return value.
     * @param range
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setReturnValue(boolean value, Range range) {
        try {
            state.setReturnValue(value, range);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call the number
     * of times specified by the range argument, and will react by returning the
     * provided return value. Available range arguments are:
     * <ul>
     * <li>{@link MockControl#ZERO_OR_MORE}</li>
     * <li>{@link MockControl#ONE}</li>
     * <li>{@link MockControl#ONE_OR_MORE}</li>
     * </ul>
     * 
     * @param value
     *            the return value.
     * @param range
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>double</code>.
     */
    public void setReturnValue(double value, Range range) {
        try {
            state.setReturnValue(value, range);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call the number
     * of times specified by the range argument, and will react by returning the
     * provided return value. Available range arguments are:
     * <ul>
     * <li>{@link MockControl#ZERO_OR_MORE}</li>
     * <li>{@link MockControl#ONE}</li>
     * <li>{@link MockControl#ONE_OR_MORE}</li>
     * </ul>
     * 
     * @param value
     *            the return value.
     * @param range
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>float</code>.
     */
    public void setReturnValue(float value, Range range) {
        try {
            state.setReturnValue(value, range);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call the number
     * of times specified by the range argument, and will react by returning the
     * provided return value. Available range arguments are:
     * <ul>
     * <li>{@link MockControl#ZERO_OR_MORE}</li>
     * <li>{@link MockControl#ONE}</li>
     * <li>{@link MockControl#ONE_OR_MORE}</li>
     * </ul>
     * 
     * @param value
     *            the return value.
     * @param range
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return
     *             <code>byte, short, char, int, or long</code>.
     */
    public void setReturnValue(long value, Range range) {
        try {
            state.setReturnValue(value, range);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call the number
     * of times specified by the range argument, and will react by returning the
     * provided return value. Available range arguments are:
     * <ul>
     * <li>{@link MockControl#ZERO_OR_MORE}</li>
     * <li>{@link MockControl#ONE}</li>
     * <li>{@link MockControl#ONE_OR_MORE}</li>
     * </ul>
     * 
     * @param value
     *            the return value.
     * @param range
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return an object.
     * @throws IllegalArgumentException
     *             if the provided return value is not compatible to the return
     *             value of the last method called on the mock object.
     */
    public void setReturnValue(Object value, Range range) {
        try {
            state.setReturnValue(value, range);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call.
     * 
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setDefaultVoidCallable() {
        try {
            state.setDefaultVoidCallable();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call, and will react by throwing the provided
     * Throwable.
     * 
     * @param throwable
     *            throwable the throwable to be thrown
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     * @exception IllegalStateException
     *                if the mock object is in replay state, or if no method was
     *                called on the mock object before.
     */
    public void setDefaultThrowable(Throwable throwable) {
        try {
            state.setDefaultThrowable(throwable);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call, and will react by returning the provided
     * return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setDefaultReturnValue(boolean value) {
        try {
            state.setDefaultReturnValue(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call, and will react by returning the provided
     * return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return
     *             <code>byte, short, char, int, or long</code>.
     */
    public void setDefaultReturnValue(long value) {
        try {
            state.setDefaultReturnValue(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call, and will react by returning the provided
     * return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>float</code>.
     */
    public void setDefaultReturnValue(float value) {
        try {
            state.setDefaultReturnValue(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call, and will react by returning the provided
     * return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>double</code>.
     */
    public void setDefaultReturnValue(double value) {
        try {
            state.setDefaultReturnValue(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call, and will react by returning the provided
     * return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return an object.
     * @throws IllegalArgumentException
     *             if the provided return value is not compatible to the return
     *             value of the last method called on the mock object.
     */
    public void setDefaultReturnValue(Object value) {
        try {
            state.setDefaultReturnValue(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Sets the ArgumentsMatcher for the last method called on the mock object.
     * The matcher must be set before any behavior for the method is defined.
     * 
     * @throws IllegalStateException
     *             if called in replay state, or if no method was called on the
     *             mock object before.
     */
    public void setMatcher(ArgumentsMatcher matcher) {
        try {
            state.setMatcher(matcher);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by returning silently.
     * 
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setVoidCallable(int minCount, int maxCount) {
        try {
            state.setVoidCallable(new Range(minCount, maxCount));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by throwing the provided Throwable.
     * 
     * @param throwable
     *            the Throwable to throw.
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state or if no method was
     *                called on the mock object before.
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     */
    public void setThrowable(Throwable throwable, int minCount, int maxCount) {
        try {
            state.setThrowable(throwable, new Range(minCount, maxCount));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setReturnValue(boolean value, int minCount, int maxCount) {
        try {
            state.setReturnValue(value, new Range(minCount, maxCount));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return
     *             <code>byte, short, char, int, or long</code>.
     */
    public void setReturnValue(long value, int minCount, int maxCount) {
        try {
            state.setReturnValue(value, new Range(minCount, maxCount));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>float</code>.
     */
    public void setReturnValue(float value, int minCount, int maxCount) {
        try {
            state.setReturnValue(value, new Range(minCount, maxCount));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>double</code>.
     */
    public void setReturnValue(double value, int minCount, int maxCount) {
        try {
            state.setReturnValue(value, new Range(minCount, maxCount));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return an object.
     * @throws IllegalArgumentException
     *             if the provided return value is not compatible to the return
     *             value of the last method called on the mock object.
     */
    public void setReturnValue(Object value, int minCount, int maxCount) {
        try {
            state.setReturnValue(value, new Range(minCount, maxCount));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Exactly one call.
     */
    public static final Range ONE = new Range(1);

    /**
     * One or more calls.
     */
    public static final Range ONE_OR_MORE = new Range(1, Integer.MAX_VALUE);

    /**
     * Zero or more calls.
     */
    public static final Range ZERO_OR_MORE = new Range(0, Integer.MAX_VALUE);

    /**
     * Matches if each expected argument is equal to the corresponding actual
     * argument.
     */
    public static final ArgumentsMatcher EQUALS_MATCHER = new EqualsMatcher();

    /**
     * Matches always.
     */
    public static final ArgumentsMatcher ALWAYS_MATCHER = new AlwaysMatcher();

    /**
     * Matches if each expected argument is equal to the corresponding actual
     * argument for non-array arguments; array arguments are compared with the
     * appropriate <code>java.util.Arrays.equals()</code> -method.
     */
    public static final ArgumentsMatcher ARRAY_MATCHER = new ArrayMatcher();

    /**
     * Sets the default ArgumentsMatcher for all methods of the mock object. The
     * matcher must be set before any behavior is defined on the mock object.
     * 
     * @throws IllegalStateException
     *             if called in replay state, or if any behavior is already
     *             defined on the mock object.
     */
    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        try {
            state.setDefaultMatcher(matcher);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Same as {@link MockControl#setReturnValue(boolean)}. For explanation,
     * see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(boolean ignored, boolean value) {
        setReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setReturnValue(long)}. For explanation, see
     * "Convenience Methods for Return Values" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(long ignored, long value) {
        setReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setReturnValue(float)}. For explanation, see
     * "Convenience Methods for Return Values" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(float ignored, float value) {
        setReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setReturnValue(double)}. For explanation, see
     * "Convenience Methods for Return Values" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(double ignored, double value) {
        setReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setReturnValue(Object)}. For explanation, see
     * "Convenience Methods for Return Values" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(Object ignored, Object value) {
        setReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setReturnValue(boolean, Range)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(boolean ignored, boolean value, Range range) {
        setReturnValue(value, range);
    }

    /**
     * Same as {@link MockControl#setReturnValue(long, Range)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(long ignored, long value, Range range) {
        setReturnValue(value, range);
    }

    /**
     * Same as {@link MockControl#setReturnValue(float, Range)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(float ignored, float value, Range range) {
        setReturnValue(value, range);
    }

    /**
     * Same as {@link MockControl#setReturnValue(double, Range)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(double ignored, double value, Range range) {
        setReturnValue(value, range);
    }

    /**
     * Same as {@link MockControl#setReturnValue(Object, Range)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(Object ignored, Object value, Range range) {
        setReturnValue(value, range);
    }

    /**
     * Same as {@link MockControl#setReturnValue(boolean, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(boolean ignored, boolean value, int count) {
        setReturnValue(value, count);
    }

    /**
     * Same as {@link MockControl#setReturnValue(long, int)}. For explanation,
     * see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(long ignored, long value, int count) {
        setReturnValue(value, count);
    }

    /**
     * Same as {@link MockControl#setReturnValue(float, int)}. For explanation,
     * see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(float ignored, float value, int count) {
        setReturnValue(value, count);
    }

    /**
     * Same as {@link MockControl#setReturnValue(double, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(double ignored, double value, int count) {
        setReturnValue(value, count);
    }

    /**
     * Same as {@link MockControl#setReturnValue(Object, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(Object ignored, Object value, int count) {
        setReturnValue(value, count);
    }

    /**
     * Same as {@link MockControl#setReturnValue(boolean, int, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(boolean ignored, boolean value, int min, int max) {
        setReturnValue(value, min, max);
    }

    /**
     * Same as {@link MockControl#setReturnValue(long, int, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(long ignored, long value, int min, int max) {
        setReturnValue(value, min, max);
    }

    /**
     * Same as {@link MockControl#setReturnValue(float, int, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(float ignored, float value, int min, int max) {
        setReturnValue(value, min, max);
    }

    /**
     * Same as {@link MockControl#setReturnValue(double, int, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(double ignored, double value, int min, int max) {
        setReturnValue(value, min, max);
    }

    /**
     * Same as {@link MockControl#setReturnValue(Object, int, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndReturn(Object ignored, Object value, int min, int max) {
        setReturnValue(value, min, max);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
     * see "Convenience Methods for Throwables" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(boolean ignored, Throwable throwable) {
        setThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
     * see "Convenience Methods for Throwables" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(long ignored, Throwable throwable) {
        setThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
     * see "Convenience Methods for Throwables" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(float ignored, Throwable throwable) {
        setThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
     * see "Convenience Methods for Throwables" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(double ignored, Throwable throwable) {
        setThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
     * see "Convenience Methods for Throwables" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(Object ignored, Throwable throwable) {
        setThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, Range)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(boolean ignored, Throwable throwable, Range range) {
        setThrowable(throwable, range);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, Range)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(long ignored, Throwable throwable, Range range) {
        setThrowable(throwable, range);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, Range)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(float ignored, Throwable throwable, Range range) {
        setThrowable(throwable, range);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, Range)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(double ignored, Throwable throwable, Range range) {
        setThrowable(throwable, range);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, Range)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(Object ignored, Throwable throwable, Range range) {
        setThrowable(throwable, range);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(boolean ignored, Throwable throwable, int count) {
        setThrowable(throwable, count);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(long ignored, Throwable throwable, int count) {
        setThrowable(throwable, count);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(float ignored, Throwable throwable, int count) {
        setThrowable(throwable, count);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(double ignored, Throwable throwable, int count) {
        setThrowable(throwable, count);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(Object ignored, Throwable throwable, int count) {
        setThrowable(throwable, count);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(boolean ignored, Throwable throwable, int min,
            int max) {
        setThrowable(throwable, min, max);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(long ignored, Throwable throwable, int min,
            int max) {
        setThrowable(throwable, min, max);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(float ignored, Throwable throwable, int min,
            int max) {
        setThrowable(throwable, min, max);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(double ignored, Throwable throwable, int min,
            int max) {
        setThrowable(throwable, min, max);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(Object ignored, Throwable throwable, int min,
            int max) {
        setThrowable(throwable, min, max);
    }

    /**
     * Same as {@link MockControl#setDefaultReturnValue(boolean)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultReturn(boolean ignored, boolean value) {
        setDefaultReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setDefaultReturnValue(long)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultReturn(long ignored, long value) {
        setDefaultReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setDefaultReturnValue(float)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultReturn(float ignored, float value) {
        setDefaultReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setDefaultReturnValue(double)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultReturn(double ignored, double value) {
        setDefaultReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setDefaultReturnValue(Object)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultReturn(Object ignored, Object value) {
        setDefaultReturnValue(value);
    }

    /**
     * Same as {@link MockControl#setDefaultThrowable(Throwable)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultThrow(boolean ignored, Throwable throwable) {
        setDefaultThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setDefaultThrowable(Throwable)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultThrow(long ignored, Throwable throwable) {
        setDefaultThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setDefaultThrowable(Throwable)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultThrow(float ignored, Throwable throwable) {
        setDefaultThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setDefaultThrowable(Throwable)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultThrow(double ignored, Throwable throwable) {
        setDefaultThrowable(throwable);
    }

    /**
     * Same as {@link MockControl#setDefaultThrowable(Throwable)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultThrow(Object ignored, Throwable throwable) {
        setDefaultThrowable(throwable);
    }
}