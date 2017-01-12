package minicp.util;


public class Counter extends Box<Integer> {

    public Counter() {
        super(0);
    }

    public void incr() {
        set(get()+1);
    }

    public int getValue() {
        return get();
    }
}
