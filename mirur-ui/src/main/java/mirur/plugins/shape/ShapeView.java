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
package mirur.plugins.shape;

import java.awt.Shape;

import mirur.core.VariableObject;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.MirurView;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public class ShapeView implements MirurView {
    @Override
    public boolean supportsData(VariableObject obj) {
        return obj.getData() instanceof Shape;
    }

    @Override
    public String getName() {
        return "Shape";
    }

    @Override
    public ImageDescriptor getIcon() {
        return null;
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, VariableObject obj) {
        Shape shape = (Shape) obj.getData();

        ShapePlot plot = new ShapePlot(shape);

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());

        canvas.addLayout(result.getLayout());
        return result;
    }
}
