.. _minicp:


******************************
Exercises
******************************

We propose a set of exercises to extend mini-cp with useful features.
By doing these exercises you will gradually progress in your understanding of CP.
For each exercise, we ask you to implement JUnit tests to make sure that
your implementation works as expected.
If you don't test each feature independently you take the risk to
loose a lot of time finding very difficult bugs.


Learning Outcomes
=======================================

Be able to

* Understand reversible data structures
* Understand a domain
* Implement global constraints
* Implement custom search
* Model CP easy problems
* Use LNS
* Write unit-tests for constraints and models
* Debug constraints, models, etc



Less or equal reified constraint
=================================

Implement `IsLessOrEqual.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/IsLessOrEqual.java?at=master>`_

This is a reified constraint for `b iff x <= c`
that is boolean variable `b` is set true if and only if `x` variable is less or equal to value `c`.

For example the constraint holds for

.. code-block:: java

    b = true , x = 4, c = 5
    b = false, x = 4, c = 2


but is violated for

.. code-block:: java

    b = true , x = 5, c = 4
    b = false, x = 2, c = 4


Check that your implementation passes the tests `IsLessOrEqualTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/IsEqualTest.java?at=master>`_


Element constraint
=================================


Implement `Element1D.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/Element1D.java?at=master>`_


Two possibilities:

1. extends `Element2D` and reformulate `Element1D` as an `Element2D` constraint in super call of the constructor.
2. implement a dedicated algo (propagate, etc) for `Element1D` by taking inspiration from `Element2D`.

An element constraint is to index an array `T` by an index variable `x` and link the result with a variable `z`.
More exactly the relation `T[x]=z` must hold.

Assuming `T=[1,3,5,7,3]`, the constraint holds for

.. code-block:: java

    x = 1, z = 3
    x = 3, z = 7


but is violated for

.. code-block:: java

    x = 0, z = 2
    x = 3, z = 3


Check that your implementation passes the tests `Element1DTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/Element1DTest.java?at=master>`_


Circuit Constraint
========================

The circuit constraint enforces an hamiltonian circuit on a successor array.
On the next example the successor array is `[2,4,1,5,3,0]`

.. image:: _static/circuit.svg
    :scale: 50
    :width: 250
    :alt: Circuit


All the successors must be different.
but enforcing the `allDifferent` constraint is not enough.
We must also guarantee it forms a proper circuit (without sub-tours).
This can be done efficiently and incrementally by keeping track of the sub-chains
appearing during the search.
The data-structure for the sub-chains should be a reversible.
Our instance variables used to keep track of the sub-chains are:

.. code-block:: java

    IntVar [] x;
    ReversibleInt [] dest;
    ReversibleInt [] orig;
    ReversibleInt [] lengthToDest;



* `dest[i]` is the furthest node we can reach from node `i` following the instantiated edges.
* `orig[i]` is the furthest node we can reach from node `i` following instantiated edges in reverse direction.
* `lengthToDest[i]` is the number of instantiated edges on the path from node `i` to `dest[i]`.

Consider the following example with instantiated edges colored in grey.

.. image:: _static/circuit-subtour.svg
    :scale: 50
    :width: 250
    :alt: Circuit

Before the addition of the green link we have

.. code-block:: java

    dest = [2,1,2,5,5,5];
    orig = [0,1,0,4,4,4];
    lengthToDest = [1,0,0,1,2,0];

After the addition of the green link we have

.. code-block:: java

    dest = [2,1,2,2,2,2];
    orig = [4,1,4,4,4,4];
    lengthToDest = [1,0,0,3,4,2];


In your implementation you must update the reversible integers to reflect
the change after the addition of every new edge.
You can use the `CPIntVar.whenBind(...)` method for that.

The filtering in itself consists in preventing to close a
sub-tour that would have a length less than `n` (the number of nodes).
Since node 4 has a length to destination (node 2) of 4 (<6), the destination node 2 can not have 4 as successor
and the red link is deleted.
This filtering was introduced in [TSP1998]_ for solving the TSP with CP.


Implement `Circuit.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/Circuit.java?at=master>`_.

Check that your implementation passes the tests `CircuitTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/CircuitTest.java?at=master>`_.


.. [TSP1998] Pesant, G., Gendreau, M., Potvin, J. Y., & Rousseau, J. M. (1998). An exact constraint logic programming algorithm for the traveling salesman problem with time windows. Transportation Science, 32(1), 12-29.


Custom search strategy
=================================

Modify `TSP.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/examples/TSP.java?at=master>`_
to implement a custom search strategy.
A skeleton is the following one:


.. code-block:: java

        DFSearch dfs = makeDfs(cp,
                selectMin(succ,
                        succi -> succi.getSize() > 1, // filter
                        succi -> succi.getSize(), // variable selector
                        succi -> {
                            int v = succi.getMin(); // value selector (TODO)
                            return branch(() -> equal(succi,v),
                                    () -> notEqual(succi,v));
                        }
                ));





* The unbound variable selected is one with smallest domain (first-fail).
* It is then assigned the minimum value in the domain.

This value selection strategy is not well suited for the TSP (and VRP).
The one you design should be more similar to the decision you would
make manually in a greedy fashion.
For instance you can select as a successor for `succi`
the closest city in the domain.

Hint: Since there is no iterator on the domain of a variable, you can
iterate from the minimum value to the maximum one using a for loop
and check if it is in the domain with the `contains` method.

You can also implement a min-regret variable selection strategy.
It selects the variable with the largest different between the closest
successor city and the second closest one.
The idea is that it is critical to decide the successor for this city first
because otherwise you will regret it the most.

