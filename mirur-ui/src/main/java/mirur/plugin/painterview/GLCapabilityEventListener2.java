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

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.jogamp.opengl.GLAutoDrawable;
import com.metsci.glimpse.core.gl.GLCapabilityEventListener;

/**
 * Eclipse's logging seems very slow and logging all OpenGL capabilities at start really makes it seem like the plugin
 * is slow. So here we capture all the capabilities strings and then log in one shot.
 */
public class GLCapabilityEventListener2 extends GLCapabilityEventListener {
    private static final Logger LOGGER = Logger.getLogger(GLCapabilityEventListener2.class.getName());

    private StringBuilder appender;

    public GLCapabilityEventListener2() {
        this(new StringBuilder());
    }

    private GLCapabilityEventListener2(final StringBuilder appender) {
        super(new Logger(".", null) {
            @Override
            public void log(LogRecord record) {
                appender.append("\n");
                appender.append(record.getMessage());
            }
        });
        this.appender = appender;

        appender.append("OpenGL Capabilities List");
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);

        LOGGER.log(Level.INFO, appender.toString());
    }
}