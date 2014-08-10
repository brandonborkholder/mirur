package mirur.plugins.heatmap2d;

import java.nio.FloatBuffer;

import mirur.core.Array2D;
import mirur.core.MinMaxValueVisitor;
import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;
import mirur.plugins.Array2DTitlePainter;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.SimplePlugin2D;

import com.metsci.glimpse.axis.tagged.NamedConstraint;
import com.metsci.glimpse.axis.tagged.Tag;
import com.metsci.glimpse.axis.tagged.TaggedAxis1D;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.gl.texture.ColorTexture1D;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.painter.texture.TaggedHeatMapPainter;
import com.metsci.glimpse.plot.Plot2D;
import com.metsci.glimpse.plot.TaggedColorAxisPlot2D;
import com.metsci.glimpse.support.color.GlimpseColor;
import com.metsci.glimpse.support.colormap.ColorGradient;
import com.metsci.glimpse.support.colormap.ColorGradients;
import com.metsci.glimpse.support.projection.FlatProjection;
import com.metsci.glimpse.support.projection.Projection;
import com.metsci.glimpse.support.texture.FloatTextureProjected2D;
import com.metsci.glimpse.support.texture.FloatTextureProjected2D.MutatorFloat2D;

public class HeatmapView extends SimplePlugin2D {
    public HeatmapView() {
        super("Heatmap", null);
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, PrimitiveArray array0) {
        final Array2D array = (Array2D) array0;

        TaggedColorAxisPlot2D plot = new TaggedColorAxisPlot2D() {
            @Override
            protected SimpleTextPainter createTitlePainter() {
                SimpleTextPainter painter = new Array2DTitlePainter(getAxis());
                painter.setHorizontalPosition(HorizontalPosition.Left);
                painter.setVerticalPosition(VerticalPosition.Center);
                painter.setColor(GlimpseColor.getBlack());
                return painter;
            }

            @Override
            protected void initializePainters() {
                super.initializePainters();

                ((Array2DTitlePainter) titlePainter).setArray(array);
            }
        };

        plot.setAxisLabelX(array.getName() + "[]");
        plot.setAxisLabelY(array.getName() + "[][]");

        plot.getCrosshairPainter().showSelectionBox(false);

        TaggedAxis1D axisZ = plot.getAxisZ();
        TaggedHeatMapPainter painter = new TaggedHeatMapPainter(axisZ);

        final Tag t1 = axisZ.addTag("T1", 0.0).setAttribute(Tag.TEX_COORD_ATTR, 0.0f);
        final Tag t2 = axisZ.addTag("T2", 0.0).setAttribute(Tag.TEX_COORD_ATTR, 1.0f);

        axisZ.addConstraint(new NamedConstraint("C1") {
            @Override
            public void applyConstraint(TaggedAxis1D axis) {
                if (t1.getValue() > t2.getValue()) {
                    t1.setValue(t2.getValue());
                }
            }
        });

        final ColorTexture1D colors = new ColorTexture1D(1024);
        colors.setColorGradient(ColorGradients.jet);

        int dim0 = array.getMaxSize(0);
        int dim1 = array.getMaxSize(1);

        FloatTextureProjected2D texture = new FloatTextureProjected2D(dim0, dim1);

        Projection projection = new FlatProjection(0, dim0, 0, dim1);
        texture.setProjection(projection);
        texture.mutate(new MutatorFloat2D() {
            @Override
            public void mutate(FloatBuffer data, int dataSizeX, int dataSizeY) {
                data.clear();
                if (array.isJagged()) {
                    VisitArray.visit2d(array.getData(), new JaggedToFloatBufferVisitor(data, dataSizeY, Float.NaN));
                } else {
                    VisitArray.visit2d(array.getData(), new ToFloatBufferVisitor(data));
                }
            }
        });

        painter.setData(texture);
        painter.setColorScale(colors);

        plot.addPainter(painter, Plot2D.DATA_LAYER);
        plot.setColorScale(painter.getColorScale());

        plot.getAxisX().setMin(0);
        plot.getAxisX().setMax(dim0);
        plot.getAxisY().setMin(0);
        plot.getAxisY().setMax(dim1);

        MinMaxValueVisitor minMaxVisitor = VisitArray.visit(array.getData(), new MinMaxValueVisitor());
        double minZ = minMaxVisitor.getMin();
        double maxZ = minMaxVisitor.getMax();
        if (minZ == maxZ) {
            maxZ++;
        }

        plot.getAxisZ().setMin(minZ);
        plot.getAxisZ().setMax(maxZ);

        t1.setValue(minZ);
        t2.setValue(maxZ);

        canvas.addLayout(plot);
        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());
        result.addAxis(plot.getAxisZ());
        result.addAction(new GradientChooserAction() {
            @Override
            protected void select(ColorGradient gradient) {
                colors.setColorGradient(gradient);
            }
        });
        return result;
    }
}
