package mirur.plugin.painterview;

import static java.lang.Math.max;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.jogamp.newt.Window;
import com.jogamp.newt.swt.NewtCanvasSWT;

public class BugFixNewtCanvasSWT extends NewtCanvasSWT {
    public BugFixNewtCanvasSWT(Composite parent, int style, Window child) {
        super(parent, style, child);
    }

    @Override
    public Rectangle getClientArea() {
        Rectangle clientArea = super.getClientArea();
        // bug fix for
        if (clientArea.width == 0 || clientArea.height == 0) {
            clientArea = new Rectangle(clientArea.x, clientArea.y, max(1, clientArea.width), max(1, clientArea.height));
        }
        return clientArea;
    }
}
