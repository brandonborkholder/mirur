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
package mirur.core;

import static mirur.core.MirurAgentCoder.addTentativeValue;
import static mirur.core.MirurAgentCoder.encode;
import static mirur.core.MirurAgentCoder.exception;
import static mirur.core.MirurAgentCoder.failTentativeArray;
import static mirur.core.MirurAgentCoder.invalid;
import static mirur.core.MirurAgentCoder.startTentativeArray;
import static mirur.core.MirurAgentCoder.tooLarge;

import java.awt.Image;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JLabel;

public class MirurAgent implements Runnable {
    public static final String[] AGENT_CLASSES = new String[] {
            MirurAgent.class.getName(),
            MirurAgentCoder.class.getName(),
    };

    private final Object object;
    private final long maxBytes;
    private final int port;

    public MirurAgent(Object object, long maxBytes, int port) {
        this.object = object;
        this.maxBytes = maxBytes;
        this.port = port;
    }

    public void run() {
        try {
            streamObject(object, maxBytes, port);
        } catch (IOException ex) {
            // can't do anything with this
        }
    }

    public static void streamObjectAsync(Object object, long maxBytes, int port) {
        Thread t = new Thread(new MirurAgent(object, maxBytes, port), "mirur-agent");
        t.setDaemon(true);
        t.start();
    }

    public static void streamObject(Object object, long maxBytes, int port) throws IOException {
        Socket socket = new Socket(InetAddress.getByName(null), port);
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        try {
            if (object == null) {
                encode(null, out);
            } else {
                stream(out, object, maxBytes);
            }
        } catch (Exception ex) {
            exception(object, ex, out);
        }

        out.close();
        socket.close();
    }

    public static void stream(ObjectOutputStream out, Object value, long maxBytes) throws IOException {
        Class<?> clazz = value.getClass();
        if (isPrimitiveArray(clazz)) {
            // 1d array of primitives
            long nBytes = getArray1dBytes(value);
            if (nBytes <= maxBytes) {
                encode(value, out);
            } else {
                tooLarge(nBytes, out);
            }
        } else if (clazz.isArray() && isPrimitiveArray(clazz.getComponentType())) {
            // 2d array of primitives
            long nBytes = getArray2dBytes(value);
            if (nBytes <= maxBytes) {
                encode(value, out);
            } else {
                tooLarge(nBytes, out);
            }
        } else if (clazz.isArray() && !clazz.getComponentType().isPrimitive()) {
            // array of possibly primitive wrappers
            Object[] array = (Object[]) value;

            if (array.length == 0) {
                encode(null, out);
            } else if (maxBytes < array.length * 8L) {
                tooLarge(array.length * 8L, out);
            } else if (array[0] instanceof Number) {
                startTentativeArray(array.length, out);

                for (int i = 0; i < array.length; i++) {
                    if (array[i] instanceof Number) {
                        addTentativeValue(((Number) array[i]).doubleValue(), out);
                    } else {
                        failTentativeArray(out);
                        break;
                    }
                }
            } else {
                invalid(array.getClass().toString(), out);
            }
        } else if (value instanceof Collection<?>) {
            // collection of possibly primitive wrappers
            Collection<?> c = (Collection<?>) value;

            if (c.isEmpty()) {
                encode(null, out);
            } else if (maxBytes < c.size() * 8L) {
                tooLarge(c.size() * 8L, out);
            } else if (c.iterator().next() instanceof Number) {
                startTentativeArray(c.size(), out);

                Iterator<?> itr = c.iterator();
                while (itr.hasNext()) {
                    Object test = itr.next();
                    if (test instanceof Number) {
                        addTentativeValue(((Number) test).doubleValue(), out);
                    } else {
                        failTentativeArray(out);
                        break;
                    }
                }
            } else {
                invalid(c.getClass().toString(), out);
            }
        } else if (value instanceof Buffer) {
            Buffer buf = (Buffer) value;
            long nBytes = getBufferBytes(buf);
            if (nBytes >= 0 && nBytes <= maxBytes) {
                encode(buf, out);
            } else {
                tooLarge(nBytes, out);
            }
        } else if (value instanceof Image) {
            Image img = (Image) value;
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            if (width <= 0 || height <= 0) {
                invalid(img.getClass().toString(), out);
                return;
            }

            BufferedImage bufImg;
            if (img instanceof BufferedImage) {
                bufImg = (BufferedImage) img;
            } else {
                bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                boolean success = bufImg.getGraphics().drawImage(img, 0, 0, null);
                if (!success) {
                    invalid(img.getClass().toString(), out);
                    return;
                }
            }

            encode(bufImg, out);
        } else if (value instanceof Icon) {
            Icon icon = (Icon) value;
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();

            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            icon.paintIcon(new JLabel(), bufImg.getGraphics(), width, height);
            encode(bufImg, out);
        } else if (value instanceof Shape) {
            encode(value, out);
        } else {
            invalid(value.getClass().toString(), out);
        }
    }

