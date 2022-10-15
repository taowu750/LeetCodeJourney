package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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

    public static char[] toCharArray(List<Character> list) {
        char[] result = new char[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    public static char[] toCharArray(Character[] arr) {
        char[] result = new char[arr.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = arr[i];
        }

        return result;
    }

    public static int[] toIntArray(List<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    public static int[] toIntArray(Integer[] arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = arr[i];
        }

        return result;
    }

    public static boolean setEquals(int[] expected, int[] actual) {
        return new HashSet<>(Arrays.asList(
                IntStream.of(actual).boxed().toArray(Integer[]::new)))
                .equals(new HashSet<>(Arrays.asList(
                        IntStream.of(expected).boxed().toArray(Integer[]::new))));
    }

    public static <T extends Comparable<T>> boolean equalsIgnoreOrder(T[] expected, T[] actual) {
        if (expected.length != actual.length)
            throw new AssertionError("expected and actual do not match in length");
        expected = expected.clone();
        actual = actual.clone();
        Arrays.sort(expected);
        Arrays.sort(actual);
        for (int i = 0; i < expected.length; i++) {
            if (expected[i].compareTo(actual[i]) != 0)
                throw new AssertionError("\nexpected=" + Arrays.toString(expected)
                        + "\nactual=" + Arrays.toString(actual));
        }

        return true;
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
                throw new AssertionError("actual=" + Arrays.toString(actual));
        }

        return true;
    }

    public static void equalsIgnoreOutOrder(int[][] expected, int[][] actual) {
        if (expected.length != actual.length)
            throw new AssertionError("expected and actual do not match in length");
        expected = expected.clone();
        actual = actual.clone();
        Comparator<int[]> comparator = (a, b) -> {
            int cmp = Integer.compare(a.length, b.length);
            if (cmp != 0) {
                return cmp;
            }
            for (int i = 0; i < a.length; i++) {
                cmp = Integer.compare(a[i], b[i]);
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0;
        };
        Arrays.sort(expected, comparator);
        Arrays.sort(actual, comparator);

        for (int i = 0; i < expected.length; i++) {
            if (comparator.compare(expected[i], actual[i]) != 0)
                throw new AssertionError("\nexpected=" + Arrays.deepToString(expected)
                        + "\nactual=" + Arrays.deepToString(actual));
        }
    }

    public static boolean isAscending(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1])
                throw new AssertionError(String.format("arr[%d] is %d, arr[%d] is %d",
                        i - 1, array[i - 1], i, array[i]));
        }

        return true;
    }

    /**
     * 在已排序数组 a 中查找等于 x 的最小下标，不存在则返回大于 x 的最小下标
     *
     * @param lo 包含
     * @param hi 不包含
     */
    public static int bsl(int[] a, int lo, int hi, int x) {
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (x <= a[mid]) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return lo;
    }

    /**
     * 在已排序数组 a 中查找等于 x 的最大下标，不存在则返回大于 x 的最小下标
     *
     * @param lo 包含
     * @param hi 不包含
     */
    public static int bsr(int[] a, int lo, int hi, int x) {
        int start = lo;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (x >= a[mid]) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        if (lo > start && a[lo - 1] == x) {
            return lo - 1;
        } else {
            return lo;
        }
    }

    @Test
    public void test() {
        int[] a = {1, 3, 5, 5, 5, 7};
        Assertions.assertEquals(2, bsl(a, 0, a.length, 5));
        Assertions.assertEquals(1, bsl(a, 0, a.length, 3));
        Assertions.assertEquals(2, bsl(a, 0, a.length, 4));
        Assertions.assertEquals(5, bsl(a, 0, a.length, 6));
        Assertions.assertEquals(6, bsl(a, 0, a.length, 8));
        Assertions.assertEquals(0, bsl(a, 0, a.length, -1));

        Assertions.assertEquals(4, bsr(a, 0, a.length, 5));
        Assertions.assertEquals(1, bsr(a, 0, a.length, 3));
        Assertions.assertEquals(2, bsr(a, 0, a.length, 4));
        Assertions.assertEquals(5, bsr(a, 0, a.length, 6));
        Assertions.assertEquals(6, bsr(a, 0, a.length, 8));
        Assertions.assertEquals(0, bsr(a, 0, a.length, -1));
    }
}
