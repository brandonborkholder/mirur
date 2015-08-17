package mirur.plugins.image;

import java.awt.image.BufferedImage;

import com.metsci.glimpse.painter.decoration.BackgroundPainter;
import com.metsci.glimpse.painter.decoration.BorderPainter;
import com.metsci.glimpse.plot.Plot2D;

public class ImagePlot extends Plot2D {
    public ImagePlot(BufferedImage image) {
        initialize();

        setTitleHeight(0);
        setAxisSizeX(0);
        setAxisSizeY(0);
        setAxisSizeZ(0);

        ImagePainter painter = new ImagePainter(image);
        addPainter(new BackgroundPainter(true), Plot2D.BACKGROUND_LAYER);
        getLayoutCenter().addPainter(new BorderPainter(), Plot2D.FOREGROUND_LAYER);
        getLayoutCenter().addPainter(painter, Plot2D.DATA_LAYER);

        getAxisX().setMin(-1);
        getAxisX().setMax(painter.image.getWidth() + 1);
        getAxisY().setMin(-1);
        getAxisY().setMax(painter.image.getHeight() + 1);
    }
}
