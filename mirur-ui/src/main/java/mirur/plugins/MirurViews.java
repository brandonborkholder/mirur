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
package mirur.plugins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import mirur.plugins.bar1d.BarView;
import mirur.plugins.complex.ComplexView;
import mirur.plugins.heatmap2d.HeatmapView;
import mirur.plugins.histogram.HistogramView;
import mirur.plugins.image.ImageView;
import mirur.plugins.line1d.LineView;
import mirur.plugins.shape.ShapeView;
import mirur.plugins.xyscatter.XyScatterView;

public class MirurViews {
    private static MirurViews manager;

    private final Collection<MirurView> plugins;

    private MirurViews() {
        plugins = new ArrayList<>();
        plugins.add(new LineView());
        plugins.add(new BarView());
        plugins.add(new HeatmapView());
        plugins.add(new ImageView());
        plugins.add(new ShapeView());
        plugins.add(new HistogramView());
        plugins.add(new ComplexView());
        plugins.add(new XyScatterView());
    }

    public static synchronized Collection<MirurView> plugins() {
        if (manager == null) {
            manager = new MirurViews();
        }

        return Collections.unmodifiableCollection(manager.plugins);
    }
}
