package mirur.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MirurAgentTest {
    public static void main(String[] args) {
        List<Integer> test1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            test1.add(i);
        }

        Object result = MirurAgent.toArray(test1, Long.MAX_VALUE);
        if (result instanceof double[]) {
            System.out.println(Arrays.toString((double[]) result));
        }

        Double[] test2 = new Double[50];
        for (int i = 0; i < test2.length; i++) {
            test2[i] = Math.random();
        }

        result = MirurAgent.toArray(test2, Long.MAX_VALUE);
        if (result instanceof double[]) {
            System.out.println(Arrays.toString((double[]) result));
        }

        AtomicLong[] test3 = new AtomicLong[10];
        for (int i = 0; i < test3.length; i++) {
            test3[i] = new AtomicLong(i);
        }

        result = MirurAgent.toArray(test3, Long.MAX_VALUE);
        if (result instanceof double[]) {
            System.out.println(Arrays.toString((double[]) result));
        }
    }
}
