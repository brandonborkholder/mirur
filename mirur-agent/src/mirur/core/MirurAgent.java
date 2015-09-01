package mirur.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
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

public class MirurAgent {
    public static final String[] AGENT_CLASSES = new String[] {
        MirurAgent.class.getName(),
        MirurAgentCoder.class.getName(),
    };

    private static final long MAX_BYTES = 50 * 1024 * 1024;

    public static final Object INVALID = new Object();

    private int guessedSize;
    private double[] array;
    private int index;

    public static Object test(Object value) {
        System.out.println("Testing with " + value);
        return value != null;
    }

    public static void sendAsArray(Object object, int port) throws IOException {
        Socket socket = new Socket(InetAddress.getByName(null), port);
        OutputStream out = socket.getOutputStream();

        try {
            Object transformed = toArray(object);
            new MirurAgentCoder().encode(transformed, out);
        } catch (Exception ex) {
            new MirurAgentCoder().exception(object, ex, out);
        }

        out.close();
        socket.close();
    }

    public static Object toArray(Object value) {
        if (value == null) {
            return null;
        }

        Class<?> clazz = value.getClass();
        if (isPrimitiveArray(clazz)) {
            // 1d array of primitives
            long nBytes = getArray1dBytes(value);
            if (nBytes >= 0 && nBytes <= MAX_BYTES) {
                return value;
            } else {
                return INVALID;
            }
        } else if (clazz.isArray() && isPrimitiveArray(clazz.getComponentType())) {
            // 2d array of primitives
            return value;
        } else if (clazz.isArray() && !clazz.getComponentType().isPrimitive()) {
            // array of possibly primitive wrappers
            Object[] array = (Object[]) value;

            MirurAgent helper = new MirurAgent(array.length);
            for (int i = 0; i < array.length; i++) {
                if (!helper.tryAdd(array[i])) {
                    return INVALID;
                }
            }

            return helper.toArray();
        } else if (value instanceof Collection<?>) {
            // collection of possibly primitive wrappers
            Collection<?> c = (Collection<?>) value;
            Iterator<?> itr = c.iterator();
            MirurAgent helper = new MirurAgent(c.size());
            while (itr.hasNext()) {
                if (!helper.tryAdd(itr.next())) {
                    return INVALID;
                }
            }

            return helper.toArray();
        } else if (value instanceof Buffer) {
            Buffer buf = (Buffer) value;
            long nBytes = getBufferBytes(buf);
            if (nBytes >= 0 && nBytes <= MAX_BYTES) {
                return buf;
            } else {
                return INVALID;
            }
        } else if (value instanceof Image) {
            Image img = (Image) value;
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            if (width <= 0 || height <= 0) {
                return INVALID;
            }

            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            boolean success = bufImg.getGraphics().drawImage(img, 0, 0, null);
            if (success) {
                return bufImg;
            } else {
                return INVALID;
            }
        }

        return INVALID;
    }

    private static boolean isPrimitiveArray(Class<?> clazz) {
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    private MirurAgent(int guessSize) {
        guessedSize = guessSize;
        index = 0;
        // don't allocate until we are able to add a valid number
        array = null;
    }

    private Object toArray() {
        if (index == 0 || array == null) {
            return INVALID;
        } else if (index == array.length - 1) {
            return array;
        } else {
            double[] copy = new double[index];
            System.arraycopy(array, 0, copy, 0, index);
            return copy;
        }
    }

    private boolean tryAdd(Object value) {
        if (value instanceof Number) {
            if (array == null) {
                array = new double[guessedSize];
            }

            array[index++] = ((Number) value).doubleValue();
            return true;
        } else {
            return false;
        }
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
            return -1;
        }
    }
}
