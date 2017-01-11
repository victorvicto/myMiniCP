package minicp.cp.core;

import minicp.search.Alternative;
import minicp.search.Branching;
import minicp.search.Choice;

/**
 * Created by ldm on 1/10/17.
 * @Pierre
 * The selectMin method is a variable selector (for first-fail).
 * It is generic for the type of "variable" making the explorer class truly solver independent.
 * Note that the three functional interface are, therefore, also generic and that Java
 * is fully capable of inferring the type based on the call to the selector (selectMin).
 */
public class Explorer {
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

    public <T> Choice selectMin(T[] x, Filter<T> p, ValueFun<T> f, BranchOn<T> body) {
        return () -> {
            T  sel   = null;
            for(T xi : x) {
                if (p.call(xi)) {
                    sel = sel==null ||  (f.call(xi) < f.call(sel)) ? xi : sel;
                }
            }
            if (sel == null) {
                return Branching.EMPTY;
            } else {
                return body.call(sel);
            }
        };
    }
}
