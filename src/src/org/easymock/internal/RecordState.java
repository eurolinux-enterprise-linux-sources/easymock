/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;

public class RecordState implements IMockControlState {

    private MethodCall lastMethodCall;

    private boolean lastMethodCallUsed = true;

    private IBehavior behavior;

    private static Map emptyReturnValues = new HashMap();

    static {
        emptyReturnValues.put(Void.TYPE, null);
        emptyReturnValues.put(Boolean.TYPE, Boolean.FALSE);
        emptyReturnValues.put(Byte.TYPE, new Byte((byte) 0));
        emptyReturnValues.put(Short.TYPE, new Short((short) 0));
        emptyReturnValues.put(Character.TYPE, new Character((char) 0));
        emptyReturnValues.put(Integer.TYPE, new Integer(0));
        emptyReturnValues.put(Long.TYPE, new Long(0));
        emptyReturnValues.put(Float.TYPE, new Float(0));
        emptyReturnValues.put(Double.TYPE, new Double(0));
    }

    public RecordState(IBehavior behavior) {
        this.behavior = behavior;
    }

    public java.lang.Object invoke(Object proxy, Method method, Object[] args) {
        closeVoidMethod();
        lastMethodCall = new MethodCall(method, args);
        lastMethodCallUsed = false;
        return emptyReturnValueFor(method.getReturnType());
    }

    public void replay() {
        closeVoidMethod();
    }

