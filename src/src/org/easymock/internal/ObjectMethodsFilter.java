/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ObjectMethodsFilter implements InvocationHandler {
    private Method equalsMethod;

    private Method hashCodeMethod;

    private Method toStringMethod;

    private InvocationHandler delegate;

    public ObjectMethodsFilter(InvocationHandler delegate) {
        try {
            equalsMethod = Object.class.getMethod("equals",
                    new Class[] { Object.class });
            hashCodeMethod = Object.class.getMethod("hashCode", (Class[]) null);
            toStringMethod = Object.class.getMethod("toString", (Class[]) null);
        } catch (NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new RuntimeException("An Object method could not be found!");
            // ///CLOVER:ON
        }
        this.delegate = delegate;
    }

    public final Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (equalsMethod.equals(method)) {
            return proxy == args[0] ? Boolean.TRUE : Boolean.FALSE;
        }
        if (hashCodeMethod.equals(method)) {
            return new Integer(System.identityHashCode(proxy));
        }
        if (toStringMethod.equals(method)) {
            return mockToString(proxy);
        }
        return delegate.invoke(proxy, method, args);
    }

    private String mockToString(Object proxy) {
        return "EasyMock for " + mockedInterface(proxy);
    }

    private String mockedInterface(Object proxy) {
        Class[] interfaces = proxy.getClass().getInterfaces();
        return interfaces.length > 0 ? interfaces[0].toString() : proxy
                .getClass().getSuperclass().toString();
    }
}