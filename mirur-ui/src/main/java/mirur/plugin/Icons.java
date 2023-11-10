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
package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logWarning;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.metsci.glimpse.core.support.colormap.ColorGradient;
import com.metsci.glimpse.core.support.colormap.ColorGradients;

public class Icons {
    private static final Logger LOGGER = Logger.getLogger(Icons.class.getName());

    private static final String MIRUR_64_PATH = "icons/mirur_64.png";
    private static final String MIRUR_LOGO_PATH = "icons/mirur-logo.png";

    public static Image getMirurLargeIcon() {
        return Activator.getCachedImage(MIRUR_64_PATH);
    }

    public static ImageDescriptor getNewPainter(boolean enabled) {
        return Activator.getImageDescriptor("icons/painter.gif");
    }

    public static ImageDescriptor getConfig(boolean enabled) {
        return Activator.getImageDescriptor("icons/config.gif");
    }

    public static ImageDescriptor getRotate(boolean enabled) {
        return Activator.getImageDescriptor("icons/rotate.gif");
    }

    public static ImageDescriptor getGradient(boolean enabled) {
        return Activator.getImageDescriptor("icons/gradient_jet.gif");
    }

    public static ImageDescriptor getGradient(ColorGradient grad) {
        if (grad == ColorGradients.jet) {
            return Activator.getImageDescriptor("icons/gradient_jet.gif");
        } else if (grad == ColorGradients.viridis) {
            return Activator.getImageDescriptor("icons/gradient_viridis.gif");
        } else if (grad == ColorGradients.reverseBone) {
            return Activator.getImageDescriptor("icons/gradient_bone.gif");
        } else if (grad == ColorGradients.gray) {
            return Activator.getImageDescriptor("icons/gradient_grey.gif");
        } else if (grad == ColorGradients.spectral) {
            return Activator.getImageDescriptor("icons/gradient_spectral.gif");
        } else {
            return getGradient(true);
        }
    }

    public static ImageDescriptor getSaveAs(boolean enabled) {
        if (enabled) {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVEAS_EDIT);
        } else {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVEAS_EDIT_DISABLED);
        }
    }

    public static ImageDescriptor getSync(boolean enabled) {
        if (enabled) {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED);
        } else {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED_DISABLED);
        }
    }

    public static ImageDescriptor getUndo(boolean enabled) {
        if (enabled) {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UNDO);
        } else {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED);
        }
    }

    public static ImageDescriptor getDuplicateView(boolean enabled) {
        if (enabled) {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD);
        } else {
            return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED);
        }
    }

    public static ImageDescriptor getDelete() {
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE);
    }

    public static ImageDescriptor getPin(boolean enabled) {
        if (enabled) {
            return Activator.getImageDescriptor("icons/pin.gif");
        } else {
            return Activator.getImageDescriptor("icons/pin_disabled.gif");
        }
    }

    public static BufferedImage getMirurLogoImage() {
        try (InputStream in = Icons.class.getClassLoader().getResourceAsStream(MIRUR_LOGO_PATH)) {
            return ImageIO.read(in);
        } catch (IOException ex) {
            logWarning(LOGGER, "Failed to load logo image", ex);
            return new BufferedImage(0, 0, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }
}