    private static boolean isPrimitiveArray(Class<?> clazz) {
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    private static long getBufferBytes(Buffer buf) {
        if (buf instanceof DoubleBuffer) {
            return buf.capacity() * 8L;
        } else if (buf instanceof LongBuffer) {
            return buf.capacity() * 8L;
        } else if (buf instanceof FloatBuffer) {
            return buf.capacity() * 4L;
        } else if (buf instanceof IntBuffer) {
            return buf.capacity() * 4L;
        } else if (buf instanceof ShortBuffer) {
            return buf.capacity() * 2L;
        } else if (buf instanceof CharBuffer) {
            return buf.capacity() * 2L;
        } else if (buf instanceof ByteBuffer) {
            return buf.capacity() * 1L;
        } else {
            return -1;
        }
    }

    private static long getArray1dBytes(Object array) {
        if (array instanceof boolean[]) {
            return ((boolean[]) array).length * 1L;
        } else if (array instanceof byte[]) {
            return ((byte[]) array).length * 1L;
        } else if (array instanceof char[]) {
            return ((char[]) array).length * 2L;
        } else if (array instanceof short[]) {
            return ((short[]) array).length * 2L;
        } else if (array instanceof float[]) {
            return ((float[]) array).length * 4L;
        } else if (array instanceof int[]) {
            return ((int[]) array).length * 4L;
        } else if (array instanceof long[]) {
            return ((long[]) array).length * 8L;
        } else if (array instanceof double[]) {
            return ((double[]) array).length * 8L;
        } else {
            throw new AssertionError("Unexpected " + array.getClass().getName());
        }
    }

    private static long getArray2dBytes(Object array) {
        if (array instanceof boolean[][]) {
            long sum = 0;
            for (boolean[] a : (boolean[][]) array) {
                sum += a == null ? 0 : a.length;
            }
            return sum * 1L;
        } else if (array instanceof byte[][]) {
            long sum = 0;
            for (byte[] a : (byte[][]) array) {
                sum += a == null ? 0 : a.length;
            }
            return sum * 1L;
        } else if (array instanceof char[][]) {
            long sum = 0;
            for (char[] a : (char[][]) array) {
                sum += a == null ? 0 : a.length;
            }
            return sum * 2L;
        } else if (array instanceof short[][]) {
            long sum = 0;
            for (short[] a : (short[][]) array) {
                sum += a == null ? 0 : a.length;
            }
            return sum * 2L;
        } else if (array instanceof float[][]) {
            long sum = 0;
            for (float[] a : (float[][]) array) {
                sum += a == null ? 0 : a.length;
            }
            return sum * 4L;
        } else if (array instanceof int[][]) {
            long sum = 0;
            for (int[] a : (int[][]) array) {
                sum += a == null ? 0 : a.length;
            }
            return sum * 4L;
        } else if (array instanceof long[][]) {
            long sum = 0;
            for (long[] a : (long[][]) array) {
                sum += a == null ? 0 : a.length;
            }
            return sum * 8L;
        } else if (array instanceof double[][]) {
            long sum = 0;
            for (double[] a : (double[][]) array) {
                sum += a == null ? 0 : a.length;
            }
            return sum * 8L;
        } else {
            throw new AssertionError("Unexpected " + array.getClass().getName());
        }
    }
}
