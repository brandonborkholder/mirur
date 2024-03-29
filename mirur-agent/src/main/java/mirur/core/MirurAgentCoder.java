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

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class MirurAgentCoder {
    private static final Logger LOGGER = Logger.getLogger(MirurAgentCoder.class.getName());

    private static final byte NATIVE_TYPE_BYTE = 1;
    private static final byte NATIVE_TYPE_SHORT = 2;
    private static final byte NATIVE_TYPE_CHAR = 3;
    private static final byte NATIVE_TYPE_FLOAT = 4;
    private static final byte NATIVE_TYPE_INT = 5;
    private static final byte NATIVE_TYPE_LONG = 6;
    private static final byte NATIVE_TYPE_DOUBLE = 7;

    public static final short TYPE_EXCEPTION = -1;
    public static final short TYPE_NULL = 0;
    public static final short TYPE_INVALID = 1;
    public static final short TYPE_OBJ_JAVA_SER = 2;
    public static final short TYPE_BUFFERED_IMAGE = 3;
    public static final short TYPE_BUFFER = 4;
    public static final short TYPE_SHAPE = 5;
    public static final short TYPE_TOO_LARGE = 6;
    public static final short TYPE_TENTATIVE_ARRAY = 7;

    public static Object decode(ObjectInputStream in) throws IOException, ClassNotFoundException {
        short type = in.readShort();

        switch (type) {
        case TYPE_EXCEPTION:
            String ex = in.readUTF();
            LOGGER.warning("Mirur Agent had exception: " + ex);
            return null;

        case TYPE_NULL:
            return null;

        case TYPE_INVALID:
            String typeStr = in.readUTF();
            LOGGER.warning("Mirur Agent could not transform: " + typeStr);
            return null;

        case TYPE_TOO_LARGE:
            long size = in.readLong();
            return String.format("Object too large (%,d bytes). Increase max size in preferences.", size);

        case TYPE_OBJ_JAVA_SER:
            return in.readObject();

        case TYPE_BUFFERED_IMAGE: {
            byte[] buf = new byte[in.readInt()];
            int read = 0;
            while (read < buf.length) {
                read += in.read(buf, read, buf.length - read);
            }
            return ImageIO.read(new ByteArrayInputStream(buf));
        }

        case TYPE_BUFFER: {
            int capacity = in.readInt();
            int position = in.readInt();
            int limit = in.readInt();

            Buffer buf = deserializeBuffer(capacity, in);
            buf.position(position);
            buf.limit(limit);
            return buf;
        }

        case TYPE_SHAPE: {
            Path2D path = new Path2D.Float(in.readInt());
            int t = in.readInt();

            while (t != Integer.MIN_VALUE) {
                switch (t) {
                case PathIterator.SEG_CLOSE:
                    path.closePath();
                    break;

                case PathIterator.SEG_MOVETO:
                    path.moveTo(in.readDouble(), in.readDouble());
                    break;

                case PathIterator.SEG_LINETO:
                    path.lineTo(in.readDouble(), in.readDouble());
                    break;

                case PathIterator.SEG_QUADTO:
                    path.quadTo(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble());
                    break;

                case PathIterator.SEG_CUBICTO:
                    path.curveTo(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble());
                    break;
                }

                t = in.readInt();
            }

            return path;
        }

        case TYPE_TENTATIVE_ARRAY: {
            double[] array = new double[in.readInt()];
            for (int i = 0; i < array.length; i++) {
                if (in.readByte() == TYPE_TENTATIVE_ARRAY) {
                    array[i] = in.readDouble();
                } else {
                    return null;
                }
            }

            return array;
        }

        default:
            throw new IOException("Unknown type flag: " + type);
        }
    }

    public static void tooLarge(long bytes, ObjectOutputStream out) throws IOException {
        out.writeShort(TYPE_TOO_LARGE);
        out.writeLong(bytes);
    }

    public static void invalid(String type, ObjectOutputStream out) throws IOException {
        out.writeShort(TYPE_INVALID);
        out.writeUTF(type);
    }

    public static void encode(Object obj, ObjectOutputStream out) throws IOException {
        if (obj == null) {
            out.writeShort(TYPE_NULL);
        } else if (obj instanceof BufferedImage) {
            out.writeShort(TYPE_BUFFERED_IMAGE);
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage) obj, "bmp", o);
            o.close();
            out.writeInt(o.size());
            out.write(o.toByteArray());
        } else if (obj instanceof Buffer) {
            out.writeShort(TYPE_BUFFER);
            Buffer buf = (Buffer) obj;
            out.writeInt(buf.capacity());
            out.writeInt(buf.position());
            out.writeInt(buf.limit());

            int oldPos = buf.position();
            int oldLim = buf.limit();
            buf.limit(buf.capacity());
            buf.position(0);
            serialize(buf, out);
            buf.position(oldPos);
            buf.limit(oldLim);
        } else if (obj instanceof Shape) {
            out.writeShort(TYPE_SHAPE);

            Shape s = (Shape) obj;
            PathIterator itr = s.getPathIterator(null);
            out.writeInt(itr.getWindingRule());

            double[] coords = new double[6];
            while (!itr.isDone()) {
                int type = itr.currentSegment(coords);
                itr.next();

                out.writeInt(type);
                switch (type) {
                case PathIterator.SEG_CLOSE:
                    break;

                case PathIterator.SEG_LINETO:
                case PathIterator.SEG_MOVETO:
                    out.writeDouble(coords[0]);
                    out.writeDouble(coords[1]);
                    break;

                case PathIterator.SEG_QUADTO:
                    out.writeDouble(coords[0]);
                    out.writeDouble(coords[1]);
                    out.writeDouble(coords[2]);
                    out.writeDouble(coords[3]);
                    break;

                case PathIterator.SEG_CUBICTO:
                    out.writeDouble(coords[0]);
                    out.writeDouble(coords[1]);
                    out.writeDouble(coords[2]);
                    out.writeDouble(coords[3]);
                    out.writeDouble(coords[4]);
                    out.writeDouble(coords[5]);
                    break;
                }
            }
            out.writeInt(Integer.MIN_VALUE);
        } else {
            out.writeShort(TYPE_OBJ_JAVA_SER);
            out.writeObject(obj);
        }
    }

    private static Buffer deserializeBuffer(int capacity, ObjectInputStream in) throws IOException {
        switch (in.readByte()) {
        case NATIVE_TYPE_BYTE: {
            ByteBuffer b = ByteBuffer.allocate(capacity);
            for (int i = 0; i < capacity; i++) {
                b.put(in.readByte());
            }
            return b;
        }
        case NATIVE_TYPE_SHORT: {
            ShortBuffer b = ShortBuffer.allocate(capacity);
            for (int i = 0; i < capacity; i++) {
                b.put(in.readShort());
            }
            return b;
        }
        case NATIVE_TYPE_CHAR: {
            CharBuffer b = CharBuffer.allocate(capacity);
            for (int i = 0; i < capacity; i++) {
                b.put(in.readChar());
            }
            return b;
        }
        case NATIVE_TYPE_INT: {
            IntBuffer b = IntBuffer.allocate(capacity);
            for (int i = 0; i < capacity; i++) {
                b.put(in.readInt());
            }
            return b;
        }
        case NATIVE_TYPE_FLOAT: {
            FloatBuffer b = FloatBuffer.allocate(capacity);
            for (int i = 0; i < capacity; i++) {
                b.put(in.readFloat());
            }
            return b;
        }
        case NATIVE_TYPE_LONG: {
            LongBuffer b = LongBuffer.allocate(capacity);
            for (int i = 0; i < capacity; i++) {
                b.put(in.readLong());
            }
            return b;
        }
        case NATIVE_TYPE_DOUBLE: {
            DoubleBuffer b = DoubleBuffer.allocate(capacity);
            for (int i = 0; i < capacity; i++) {
                b.put(in.readDouble());
            }
            return b;
        }
        default:
            return null;
        }
    }

    private static void serialize(Buffer buf, ObjectOutputStream out) throws IOException {
        if (buf instanceof ByteBuffer) {
            out.writeByte(NATIVE_TYPE_BYTE);
            ByteBuffer b = (ByteBuffer) buf;
            for (int i = 0, n = b.capacity(); i < n; i++) {
                out.writeByte(b.get(i));
            }
        } else if (buf instanceof ShortBuffer) {
            out.writeByte(NATIVE_TYPE_SHORT);
            ShortBuffer b = (ShortBuffer) buf;
            for (int i = 0, n = b.capacity(); i < n; i++) {
                out.writeShort(b.get(i));
            }
        } else if (buf instanceof CharBuffer) {
            out.writeByte(NATIVE_TYPE_CHAR);
            CharBuffer b = (CharBuffer) buf;
            for (int i = 0, n = b.capacity(); i < n; i++) {
                out.writeChar(b.get(i));
            }
        } else if (buf instanceof FloatBuffer) {
            out.writeByte(NATIVE_TYPE_FLOAT);
            FloatBuffer b = (FloatBuffer) buf;
            for (int i = 0, n = b.capacity(); i < n; i++) {
                out.writeFloat(b.get(i));
            }
        } else if (buf instanceof IntBuffer) {
            out.writeByte(NATIVE_TYPE_INT);
            IntBuffer b = (IntBuffer) buf;
            for (int i = 0, n = b.capacity(); i < n; i++) {
                out.writeInt(b.get(i));
            }
        } else if (buf instanceof LongBuffer) {
            out.writeByte(NATIVE_TYPE_LONG);
            LongBuffer b = (LongBuffer) buf;
            for (int i = 0, n = b.capacity(); i < n; i++) {
                out.writeLong(b.get(i));
            }
        } else if (buf instanceof DoubleBuffer) {
            out.writeByte(NATIVE_TYPE_DOUBLE);
            DoubleBuffer b = (DoubleBuffer) buf;
            for (int i = 0, n = b.capacity(); i < n; i++) {
                out.writeDouble(b.get(i));
            }
        } else {
            throw new AssertionError("Unknown buffer type " + buf.getClass().getName());
        }
    }

    public static void exception(Object origObject, Throwable ex, ObjectOutputStream out) throws IOException {
        StringWriter sout0 = new StringWriter();
        PrintWriter sout1 = new PrintWriter(sout0);
        ex.printStackTrace(sout1);
        sout1.close();

        out.writeShort(TYPE_EXCEPTION);
        out.writeUTF(sout0.toString());
    }

    public static void startTentativeArray(int len, ObjectOutputStream out) throws IOException {
        out.writeShort(TYPE_TENTATIVE_ARRAY);
        out.writeInt(len);
    }

    public static void addTentativeValue(double v, ObjectOutputStream out) throws IOException {
        out.writeByte(TYPE_TENTATIVE_ARRAY);
        out.writeDouble(v);
    }

    public static void failTentativeArray(ObjectOutputStream out) throws IOException {
        out.writeByte(TYPE_INVALID);
    }
}
