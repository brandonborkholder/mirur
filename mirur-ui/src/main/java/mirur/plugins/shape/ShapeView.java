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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;

import mirur.core.VariableObject;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.MirurView;
import mirur.plugins.image.ImagePlot;

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
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        Shape shape = (Shape) obj.getData();

        Rectangle2D bounds = shape.getBounds2D();

        int padW = (int) Math.max(bounds.getWidth() * 0.05, 10);
        int padH = (int) Math.max(bounds.getHeight() * 0.05, 10);
        int width = 1200;
        int height = (int) (bounds.getHeight() / bounds.getWidth() * width);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.translate(-bounds.getMinX(), -bounds.getMinY());
        g2d.scale(width / bounds.getWidth(), height / bounds.getHeight());
        g2d.translate(padW, padH);

        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2d.setColor(Color.black);
        g2d.draw(shape);
        g2d.dispose();

        ImagePlot plot = new ImagePlot(image);

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());
        return result;
    }
}
