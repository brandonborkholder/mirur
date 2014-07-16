package mirur.plugin;

import static com.metsci.glimpse.support.color.GlimpseColor.getBlack;
import static com.metsci.glimpse.support.color.GlimpseColor.getGray;
import static com.metsci.glimpse.support.color.GlimpseColor.getWhite;

import com.metsci.glimpse.swt.misc.SwtLookAndFeel;

public class MirurLAF extends SwtLookAndFeel {
    public static final String DATA_COLOR = "mirur.data.color";
    public static final String DATA_BORDER_COLOR = "mirur.data.border.color";

    public MirurLAF() {
        map.put(PLOT_BACKGROUND_COLOR, getWhite());
        map.put(FRAME_BACKGROUND_COLOR, getWhite());

        map.put(DATA_COLOR, getBlack());
        map.put(DATA_BORDER_COLOR, getGray());
    }
}