Observe the first solution obtained and its objective value ?
Is it better than the naive first fail ?
Also observe the time and number of backtracks necessary for proving optimality.
By how much did you reduce the computation time ?


LNS
=================================================================

Modify further `TSP.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/examples/TSP.java?at=master>`_
to implement a LNS search.
Use the larger 17x17 distance matrix for this exercise.

What you should do:


* Record the assignment of the current best solution. Hint: use the `onSolution` call-back on the `DFSearch`object.
* Implement a restart strategy fixing randomly '10%' of the variables to their value in the current best solution.
* Each restart has a failure limit of 100 backtracks.

An example of LNS search is given in  `QAPLNS.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/examples/QAPLNS.java?at=master>`_.
You can simply copy/paste/modify this implementation for the TSP.


Does it converge faster to good solutions than the standard DFS ?
What is the impact of the percentage of variables relaxed (experiment with 5, 10 and 20%) ?
What is the impact of the failure limit (experiment with 50, 100 and 1000)?
Which parameter setting work best? How would you choose it?


Cumulative Constraint: Decomposition
========================

The `Cumulative` constraint models a scheduling resource with fixed capacity.
It has the following signature:

.. code-block:: java

    public Cumulative(IntVar[] start, int[] duration, int[] demand, int capa)

where `capa` is the capacity of the resource and `start`, `duration`, and `demand` are arrays of the same size and represents
properties of activities:

* `start[i]` is the variable specifying the start time of activity `i`
* `duration[i]` is the duration of activity `i`
* `demand[i]` is the resource consumption or demand of activity `i`




The constraint ensures that the cumulative consumption of activities (also called consumption profile)
at any time is below a given capacity:

.. math:: \forall t: \sum_{i \mid t \in \left [start[i]..start[i]+duration[i]-1 \right ]} demand[i] \le capa



The next visual example depicts three activities and their corresponding
consumption profile. As it can be observed, the profile never exceeds
the capacity 4.


.. image:: _static/scheduling.svg
    :scale: 50
    :width: 400
    :alt: scheduling cumulative


It corresponds to the instantiation of the Cumulative constraint:

.. code-block:: java

    Cumulative(start = [ 1, 2, 3], duration = [8, 3, 3], demand = [1, 2, 2], capa = 4)



Implement `CumulativeDecomp.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/CumulativeDecomp.java?at=master>`_.
This is a decomposition or reformulation of the cumulative constraint
in terms of simple arithmetic and logical constraints as
used in the above equation to describe its semantic.


At any time `t` of the horizon a `BoolVar overlaps[i]`
tells whether activity `i` overlaps time `t` or not.
Then the overall consumption in `t` is obtained by:

.. math:: \sum_{i} overlaps[i]*demand[i] \le capa


First make sure you understand the following code, then
add the few lines in the `TODO` to make
sure `overlaps` has the intended meaning.


.. code-block:: java

    public void post() throws InconsistencyException {

        int min = Arrays.stream(start).map(s -> s.getMin()).min(Integer::compare).get();
        int max = Arrays.stream(end).map(e -> e.getMax()).max(Integer::compare).get();

        for (int t = min; t < max; t++) {

            BoolVar[] overlaps = new BoolVar[start.length];
            for (int i = 0; i < start.length; i++) {
                overlaps[i] = makeBoolVar(cp);

                // TODO
                // post the constraints to enforce
                // that overlaps[i] is true iff start[i] <= t && t < start[i] + duration[i]
                // hint: use IsLessOrEqual, introduce BoolVar, use views minus, plus, etc.
                //       logical constraints (such as logical and can be modeled with sum)

            }

            IntVar[] overlapHeights = makeIntVarArray(cp, start.length, i -> mul(overlaps[i], demand[i]));
            IntVar cumHeight = sum(overlapHeights);
            cumHeight.removeAbove(capa);

        }




Check that your implementation passes the tests `CumulativeDecompTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/CumulativeDecompTest.java?at=master>`_.




Cumulative Constraint: Time-Table filtering
========================

Cumulative and Time-Table Filtering [TT2015]_

.. [TT2015] Gay, S., Hartert, R., & Schaus, P. (2015, August). Simple and scalable time-table filtering for the cumulative constraint. In International Conference on Principles and Practice of Constraint Programming (pp. 149-157). Springer.


Table Constraint
========================

Compact Table [CT2016]_

.. [CT2016] Demeulenaere, J., Hartert, R., Lecoutre, C., Perez, G., Perron, L., Régin, J. C., & Schaus, P. (2016, September). Compact-table: Efficiently filtering table constraints with reversible sparse bit-sets. In International Conference on Principles and Practice of Constraint Programming (pp. 207-223). Springer.


Conflict based search strategy (optional)
=================================================================


Last Conflict [LC2009]_
Conflict Ordering Search [COS2015]_


.. [LC2009] Lecoutre, C., Saïs, L., Tabary, S., & Vidal, V. (2009). Reasoning from last conflict (s) in constraint programming. Artificial Intelligence, 173(18), 1592-1614.

.. [COS2015] Gay, S., Hartert, R., Lecoutre, C., & Schaus, P. (2015, August). Conflict ordering search for scheduling problems. In International conference on principles and practice of constraint programming (pp. 140-148). Springer.


Discrepancy Limited Search (optional)
=================================================================

Implement ``DiscrepancyBranching``, a branching that can wrap any branching
to limit the discrepancy of the branching.


Restarts (optional)
========================

TODO

Watched Literals: The or (clause) constraint
========================

TODO

AllDifferent Forward Checking (optional)
=================================

Implement a dedicated algorithm for the all-different.
Whenever a variable is bound to a value, this value is removed from the domain of other variables.


Implement a domain iterator (optional)
============================

TODO










  
     


