/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.tests;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.internal.ObjectMethodsFilter;

public class ObjectMethodsTest extends TestCase {
    private MockControl control;

    private EmptyInterface mock;

    private interface EmptyInterface {
    }

    protected void setUp() {
        control = MockControl.createControl(EmptyInterface.class);
        mock = (EmptyInterface) control.getMock();
    }

    public void testEqualsBeforeActivation() {
        assertEquals(mock, mock);
        assertTrue(!mock.equals(null));
    }

    public void testEqualsAfterActivation() {
        control.replay();
        assertEquals(mock, mock);
        assertTrue(!mock.equals(null));
    }

    public void testHashCode() {
        int hashCodeBeforeActivation = mock.hashCode();
        control.replay();
        int hashCodeAfterActivation = mock.hashCode();
        assertEquals(hashCodeBeforeActivation, hashCodeAfterActivation);
    }

    public void testToStringBeforeActivation() {
        assertEquals("EasyMock for " + EmptyInterface.class.toString(), mock
                .toString());
    }

    public void testToStringAfterActivation() {
        control.replay();
        assertEquals("EasyMock for " + EmptyInterface.class.toString(), mock
                .toString());
    }

    private static class MockedClass {
    }

    private static class DummyProxy extends MockedClass {
    }

    // if no interface is found, ObjectMethodFilter should use the
    // superclasses' name. This is needed for the class extension.
    // We have to find a way to find that special case there...
    public void testToStringForClasses() throws Throwable {
        ObjectMethodsFilter filter = new ObjectMethodsFilter(null);
        Method toString = Object.class.getMethod("toString", new Class[0]);
        assertEquals("EasyMock for " + MockedClass.class.toString(), filter
                .invoke(new DummyProxy(), toString, new Object[0]));
    }

}
