/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

import static com.metsci.glimpse.core.support.color.GlimpseColor.getBlack;
import static com.metsci.glimpse.core.support.color.GlimpseColor.getGray;
import static com.metsci.glimpse.core.support.color.GlimpseColor.getRed;
import static com.metsci.glimpse.core.support.color.GlimpseColor.getWhite;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.themes.ColorDefinition;
import org.eclipse.ui.internal.themes.WorkbenchThemeManager;

import com.metsci.glimpse.core.support.color.GlimpseColor;
import com.metsci.glimpse.core.support.settings.SwingLookAndFeel;

public class MirurLAF extends SwingLookAndFeel {
    public static final String DATA_COLOR = "mirur.data.color";
    public static final String DATA_BORDER_COLOR = "mirur.data.border.color";

    public MirurLAF(Color background, Color foreground) {
        float[] bg = GlimpseColor.fromColorRgb(background.getRed() / 255f, background.getGreen() /255f, background.getBlue() / 255f);
        float[] fg = GlimpseColor.fromColorRgb(foreground.getRed() / 255f, foreground.getGreen() /255f, foreground.getBlue() / 255f);

        map.put(PLOT_BACKGROUND_COLOR, bg);
        map.put(FRAME_BACKGROUND_COLOR, bg);
        map.put(TOOLTIP_BACKGROUND_COLOR, bg);

        map.put(BORDER_COLOR, fg);
        map.put(TOOLTIP_TEXT_COLOR, fg);
        map.put(AXIS_TEXT_COLOR, fg);
        map.put(AXIS_TICK_COLOR, fg);
        map.put(AXIS_TAG_COLOR, fg);

        map.put(DATA_COLOR, getBlack());
        map.put(DATA_BORDER_COLOR, getGray());
        map.put(CROSSHAIR_COLOR, getRed());
    }
}
