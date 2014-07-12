package mirur.core;

import java.util.Collection;
import java.util.Iterator;

public class MirurAgent {
    private static final Object INVALID = null;

    private int guessedSize;
    private double[] array;
    private int index;

    public static Object test(Object value) {
        System.out.println("Testing with " + value);
        return value != null;
    }

    public static Object toArray(Object value) {
        if (value == null) {
            return null;
        }

        Class<?> clazz = value.getClass();
        if (isPrimitiveArray(clazz)) {
            // 1d array of primitives
            return value;
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
}
