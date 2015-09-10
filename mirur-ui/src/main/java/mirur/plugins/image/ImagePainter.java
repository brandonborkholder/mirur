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
package mirur.plugins.image;

import static com.jogamp.opengl.util.texture.TextureIO.newTexture;
import static com.metsci.glimpse.util.logging.LoggerUtils.logWarning;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_NEAREST;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_REPLACE;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV_MODE;
import static javax.media.opengl.GL2GL3.GL_QUADS;

import java.awt.image.BufferedImage;

import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureData;
import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;

public class ImagePainter extends GlimpseDataPainter2D {
    final BufferedImage image;
    Texture texture;
    private boolean initialized;

    public ImagePainter(BufferedImage image) {
        this.image = image;
        this.texture = null;
        this.initialized = false;
    }

    protected void initIfNecessary(GL2 gl) {
        if (initialized) {
            return;
        }

        try {
            GLProfile profile = gl.getContext().getGLDrawable().getGLProfile();
            texture = newTexture(gl, new AWTTextureData(profile, 0, 0, false, image));
        } catch (GLException e) {
            logWarning(logger, "Failed to create image texture", e);
        }

        initialized = true;
    }

    @Override
    public void paintTo(GL2 gl, GlimpseBounds bounds, Axis2D axis) {
        initIfNecessary(gl);
        if (texture == null) {
            return;
        }

        texture.setTexParameteri(gl, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        texture.setTexParameteri(gl, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        texture.enable(gl);
        texture.bind(gl);

        // See the "Alpha premultiplication" section in Texture's class comment
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);

        gl.glBegin(GL_QUADS);

        try
        {
            float xLeft = 0;
            float xRight = texture.getWidth();
            float yTop = texture.getHeight();
            float yBottom = 0;

            gl.glTexCoord2f(0, 1);
            gl.glVertex2f(xLeft, yBottom);

            gl.glTexCoord2f(1, 1);
            gl.glVertex2f(xRight, yBottom);

            gl.glTexCoord2f(1, 0);
            gl.glVertex2f(xRight, yTop);

            gl.glTexCoord2f(0, 0);
            gl.glVertex2f(xLeft, yTop);
        } finally {
            gl.glEnd();
            texture.disable(gl);
        }
    }
}
