package minicp.core;


import minicp.search.Inconsistency;

import java.util.Comparator;

public abstract class Constraint {

    protected boolean inQueue = false;

    public abstract void setUp() throws Inconsistency;
    public void propagate() throws Inconsistency {};
}
