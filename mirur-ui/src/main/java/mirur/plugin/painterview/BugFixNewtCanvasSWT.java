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
