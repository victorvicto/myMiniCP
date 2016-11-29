package minicp.search;


import java.util.LinkedList;
import java.util.List;

public class DFSearch {

    public static interface SolutionListener {
        public void solutionFound();
    }

    private List<SolutionListener> solutionListeners;
    private Branching branching;
    private DFSearchNode node;

    public DFSearch(DFSearchNode root, Branching branching) {
        this.node = root;
        this.branching = branching;
        solutionListeners = new LinkedList<SolutionListener>();
    }

    public void onSolution(SolutionListener listener) {
        solutionListeners.add(listener);
    }

    private void notifySolutionFound() {
        solutionListeners.forEach(s -> s.solutionFound());
    }

    public void start() {
        dfs();
    }

    public void dfs() {
        Alternative [] alternatives = branching.getAlternatives();
        if (alternatives.length == 0 && !node.isFailed())
            notifySolutionFound();
        else {
            for (Alternative alt : alternatives) {
                node.push();
                if (alt.execute()) {
                    dfs();
                }
                node.pop();
            }
        }
    }

}
