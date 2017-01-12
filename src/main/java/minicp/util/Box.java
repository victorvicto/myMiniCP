package minicp.util;

/**
 * Created by ldm on 1/9/17.
 */

public class Box<T> {
    private T _value;
    public Box(T v) { _value = v;}
    public T get() { return _value;}
    public void set(T v) { _value = v;}
    public String toString() { return _value.toString();}
}
