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
in appearing the search.
The data-structure for the sub-chains should be a reversible.
Our instance variables used to keep track of the sub-chains are:

.. code-block:: java

    IntVar [] x;
    ReversibleInt [] dest;
    ReversibleInt [] orig;
    ReversibleInt [] lengthToDest;



* `dest[i]` is the furthest node we can reach from node `i` following the instantiated edges.
* `orig[i]` is the furthest node we can reach from node `i` following instantiated edges in reverse direction.
* `lengthToDest[i]` is the number of instantiated on the path from node `i` to `dest[i]`.

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


Implement `Circuit.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/main/java/minicp/engine/constraints/Circuit.java?at=master>`_
Check that your implementation passes the tests `CircuitTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/Circuit.java?at=master>`_


.. [TSP1998] Pesant, G., Gendreau, M., Potvin, J. Y., & Rousseau, J. M. (1998). An exact constraint logic programming algorithm for the traveling salesman problem with time windows. Transportation Science, 32(1), 12-29.


Custom search strategy
=================================

For the TSP


LNS
=================================================================

For the TSP

Cumulative Constraint: Decomposition
========================

TODO

Cumulative Constraint: Time-Table filtering
========================

Cumulative and Time-Table Filtering [TT2015]_

.. [TT2015] Gay, S., Hartert, R., & Schaus, P. (2015, August). Simple and scalable time-table filtering for the cumulative constraint. In International Conference on Principles and Practice of Constraint Programming (pp. 149-157). Springer.


Table Constraint
================

The table constraint (also called extension constraint) represents a list of tuples assignable to a given list of variables.

More precisely, given an array `X` containing `n` variables, and an array `T` of size `m*n`, this constraint holds:

.. math::

    \exists i: \forall j T_{i,j} = X_j

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

.. image:: _static/tableline.png
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












  
     


