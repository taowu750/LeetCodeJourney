package util.datastructure;

import java.util.Collection;
import java.util.HashSet;

public class CollectionUtil {

    public static <T> boolean setEquals(Collection<T> c1, Collection<T> c2) {
        return new HashSet<>(c1).equals(new HashSet<>(c2));
    }
}