    public void verify() {
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "calling verify is not allowed in record state"));
    }

    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        behavior.setDefaultMatcher(matcher);
    }

    public void setMatcher(ArgumentsMatcher matcher) {
        requireMethodCall("matcher");
        behavior.setMatcher(lastMethodCall.getMethod(), matcher);
    }

    public void setVoidCallable(Range count) {
        requireMethodCall("void callable");
        requireVoidMethod();
        behavior.addExpected(lastMethodCall, Result.createReturnResult(null),
                count);
        lastMethodCallUsed = true;
    }

    public void setThrowable(Throwable throwable, Range count) {
        requireMethodCall("Throwable");
        requireValidThrowable(throwable);
        behavior.addExpected(lastMethodCall, Result
                .createThrowResult(throwable), count);
        lastMethodCallUsed = true;
    }

    public void setReturnValue(long value, Range count) {
        requireMethodCall("return value");
        Class returnType = lastMethodCall.getMethod().getReturnType();
        Object primitiveReturnValue = createNumberObject(value, returnType);
        behavior.addExpected(lastMethodCall, Result
                .createReturnResult(primitiveReturnValue), count);
        lastMethodCallUsed = true;
    }

    public void setReturnValue(boolean value, Range count) {
        requireMethodCall("return value");
        requireReturnType(Boolean.TYPE);
        behavior.addExpected(lastMethodCall, Result
                .createReturnResult(value ? Boolean.TRUE : Boolean.FALSE),
                count);
        lastMethodCallUsed = true;
    }

    private void requireReturnType(Class clazz) {
        if (!lastMethodCall.getMethod().getReturnType().equals(clazz)) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "incompatible return value type"));
        }
    }

    public void setReturnValue(float value, Range count) {
        requireMethodCall("return value");
        requireReturnType(Float.TYPE);
        behavior.addExpected(lastMethodCall, Result
                .createReturnResult(new Float(value)), count);
        lastMethodCallUsed = true;
    }

    public void setReturnValue(double value, Range count) {
        requireMethodCall("return value");
        requireReturnType(Double.TYPE);
        behavior.addExpected(lastMethodCall, Result
                .createReturnResult(new Double(value)), count);
        lastMethodCallUsed = true;
    }

    public void setReturnValue(Object value, Range count) {
        requireMethodCall("return value");
        requireAssignable(value);
        behavior.addExpected(lastMethodCall, Result.createReturnResult(value),
                count);
        lastMethodCallUsed = true;
    }

    public void setDefaultThrowable(Throwable throwable) {
        requireMethodCall("default Throwable");
        requireValidThrowable(throwable);
        behavior.setDefaultResult(lastMethodCall.getMethod(), Result
                .createThrowResult(throwable));
        lastMethodCallUsed = true;
    }

    public void setDefaultVoidCallable() {
        requireMethodCall("default void callable");
        requireVoidMethod();
        behavior.setDefaultResult(lastMethodCall.getMethod(), Result
                .createReturnResult(null));
        lastMethodCallUsed = true;
    }

    public void setDefaultReturnValue(long value) {
        requireMethodCall("default return value");
        Class returnType = lastMethodCall.getMethod().getReturnType();
        Object primitiveReturnValue = createNumberObject(value, returnType);
        behavior.setDefaultResult(lastMethodCall.getMethod(), Result
                .createReturnResult(primitiveReturnValue));
        lastMethodCallUsed = true;
    }

    public void setDefaultReturnValue(boolean value) {
        requireMethodCall("default return value");
        requireReturnType(Boolean.TYPE);
        behavior.setDefaultResult(lastMethodCall.getMethod(), Result
                .createReturnResult(value ? Boolean.TRUE : Boolean.FALSE));
        lastMethodCallUsed = true;
    }

    public void setDefaultReturnValue(float value) {
        requireMethodCall("default return value");
        requireReturnType(Float.TYPE);
        behavior.setDefaultResult(lastMethodCall.getMethod(), Result
                .createReturnResult(new Float(value)));
        lastMethodCallUsed = true;
    }

    public void setDefaultReturnValue(double value) {
        requireMethodCall("default return value");
        requireReturnType(Double.TYPE);
        behavior.setDefaultResult(lastMethodCall.getMethod(), Result
                .createReturnResult(new Double(value)));
        lastMethodCallUsed = true;
    }

    public void setDefaultReturnValue(Object value) {
        requireMethodCall("default return value");
        requireAssignable(value);
        behavior.setDefaultResult(lastMethodCall.getMethod(), Result
                .createReturnResult(value));
        lastMethodCallUsed = true;
    }

    private Object createNumberObject(long value, Class returnType) {
        if (returnType.equals(Byte.TYPE)) {
            return new Byte((byte) value);
        } else if (returnType.equals(Short.TYPE)) {
            return new Short((short) value);
        } else if (returnType.equals(Character.TYPE)) {
            return new Character((char) value);
        } else if (returnType.equals(Integer.TYPE)) {
            return new Integer((int) value);
        } else if (returnType.equals(Long.TYPE)) {
            return new Long(value);
        }
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "incompatible return value type"));
    }

    private void closeVoidMethod() {
        if (lastMethodCallUsed)
            return;
        try {
            this.requireVoidMethod();
        } catch (RuntimeExceptionWrapper e) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "missing behavior definition for the preceeding method call "
                            + lastMethodCall
                                    .toString(MockControl.EQUALS_MATCHER)));
        }
        behavior.addExpected(lastMethodCall, Result.createReturnResult(null),
                MockControl.ONE);
        lastMethodCallUsed = true;
    }

    public static Object emptyReturnValueFor(Class type) {
        if (!type.isPrimitive()) {
            return null;
        }

        // ///CLOVER:OFF
        if (!emptyReturnValues.containsKey(type)) {
            throw new RuntimeException(
                    "EasyMock bug: no return value generated!");
        }
        // ///CLOVER:ON

        return emptyReturnValues.get(type);
    }

    private void requireMethodCall(String failMessage) {
        if (lastMethodCall == null) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "method call on the mock needed before setting "
                            + failMessage));
        }
    }

    private void requireAssignable(Object returnValue) {
        if (lastMethodIsVoidMethod()) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "void method cannot return a value"));
        }
        if (returnValue == null) {
            return;
        }
        Class returnedType = lastMethodCall.getMethod().getReturnType();
        if (!returnedType.isAssignableFrom(returnValue.getClass())) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "incompatible return value type"));
        }
    }

    private void requireValidThrowable(Throwable throwable) {
        if (throwable == null)
            throw new RuntimeExceptionWrapper(new NullPointerException(
                    "null cannot be thrown"));
        if (isValidThrowable(throwable))
            return;

        throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                "last method called on mock cannot throw "
                        + throwable.getClass().getName()));
    }

    private void requireVoidMethod() {
        if (!lastMethodIsVoidMethod()) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "last method called on mock is not a void method"));
        }
    }

    private boolean lastMethodIsVoidMethod() {
        Class returnType = lastMethodCall.getMethod().getReturnType();
        boolean isVoid = returnType.equals(Void.TYPE);
        return isVoid;
    }

    private boolean isValidThrowable(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return true;
        }
        if (throwable instanceof Error) {
            return true;
        }
        Class[] exceptions = lastMethodCall.getMethod().getExceptionTypes();
        Class throwableClass = throwable.getClass();
        for (int i = 0; i < exceptions.length; i++) {
            if (exceptions[i].isAssignableFrom(throwableClass))
                return true;
        }
        return false;
    }
}