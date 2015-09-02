package simple;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Main {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        double[] measurements = new double[500];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = v();
        }

        boolean[] included = new boolean[500];
        for (int i = 0; i < included.length; i++) {
            included[i] = v() > 0.3;
        }

        float[][] surface = new float[500][500];
        for (int i = 0; i < surface.length; i++) {
            for (int j = 0; j < surface[0].length; j++) {
                surface[i][j] = (float) v() * i;
            }
        }

        List<String> strList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            strList.add(String.valueOf(v()));
        }

        List<Double> dblList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dblList.add(v());
        }

        Input foo = new Input(measurements, dblList);

        float[][] hiRes = new float[200][100000];
        for (int i = 0; i < hiRes.length; i++) {
            for (int j = 0; j < hiRes[0].length; j++) {
                hiRes[i][j] = (float) v() * i;
            }
        }

        float[][] jaggedSurface = new float[100][];
        for (int i = 0; i < jaggedSurface.length; i++) {
            int len = (int) (Math.random() * 100);
            jaggedSurface[i] = new float[len];
            for (int j = 0; j < len; j++) {
                jaggedSurface[i][j] = (float) v();
            }
        }

        FloatBuffer buffer = FloatBuffer.allocate(1000);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((float) v() * 10);
        }
        buffer.position((int) (v() * buffer.capacity() / 2));
        buffer.limit((int) (v() * buffer.capacity() / 2) + buffer.position());

        Path2D.Float line = new Path2D.Float();
        line.moveTo(1, 50);
        line.lineTo(90, 90);
        BasicStroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(Color.blue);
        g2d.fillRect(20, 20, 50, 80);
        g2d.setColor(Color.red);
        g2d.setStroke(stroke);
        g2d.draw(line);

        double[] beyondFloatPrecision = new double[500];
        for (int i = 0; i < beyondFloatPrecision.length; i++) {
            beyondFloatPrecision[i] = v() * 1e-9 + 100;
        }

        System.out.println("Debug here");
    }

    static double v() {
        if (Math.random() < 0.01) {
            return Double.NaN;
        } else if (Math.random() < 0.01) {
            return Double.POSITIVE_INFINITY;
        } else if (Math.random() < 0.01) {
            return Double.NEGATIVE_INFINITY;
        } else {
            return Math.random();
        }
    }
}
