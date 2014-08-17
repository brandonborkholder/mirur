package simple;

public class SortingExample {
	public static void main(String[] args) {
		double[] array = new double[10];
		for (int i = 0; i < array.length; i++) {
			array[i] = Math.random();
		}
		
		// bubble sort
		while (true) {
			boolean madeChange = false;
			for (int i = 1; i < array.length; i++) {
				if (array[i - 1] > array[i]) {
					double t = array[i];
					array[i] = array[i - 1];
					array[i - 1] = t;
					madeChange = true;
				}
			}

			if (!madeChange) {
				break;
			}
		}
	}
}
