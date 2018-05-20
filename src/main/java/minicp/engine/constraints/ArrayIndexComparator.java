package minicp.engine.constraints;

import minicp.engine.core.IntVar;

import java.util.Comparator;

public class ArrayIndexComparator implements Comparator<Integer>
{
    private final IntVar[] array;
    private final int[] duration;
    private final int by;

    public ArrayIndexComparator(IntVar[] array, int[] duration, int by)
    {
        this.array = array;
        this.duration = duration;
        this.by = by;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2) {
        if (by == 0) {
            return array[index1].getMax() + duration[index1] - array[index2].getMax() - duration[index2];
        }
        else if (by == 1){
            return array[index1].getMax() - array[index2].getMax();
        }
        else if (by== 2){
            return array[index1].getMin()+duration[index1]-array[index2].getMin()-duration[index2];
        }
        else {
            return array[index1].getMin() - array[index2].getMin();
        }
    }
}