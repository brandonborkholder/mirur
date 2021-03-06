/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugin.painterview;

import static com.metsci.glimpse.support.color.GlimpseColor.getBlack;
import static com.metsci.glimpse.support.color.GlimpseColor.getGray;
import static com.metsci.glimpse.support.color.GlimpseColor.getRed;
import static com.metsci.glimpse.support.color.GlimpseColor.getWhite;

import com.metsci.glimpse.support.settings.SwingLookAndFeel;

public class MirurLAF extends SwingLookAndFeel {
    public static final String DATA_COLOR = "mirur.data.color";
    public static final String DATA_BORDER_COLOR = "mirur.data.border.color";

    public MirurLAF() {
        map.put(PLOT_BACKGROUND_COLOR, getWhite());
        map.put(FRAME_BACKGROUND_COLOR, getWhite());

        map.put(DATA_COLOR, getBlack());
        map.put(DATA_BORDER_COLOR, getGray());
        map.put(CROSSHAIR_COLOR, getRed());
    }
}
