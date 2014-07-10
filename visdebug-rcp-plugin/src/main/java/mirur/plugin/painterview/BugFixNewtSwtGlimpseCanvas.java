package mirur.plugin.painterview;

import javax.media.opengl.GLContext;
import javax.media.opengl.GLProfile;

import org.eclipse.swt.widgets.Composite;

import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;

public class BugFixNewtSwtGlimpseCanvas extends NewtSwtGlimpseCanvas {
    public BugFixNewtSwtGlimpseCanvas(Composite parent, GLProfile profile, int options) {
        super(parent, profile, options);
    }

    @Override
    public void init(Composite parent, GLProfile glProfile, GLContext context, int options) {
        super.init(parent, glProfile, context, options);

        glCanvas.dispose();
        glCanvas = new BugFixNewtCanvasSWT(this, options, glWindow);
    }
}