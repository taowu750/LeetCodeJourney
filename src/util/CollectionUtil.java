package util;

import org.junit.jupiter.api.Assertions;

import java.util.*;

public class CollectionUtil {

    public static <T> boolean setEquals(Collection<T> expected, Collection<T> actual) {
        if (!(new HashSet<>(expected).equals(new HashSet<>(actual)))) {
            throw new AssertionError("not equal, actual=" + actual);
        }

        return true;
    }

    public static <T extends Comparable<T>> void equalsIgnoreOrder(List<T> expected, List<T> actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("expected and actual size not match:\n" +
                    "expected: " + expected + "\n" +
                    "actual: " + actual);
        }
        if (expected.size() == 0) {
            return;
        }

        Collections.sort(expected);
        Collections.sort(actual);
        Assertions.assertEquals(expected, actual);
    }

    public static <T extends Comparable<T>> boolean deepEqualsIgnoreOrder(List<List<T>> expected,
                                                                          List<List<T>> actual) {
        if (expected.size() != actual.size())
            throw new AssertionError("expected and actual do not match in size, actual=" + actual);

        for (List<T> ts : expected)
            Collections.sort(ts);
        for (List<T> ts : actual)
            Collections.sort(ts);

        HashSet<List<T>> s1 = new HashSet<>(expected), s2 = new HashSet<>(actual);
        if (s1.size() != s2.size())
            throw new AssertionError("expected and actual do not match in size");

        for (List<T> ts : s2) {
            if (!s1.contains(ts))
                throw new AssertionError("The list in actual is not in expected: "
                        + ts);
        }

        return true;
    }

    public static <T extends Comparable<T>> boolean deepEqualsIgnoreOutOrder(List<List<T>> expected, List<List<T>> actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("size mismatch, actual=" + actual);
        }

        if (!(new HashSet<>(expected).equals(new HashSet<>(actual)))) {
            throw new AssertionError("mismatch, actual=" + actual);
        }

        return true;
    }

    public static <T> void in(List<T> actual, List<T>... expected) {
        for (List<T> ts : expected) {
            if (actual.equals(ts)) {
                return;
            }
        }
        throw new AssertionError("no match. actual=" + actual);
    }

    public static String toString(List<int[]> intArrays) {
        if (intArrays.size() == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder(intArrays.size() * 8);
        sb.append('[');
        for (int[] intArray : intArrays) {
            sb.append(Arrays.toString(intArray)).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(']');

        return sb.toString();
    }

    public static void equalsIntArrays(List<int[]> expected, List<int[]> actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("size no match, actual=" + toString(actual));
        }
        for (int i = 0; i < expected.size(); i++) {
            int[] e = expected.get(i), a = actual.get(i);
            if (!Arrays.toString(e).equals(Arrays.toString(a))) {
                throw new AssertionError("no match, idx=" + i + ", actual=" + toString(actual));
            }
        }
    }
}
