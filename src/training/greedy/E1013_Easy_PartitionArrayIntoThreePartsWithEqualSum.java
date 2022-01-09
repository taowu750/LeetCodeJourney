package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 1013. 将数组分成和相等的三个部分: https://leetcode-cn.com/problems/partition-array-into-three-parts-with-equal-sum/
 *
 * 给你一个整数数组 arr，只有可以将其划分为三个和相等的「非空」部分时才返回 true，否则返回 false。
 *
 * 形式上，如果可以找出索引 i + 1 < j 且满足
 * (arr[0] + arr[1] + ... + arr[i] == arr[i + 1] + arr[i + 2] + ... + arr[j - 1] == arr[j] + arr[j + 1] + ... + arr[arr.length - 1])
 * 就可以将数组三等分。
 *
 * 例 1：
 * 输入：arr = [0,2,1,-6,6,-7,9,1,2,0,1]
 * 输出：true
 * 解释：0 + 2 + 1 = -6 + 6 - 7 + 9 + 1 = 2 + 0 + 1
 *
 * 例 2：
 * 输入：arr = [0,2,1,-6,6,7,9,-1,2,0,1]
 * 输出：false
 *
 * 例 3：
 * 输入：arr = [3,3,6,5,-2,2,5,1,-9,4]
 * 输出：true
 * 解释：3 + 3 = 6 = 5 - 2 + 2 + 5 + 1 - 9 + 4
 *
 * 说明：
 * - 3 <= arr.length <= 5 * 10^4
 * - -10^4 <= arr[i] <= 10^4
 */
public class E1013_Easy_PartitionArrayIntoThreePartsWithEqualSum {

    public static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{0,2,1,-6,6,-7,9,1,2,0,1}));
        assertFalse(method.test(new int[]{0,2,1,-6,6,7,9,-1,2,0,1}));
        assertTrue(method.test(new int[]{3,3,6,5,-2,2,5,1,-9,4}));
        assertTrue(method.test(new int[]{0, 0, 0, 0}));
        assertFalse(method.test(new int[]{1, -1, 1, -1}));
    }

    /**
     * 要注意 0 的情况。
     *
     * LeetCode 耗时：9 ms - 11.14%
     *          内存消耗：51.9 MB - 5.04%
     */
    public boolean canThreePartsEqualSum(int[] arr) {
        int sum = 0;
        for (int a : arr) {
            sum += a;
        }
        if (sum % 3 != 0) {
            return false;
        }

        return dfs(arr, 0, sum / 3, 0, 0);
    }

    private boolean dfs(int[] arr, int i, int part, int sum, int stage) {
        if (i == arr.length) {
            return stage == 3;
        }
        sum += arr[i];
        if (sum == part && dfs(arr, i + 1, part, 0, stage + 1)) {
            return true;
        }
        return dfs(arr, i + 1, part, sum, stage);
    }

    @Test
    public void testCanThreePartsEqualSum() {
        test(this::canThreePartsEqualSum);
    }


    /**
     * 贪心法。参见：
     * https://leetcode-cn.com/problems/partition-array-into-three-parts-with-equal-sum/solution/1013-jiang-shu-zu-fen-cheng-he-xiang-deng-de-san-2/
     *
     * 首先我们需要找出索引 i。具体地，我们从第一个元素开始遍历数组 A 并对数组中的数进行累加。
     * 当累加的和等于 sum(A) / 3 时，我们就将当前的位置置为索引 i。由于数组中的数有正有负，我们可能会得到若干个索引
     * i0, i1, i2, ...，从 A[0] 到这些索引的数之和均为 sum(A) / 3。那么我们应该选取那个索引呢？直觉告诉我们，
     * 应该贪心地选择最小的那个索引 i0，这也是可以证明的：假设最终的答案中我们选取了某个不为 i0 的索引 ik 以及另一个索引 j，
     * 那么根据上面的两条要求，有：
     * - A[0] + A[1] + ... + A[ik] = sum(A) / 3;
     * - A[0] + A[1] + ... + A[j] = sum(A) / 3 * 2 且 j > ik。
     *
     * 然而 i0 也是满足第一条要求的一个索引，因为 A[0] + A[1] + ... + A[i0] = sum(A) / 3 并且 j > ik > i0，
     * 我们可以将 ik 替换为 i0，因此选择最小的那个索引是合理的。
     *
     * 需要注意，找到 j 后，只要 j 后面还有元素，那么就可以返回 true 了。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：46.1 MB - 39.92%
     */
    public boolean greedyMethod(int[] arr) {
        int sum = 0;
        for (int a : arr) {
            sum += a;
        }
        if (sum % 3 != 0) {
            return false;
        }

        int stage = 0, part = sum / 3, i = 0, s = 0;
        for (; i < arr.length; i++) {
            s += arr[i];
            if (s == part) {
                if (++stage == 2) {
                    break;
                }
                s = 0;
            }
        }

        return stage == 2 && i < arr.length - 1;
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
