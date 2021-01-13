package learn.array;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个非负整数数组 A，将 A 中偶数放在前面，奇数放在后面。数字之间的顺序没有要求。
 * <p>
 * 例 1：
 * Input: [3,1,2,4]
 * Output: [2,4,3,1]
 * Explanation: [4,2,3,1], [2,4,1,3], [4,2,1,3] 都可以
 * <p>
 * 约束：
 * - 1 <= A.length <= 5000
 * - 0 <= A[i] <= 5000
 */
public class SortArrayByParity {

    static void test(Function<int[], int[]> method) {
        int[] arr = {3, 1, 2, 4};
        int[] result = method.apply(arr);
        for (int i = 0; i < 2; i++) {
            assertTrue(result[i] == 2 || result[i] == 4);
        }
        for (int i = 2; i < 4; i++) {
            assertTrue(result[i] == 1 || result[i] == 3);
        }

        arr = new int[]{2, 4, 6, 8};
        result = method.apply(arr);
        for (int i = 0; i < arr.length; i++) {
            assertTrue(result[i] == 2 || result[i] == 4 || result[i] == 6 || result[i] == 8);
        }

        arr = new int[]{1, 3, 5, 7};
        result = method.apply(arr);
        for (int i = 0; i < arr.length; i++) {
            assertTrue(result[i] == 1 || result[i] == 3 || result[i] == 5 || result[i] == 7);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public int[] sortArrayByParity(int[] A) {
        int len = A.length;
        for (int i = -1, j = len; ; ) {
            while (i < len - 1 && A[++i] % 2 == 0) ;
            while (j > 0 && A[--j] % 2 == 1) ;

            if (i >= j)
                break;
            int tmp = A[i];
            A[i] = A[j];
            A[j] = tmp;
        }

        return A;
    }

    @Test
    public void testSortArrayByParity() {
        test(this::sortArrayByParity);
    }
}
