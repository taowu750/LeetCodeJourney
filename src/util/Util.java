package util;

import java.util.Arrays;
import java.util.HashSet;

public class Util {

    public static <T> boolean in(T actual, T... expected) {
        boolean result = new HashSet<>(Arrays.asList(expected)).contains(actual);
        if (!result) {
            throw new AssertionError("actual=" + actual);
        }

        return true;
    }
}
