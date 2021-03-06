package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;

public class ArrayUtil {

    public static int[] rotateArray(int[] arr, int pivot) {
        if (pivot > 0 && pivot < arr.length) {
            int[] tmp = Arrays.copyOf(arr, pivot);
            System.arraycopy(arr, pivot, arr, 0, arr.length - pivot);
            System.arraycopy(tmp, 0, arr, arr.length - pivot, pivot);
        }

        return arr;
    }

    public static boolean setEquals(int[] expected, int[] actual) {
        return new HashSet<>(Arrays.asList(
                IntStream.of(actual).boxed().toArray(Integer[]::new)))
                .equals(new HashSet<>(Arrays.asList(
                        IntStream.of(expected).boxed().toArray(Integer[]::new))));
    }

    public static boolean equalsIgnoreOrder(int[] expected, int[] actual) {
        if (expected.length != actual.length)
            throw new AssertionError("expected and actual do not match in length");
        expected = expected.clone();
        actual = actual.clone();
        Arrays.sort(expected);
        Arrays.sort(actual);
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i])
                throw new AssertionError("expected=" + expected[i]
                        + ", but actual=" + actual[i]);
        }

        return true;
    }

    public static boolean isAscending(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1])
                throw new AssertionError(String.format("arr[%d] is %d, arr[%d] is %d",
                        i - 1, array[i - 1], i, array[i]));
        }

        return true;
    }
}
