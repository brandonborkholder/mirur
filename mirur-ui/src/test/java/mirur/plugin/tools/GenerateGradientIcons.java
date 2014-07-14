package mirur.plugin.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.metsci.glimpse.support.color.GlimpseColor;
import com.metsci.glimpse.support.colormap.ColorGradient;
import com.metsci.glimpse.support.colormap.ColorGradients;

public class GenerateGradientIcons {
    public static void main(String[] args) throws Exception {
        final int size = 16;

        writeGradientIcon(new File("icons/gradient_grey.gif"), ColorGradients.gray, size, size);
        writeGradientIcon(new File("icons/gradient_bone.gif"), ColorGradients.reverseBone, size, size);
        writeGradientIcon(new File("icons/gradient_jet.gif"), ColorGradients.jet, size, size);
        writeGradientIcon(new File("icons/gradient_bathy.gif"), ColorGradients.bathymetry, size, size);
        writeGradientIcon(new File("icons/gradient_topo.gif"), ColorGradients.topography, size, size);
    }

    static void writeGradientIcon(File file, ColorGradient gradient, int w, int h) throws IOException {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = image.createGraphics();

        float[] rgba = new float[4];
        for (int i = 0; i < h; i++) {
            gradient.toColor(1 - i / (float) (h - 1), rgba);
            rgba[3] = 1;

            g2d.setColor(GlimpseColor.toColorAwt(rgba));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(1, i, w - 2, i);
        }

        g2d.setColor(Color.black);
        g2d.draw(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 2, 2));

        ImageIO.write(image, "gif", file);
    }
}
