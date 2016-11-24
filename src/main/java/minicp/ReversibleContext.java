package minicp;

import java.util.Stack;


public class ReversibleContext {

    public long magic = 0;

    private Stack<TrailEntry> trailStack= new Stack<TrailEntry>();
    private Stack<TrailEntry> pointerStack= new Stack<TrailEntry>();

    public void pushOnTrail(ReversibleInt rInt, int v) {
        trailStack.push(new TrailEntry(rInt,v));
    }

    private void restoreUntil(TrailEntry limit) {
        while (trailStack.peek() != limit) {
            trailStack.pop().restore();
        }
    }

    /** Stores the current state of the node on a stack */
    public void push() {
        magic++;
        pointerStack.push(trailStack.peek());
    }


    /** Restores state on top of the stack of states and remove it from the stack */
    public void pop() {
        // Restores the state of each reversible
        restoreUntil(pointerStack.pop());
        // Increments the magic because we want to trail again
        magic++;
    }

    /**
     *  Restores the node to its initial state
     *  Note: does not execute the on pop actions
     */
    public void popAll() {
        if (!pointerStack.isEmpty()) {
            restoreUntil(pointerStack.firstElement());
        }
        // increments the magic because we want to trail again
        magic++;
    }


}