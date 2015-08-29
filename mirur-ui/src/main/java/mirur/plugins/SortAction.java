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
package mirur.plugins;

import mirur.core.Array1D;
import mirur.core.Array1DImpl;
import mirur.core.SortHelperVisitor;
import mirur.core.VisitArray;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public abstract class SortAction extends Action {
    protected final Array1D unsorted;
    protected Array1D sorted;
    protected int[] indexMapSorted;

    public SortAction(Array1D data) {
        super("Sorted", IAction.AS_CHECK_BOX);
        setId(SortAction.class.getName());

        unsorted = data;
    }

    @Override
    public void run() {
        if (isChecked()) {
            if (sorted == null) {
                sort();
            }

            swapPainter(sorted, indexMapSorted);
        } else {
            swapPainter(unsorted, null);
        }
    }

    private void sort() {
        SortHelperVisitor sortHelper = VisitArray.visit1d(unsorted.getData(), new SortHelperVisitor());
        sorted = new Array1DImpl(unsorted.getName(), sortHelper.sorted);
        indexMapSorted = sortHelper.indexes;
    }

    protected abstract void swapPainter(Array1D toPaint, int[] indexMap);
}
