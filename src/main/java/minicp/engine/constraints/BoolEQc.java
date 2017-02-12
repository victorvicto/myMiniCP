package minicp.engine.constraints;

import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.util.InconsistencyException;

/**
 * Created by ldm on 2/12/17.
 */
public class BoolEQc extends Constraint { // b <=> _x == _c
    private IntVar _b;
    private IntVar _x;
    private int _c;

    public BoolEQc(IntVar b, IntVar x, int c) {
        super(x.getSolver());
        _b = b;
        _x = x;
        _c = c;
    }

    @Override
    public void post() throws InconsistencyException {
        if (_b.isBound()) {
            if (_b.getMin()==1)
                _x.assign(_c);
            else
                _x.remove(_c);
        } else if (_x.isBound()) {
            _b.assign(_x.getMin() == _c ? 1 : 0);
        } else if (!_x.contains(_c)) {
            _b.assign(0);
        } else {
            _b.whenBind(() -> {
                        if (_b.getMin()==1)
                            _x.assign(_c);
                        else _x.remove(_c);
                    });
            _x.whenBind(() -> {
                        _b.assign(_x.getMin() == _c ? 1 : 0);
                    });
            _x.whenDomainChange(() ->  {
                        if (!_x.contains(_c))
                            _b.assign(0);
                    });
        }
    }
}
