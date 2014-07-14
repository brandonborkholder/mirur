package mirur.plugins;

import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.painter.decoration.BackgroundPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.support.color.GlimpseColor;

public class InvalidPlaceholderLayout extends GlimpseLayout {
    public InvalidPlaceholderLayout() {
        BackgroundPainter backgroundPainter = new BackgroundPainter(true);
        addPainter(backgroundPainter);

        SimpleTextPainter textPainter = new SimpleTextPainter();
        textPainter.setColor(GlimpseColor.getRed());
        textPainter.setFont(18, true);
        textPainter.setText("no valid data or painter selected");
        textPainter.setHorizontalPosition(HorizontalPosition.Center);
        textPainter.setVerticalPosition(VerticalPosition.Center);
        addPainter(textPainter);
    }
}
