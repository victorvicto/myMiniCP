package minicp.reversible;



public class ReversibleSparseSet {

    private int [] values;
    private int [] indexes;
    private ReversibleInt size;
    private ReversibleInt min;
    private ReversibleInt max;
    private int n;

    public ReversibleSparseSet(ReversibleContext rc, int n) {
        this.n = n;
        size = new ReversibleInt(rc,n);
        min = new ReversibleInt(rc,0);
        max = new ReversibleInt(rc,n-1);
        values = new int [n];
        indexes = new int [n];
        for (int i = 0; i < n; i++) {
            values[i] = i;
            indexes[i] = i;
        }
    }


    private void exchangePositions(int val1, int val2) {
        assert(checkVal(val1));
        assert(checkVal(val2));
        int v1 = val1;
        int v2 = val2;
        int i1 = indexes[v1];
        int i2 = indexes[v2];
        values[i1] = v2;
        values[i2] = v1;
        indexes[v1] = i2;
        indexes[v2] = i1;
    }

    private boolean checkVal(int val) {
        assert(val <= values.length-1);
        return true;
    }

    /**
     * @return an array representation of values present in the set
     */
    public int[] toArray()  {
        int [] res = new int[getSize()];
        fillArray(res);
        return res;
    }

    /**
     * @return set the first values of dest to the ones
     *         of the set and return the size of the set
     */
    public int fillArray(int [] dest) {
        int s = size.getValue();
        System.arraycopy(values, 0, dest, 0, s);
        return s;
    }


    /**
     * remove all elements in the set
     */
    public void empty() {
        size.setValue(0);
    }

    /**
     * @return true if the set is empty
     */
    public boolean isEmpty() {
        return getSize() == 0;
    }

    public int getSize() { return size.getValue(); }

    public int getMin() { return min.getValue(); }

    public int getMax() { return max.getValue(); }

    private void updateBoundsValRemoved(int val) {
        updateMaxValRemoved(val);
        updateMinValRemoved(val);
    }

    private void updateMaxValRemoved(int val) {
        if (!isEmpty() && max.getValue() == val) {
            assert(!contains(val));
            //the maximum was removed, search the new one
            for (int v = val-1; v >= min.getValue(); v--) {
                if (contains(v)) {
                    max.setValue(v);
                    return;
                }
            }
        }
    }

    private void updateMinValRemoved(int val) {
        if (!isEmpty() && min.getValue() == val) {
            assert(!contains(val));
            //the minimum was removed, search the new one
            for (int v = val+1; v <= max.getValue(); v++) {
                if (contains(v)) {
                    min.setValue(v);
                    return;
                }
            }
        }
    }

    public boolean remove(int val) {
        assert(checkVal(val));
        if (!contains(val)) return false; //the value has already been removed
        int s = getSize();
        exchangePositions(val, values[s-1]);
        size.decrement();
        updateBoundsValRemoved(val);
        return true;
    }

    public boolean contains(int val) {
        if (val < 0 || val >= n) return false;
        return indexes[val] < getSize();
    }

    public void removeAllBut(int v) {
        // we only have to put in first position this value and set the size to 1
        assert(checkVal(v));
        assert(contains(v));
        int val = values[0];
        int index = indexes[v];
        indexes[v] = 0;
        values[0] = v;
        indexes[val] = index;
        values[index] = val;
        min.setValue(v);
        max.setValue(v);
        size.setValue(1);
    }

    public void removeAll() {
        size.setValue(0);
    }


    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("{");
        for (int i = 0; i < getSize()-1; i++) {
            b.append(values[i]);
            b.append(',');
        }
        if (getSize() > 0) b.append(values[getSize()-1]);
        b.append("}");
        return b.toString();
    }

}

