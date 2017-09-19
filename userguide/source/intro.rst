.. _intro:



************
Preface
************


This document is made for anyone who wants to learn
constraint programming using using mini-cp as a support.

This tutorial will continuously evolve.
Don't hesitate to give us feedback or suggestion for improvement.
You are also welcome to report any mistake or bug.

 
Install mini-cp
=======================================

Minicp is available from bitbucket_.
The `Javavadoc API <_static/index.html>`_.

**Using an IDE**

We recommend using IntelliJ_ or Eclipse_.

From IntelliJ_ you can simply import the project.

.. code-block:: none

    Open > (select minicp directory)


From Eclipse_ you can simply import the project.

.. code-block:: none

    Import > Maven > Existing Maven Projects (select minicp directory)


**From the command line**

Using maven_ command line you can do you can do:


.. code-block:: none

    $mvn compile
    $mvn test


Using sbt_ (simple build tool) command line you can do you can do:


.. code-block:: none

    $sbt
    > compile
    > test
    > test-only
    > run
    > run-main


.. _bitbucket: https://bitbucket.org/pschaus/minicp
.. _IntelliJ: https://www.jetbrains.com/idea/
.. _Eclipse: https://www.eclipse.org
.. _sbt: http://www.scala-sbt.org
.. _maven: https://maven.apache.org


Getting Help with mini-cp
=======================================

You'll get greatest chance of getting answers to your questions using the mini-cp usergroup_.

.. _usergroup: https://groups.google.com/d/forum/mini-cp

     

Miscellaneous
==============



Citing mini-cp
------------------

If you use find mini-cp useful for your research or teaching you can cite:

.. code-block:: latex
	
	@Misc{minicp,
	  author = "{Laurent Michel, Pierre Schaus, Pascal Van Hentenryck}",
	  title = "{Mini-CP: A Minimalist Open-Source Solver to teach Constraint Programming",
	  year = {2017},
	  note = {Available from \texttt{www.info.ucl.ac.be/~pschaus/minicp}},
	}




