package minicp.cp.core;

/**
 * Created by ldm on 1/9/17.
 */
public class Status extends Throwable {
    public enum Type {
        Failure,
        Suspend,
        Success,
        NotImplemented
    };
    private Type type;
    public Status(Type t) { type = t;}
    public String toString() { return type.toString();}
}
