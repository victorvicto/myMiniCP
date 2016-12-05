package minicp.core;


import minicp.search.DFSearch;
import minicp.search.DFSearchNode;
import minicp.search.Inconsistency;

import java.util.Stack;

public class Store extends DFSearchNode {

    Stack<Constraint> propagationQueue = new Stack<>();

    public void enqueue(Constraint c) {
        if (!c.inQueue) {
            c.inQueue = true;
            propagationQueue.add(c);
        }
    }


    public void fixPoint() throws Inconsistency {
        boolean failed = false;
        while (!propagationQueue.isEmpty()) {
            Constraint c = propagationQueue.pop();
            c.inQueue = false;
            if (!failed) {
                try { c.propagate(); }
                catch (Inconsistency e) {
                    failed = true;
                }
            }
        }
        if (failed) throw DFSearch.INCONSISTENCY;
    }


    public void add(Constraint c) throws Inconsistency {
        add(c,true);
    }

    public void add(Constraint c, boolean fixPoint) throws Inconsistency {
        c.setUp();
        if (fixPoint) {
            fixPoint();
        }
    }

}

