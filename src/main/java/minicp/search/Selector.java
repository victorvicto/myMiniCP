package minicp.search;

public class Selector {

    public static final Alternative[] EMPTY = new Alternative[0];

    public static Alternative[] branch(Alternative... alternatives) {
        return alternatives;
    }

    @FunctionalInterface
    public interface Filter<T> {
        boolean call(T x);
    }

    @FunctionalInterface
    public interface ValueFun<T> {
        float call(T x);
    }

    @FunctionalInterface
    public interface BranchOn<T> {
        Alternative[] call(T x);
    }

    public static <T> Choice selectMin(T[] x, Filter<T> p, ValueFun<T> f, BranchOn<T> body) {
        return () -> {
            T sel = null;
            for (T xi : x) {
                if (p.call(xi)) {
                    sel = sel == null || (f.call(xi) < f.call(sel)) ? xi : sel;
                }
            }
            if (sel == null) {
                return EMPTY;
            } else {
                return body.call(sel);
            }
        };
    }
}
