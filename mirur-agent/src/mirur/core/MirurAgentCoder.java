package mirur.core;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class MirurAgentCoder {
    private static final Logger LOGGER = Logger.getLogger(MirurAgentCoder.class.getName());

    public static final short TYPE_EXCEPTION = -1;
    public static final short TYPE_NULL = 0;
    public static final short TYPE_INVALID = 1;
    public static final short TYPE_OBJ_JAVA_SER = 2;
    public static final short TYPE_BUFFERED_IMAGE = 3;

    public Object decode(InputStream in0) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(in0);
        try {
            short type = in.readShort();

            switch (type) {
            case TYPE_EXCEPTION:
                String ex = in.readUTF();
                LOGGER.warning("Mirur Agent had exception: " + ex);
                return null;

            case TYPE_NULL:
            case TYPE_INVALID:
                return null;

            case TYPE_OBJ_JAVA_SER:
                return in.readObject();

            case TYPE_BUFFERED_IMAGE:
                byte[] buf = new byte[in.readInt()];
                in.read(buf);
                System.out.println(buf.length);
                return ImageIO.read(new ByteArrayInputStream(buf));

            default:
                throw new IOException("Unknown type flag: " + type);
            }
        } finally {
            in.close();
        }
    }

    public void encode(Object obj, OutputStream out0) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(out0);

        try {
            if (MirurAgent.INVALID.equals(obj)) {
                out.writeShort(TYPE_INVALID);
            } else if (obj == null) {
                out.writeShort(TYPE_NULL);
            } else if (obj instanceof BufferedImage) {
                out.writeShort(TYPE_BUFFERED_IMAGE);
                ByteArrayOutputStream o = new ByteArrayOutputStream();
                ImageIO.write((BufferedImage) obj, "png", o);
                o.close();
                out.writeInt(o.size());
                out.write(o.toByteArray());
            } else {
                out.writeShort(TYPE_OBJ_JAVA_SER);
                out.writeObject(obj);
            }
        } finally {
            out.close();
        }
    }

    public void exception(Object origObject, Throwable ex, OutputStream out0) throws IOException {
        DataOutputStream out = new DataOutputStream(out0);

        try {
            StringWriter sout0 = new StringWriter();
            PrintWriter sout1 = new PrintWriter(sout0);
            ex.printStackTrace(sout1);
            sout1.close();

            out.writeShort(TYPE_EXCEPTION);
            out.writeUTF(sout0.toString());
        } finally {
            out.close();
        }
    }
}
