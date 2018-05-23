package minicp.util;

import minicp.engine.core.IntVar;

public class storingRetValues {
    private int finalSpan;
    private int[] finalStarts;

    public storingRetValues (int len) {
        this.finalSpan = -1;
        this.finalStarts = new int[len];
    }

    public void UpdateSpan (int s) {
        finalSpan = s;
    }

    public void UpdateStarts (IntVar[] s) {
        for (int i=0; i<s.length; i++) {
            finalStarts[i] = s[i].getMin();
        }
    }

    public void print () {
        if (finalSpan==-1)
            System.out.println("NO FOUND SOLUTION");
        else {
            System.out.println(finalSpan);
            for (int i : finalStarts) {
                System.out.print(i);
                System.out.print(" ");
            }
        }
    }

}
