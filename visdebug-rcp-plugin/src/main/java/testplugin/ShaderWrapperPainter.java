package testplugin;

import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.base.GlimpsePainter;
import com.metsci.glimpse.painter.base.GlimpsePainterCallback;

public class ShaderWrapperPainter implements GlimpsePainterCallback {
    private volatile InitializablePipeline newPipeline;
    private InitializablePipeline pipeline;

    public ShaderWrapperPainter() {
        pipeline = InitializablePipeline.DEFAULT;
    }

    public synchronized void setPipeline(InitializablePipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public synchronized void prePaint(GlimpsePainter painter, GlimpseContext context) {
        if (newPipeline != null) {
            pipeline = newPipeline;
            newPipeline = null;
        }

        pipeline.initialize(context);
        pipeline.beginUse(context.getGL().getGL2());
    }

    @Override
    public void postPaint(GlimpsePainter painter, GlimpseContext context) {
        pipeline.endUse(context.getGL().getGL2());
    }
}
