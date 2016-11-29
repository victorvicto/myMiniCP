package minicp.reversible;

import java.util.ArrayList;

public class ReversibleStack<E> {

    ReversibleInt size;
    ArrayList<E> stack;

    public ReversibleStack(ReversibleContext rc) {
        size = new ReversibleInt(rc,0);
        stack = new ArrayList<E>();
    }

    public void push(E elem) {
        stack.add(size.getValue(),elem);
        size.increment();
    }

    public int size() { return size.getValue(); }

    public E get(int index) { return stack.get(index); }
}
