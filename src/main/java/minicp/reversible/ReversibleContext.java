package minicp.reversible;

import java.util.Stack;


public class ReversibleContext {

    public long magic = 0;

    private Stack<TrailEntry> trail = new Stack<TrailEntry>();
    private Stack<Integer>    trailLimit = new Stack<Integer>();

    /**
     * Initialize a reversible context
     * The current level is -1
     */
    public ReversibleContext() {}

    public void pushOnTrail(TrailEntry entry) {
        trail.push(entry);
    }

    /**
     *
     * Restore all the entries from the top of the trailStack
     * to the limit (excluded)
     */
    private void restoreToSize(int size) {
        int n = trail.size() - size;
        for (int i = 0; i < n; i++) {
            trail.pop().restore();
        }
    }

    /**
     * @return The current level
     */
    public int getLevel() {
        return trailLimit.size()-1;
    }

    /**
     * Stores the current state
     * such that it can be recovered using pop()
     * Increase the level by 1
     */
    public void push() {
        magic++;
        trailLimit.push(trail.size());
    }


    /**
     *  Restores state as it was at getLevel()-1
     *  Decrease the level by 1
     */
    public void pop() {
        restoreToSize(trailLimit.pop());
        // Increments the magic because we want to trail again
        magic++;
    }

    /**
     *  Restores the state as it was at level 0 (first push)
     *  The level is now -1.
     *  Notice that you'll probably want to push after this operation.
     */
    public void popAll() {
        popUntil(0);
        trail.clear();
    }

    /**
     *  Restores the state as it was at level
     *  @param level
     */
    public void popUntil(int level) {
        while(trailLimit.size() > level) {
            pop();
        }
    }


}