/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JavaProxyFactory implements IProxyFactory {
    public Object createProxy(Class toMock, InvocationHandler handler) {
        return Proxy.newProxyInstance(toMock.getClassLoader(),
                new Class[] { toMock }, handler);
    }
}
