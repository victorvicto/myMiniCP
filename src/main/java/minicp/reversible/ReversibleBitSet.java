package minicp.reversible;

import java.util.Arrays;
import java.util.BitSet;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ReversibleBitSet {

    /**
     * Entry of the trail. Contains the word index to be restored, and the previous value of this word.
     */
    class TrailEntryBitSet implements TrailEntry {
        private final int widx;
        private final long wval;

        public TrailEntryBitSet(int widx, long wval) {
            this.widx = widx;
            this.wval = wval;
        }

        public void restore() {
            if (ReversibleBitSet.this.currentValues[widx] == 0)
                ReversibleBitSet.this.nZeros--;
            ReversibleBitSet.this.currentValues[widx] = wval;
            if (ReversibleBitSet.this.currentValues[widx] == 0)
                ReversibleBitSet.this.nZeros++;
        }
    }

    private Trail trail;
    private int nZeros; // total number of *words* that are currently at zero
    private long[] currentValues; // values of the words
    private long[] magic; // magic[i] indicates the last time the word i was trailed

    /**
     * Init the ReversibleBitSet
     *
     * @param trail         trail context
     * @param size          number of elements that can be contained inside the RBitSet
     * @param initialValues initial values to be put inside the RBitSet. initialValues.length must be < size.
     */
    public ReversibleBitSet(Trail trail, int size, BitSet initialValues) {
        // Create currentValues
        long[] initialValuesLong = initialValues.toLongArray();
        currentValues = new long[(size + 63) / 64];
        System.arraycopy(initialValuesLong, 0, currentValues, 0, initialValuesLong.length);

        // Init nZeros
        nZeros = 0;
        for (int i = 0; i < currentValues.length; i++)
            if (currentValues[i] == 0)
                nZeros++;

        // Create the magic array, that will help us to know when we should trail words
        magic = new long[currentValues.length];
        Arrays.fill(magic, -1);
        this.trail = trail;
    }

    /**
     * @return true if the set is empty, false else
     */
    public boolean isEmpty() {
        return nZeros == currentValues.length;
    }

    /**
     * @return the number of items in this set
     */
    public int getSize() {
        int size = 0;
        for (int i = 0; i < currentValues.length; i++)
            size += Long.bitCount(currentValues[i]);
        return size;
    }

    /**
     * @return true if value idx is in the set, false else
     */
    public boolean contains(int idx) {
        return (currentValues[idx / 64] & (1L << (idx % 64))) != 0L;
    }

    /**
     * @return this set as a BitSet
     */
    public BitSet asBitSet() {
        return BitSet.valueOf(currentValues);
    }

    /**
     * @return an array representation of values present in the set
     */
    public int[] toArray() {
        int[] res = new int[getSize()];
        fillArray(res);
        return res;
    }

    /**
     * set the first values of <code>dest</code> to the ones
     * present in the set
     *
     * @param dest, an array large enough dest.length >= getSize()
     * @return the size of the set
     */
    public int fillArray(int[] dest) {
        int destIdx = 0;
        for (int i = 0; i < currentValues.length; i++) {
            long v = currentValues[i];
            while (v != 0) {
                int next = Long.numberOfTrailingZeros(v); //gets the position of the next non-zero bit
                dest[destIdx] = i * 64 + next;
                destIdx++;
                v &= ~(1L << next); //remove the bit we just found
            }
        }
        return destIdx;
    }

    /***************** HELPERS **************/

    /**
     * Apply an operation to each word in the bitset, and push modifications on the trail if needed
     *
     * @param operator a function (Int i, Long v) -> Long that takes as input i, the index of the current word,
     *                 v, the current value of this word, and returns the new value the word should be assigned to.
     */
    private void applyOperation(BiFunction<Integer, Long, Long> operator) {
        for (int i = 0; i < currentValues.length; i++) {
            int finali = i; //i must be final to be used inside a lambda...
            applyOperation(i, l -> operator.apply(finali, l));
        }
    }

    /**
     * Apply an operation to a specific word in the bitset, and push modifications on the trail if needed
     *
     * @param widx     index of the word to modify
     * @param operator a function (Long v) -> Long that takes as input v, the current value of this word,
     *                 and returns the new value the word should be assigned to.
     */
    private void applyOperation(int widx, Function<Long, Long> operator) {
        long origValue = currentValues[widx];
        long newValue = operator.apply(origValue);
        if (origValue != newValue) {
            // Update nZeros
            if (origValue == 0)
                nZeros--;
            if (newValue == 0)
                nZeros++;

            // Update currentValues
            currentValues[widx] = newValue;

            // Push on trail if needed
            if (magic[widx] != trail.magic) {
                trail.pushOnTrail(new TrailEntryBitSet(widx, origValue));
                magic[widx] = trail.magic;
            }
        }
    }

    /***************** OPERATIONS **************/

    /**
     * Apply the bitwise or operation with another bitset and store the result in this RBitSet
     */
    public void or(BitSet bs) {
        long[] bst = bs.toLongArray();
        applyOperation((idx, val) -> idx < bst.length ? val | bst[idx] : val);
    }

    /**
     * Apply the bitwise and operation with another bitset and store the result in this RBitSet
     */
    public void and(BitSet bs) {
        long[] bst = bs.toLongArray();
        applyOperation((idx, val) -> idx < bst.length ? val & bst[idx] : 0L);
    }

    /**
     * Add item idx in the set (if not already present)
     */
    public void add(int idx) {
        applyOperation(idx / 64, (v) -> v |= 1L << (idx % 64));
    }

    /**
     * Remove item idx from the set (if present)
     */
    public void remove(int idx) {
        applyOperation(idx / 64, (v) -> v & ~(1L << (idx % 64)));
    }
}
