package util;

import org.junit.jupiter.api.Assertions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class CollectionUtil {

    public static <T> boolean setEquals(Collection<T> c1, Collection<T> c2) {
        return new HashSet<>(c1).equals(new HashSet<>(c2));
    }

    public static <T extends Comparable<T>> void equalsIgnoreOrder(List<T> c1, List<T> c2) {
        Collections.sort(c1);
        Collections.sort(c2);
        Assertions.assertEquals(c1, c2);
    }

    public static <T extends Comparable<T>> boolean deepEqualsIgnoreOrder(List<List<T>> expected,
                                                                          List<List<T>> actual) {
        if (expected.size() != actual.size())
            throw new AssertionError("expected and actual do not match in size");

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

    public static <T extends Comparable<T>> boolean deepEqualsIgnoreOutOrder(List<List<T>> c1, List<List<T>> c2) {
        if (c1.size() != c2.size())
            return false;

        return new HashSet<>(c1).equals(new HashSet<>(c2));
    }
}
