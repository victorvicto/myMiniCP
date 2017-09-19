/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2017. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.reversible;


import org.junit.Test;

import java.util.BitSet;
import java.util.Random;
import java.util.Stack;

import static org.junit.Assert.*;


public class ReversibleBitSetTest {

    @Test
    public void bs_random_init() {
        Random r = new Random(45773);

        Trail trail = new Trail();

        byte[] randomBytes = new byte[1024 / 8];
        r.nextBytes(randomBytes);

        BitSet obs = BitSet.valueOf(randomBytes);
        ReversibleBitSet rbs = new ReversibleBitSet(trail, 1024, obs);

        for (int i = 0; i < 1024; i++)
            assertEquals(obs.get(i), rbs.contains(i));
    }

    @Test
    public void bs_zero_init() {
        Trail trail = new Trail();

        BitSet obs = new BitSet();

        assertTrue(new ReversibleBitSet(trail, 128, obs).isEmpty());

        obs.set(82, true);

        assertFalse(new ReversibleBitSet(trail, 128, obs).isEmpty());
    }

    @Test
    public void bs_zero_modif() {
        Trail trail = new Trail();

        BitSet obs = new BitSet();
        ReversibleBitSet rbs = new ReversibleBitSet(trail, 1024, obs);

        trail.push();
        assertTrue(rbs.isEmpty());
        rbs.add(637);
        assertFalse(rbs.isEmpty());
        trail.push();
        assertFalse(rbs.isEmpty());
        rbs.add(892);
        assertFalse(rbs.isEmpty());
        trail.push();
        rbs.remove(637);
        assertFalse(rbs.isEmpty());
        trail.push();
        rbs.remove(892);
        assertTrue(rbs.isEmpty());
        trail.pop();
        assertFalse(rbs.isEmpty());
        trail.pop();
        assertFalse(rbs.isEmpty());
        trail.pop();
        assertFalse(rbs.isEmpty());
        trail.pop();
        assertTrue(rbs.isEmpty());
    }

    @Test
    public void bs_random_modif() {
        Random r = new Random(45773);

        for (int i = 0; i < 10; i++) {
            Trail trail = new Trail();

            byte[] randomBytes = new byte[1024 / 8];
            r.nextBytes(randomBytes);

            BitSet obs = BitSet.valueOf(randomBytes);
            ReversibleBitSet rbs = new ReversibleBitSet(trail, 1024, obs);

            Stack<BitSet> pastBitSets = new Stack<>();

            pastBitSets.push(obs);
            trail.push();

            // Do 100 trailings
            for (int j = 0; j < 100; j++) {
                obs = (BitSet) obs.clone();
                // flip 10 bits randomly
                for (int k = 0; k < 10; k++) {
                    int next = r.nextInt(1024);
                    if (obs.get(next)) {
                        obs.set(next, false);
                        rbs.remove(next);
                    } else {
                        obs.set(next, true);
                        rbs.add(next);
                    }
                }
                assertEquals(obs, rbs.asBitSet());
                assertEquals(obs.cardinality(), rbs.getSize());
                pastBitSets.push(obs);
                trail.push();
            }

            while (!pastBitSets.isEmpty()) {
                obs = pastBitSets.pop();
                trail.pop();
                assertEquals(obs, rbs.asBitSet());
                assertEquals(obs.cardinality(), rbs.getSize());
            }
        }
    }

    @Test
    public void bs_to_array() {
        Random r = new Random(45773);

        Trail trail = new Trail();

        byte[] randomBytes = new byte[1024 / 8];
        r.nextBytes(randomBytes);

        BitSet obs = BitSet.valueOf(randomBytes);
        ReversibleBitSet rbs = new ReversibleBitSet(trail, 1024, obs);

        assertArrayEquals(obs.stream().toArray(), rbs.toArray());

        // flip 100 bits randomly
        for (int k = 0; k < 100; k++) {
            int next = r.nextInt(1024);
            if (obs.get(next)) {
                obs.set(next, false);
                rbs.remove(next);
            } else {
                obs.set(next, true);
                rbs.add(next);
            }
        }

        assertArrayEquals(obs.stream().toArray(), rbs.toArray());
    }

    @Test
    public void bs_and_or() {
        Random r = new Random(45773);

        Trail trail = new Trail();

        byte[] randomBytes = new byte[1024 / 8];
        r.nextBytes(randomBytes);

        BitSet obs = BitSet.valueOf(randomBytes);

        randomBytes = new byte[1024 / 8];
        r.nextBytes(randomBytes);

        BitSet obs2 = BitSet.valueOf(randomBytes);
        ReversibleBitSet rbs = new ReversibleBitSet(trail, 1024, obs);


        BitSet obsAnd = (BitSet) obs.clone();
        obsAnd.and(obs2);
        BitSet obsOr = (BitSet) obs.clone();
        obsOr.or(obs2);


        assertArrayEquals(obs.stream().toArray(), rbs.toArray());

        trail.push();
        rbs.and(obs2);
        assertArrayEquals(obsAnd.stream().toArray(), rbs.toArray());

        trail.pop();
        assertArrayEquals(obs.stream().toArray(), rbs.toArray());

        trail.push();
        rbs.or(obs2);
        assertArrayEquals(obsOr.stream().toArray(), rbs.toArray());

        trail.pop();
        assertArrayEquals(obs.stream().toArray(), rbs.toArray());
    }
}