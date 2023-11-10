/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © 2023 Brandon Borkholder (support@mirur.io)
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

import com.metsci.glimpse.core.axis.Axis2D;

public class ResetAxesTask implements Runnable {
    private final Axis2D axis;

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    public ResetAxesTask(Axis2D axis) {
        this.axis = axis;
        this.minX = axis.getMinX();
        this.maxX = axis.getMaxX();
        this.minY = axis.getMinY();
        this.maxY = axis.getMaxY();
    }

    @Override
    public void run() {
        axis.getAxisX().setMin(minX);
        axis.getAxisX().setMax(maxX);
        axis.getAxisY().setMin(minY);
        axis.getAxisY().setMax(maxY);
    }
}
