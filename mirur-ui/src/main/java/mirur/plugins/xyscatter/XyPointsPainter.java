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
package mirur.plugins.xyscatter;

import static com.metsci.glimpse.core.support.color.GlimpseColor.getBlack;

import com.metsci.glimpse.core.painter.shape.PointSetPainter;
import com.metsci.glimpse.core.support.settings.LookAndFeel;

import mirur.core.Array1D;
import mirur.core.VisitArray;
import mirur.plugin.painterview.MirurLAF;
import mirur.plugins.DataUnitConverter;

public class XyPointsPainter extends PointSetPainter {
    public XyPointsPainter(Array1D array, DataUnitConverter xUnitConverter, DataUnitConverter yUnitConverter) {
        super(false);
        setShowAsDensity(false);
        setPointColor(getBlack());

        FillWithPointsVisitor visitor = new FillWithPointsVisitor(xUnitConverter, yUnitConverter);
        VisitArray.visit1d(array.getData(), visitor);
        dataBuffer = visitor.getDataBuffer();
        dataSize = dataBuffer.position() / 2;
        dataBuffer.flip();
        newData = true;
    }

    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);
        setPointColor(laf.getColor(MirurLAF.DATA_COLOR));
    }

    public void setShowAsDensity(boolean density) {
        if (density) {
            setPointSize(4);
            pointColor[3] = 0.5f;
        } else {
            setPointSize(5);
            pointColor[3] = 1;
        }
    }
}
