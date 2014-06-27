package mirur.plugin;

public class PrimitiveTest {
    public static boolean isPrimitiveArray1d(Class<?> clazz) {
        return clazz.isArray() && isPrimitive(clazz.getComponentType());
    }

    public static boolean isPrimitiveArray2d(Class<?> clazz) {
        return clazz.isArray() && isPrimitiveArray1d(clazz.getComponentType());
    }

    public static boolean isPrimitiveArray(Class<?> clazz) {
        return clazz.isArray() &&
                (isPrimitive(clazz.getComponentType()) || isPrimitiveArray(clazz.getComponentType()));
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive();
    }

    public static Class<?> getPrimitiveComponent(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return clazz;
        } else if (clazz.isArray()) {
            return getPrimitiveComponent(clazz.getComponentType());
        } else {
            throw new IllegalArgumentException(clazz.getName() + " is not a primitive array");
        }
    }
}
