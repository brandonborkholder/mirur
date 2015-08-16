package mirur.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MirurAgentCoder {
    public static final short TYPE_NULL = 0;
    public static final short TYPE_INVALID = 1;
    public static final short TYPE_OBJ_JAVA_SER = 2;

    public Object decode(InputStream in0) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(in0);
        try {
            short type = in.readShort();

            switch (type) {
            case TYPE_NULL:
            case TYPE_INVALID:
                return null;

            case TYPE_OBJ_JAVA_SER:
                return in.readObject();

            default:
                throw new IOException("Unknown type flag: " + type);
            }
        } finally {
            in.close();
        }
    }

    public void encode(Object obj, OutputStream out0) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(out0);

        if (MirurAgent.INVALID.equals(obj)) {
            out.writeShort(TYPE_INVALID);
        } else if (obj == null) {
            out.writeShort(TYPE_NULL);
        } else {
            out.writeShort(TYPE_OBJ_JAVA_SER);
            out.writeObject(obj);
        }
    }
}
