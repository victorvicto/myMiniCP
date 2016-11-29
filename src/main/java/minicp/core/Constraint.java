package minicp.core;


import java.util.Comparator;

public abstract class Constraint {

    protected boolean inQueue = false;

    public abstract boolean setUp();
    public boolean propagate() {return true; };
}
