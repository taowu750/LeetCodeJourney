package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给两个整数数组 A 和 B ，返回两个数组中公共的、长度最长的子数组的长度。
 *
 * 例 1：
 * 输入：
 * A: [1,2,3,2,1]
 * B: [3,2,1,4,7]
 * 输出：3
 * 解释：
 * 长度最长的公共子数组是 [3, 2, 1] 。
 *
 * 约束：
 * - 1 <= len(A), len(B) <= 1000
 * - 0 <= A[i], B[i] < 100
 */
public class E718_Medium_MaximumLengthOfRepeatedSubarray {

    static void test(ToIntBiFunction<int[], int[]> method) {
        assertEquals(method.applyAsInt(new int[]{1,2,3,2,1}, new int[]{3,2,1,4,7}), 3);
        assertEquals(method.applyAsInt(new int[]{0,1,1,1,1}, new int[]{1,0,1,0,1}), 2);
        assertEquals(method.applyAsInt(new int[]{1,0,0,0,1}, new int[]{1,0,0,1,1}), 3);
    }

    /**
     * 注意这是求最长「子数组」，要求连续。
     *
     * LeetCode 耗时：80 ms - 11.48%
     *          内存消耗：47.3 MB - 43.52%
     */
    public int dpMethod(int[] A, int[] B) {
        final int n = A.length, m = B.length;
        // (i,j) 表示 A 中 i 开头 和 B 中 j 开头的最长公共子数组的长度。
        // (i,j) 依赖 (i+1,j+1)、(i+1,j)、(i,j+1)
        int[][] dp = new int[n + 1][m + 1];

        int max = 0;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = m - 1; j >= 0; j--) {
                if (A[i] == B[j])
                    dp[i][j] = dp[i + 1][j + 1] + 1;
                // A[i] != B[j]，dp[i][j] 隐含为 0
                max = Math.max(max, dp[i][j]);
            }
        }

        return max;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * LeetCode 耗时：53 ms - 70.45%
     *          内存消耗：38.4 MB - 77.03%
     */
    public int compressMethod(int[] A, int[] B) {
        final int n = A.length, m = B.length;
        int[] dp = new int[m + 1];

        int max = 0;
        for (int i = n - 1; i >= 0; i--) {
            // 从左到右遍历，因为左边的状态依赖右边的
            for (int j = 0; j < m; j++) {
                if (A[i] == B[j])
                    dp[j] = dp[j + 1] + 1;
                else
                    // 注意要置 0
                    dp[j] = 0;
                max = Math.max(max, dp[j]);
            }
        }

        return max;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
