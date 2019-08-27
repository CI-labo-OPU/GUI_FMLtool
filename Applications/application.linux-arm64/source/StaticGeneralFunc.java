import java.util.Arrays;
import java.util.List;

public class StaticGeneralFunc {

	StaticGeneralFunc(){}

	//******************************************************************************//

	public static double log(double a, double b){
		return Math.log(a) / Math.log(b);
	}

	//array multisort
	public static void multiQuickSort(double[]... arrays) {
		multiQuickSort(0, arrays);
	}

	public static void multiQuickSort(int sortDimension, double[]... arrays) {
		// check if the lengths are equal, break if everything is empty
		if (arrays == null || arrays.length == 0) {
			return;
		}
		// if the array only has a single dimension, sort it and return
		if (arrays.length == 1) {
			Arrays.sort(arrays[0]);
			return;
		}
		// also return if the sort dimension is not in our array range
		if (sortDimension < 0 || sortDimension >= arrays.length) {
			return;
		}
		// check sizes
		int firstArrayLength = arrays[0].length;
		for (int i = 1; i < arrays.length; i++) {
			if (arrays[i] == null || firstArrayLength != arrays[i].length)
				return;
		}

		multiQuickSort(arrays, 0, firstArrayLength, sortDimension);
	}

	private static void multiQuickSort(double[][] a, int offset, int length, int indexToSort) {
		if (offset < length) {
			int pivot = multiPartition(a, offset, length, indexToSort);
			multiQuickSort(a, offset, pivot, indexToSort);
			multiQuickSort(a, pivot + 1, length, indexToSort);
		}
	}

	private static int multiPartition(double[][] array, int start, int end, int partitionArrayIndex) {
		final int ending = end - 1;
		final double x = array[partitionArrayIndex][ending];
		int i = start - 1;
		for (int j = start; j < ending; j++) {
			if (array[partitionArrayIndex][j] <= x) {
				i++;
				for (int arrayIndex = 0; arrayIndex < array.length; arrayIndex++) {
					swap(array[arrayIndex], i, j);
				}
			}
		}
		i++;
		for (int arrayIndex = 0; arrayIndex < array.length; arrayIndex++) {
			swap(array[arrayIndex], i, ending);
		}
		return i;
	}

	public static void swap(double[] array, int x, int y) {
		double tmpIndex = array[x];
		array[x] = array[y];
		array[y] = tmpIndex;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void swap(List<?> list, int i, int j) {
		final List l = list;
		l.set(i, l.set(j, l.get(i)));
	}

	private static void swap(Object[] arr, int i, int j) {
		Object tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

}
