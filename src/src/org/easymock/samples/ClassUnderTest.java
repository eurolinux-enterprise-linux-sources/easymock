/*
 * Copyright (c) 2001-2005 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package org.easymock.samples;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ClassUnderTest {

    private Set listeners = new HashSet();

    private Map documents = new HashMap();

    public void addListener(Collaborator listener) {
        listeners.add(listener);
    }

    public void addDocument(String title, byte[] document) {
        boolean documentChange = documents.containsKey(title);
        documents.put(title, document);
        if (documentChange) {
            notifyListenersDocumentChanged(title);
        } else {
            notifyListenersDocumentAdded(title);
        }
    }

    public boolean removeDocument(String title) {
        if (!documents.containsKey(title)) {
            return true;
        }

        if (!listenersAllowRemoval(title)) {
            return false;
        }

        documents.remove(title);
        notifyListenersDocumentRemoved(title);

        return true;
    }

    public boolean removeDocuments(String[] titles) {
        if (!listenersAllowRemovals(titles)) {
            return false;
        }

        for (int i = 0; i < titles.length; i++) {
            documents.remove(titles[i]);
            notifyListenersDocumentRemoved(titles[i]);
        }
        return true;
    }

    private void notifyListenersDocumentAdded(String title) {
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            Collaborator listener = (Collaborator) it.next();
            listener.documentAdded(title);
        }
    }

    private void notifyListenersDocumentChanged(String title) {
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            Collaborator listener = (Collaborator) it.next();
            listener.documentChanged(title);
        }
    }

    private void notifyListenersDocumentRemoved(String title) {
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            Collaborator listener = (Collaborator) it.next();
            listener.documentRemoved(title);
        }
    }

    private boolean listenersAllowRemoval(String title) {
        int result = 0;
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            Collaborator listener = (Collaborator) it.next();
            result += listener.voteForRemoval(title);
        }
        return result > 0;
    }

    private boolean listenersAllowRemovals(String[] titles) {
        int result = 0;
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            Collaborator listener = (Collaborator) it.next();
            result += listener.voteForRemovals(titles);
        }
        return result > 0;
    }

}
