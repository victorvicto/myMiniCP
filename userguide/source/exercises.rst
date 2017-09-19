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



DFS Explicit Stack
===================


The search algorithm of mini-cp is *depth-first-search*.
It is implemented using a recursive method in the class
`DFSSearch.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/search/DFSearch.java?at=master>`_.
To avoid any `stack-overflow` exception due to a deep recursion in Java
you are ask to reimplement the depth-first-search with an explicit stack
of `Alternative` objects.


Check that your implementation passes the tests `DFSearchTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/search/DFSearchTest.java?at=master>`_



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
====================================

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
==============================================

The Cumulative and Time-Table Filtering introduced in  [TT2015]_
is an efficient yet simple filtering for Cumulative.

It is a two stage algorithm:

1. Build an optimistic profile of the resource consumption and check it does not exceed the capacity.
2. Filter the earliest start of the activities such that they are not in conflict with the profile.

Consider on the next example the depicted activity that can be executed anywhere between
the two brackets.
It can not execute at its earliest start since this would
violate the capacity of the resource.
We thus need to push the activity up until we find a time
where it can execute over its entire duration
without being in conflict with the profile and the capacity.
The earliest time  is 7.


.. image:: _static/timetable2.svg
    :scale: 50
    :width: 600
    :alt: scheduling timetable1


**Profiles**


We provide a class `Profile.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/Profile.java?at=master>`_
that is able to build efficiently a resource profile given an array of rectangles in input.
A rectangle has three attributes: `start`, `end`, `height` as shown next:

.. image:: _static/rectangle.svg
    :scale: 50
    :width: 250
    :alt: rectangle

A profile is nothing else than a sequence of rectangles.
An example of profile is given next. It is built from three input rectangles provided to the constructor
of `Profile.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/Profile.java?at=master>`_.
The profile consists in 7 contiguous rectangles.
The first rectangle `R0` starts at `Integer.MIN_VALUE` with a height of zero
and the last rectangle `R6` ends in `Integer.MAX_VALUE` also with a height of zero.
These two `dummy` rectangles are convenient because they guarantee
the property that any time point falls on one rectangle of the profile.


.. image:: _static/profile.svg
    :scale: 50
    :width: 650
    :alt: profile


Make sure you understand how to build and manipulate
`Profile.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/Profile.java?at=master>`_.

Have a quick look at `ProfileTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/ProfileTest.java?at=master>`_
for some examples of profile construction.


**Filtering**



Implement `Cumulative.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/Cumulative.java?at=master>`_.
You have three TODO tasks:

1. Build the optimistic profile from the mandatory parts.
2. Check that the profile is not exceeding the capacity.
3. Filter the earliest start of activities.

*TODO 1* is to build the optimistic profile
from the mandatory parts of the activities.
As can be seen on the next visual example, a mandatory part of an activity
is a part that is always executed whatever will be the start time of the activity
on its current domain.
It is the rectangle starting at `start[i].getMax()` that ends in `start[i].getMin()+duration()`
with a height equal to the demand of the activity.
Be careful because not every activity has a mandatory part.

.. image:: _static/timetable1.svg
    :scale: 50
    :width: 600
    :alt: scheduling timetable1

*TODO 2* is to check that the profile is not exceeding the capacity.
You can check that each rectangle of the profile is not exceeding the capacity
otherwise you throw an `InconsitencyException`.

*TODO 3* is to filter the earliest start of unbound activities by pushing each
activity (if needed) to the earliest slot when it can be executed without violating the capacity threshold.


.. code-block:: java

    for (int i = 0; i < start.length; i++) {
            if (!start[i].isBound()) {
                // j is the index of the profile rectangle overlapping t
                int j = profile.rectangleIndex(start[i].getMin());
                // TODO 3: push j to the right
                // hint:
                // You need to check that at every-point on the interval
                // [start[i].getMin() ... start[i].getMin()+duration[i]-1] there is enough space.
                // You may have to look-ahead on the next profile rectangle(s)
                // Be careful that the activity you are currently pushing may have contributed to the profile.

            }
        }




Check that your implementation passes the tests `CumulativeTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/CumulativeTest.java?at=master>`_.


.. [TT2015] Gay, S., Hartert, R., & Schaus, P. (2015, August). Simple and scalable time-table filtering for the cumulative constraint. In International Conference on Principles and Practice of Constraint Programming (pp. 149-157). Springer.


Table Constraint
================

The table constraint (also called extension constraint) represents a list of tuples assignable to a given list of variables.

More precisely, given an array `X` containing `n` variables, and an array `T` of size `m*n`, this constraint holds:

.. math::

    \exists i: \forall\ j\ T_{i,j} = X_j

That is, each line of the table is a valid assignment to `X`.

Here is an example of a table, with five tuples and four variables:

+-------------+------+------+------+------+
| Tuple index | X[0] | X[1] | X[2] | X[3] |
+=============+======+======+======+======+
|           1 |    0 |    1 |    2 |    3 |
+-------------+------+------+------+------+
|           2 |    0 |    0 |    3 |    2 |
+-------------+------+------+------+------+
|           3 |    2 |    1 |    0 |    3 |
+-------------+------+------+------+------+
|           4 |    3 |    2 |    1 |    2 |
+-------------+------+------+------+------+
|           5 |    3 |    0 |    1 |    1 |
+-------------+------+------+------+------+

