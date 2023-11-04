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

import java.util.Collection;

import com.metsci.glimpse.core.painter.shape.PointSetPainter.IdXy;
import com.metsci.glimpse.util.quadtree.QuadTreeXys;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import mirur.core.AbstractInterleavedVisitor;
import mirur.plugins.DataUnitConverter;

public class XyPointIndex extends AbstractInterleavedVisitor {
    private DataUnitConverter xUnitConverter;
    private DataUnitConverter yUnitConverter;
    private QuadTreeXys<IdXy> quadTree;

    public XyPointIndex(DataUnitConverter xUnitConverter, DataUnitConverter yUnitConverter) {
        this.xUnitConverter = xUnitConverter;
        this.yUnitConverter = yUnitConverter;
        quadTree = new QuadTreeXys<>(5);
    }

    public IntList getArrayIdx(double x, double y, double radiusX, double radiusY) {
        Collection<IdXy> result = quadTree.search((float) (x - radiusX), (float) (x + radiusX), (float) (y - radiusY), (float) (y + radiusY));
        if (result == null) {
            return IntLists.EMPTY_LIST;
        } else {
            IntList indexes = new IntArrayList(result.size());
            for (IdXy r : result) {
                indexes.add(r.id());
            }

            return indexes;
        }
    }

    @Override
    protected void visit(int i, double x, double y) {
        quadTree.add(new IdXy(i, (float) xUnitConverter.data2painter(x), (float) yUnitConverter.data2painter(y)));
    }

    @Override
    protected void visit(int i, float x, float y) {
        quadTree.add(new IdXy(i, (float) xUnitConverter.data2painter(x), (float) yUnitConverter.data2painter(y)));
    }
}
