/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.InvocationHandler;

public interface IProxyFactory {
    Object createProxy(Class toMock, InvocationHandler handler);
}
