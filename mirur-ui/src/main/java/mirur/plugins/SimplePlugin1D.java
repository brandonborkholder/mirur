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
import mirur.core.VariableObject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;

public abstract class SimplePlugin1D implements MirurView {
    private final String name;
    private final ImageDescriptor icon;

    public SimplePlugin1D(String name, ImageDescriptor icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public boolean supportsData(VariableObject obj) {
        if (obj instanceof Array1D) {
            Class<?> clazz = ((Array1D) obj).getData().getClass();
            return int[].class.equals(clazz) ||
                   long[].class.equals(clazz) ||
                   float[].class.equals(clazz) ||
                   double[].class.equals(clazz) ||
                   char[].class.equals(clazz) ||
                   short[].class.equals(clazz) ||
                   boolean[].class.equals(clazz);
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageDescriptor getIcon() {
        return icon;
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, VariableObject obj) {
        Array1D array1d = (Array1D) obj;
        GlimpseDataPainter2D painter = createPainter(array1d);
        Array1DPlot plot = new Array1DPlot(painter, array1d);

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAction(getFisheyeAction(plot));
        result.addAxis(plot.getAxis());

        finalInstall(canvas, result);
        return result;
    }

    protected void finalInstall(GlimpseCanvas canvas, DataPainter painter) {
        canvas.addLayout(painter.getLayout());
    }

    protected Action getFisheyeAction(final Array1DPlot plot) {
        return new FisheyeAction() {
            @Override
            public void run() {
                if (isChecked()) {
                    plot.setShaders(new FisheyePipeline());
                } else {
                    plot.setShaders(InitializablePipeline.DEFAULT);
                }
            }
        };
    }

    protected abstract GlimpseDataPainter2D createPainter(Array1D array);
}
