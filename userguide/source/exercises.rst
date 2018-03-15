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
To avoid any `stack-overflow` exception due to too a deep recursion in Java
we ask you to reimplement the depth-first-search with an explicit stack
of instead of relying on the recursion call stack.

Consider the following search tree where alternatives to execute are represented as letters. 


.. image:: _static/dfs.svg
    :scale: 50
    :width: 250
    :alt: DFS

A DFS exploration should executes the alternative in the following order `A->D->E->B->C->F->G`.
On backtrack, the state should be restored and therefore these successive executions of the alternatives
should be interleaved with 'push' and 'pop' operations on the trail.
For instance a valid sequence for restoring the states on backtrack is the following:
`push->A->push->D->pop->push->E->pop->pop->push->B->pop->push->C->push->F->pop->push->G->pop->pop`.
The `push` operations are executed in pre-order fashion while the `pop` operations are executed in a post-order fashion.
This is highlighted in the recursive dfs code given next.

.. code-block:: java
   :emphasize-lines: 10, 13, 19

    private void dfs(SearchStatistics statistics, SearchLimit limit) {
        if (limit.stopSearch(statistics)) throw new StopSearchException();
        Alternative [] alternatives = choice.call(); // generate the alternatives
        if (alternatives.length == 0) {
            statistics.nSolutions++;
            notifySolutionFound();
        }
        else {
            for (Alternative alt : alternatives) {
                state.push(); // pre-order
                try {
                    statistics.nNodes++;
                    alt.call(); // call the alternative
                    dfs(statistics,limit);
                } catch (InconsistencyException e) {
                    notifyFailure();
                    statistics.nFailures++;
                }
                state.pop(); // post-order
            }
        }
    }

A skeletton of solution is given next but you don't have to follow exactly this solution since there are many ways
to implement it.

.. code-block:: java
   :emphasize-lines: 3

    private void dfs(SearchStatistics statistics, SearchLimit limit) {
        Stack<Alternative> alternatives = new Stack<Alternative>();
        expandNode(alternatives,statistics); // root expension
        while (!alternatives.isEmpty()) {
            if (limit.stopSearch(statistics)) throw new StopSearchException();
            try {
                alternatives.pop().call();
            } catch (InconsistencyException e) {
                notifyFailure();
                statistics.nFailures++;
            }
        }
    }
    private void expandNode(Stack<Alternative> alternatives, SearchStatistics statistics) {
       // TODO
    }

The idea of this solution is wrap the push/pop/alternative execution inside `Alternative` closure objects
as illustrated on the next figure showing the stack after the root node expansion at line 3. 

.. image:: _static/stackalternatives.svg
    :scale: 50
    :width: 250
    :alt: DFS
    
    
    
Check that your implementation passes the tests `DFSearchTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/search/DFSearchTest.java?at=master>`_


Remark (optional): It is actually possible to reduce the number of operations on the trail 
by skipping the push on a last branch at a given node. 
The sequence of operations becomes `push->push->A->push->D->pop->E->pop->push->B->pop->C->push->F->pop->G->pop`.



Domain with an arbitrary set of values
=================================================================================

Implement the missing constructor in `IntVarImpl.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/core/IntVarImpl.java?at=master>`_


.. code-block:: java

    public IntVarImpl(Solver cp, Set<Integer> values) {
        throw new NotImplementedException();
    }


This exercise is straightforward: just create a dense domain then remove the values not present in the set.

Check that your implementation passes the tests `IntVarTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/core/IntVarTest.java?at=master>`_


Implement a domain iterator
======================================

Many filtering algorithms require to iterate over the values of a domain.
The `fillArray` method from `ReversibleSparseSet.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/minicp/reversible/ReversibleSparseSet.java?at=master>`_
allows to fill an array with all the values present in the sparse-set relying on the very efficient 'System.arraycopy'.

.. code-block:: java

    /**
     * set the first values of <code>dest</code> to the ones
     * present in the set
     * @param dest, an array large enough dest.length >= getSize()
     * @return the size of the set
     */
    public int fillArray(int [] dest) {
        int s = size.getValue();
        System.arraycopy(values, 0, dest, 0, s);
        return s;
    }
    
    
