/*
 * This file is part of mini-cp.
 *
 * mini-cp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mini-cp.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2016 L. Michel, P. Schaus, P. Van Hentenryck
 */

package minicp.cp.core;

import minicp.reversible.ReversibleStack;
import minicp.util.InconsistencyException;
import minicp.util.NotImplementedException;

import java.security.InvalidParameterException;
import java.util.Set;

public interface IntVar {

    /**
     * Return the solver in which this variable was created
     * @return the solver in which this variable was created
     */
    Solver getSolver();

    /**
     * Ask that the closure is called whenever the domain change
     * of this variable changes
     * @param c
     */
    void whenDomainChange(ConstraintClosure.Closure c);

    /**
     * Ask that the closure is called whenever the domain
     * of this variable is reduced to a single value
     * @param c
     */
    void whenBind(ConstraintClosure.Closure c);

    /**
     * Ask that the closure is called whenever
     * the max or min value of the domain of this variable changes
     * @param c
     */
    void whenBoundsChange(ConstraintClosure.Closure c);

    /**
     * Ask that c.propagate() is called whenever the domain change
     * of this variable changes
     * @param c
     */
    void propagateOnDomainChange(Constraint c);

    /**
     * Ask that c.propagate() is called whenever the domain
     * of this variable is reduced to a single value
     * @param c
     */
    void propagateOnBind(Constraint c);

    /**
     * Ask that c.propagate() is called whenever
     * the max or min value of the domain of this variable changes
     * @param c
     */
    void propagateOnBoundChange(Constraint c);

    /**
     * Return the minimum of the domain of the variable
     * @return the minimum of the domain of the variable
     */
    int getMin();

    /**
     * Return the maximum of the domain of the variable
     * @return the maximum of the domain of the variable
     */
    int getMax();

    /**
     * Return the size of the domain of the variable
     * @return the size of the domain of the variable
     */
    int getSize();

    /**
     * Return true if the domain of the variable has a single value
     * @return true if the domain of the variable has a single value
     */
    boolean isBound();

    /**
     * @param v
     * @return true iff the value v is the domain
     */
    boolean contains(int v);
    /**
     * Remove the value v from the domain
     * @param v
     * @throws InconsistencyException
     */
    void remove(int v) throws InconsistencyException;

    /**
     * Assign the value v i.e.
     * remove every value different from v
     * @param v
     * @throws InconsistencyException
     */
    void assign(int v) throws InconsistencyException;

    /**
     * Remove all the values < va
     * @param v
     * @return the new minimum
     * @throws InconsistencyException
     */
    int removeBelow(int v) throws InconsistencyException;

    /**
     * Remove all the values > v
     * @param v
     * @return the new maximum
     * @throws InconsistencyException
     */
    int removeAbove(int v) throws InconsistencyException;

}
