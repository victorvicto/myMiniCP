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




Implement the less or equal reified constraint
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


Check that your implementation pass the tests `IsLessOrEqualTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/IsEqualTest.java?at=master>`_


Implement element constraint
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


Check that your implementation pass the tests `Element1DTest.java <https://bitbucket.org/pschaus/minicp/src/HEAD/src/test/java/minicp/engine/constraints/Element1DTest.java?at=master>`_



AllDifferent Forward Checking
=================================

Implement a dedicated algorithm for the all-different.
Whenever a variable is bound to a value, this value is removed from the domain of other variables.


Conflict based search branching
=================================================================



Implement a binary first fail search strategy.
You can use the ``branch`` helper method to create the array of alternatives.
You should select the unbound variable with minimum domain first.
On the left this variable is assigned to the value given by the heuristic.
On the right this same value is removed.
Capture a call to ``Store::add(new EqualVal(x,v))`` on the left branch,
and ``Store::add(new DifferentVal(x,v))`` on the right branch.


Last Conflict [LC2009]_
Conflict Ordering Search [LC2009]_


.. [LC2009] Lecoutre, C., Saïs, L., Tabary, S., & Vidal, V. (2009). Reasoning from last conflict (s) in constraint programming. Artificial Intelligence, 173(18), 1592-1614.




Discrepancy Search
=================================================================

Implement ``DiscrepancyBranching``, a branching that can wrap any branching
to limit the discrepancy of the branching.

Depth First Search
=================================================================

Replace the recursive DFS search by a non recursive implementation using an explicit stack.



Circuit Constraint
========================


Circuit Filtering [TSP1998]_

.. [TSP1998] Pesant, G., Gendreau, M., Potvin, J. Y., & Rousseau, J. M. (1998). An exact constraint logic programming algorithm for the traveling salesman problem with time windows. Transportation Science, 32(1), 12-29.


Element Constraint
========================

TODO


Cumulative Constraint
========================

Cumulative and Time-Table Filtering [TT2015]_

.. [TT2015] Gay, S., Hartert, R., & Schaus, P. (2015, August). Simple and scalable time-table filtering for the cumulative constraint. In International Conference on Principles and Practice of Constraint Programming (pp. 149-157). Springer.

Table Constraint
========================

Compact Table [CT2016]_

.. [CT2016] Demeulenaere, J., Hartert, R., Lecoutre, C., Perez, G., Perron, L., Régin, J. C., & Schaus, P. (2016, September). Compact-table: Efficiently filtering table constraints with reversible sparse bit-sets. In International Conference on Principles and Practice of Constraint Programming (pp. 207-223). Springer.

Restarts
========================

TODO

Watched Literals: The or (clause) constraint
========================

TODO


LNS
========================

TODO

#Implement: The closure must be deactivated when filtering is lost
#x.whenValueLost(filtering, () -> {});














  
     


