package testplugin;

import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.base.GlimpsePainter;
import com.metsci.glimpse.painter.base.GlimpsePainterCallback;

public class ShaderWrapperPainter implements GlimpsePainterCallback {
    private InitializablePipeline pipeline;

    public void setPipeline(InitializablePipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void prePaint(GlimpsePainter painter, GlimpseContext context) {
        if (pipeline != null) {
            pipeline.initialize(context);
            pipeline.beginUse(context.getGL().getGL2());
        }
    }

    @Override
    public void postPaint(GlimpsePainter painter, GlimpseContext context) {
        if (pipeline != null) {
            pipeline.endUse(context.getGL().getGL2());
        }
    }
}
