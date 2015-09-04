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
