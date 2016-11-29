import minicp.reversible.ReversibleContext;
import minicp.reversible.ReversibleInt;
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

    @Test
    public void testPopAll() {

        ReversibleContext rc = new ReversibleContext();
        ReversibleInt a = new ReversibleInt(rc,5);
        ReversibleInt b = new ReversibleInt(rc,5);

        rc.push();

        a.setValue(7);
        b.setValue(13);
        a.setValue(13);

        rc.push();

        a.setValue(5);
        b.setValue(10);

        ReversibleInt c = new ReversibleInt(rc,5);

        rc.push();

        a.setValue(8);
        b.setValue(1);
        c.setValue(10);

        rc.popAll();
        rc.push();

        assertEquals(5,a.getValue());
        assertEquals(5,b.getValue());
        assertEquals(5,c.getValue());


        a.setValue(10);
        b.setValue(13);
        b.setValue(16);

        rc.push();

        a.setValue(8);
        b.setValue(10);

        rc.pop();


        assertEquals(10,a.getValue());
        assertEquals(16,b.getValue());
        assertEquals(5,c.getValue());

        rc.popAll();

        assertEquals(5,a.getValue());
        assertEquals(5,b.getValue());
        assertEquals(5,c.getValue());

    }


    @Test
    public void testPopUntill() {

        ReversibleContext rc = new ReversibleContext();
        ReversibleInt a = new ReversibleInt(rc,5);
        ReversibleInt b = new ReversibleInt(rc,5);

        a.setValue(7);
        b.setValue(13);
        a.setValue(13);

        rc.push(); // level 0

        a.setValue(5);
        b.setValue(10);

        ReversibleInt c = new ReversibleInt(rc,5);

        rc.push(); // level 1

        a.setValue(8);
        b.setValue(1);
        c.setValue(10);

        rc.push();

        a.setValue(10);
        b.setValue(13);
        b.setValue(16);

        rc.push();

        a.setValue(8);
        b.setValue(10);

        rc.popUntil(1);
        rc.push();

        assertEquals(5,a.getValue());
        assertEquals(10,b.getValue());
        assertEquals(5,c.getValue());

        a.setValue(8);
        b.setValue(10);
        b.setValue(8);
        b.setValue(10);

        rc.popUntil(0);

        assertEquals(13,a.getValue());
        assertEquals(13,b.getValue());
        assertEquals(5,c.getValue());


    }

}
