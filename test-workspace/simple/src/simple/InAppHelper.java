package simple;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class InAppHelper {
	private static final Object INVALID = Void.TYPE;

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
		} else if (clazz.isArray() && !clazz.getComponentType().isPrimitive()){
			// array of possibly primitive wrappers
			Object[] array = (Object[]) value;
			
			Collection2DoubleArrayHelper helper = new Collection2DoubleArrayHelper(array.length);
			for (int i = 0; i < array.length && helper.isValid; i++){
				helper.tryAdd(array[i]);
			}
			
			if (helper.isValid){
				return helper.toArray();
			}
		} else if (value instanceof Collection<?>) {
			// collection of possibly primitive wrappers
			Collection<?> c = (Collection<?>) value;
			Iterator<?> itr = c.iterator();
			Collection2DoubleArrayHelper helper = new Collection2DoubleArrayHelper(c.size());
			while (itr.hasNext() && helper.isValid){
				helper.tryAdd(itr.next());
			}
			
			if (helper.isValid){
				return helper.toArray();
			}
		}

		return INVALID;
	}

	private static boolean isPrimitiveArray(Class<?> clazz) {
		return clazz.isArray() && clazz.getComponentType().isPrimitive();
	}

	private static class Collection2DoubleArrayHelper {
		private boolean isValid;

		private double[] array;
		private int index;

		Collection2DoubleArrayHelper(int guessSize) {
			array = new double[guessSize];
			isValid = true;
			index = 0;
		}

		Object toArray() {
			if (index == array.length - 1) {
				return array;
			} else {
				return Arrays.copyOfRange(array, 0, index);
			}
		}

		boolean tryAdd(Object value) {
			if (value instanceof Number) {
				array[index++] = ((Number) value).doubleValue();
				return true;
			} else {
				return false;
			}
		}
	}
}
