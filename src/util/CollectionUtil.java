package util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class CollectionUtil {

    public static <T> boolean setEquals(Collection<T> c1, Collection<T> c2) {
        return new HashSet<>(c1).equals(new HashSet<>(c2));
    }

    public static <T extends Comparable<T>> boolean equalsIgnoreOrder(List<T> c1, List<T> c2) {
        Collections.sort(c1);
        Collections.sort(c2);
        return c1.equals(c2);
    }

    public static <T extends Comparable<T>> boolean deepEqualsIgnoreOrder(List<List<T>> c1, List<List<T>> c2) {
        if (c1.size() != c2.size())
            return false;

        for (List<T> ts : c1)
            Collections.sort(ts);
        for (List<T> ts : c2)
            Collections.sort(ts);
        return new HashSet<>(c1).equals(new HashSet<>(c2));
    }
}
