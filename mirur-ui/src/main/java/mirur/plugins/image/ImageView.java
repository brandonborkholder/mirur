package mirur.plugins.image;

import java.awt.image.BufferedImage;

import mirur.core.VariableObject;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.MirurView;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public class ImageView implements MirurView {
    @Override
    public boolean supportsData(VariableObject obj) {
        return obj.getData() instanceof BufferedImage;
    }

    @Override
    public String getName() {
        return "Image";
    }

    @Override
    public ImageDescriptor getIcon() {
        return null;
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, VariableObject obj) {
        BufferedImage img = (BufferedImage) obj.getData();
        ImagePlot plot = new ImagePlot(img);

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());

        canvas.addLayout(result.getLayout());
        return result;
    }
}