The main advantage over the iterator mechanism is that not object is created (and thus garbage collected). 
Indeed `dest` is typically a container array stored as an instance variable and reused many times.
This is important for efficiency to avoid creating objects on the heap at each execution of a propagator.
Never forget that a 'propagate()' method of 'Constraint' may be called thousands of times per second.
This implementation using `fillArray` avoids the `ConcurrentModificationException` discussion 
when implementing an Iterator: should we allow to modify a domain while iterating on it ?
The answer here is very clear: you get a snapshot of the domain at the time of the call to `fillArray` and you can thus
safely iterate over this `dest` array and modifying the domain at the same time.


To do:

* Implement `public int fillArray(int [] dest)` in `IntVar.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/core/IntVar.java?at=master>`_ As a consequence in all the classes implementing the interface. You may need to add implementations in other classes such as the domain implementation.
* Check that your implementation passes the tests `IntVarTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/core/IntVarTest.java?at=master>`_ add also add more tests.


Implement a Custom Search
=================================

Modify the Quadratic Assignment Model `QAP.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/examples/QAP.java?at=master>`_
to implement a custom search strategy. A skeleton for a custom search is the following one:


.. code-block:: java

        DFSearch dfs = makeDfs(cp,
                selectMin(x,
                        x -> x.getSize() > 1, // filter
                        x -> x.getSize(), // variable selector
                        xi -> {
                            int v = xi.getMin(); // value selector (TODO)
                            return branch(() -> equal(xi,v),
                                    () -> notEqual(xi,v));
                        }
                ));
                

* As a variable heuristic, select the unbound variable `x[i]` (a facility `i` not yet assigned to a location) that has a maximum weight `w[i][j]` with another facility `j` (`x[j]` may be bound or not).
* As a value heuristic, on the left branch, place this facility to on the location which is the closest possible to another location possible for facility `j`. On the right branch remove this value. 
* Hint: `selectMin` is a generic method parameterized by 'T'. To implement this heuristic, adding pairs `(i,j)` as a type for `T` is probably the easiest way to go.

   .. code-block:: java

           public static <T> Choice selectMin(T[] x, Filter<T> p, ValueFun<T> f, BranchOn<T> body)             


Experiment and modify LNS
=================================================================

Experiment the Quadratic Assignment Model with LNS `QAPLNS.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/examples/QAPLNS.java?at=master>`_

* Does it converge faster to good solutions than the standard DFS ? Use the larger instance with 25 facilities.
* What is the impact of the percentage of variables relaxed (experiment with 5, 10 and 20%) ?
* What is the impact of the failure limit (experiment with 50, 100 and 1000)?
* Which parameter setting work best? How would you choose it?
* Imagine a different relaxation specific for this problem. Try to relax the decision variables that have the strongest impact on the objective (the relaxed variables should still be somehow randomized). You can for instance compute for each facility $i$: $sum_j d[x[i]][x[j]]*w[i][j]$ and base your decision to relax or not a facilities on those values. 


    
Element constraint
=================================


Implement `Element1D.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/Element1D.java?at=master>`_


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


Two possibilities:

1. extends `Element2D` and reformulate `Element1D` as an `Element2D` constraint in super call of the constructor.
2. implement a dedicated algo (propagate, etc) for `Element1D` by taking inspiration from `Element2D`.

Does your filtering achieve domain-consistency on D(Z)? Implement a domain-consistent version, write tests to make sure it is domain consistent.


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


Table Constraint
================

The table constraint (also called extension constraint)
specify the list of solutions (tuples) assignable to a vector of variables.

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

In this particular example, the assignment `X={2, 1, 0, 3}` is then valid, but not `X={4, 3, 3, 3}` as there are no
such line in the table.

Many algorithms exists to filter table constraints.

One of the fastest filtering algorithm nowadays is Compact Table (CT) [CT2016]_.
In this exercise you'll implement a simple version of CT.

CT works in two steps:

1. Compute the list of supported tuples. A tuple `T[i]` is supported if, *for each* element `j` of the tuple,
  the domain of the variable `X[j]` contains the value `T[i][j]`.
2. Filter the domains. For each variable `x[j]` and value `v` in its
  domain, the value `v` can be removed if it's not used by any supported tuple.





Your task is to terminate the implementation in
`TableCT.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/TableCT.java?at=master>`_.


