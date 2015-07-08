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

import javax.media.opengl.GLContext;
import javax.media.opengl.GLProfile;

import org.eclipse.swt.widgets.Composite;

import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;

public class BugFixNewtSwtGlimpseCanvas extends NewtSwtGlimpseCanvas {
    public BugFixNewtSwtGlimpseCanvas(Composite parent, GLProfile profile, int options) {
        super(parent, profile, options);
    }

    @Override
    public void init(Composite parent, GLProfile glProfile, GLContext context, int options) {
        super.init(parent, glProfile, context, options);

        glCanvas.dispose();
        glCanvas = new BugFixNewtCanvasSWT(this, options, glWindow);
    }
}