package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 992. K 个不同整数的子数组: https://leetcode-cn.com/problems/subarrays-with-k-different-integers/
 *
 * 给定一个正整数数组 A，如果 A 的某个子数组中不同整数的个数「恰好」为 K，则称 A 的这个连续、不一定不同的子数组为好子数组。
 * （例如，[1,2,3,1,2] 中有 3 个不同的整数：1，2，以及 3。）
 * 返回 A 中好子数组的数目。
 *
 * 例 1：
 * 输入：A = [1,2,1,2,3], K = 2
 * 输出：7
 * 解释：恰好由 2 个不同整数组成的子数组：[1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2].
 *
 * 例 2：
 * 输入：A = [1,2,1,3,4], K = 3
 * 输出：3
 * 解释：恰好由 3 个不同整数组成的子数组：[1,2,1,3], [2,1,3], [1,3,4].
 *
 * 约束：
 * - 1 <= A.length <= 20000
 * - 1 <= A[i] <= A.length
 * - 1 <= K <= A.length
 */
public class E992_Hard_SubarraysWithKDifferentIntegers {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(7, method.applyAsInt(new int[]{1,2,1,2,3}, 2));
        assertEquals(3, method.applyAsInt(new int[]{1,2,1,3,4}, 3));
        assertEquals(15, method.applyAsInt(new int[]{1,1,1,1,1}, 1));
        assertEquals(7, method.applyAsInt(new int[]{1,2,1,1,1}, 2));
        assertEquals(8, method.applyAsInt(new int[]{2,1,1,1,2}, 1));
    }

    /**
     * 超时。
     */
    public int subarraysWithKDistinct(int[] nums, int k) {
        int max = 0;
        for (int num : nums) {
            if (num > max) {
                max = num;
            }
        }

        int[] counts = new int[max + 1];
        int result = 0, distinct = 0;
        for (int left = 0; left <= nums.length - k; left++) {
            for (int i = left; i < nums.length; i++) {
                if (counts[nums[i]]++ == 0) {
                    distinct++;
                }
            }

            for (int right = nums.length - 1; right - left + 1 >= k; right--) {
                if (distinct == k) {
                    result++;
                }
                if (counts[nums[right]]-- == 1) {
                    distinct--;
                }
            }

            Arrays.fill(counts, 0);
            distinct = 0;
        }

        return result;
    }

    @Test
    public void testSubarraysWithKDistinct() {
        test(this::subarraysWithKDistinct);
    }


    /**
     * 「恰好」包含 K 个不同整数的子数组的个数
     * = 「最多」包含 K 个不同整数的子数组的个数
     * - 「最多」包含 K - 1 个不同整数的子数组的个数
     *
     * 参见：https://leetcode-cn.com/problems/subarrays-with-k-different-integers/solution/k-ge-bu-tong-zheng-shu-de-zi-shu-zu-by-l-ud34/
     *
     * LeetCode 耗时：5 ms - 78.40%
     *          内存消耗：42.1 MB - 30.88%
     */
    public int slideWindowMethod(int[] nums, int k) {
        int max = 0;
        for (int num : nums) {
            if (num > max) {
                max = num;
            }
        }

        return atMostKDistinct(nums, k, max) - atMostKDistinct(nums, k - 1, max);
    }

    /**
     * 计算「最多」包含 K 个不同整数的子数组的个数。
     *
     * 其中「最多」包含 K 个不同整数的子数组的个数
     * = 「恰好」包含 1 个不同整数的子数组的个数
     * + 「恰好」包含 2 个不同整数的子数组的个数
     * + ...
     * + 「恰好」包含 K 个不同整数的子数组的个数
     */
    private int atMostKDistinct(int[] nums, int k, int max) {
        int[] counts = new int[max + 1];
        int result = 0, left = 0, right = 0, distinct = 0;
        while (right < nums.length) {
            int num = nums[right++];
            if (counts[num]++ == 0) {
                distinct++;
            }

            // 当超过 k 时，改变 left
            while (distinct > k) {
                if (counts[nums[left++]]-- == 1) {
                    distinct--;
                }
            }

            /*
            由于上面的循环，保证 [left, right) 不同整数个数小于等于 K。
            当满足条件的子数组从 [A,B,C] 增加到 [A,B,C,D] 时，新子数组的长度为4，
            同时增加的子数组为 [D],[C,D],[B,C,D],[A,B,C,D] 也为4。
             */
            result += right - left;
        }

        return result;
    }

    @Test
    public void testSlideWindowMethod() {
        test(this::slideWindowMethod);
    }
}
