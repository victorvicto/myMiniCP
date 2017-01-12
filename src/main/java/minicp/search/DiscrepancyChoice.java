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

package minicp.search;


import minicp.reversible.ReversibleContext;
import minicp.reversible.ReversibleInt;
import minicp.cp.core.Status;

/**
 * Branching wrapper that ensures that
 * that the alternatives created are always within the
 * discrepancy limit
 */

public abstract class DiscrepancyChoice {

    ReversibleInt currentDiscrepancy;

    public DiscrepancyChoice(ReversibleContext context, Choice choice, int maxD) throws Status {
        throw new Status(Status.Type.NotImplemented);
    }

    public Alternative[] getAlternatives() throws Status {
        // Hint:
        // Let b.alts denote b.getAlternatives()
        // Filter-out alternatives from b.alts that would exceed maxDiscrepancy
        // Therefore wrap each alternative in b.alts
        // such that the execute method of the wrapped alternatives
        // augment the currentDiscrepancy depending on its position
        // +0 for b.alts[0], ..., +i for b.alts[i]
        throw new Status(Status.Type.NotImplemented);
    }
}
