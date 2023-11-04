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
package mirur.plugins.line1d;

import static com.metsci.glimpse.core.painter.info.SimpleTextPainter.HorizontalPosition.Left;
import static com.metsci.glimpse.core.painter.info.SimpleTextPainter.VerticalPosition.Center;
import static com.metsci.glimpse.core.support.color.GlimpseColor.getBlue;

import java.util.Arrays;

import com.metsci.glimpse.core.painter.group.DelegatePainter;
import com.metsci.glimpse.core.painter.info.AnnotationPainter;
import com.metsci.glimpse.core.painter.shape.LineSetPainter;

public class MarkerPainter extends DelegatePainter {
    private AnnotationPainter annotationPainter;
    private LineSetPainter linePainter;

    private float[][] lineDataX;
    private float[][] lineDataY;

    public MarkerPainter() {
        annotationPainter = new AnnotationPainter();
        linePainter = new LineSetPainter();
        linePainter.setLineColor(getBlue());

        addPainter(linePainter);
        addPainter(annotationPainter);

        lineDataX = new float[0][];
        lineDataY = new float[0][];
    }

    public void addMarker(String text, float position) {
        annotationPainter.addAnnotation(text, position, 10, 10, 0, Left, Center, getBlue());

        lineDataX = Arrays.copyOf(lineDataX, lineDataX.length + 1);
        lineDataY = Arrays.copyOf(lineDataY, lineDataY.length + 1);
        lineDataX[lineDataX.length - 1] = new float[] { position, position };
        lineDataY[lineDataY.length - 1] = new float[] { 0, 1000 };
        linePainter.setData(lineDataX, lineDataY);
    }
}
