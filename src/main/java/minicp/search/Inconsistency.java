package minicp.search;

public abstract class Inconsistency extends Exception {

    public abstract Object feedBack();

    @Override
    public final String toString() {
        return  "Inconsistency";
    }
}

