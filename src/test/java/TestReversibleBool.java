import minicp.reversible.ReversibleBool;
import minicp.reversible.ReversibleContext;
import minicp.reversible.ReversibleInt;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestReversibleBool {

    @Test
    public void testReversibleBool() {
        ReversibleContext rc = new ReversibleContext();

        ReversibleBool b1 = new ReversibleBool(rc,true);
        ReversibleBool b2 = new ReversibleBool(rc,false);

        rc.push();

        b1.setValue(true);
        b1.setValue(false);
        b1.setValue(true);

        b2.setValue(false);
        b2.setValue(true);

        rc.pop();

        assertTrue(b1.getValue());
        assertFalse(b2.getValue());

    }



}