In this particular example, the assignment `X={1, 1, 4, 3}` is then valid, but not `X={4, 3, 3, 3}` as there are no
such line in the table.

Many algorithms exists to filter table constraints (see the timeline below), and most of them are Global Arc Consistent (GAC).

.. image:: _static/tabletimeline.png
    :scale: 50
    :width: 250
    :alt: Timeline of Table constraint propagators

The current state-of-the-art GAC algorithm is Compact Table [CT2016]_ (abbreviated CT from now on).

CT works in two steps:

* First, it computes the list of supported tuples. A tuple `T[i]` is supported if, *for each* element `j` of the tuple,
  the domain of the variable `X[j]` contains the value `T[i][j]`.
* Once the list of supported tuples has been computed, the pruning is easy. For each variable `x[j]` and value `v` in its
  domain, the value `v` can be removed if it's not used by any tuple.

In order to compute this efficiently, CT maintains for each pair variable/value the set of tuples the pair maintains.
In the source code, we store this as table of sets named `supportedByVarVal`, where `supportedByVarVal[j][k]` contains
the list of supported tuples of variable `X[j]` and value `k+xOffset[j]` (`xOffset[j]` is init to the minimum possible
value of `X[j]`, in order to easy addressing of each value).

Example
-------

As an example, consider that variable `X[0]` has domain `{0, 1, 3}`. Here are some values for `supportedByVarVal`:
`supportedByVarVal[0][0] = {1, 2}`
`supportedByVarVal[0][1] = {}`
`supportedByVarVal[0][3] = {4, 5}`

We can infer two things from this example: first, value `1` does not support any tuples, so it can be removed safely
from the domain of `X[0]`. Moreover, the tuples supported by `X[0]` is the union of the tuples supported by its values;
we immediately see that tuple `3` is not supported by `X[0]` and can be discarded from further calculations.

If we push the example further, and we say that variable `X[2]` has domain `{0, 1}`, we immediately see that tuples `0`
and `1` are not supported by variable `X[2]`, and, as such, can be discarded. From this, we can infer that the value
`0` can be removed from variable `X[0]` as they don't support any tuple anymore.


Using bit sets
--------------

As you may have seen, we did not describe the type of `supportedByVarVal`. You may have assumed that is was
`List<Integer>[][] supportedByVarVal`. This is not the solution used by CT.

CT uses the concept of bit sets. A bit set is an array-like data structure that stores bits. Each bit is accessible by
its index. A bitset is in fact composed of an array of `Long`, that we call in this context a *word*.
Each of these words store 64 bits from the bitset.

Using this structures is convenient for our goal:

* Each supported tuple is encoded as a `1` in the bitset. `0` encodes unsupported tuples. In the traditional list/array
  representation, each supported tuple would have taken 32 bits to be represented.
* Doing intersection and union of bit sets (and these are the main operation that will be made on `supportedByVarVal`)
  is very easy, thanks to the usage of bitwise operators included in all modern CPUs.

Java provides a default implementation of bit sets in the class BitSet, that we will use in this exercise.
We encourage you to read its documentation before going on.

A basic implementation
----------------------

In the first part of this exercise, you will implement a version of CT that makes no use of the reversible structure
of the propagator, that we will explain in the next subsection.

You have to implement the `propagate()` function of the class `TableCT`. All class variables have already been init
for you.

For now, you "simply" have to compute, for each call to `propagate()`:

* The tuples supported by each variable, which are the union of the tuples supported by the value in the domain of the
  variable
* The intersection of the tuples supported by each variable is the set of globally supported tuples
* You can now intersect the set of globally supported tuples with each variable/value pair in `supportedByVarVal`.
  If the value supports no tuple (i.e. the intersection is empty) then it can be removed.

Once it's done and working (run the tests!) you can go to the next part of this exercise.

A reversible implementation
---------------------------

Recomputing the set of supported tuples at each call to propagate is useless; we can simply store the set of currently
supported ones and update it each time a variable is modified.

In order to do that, we provide a class called `ReversibleBitSet`, that can store a bit set and reverse it when
the solver goes back in the search tree.

A simple method to detect that a variable has changed is to check the size of its domain. We will use an array
of `ReversibleInt`, called `ReversibleInt[] lastSize`, which contains the last seen size of each variable
in this branch of the search tree.

Uncomment the part marked as `advanced` in TableCT and modify the part of the code computing supported tuples to
make it uses only variables that were modified.

Final note on CT
----------------

Now that the propagator is working, we encourage you to read the code of `ReversibleBitSet`, and notice it is not
exactly what is described in the CT paper [CT2016]_ ; the authors uses Reversible Sparse Bit Sets, which avoid doing
computation on 0-words, that are words that only contains zeros.

They are other small tricks to improve the speed of the propagator in the paper.

.. [CT2016] Demeulenaere, J., Hartert, R., Lecoutre, C., Perez, G., Perron, L., Régin, J. C., & Schaus, P. (2016, September). Compact-table: Efficiently filtering table constraints with reversible sparse bit-sets. In International Conference on Principles and Practice of Constraint Programming (pp. 207-223). Springer.


Conflict based search strategy (optional)
=================================================================


Last Conflict [LC2009]_
Conflict Ordering Search [COS2015]_


.. [LC2009] Lecoutre, C., Sa?s, L., Tabary, S., & Vidal, V. (2009). Reasoning from last conflict (s) in constraint programming. Artificial Intelligence, 173(18), 1592-1614.

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










  
     


