package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个 int 数组，检查是否存在两个数字 N 和 M，其中 N = 2 * M。
 * <p>
 * 例 1：
 * Input: arr = [10,2,5,3]
 * Output: true
 * <p>
 * 例 2：
 * Input: arr = [7,1,14,11]
 * Output: true
 * <p>
 * 例 3：
 * Input: arr = [3,1,7,11]
 * Output: false
 * <p>
 * 约束：
 * - 2 <= arr.length <= 500
 * - -10^3 <= arr[i] <= 10^3
 */
public class E1346_Easy_CheckIfNAndItsDoubleExist {

    static void test(Predicate<int[]> method) {
        int[] arr = {10, 2, 5, 3};
        assertTrue(method.test(arr));

        arr = new int[]{7, 1, 14, 11};
        assertTrue(method.test(arr));

        arr = new int[]{3, 1, 7, 11};
        assertFalse(method.test(arr));

        arr = new int[]{-10, 12, -20, -8, 15};
        assertTrue(method.test(arr));

        arr = new int[]{-2, 0, 10, -19, 4, 6, -8};
        assertFalse(method.test(arr));
    }

    /**
     * 注意考虑 0 和复制。
     *
     * 此题有更加的做法，使用 HashSet
     */
    public boolean checkIfExist(int[] arr) {
        Arrays.sort(arr);
        int len = arr.length;
        for (int i = 0; i < len - 1; i++) {
            int num = arr[i];
            if (num >= 0 && Arrays.binarySearch(arr, i + 1, len, num * 2) >= 0)
                return true;
            else if (num < 0 && i > 0 && Arrays.binarySearch(arr, 0, i, num * 2) >= 0)
                return true;
        }

        return false;
    }

    @Test
    public void testCheckIfExist() {
        test(this::checkIfExist);
    }


    public boolean hashSetMethod(int[] arr) {
        Set<Integer> set = new HashSet<>();
        for (int num : arr) {
            if (set.contains(num * 2) || (num % 2 == 0 && set.contains(num / 2)))
                return true;
            set.add(num);
        }

        return false;
    }

    @Test
    public void testHashSetMethod() {
        test(this::hashSetMethod);
    }
}
