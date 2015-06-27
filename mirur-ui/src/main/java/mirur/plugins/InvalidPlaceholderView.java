package mirur.plugins;

import mirur.core.PrimitiveArray;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Menu;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public class InvalidPlaceholderView implements MirurView {
    @Override
    public boolean supportsData(PrimitiveArray array) {
        return true;
    }

    @Override
    public String getName() {
        return "Default show nothing";
    }

    @Override
    public ImageDescriptor getIcon() {
        return null;
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, PrimitiveArray array) {
        final InvalidPlaceholderLayout layout = new InvalidPlaceholderLayout();
        canvas.addLayout(layout);

        return new DataPainter() {
            @Override
            public GlimpseLayout getLayout() {
                return layout;
            }

            @Override
            public void populateMenu(Menu parent) {
            }

            @Override
            public void resetAxes() {
            }

            @Override
            public void uninstall(GlimpseCanvas canvas) {
                canvas.removeLayout(layout);
            }
        };
    }
}
