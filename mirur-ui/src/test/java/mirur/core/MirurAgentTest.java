package mirur.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MirurAgentTest {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        List<Integer> test1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            test1.add(i);
        }

        Object result = roundTrip(test1);
        if (result instanceof double[]) {
            System.out.println(Arrays.toString((double[]) result));
        }

        Double[] test2 = new Double[50];
        for (int i = 0; i < test2.length; i++) {
            test2[i] = Math.random();
        }

        result = roundTrip(test2);
        if (result instanceof double[]) {
            System.out.println(Arrays.toString((double[]) result));
        }

        AtomicLong[] test3 = new AtomicLong[10];
        for (int i = 0; i < test3.length; i++) {
            test3[i] = new AtomicLong(i);
        }

        result = roundTrip(test3);
        if (result instanceof double[]) {
            System.out.println(Arrays.toString((double[]) result));
        }
    }

    static Object roundTrip(Object o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream bos = new ObjectOutputStream(out);
        MirurAgent.stream(bos, o, Long.MAX_VALUE);
        bos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        Object result = MirurAgentCoder.decode(ois);
        ois.close();
        return result;
    }
}
