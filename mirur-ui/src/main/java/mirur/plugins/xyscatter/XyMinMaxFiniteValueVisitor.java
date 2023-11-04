/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugins.xyscatter;

import mirur.core.AbstractInterleavedVisitor;
import mirur.core.MinMaxFiniteValueVisitor;

public class XyMinMaxFiniteValueVisitor extends AbstractInterleavedVisitor {
    public final MinMaxFiniteValueVisitor xVisitor;
    public final MinMaxFiniteValueVisitor yVisitor;

    public XyMinMaxFiniteValueVisitor() {
        xVisitor = new MinMaxFiniteValueVisitor();
        yVisitor = new MinMaxFiniteValueVisitor();
    }

    @Override
    protected void visit(int i, double x, double y) {
        xVisitor.visit(x);
        yVisitor.visit(y);
    }

    @Override
    protected void visit(int i, float x, float y) {
        xVisitor.visit(x);
        yVisitor.visit(y);
    }
}
