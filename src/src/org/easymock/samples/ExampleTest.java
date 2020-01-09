/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.samples;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class ExampleTest extends TestCase {

    private ClassUnderTest classUnderTest;

    private MockControl control;

    private Collaborator mock;

    protected void setUp() {
        control = MockControl.createControl(Collaborator.class);
        mock = (Collaborator) control.getMock();
        classUnderTest = new ClassUnderTest();
        classUnderTest.addListener(mock);
    }

    public void testRemoveNonExistingDocument() {
        control.replay();
        classUnderTest.removeDocument("Does not exist");
    }

    public void testAddDocument() {
        mock.documentAdded("New Document");
        control.replay();
        classUnderTest.addDocument("New Document", new byte[0]);
        control.verify();
    }

    public void testAddAndChangeDocument() {
        mock.documentAdded("Document");
        mock.documentChanged("Document");
        control.setVoidCallable(3);
        control.replay();
        classUnderTest.addDocument("Document", new byte[0]);
        classUnderTest.addDocument("Document", new byte[0]);
        classUnderTest.addDocument("Document", new byte[0]);
        classUnderTest.addDocument("Document", new byte[0]);
        control.verify();
    }

    public void testVoteForRemoval() {
        // expect document addition
        mock.documentAdded("Document");
        // expect to be asked to vote, and vote for it
        control.expectAndReturn(mock.voteForRemoval("Document"), 42);
        // expect document removal
        mock.documentRemoved("Document");

        control.replay();
        classUnderTest.addDocument("Document", new byte[0]);
        assertTrue(classUnderTest.removeDocument("Document"));
        control.verify();
    }

    public void testVoteAgainstRemoval() {
        // expect document addition
        mock.documentAdded("Document");
        // expect to be asked to vote, and vote against it
        control.expectAndReturn(mock.voteForRemoval("Document"), -42); // 
        // document removal is *not* expected

        control.replay();
        classUnderTest.addDocument("Document", new byte[0]);
        assertFalse(classUnderTest.removeDocument("Document"));
        control.verify();
    }

    public void testVoteForRemovals() {
        mock.documentAdded("Document 1");
        mock.documentAdded("Document 2");
        mock.voteForRemovals(new String[] { "Document 1", "Document 2" });
        control.setMatcher(MockControl.ARRAY_MATCHER);
        control.setReturnValue(42);
        mock.documentRemoved("Document 1");
        mock.documentRemoved("Document 2");
        control.replay();
        classUnderTest.addDocument("Document 1", new byte[0]);
        classUnderTest.addDocument("Document 2", new byte[0]);
        assertTrue(classUnderTest.removeDocuments(new String[] { "Document 1",
                "Document 2" }));
        control.verify();
    }

    public void testVoteAgainstRemovals() {
        mock.documentAdded("Document 1");
        mock.documentAdded("Document 2");
        mock.voteForRemovals(new String[] { "Document 1", "Document 2" });
        control.setMatcher(MockControl.ARRAY_MATCHER);
        control.setReturnValue(-42);
        control.replay();
        classUnderTest.addDocument("Document 1", new byte[0]);
        classUnderTest.addDocument("Document 2", new byte[0]);
        assertFalse(classUnderTest.removeDocuments(new String[] { "Document 1",
                "Document 2" }));
        control.verify();
    }
}