`TableCT` maintains for each pair
variable/value the set of tuples the pair maintains as an array of bitsets:

.. code-block:: java

    private BitSet[][] supports;


where `supports[j][v]` is
the (bit)set of supported tuples for the assignment `x[j]=v`.

Example
-------

As an example, consider that variable `x[0]` has domain `{0, 1, 3}`. Here are some values for `supports`:
`supports[0][0] = {1, 2}`
`supports[0][1] = {}`
`supports[0][3] = {4,5}`

We can infer two things from this example: first, value `1` does not support any tuples, so it can be removed safely
from the domain of `x[0]`. Moreover, the tuples supported by `x[0]` is the union of the tuples supported by its values;
we immediately see that tuple `3` is not supported by `x[0]` and can be discarded from further calculations.

If we push the example further, and we say that variable `x[2]` has domain `{0, 1}`, we immediately see that tuples `1`
and `2` are not supported by variable `x[2]`, and, as such, can be discarded. From this, we can infer that the value
`0` can be removed from variable `x[0]` as they don't support any tuple anymore.


Using bit sets
--------------

You may have assumed that the type of `supports` would have been `List<Integer>[][] supportedByVarVal`.
This is not the solution used by CT.

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

You will implement a version of CT that makes no use of the reversible structure (therefore it is probably much less efficient that the real CT algo).

You have to implement the `propagate()` method of the class `TableCT`. All class variables have already been initialized
for you.

You "simply" have to compute, for each call to `propagate()`:

* The tuples supported by each variable, which are the union of the tuples supported by the value in the domain of the
  variable
* The intersection of the tuples supported by each variable is the set of globally supported tuples
* You can now intersect the set of globally supported tuples with each variable/value pair in `supports`.
  If the value supports no tuple (i.e. the intersection is empty) then it can be removed.

Make sure you pass all the tests `TableTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/TableTest.java?at=master>`_.


Branching Combinators
======================

Sometimes we wish to branch on a given order on two families of variables, say `x[]` and then `y[]` as show on the next picture.
A variable in `y` should not be branched on before all the variables in `x` have been decided.
Furthermore, we may want to apply a specific heuristic on `x` which is different from the heuristic we want to apply on `y` variables.


.. image:: _static/combinator.svg
    :scale: 50
    :width: 200
    :alt: combinator

This can be achieved as follows

.. code-block:: java

    IntVar [] x;
    IntVar [] y;
    makeDfs(and(firstFail(x),firstFail(y)))


The `and` factory method creates a  `ChoiceCombinator.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/search/ChoiceCombinator.java?at=master>`_.
You must complete its implementation.

Eternity Problem
======================

Fill in all the gaps in order to solve the Eternity II problem.

Your task is to terminate the implementation in
`Eternity.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/examples/Eternity.java?at=master>`_.

* Create the table 
* Model the problem using table constraints
* Search for a feasible solution using branching combinators


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
                // TODO 3: push i to the right
                // hint:
                // You need to check that at every-point on the interval
                // [start[i].getMin() ... start[i].getMin()+duration[i]-1] there is enough space.
                // You may have to look-ahead on the next profile rectangle(s)
                // Be careful that the activity you are currently pushing may have contributed to the profile.

            }
        }




Check that your implementation passes the tests `CumulativeTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/CumulativeTest.java?at=master>`_.


.. [TT2015] Gay, S., Hartert, R., & Schaus, P. (2015, August). Simple and scalable time-table filtering for the cumulative constraint. In International Conference on Principles and Practice of Constraint Programming (pp. 149-157). Springer.




..
.. A reversible implementation
.. ---------------------------

.. Recomputing the set of supported tuples at each call to propagate is useless; we can simply store the set of currently
supported ones and update it each time a variable is modified.

.. In order to do that, we provide a class called `ReversibleBitSet`, that can store a bit set and reverse it when
the solver goes back in the search tree.

.. A simple method to detect that a variable has changed is to check the size of its domain. We will use an array
of `ReversibleInt`, called `ReversibleInt[] lastSize`, which contains the last seen size of each variable
in this branch of the search tree.

.. Uncomment the part marked as `advanced` in TableCT and modify the part of the code computing supported tuples to
make it uses only variables that were modified.


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











  
     


