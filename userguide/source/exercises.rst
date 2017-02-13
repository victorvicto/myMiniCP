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
* Implement a constraint
* Implement custom search
* Extend IntVar
* Implement branch and bound into a CP solver
* Model CP easy problems


ReversibleSparseSet: range constructor
===================================================

The ReversibleSparseSet constructor ask for a single parameter.
Add a new constructor allowing to create a ReversibleSparseSet from any range between min and max: {min...max}.


.. literalinclude:: ../../src/main/java/minicp/reversible/ReversibleSparseSet.java
    :language: java
    :linenos:
    :lines: 54-62

You will need to modify the code of the class to do so.
The test to execute is :javaref:`minicp.reversible.ReversibleSparseSetTest`.



ReversibleSparseSet: removeBelow/removeAbove methods
=================================================================





IntVar: arbitrary domains
=================================================================


Add a two constructors  allowing to create a IntVar with a domain defined as

1. a range {min...max}
2. a set of initial values. Hint: create a sparse-set using the range constructor between the min and max of the set and remove values not in the set.


.. literalinclude:: ../../src/main/java/minicp/cp/core/IntVar.java
        :language: java
        :linenos:
        :lines: 52-74




IntVar: removeBelow/removeAbove
=======================================

Implement the methods allowing to remove all the values below/above a given value.
Don't forget to enqueue the constraints interested into domain/bind changes
in case a value is removed or if the domain is reduced to a single value.


.. literalinclude:: ../../src/main/java/minicp/cp/core/IntVar.java
            :language: java
            :linenos:
            :lines: 158-176


IntVar: propagateOnBoundChange
=======================================

Implement the methods permitting a constraint to register
on the bound change events.
You need to create a new ``ReversibleStack<Constraint> onBoundChange``
that will store all the constraint interested into these events.
Also you need to adapt the implementation of ``remove,assign,removeBelow,removeAbove`` methods
that can possibly trigger such an event.


.. literalinclude:: ../../src/main/java/minicp/cp/core/IntVar.java
                :language: java
                :linenos:
                :lines: 93-100


The Sum constraint
=================================================================


Implement the Sum constraint.
The filtering algorithm should be bound consistent.
As a consequence you will only remove impossible values by updating
the domains using ``removeBelow,removeAbove``.

Your implementation should only register to the bound change events ``onBoundChange``
in the ``post`` implementation.
Don't forget to terminate your ``post`` method by a call to ``propagate``.


.. literalinclude:: ../../src/main/java/minicp/cp/constraints/Sum.java
            :language: java
            :linenos:
            :lines: 27-50

AllDifferent Forward Checking
=================================

Implement a dedicated algorithm for the all-different.


Binary first fail branching
=================================================================

Implement a binary first fail search strategy.
You can use the ``branch`` helper method to create the array of alternatives.
You should select the unbound variable with minimum domain first.
On the left this variable is assigned to the value given by the heuristic.
On the right this same value is removed.
Capture a call to ``Store::add(new EqualVal(x,v))`` on the left branch,
and ``Store::add(new DifferentVal(x,v))`` on the right branch.


.. literalinclude:: ../../src/main/java/minicp/cp/branchings/BinaryFirstFail.java
                :language: java
                :linenos:
                :lines: 30-61


N-Queens
===========

Implement a model for the N-Queens.
Count the number of solution for a 8x8 chessboard.


Discrepancy Search
=================================================================

Implement ``DiscrepancyBranching``, a branching that can wrap any branching
to limit the discrepancy of the branching.

Depth First Search
=================================================================

Replace the recursive DFS search by a non recursive implementation using an explicit stack.


Magic Square
==============

Implement a model for the Magic-Square
Count the number of solution for a 5x5 magic square.


Objective Function and Branch and Bound
========================

TODO

Circuit Constraint
========================

TODO

Element Constraint
========================

TODO

Restarts
========================

TODO

Watched Literals: The or (clause) constraint
========================

TODO


LNS
========================

TODO

Implement: The closure must be deactivated when c is lost
x.whenValueLost(c, () -> {});














  
     


