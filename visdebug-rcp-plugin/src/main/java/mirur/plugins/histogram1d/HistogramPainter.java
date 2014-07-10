package mirur.plugins.histogram1d;

import mirur.plugin.MirurLAF;

import com.metsci.glimpse.support.settings.LookAndFeel;

public class HistogramPainter extends com.metsci.glimpse.painter.plot.HistogramPainter {
    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);
        setColor(laf.getColor(MirurLAF.DATA_COLOR));
    }
}
