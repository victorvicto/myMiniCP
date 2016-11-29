package minicp.core;


import minicp.search.DFSearchNode;

import java.util.Stack;

public class Store extends DFSearchNode {

    Stack<Constraint> propagationQueue = new Stack<>();

    public void enqueue(Constraint c) {
        if (!c.inQueue) {
            c.inQueue = true;
            propagationQueue.add(c);
        }
    }

    public boolean propagate() {
        boolean ok = !failed.getValue();
        while (!propagationQueue.isEmpty()) {
            Constraint c = propagationQueue.pop();
            if (ok) ok = c.propagate();
            c.inQueue = false;
        }
        propagationQueue.clear();
        failed.setValue(!ok);
        return ok;
    }

    public boolean add(Constraint c) {
        return add(c,true);
    }

    public boolean add(Constraint c, boolean propagate) {
        if (!failed.getValue()) {
            boolean ok = c.setUp();
            if (ok && propagate) {
                return propagate();
            }
            else {
                return ok;
            }
        }
        else {
            return false;
        }
    }

}

