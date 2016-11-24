import minicp.ReversibleContext;
import minicp.ReversibleInt;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestReversibleInt {

    @Test
    public void testReversibleInt() {
        ReversibleContext rc = new ReversibleContext();
        ReversibleInt a = new ReversibleInt(rc,5);
        ReversibleInt b = new ReversibleInt(rc,5);
        assertTrue(a.getValue() == 5);
        a.setValue(7);
        b.setValue(13);
        assertTrue(a.getValue() == 7);

        rc.push();

        a.setValue(10);
        assertTrue(a.getValue() == 10);
        a.setValue(11);
        assertTrue(a.getValue() == 11);
        b.setValue(16);
        b.setValue(15);
        rc.pop();
        assertTrue(a.getValue() == 7);
        assertTrue(b.getValue() == 13);

    }
}
