package minicp.cp.core;

/**
 * Created by ldm on 1/12/17.
 */
public interface Notifier {
    void bindEvt();
    void domainEvt(int dsz);
    void updateMinEvt(int dsz);
    void updateMaxEvt(int dsz);
}
