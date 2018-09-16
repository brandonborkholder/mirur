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
package mirur.plugins.complex;

import com.metsci.glimpse.canvas.GlimpseCanvas;

import mirur.core.Array1D;
import mirur.core.Array1DImpl;
import mirur.core.Array2D;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.SimplePlugin2D;

public class ComplexView extends SimplePlugin2D {
    public ComplexView() {
        super("Complex (Interleaved)", null);
    }

    @Override
    public boolean supportsData(VariableObject obj) {
        if (obj instanceof Array2D) {
            // TODO support this
            return false;
        } else if (obj instanceof Array1D && ((Array1D) obj).getSize() % 2 == 0) {
            Class<?> clazz = ((Array1D) obj).getData().getClass();
            return int[].class.equals(clazz) ||
                    long[].class.equals(clazz) ||
                    float[].class.equals(clazz) ||
                    double[].class.equals(clazz) ||
                    char[].class.equals(clazz) ||
                    short[].class.equals(clazz);
        } else {
            return false;
        }
    }

    @Override
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        if (obj instanceof Array1D) {
            Array1D angles = toAngles((Array1D) obj);
            Array1D magnitudes = toMagnitudes((Array1D) obj);
            ComplexPlotLayout layout = new ComplexPlotLayout();
            layout.setComplexData((Array1D) obj, magnitudes, angles);

            DataPainterImpl result = new DataPainterImpl(layout);
            result.addAxis(layout.getAxisY1());
            result.addAxis(layout.getAxisY2());
            result.addAxis(layout.getAxisX());
            return result;
        } else {
            return null;
        }
    }

    protected Array1D toMagnitudes(Array1D obj) {
        Object newData = VisitArray.visit1d(obj.getData(), new CplxMagnitudeVisitor()).get();
        return new Array1DImpl(obj.getName(), newData);
    }

    protected Array1D toAngles(Array1D obj) {
        Object newData = VisitArray.visit1d(obj.getData(), new CplxAngleVisitor()).get();
        return new Array1DImpl(obj.getName(), newData);
    }
}
