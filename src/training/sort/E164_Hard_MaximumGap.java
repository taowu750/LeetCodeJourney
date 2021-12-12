package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 164. 最大间距: https://leetcode-cn.com/problems/maximum-gap/
 *
 * 给定一个无序的数组，找出数组在排序之后，相邻元素之间最大的差值。
 * 如果数组元素个数小于 2，则返回 0。
 *
 * 例 1：
 * 输入: [3,6,9,1]
 * 输出: 3
 * 解释: 排序后的数组是 [1,3,6,9], 其中相邻元素 (3,6) 和 (6,9) 之间都存在最大差值 3。
 *
 * 例 2：
 * 输入: [10]
 * 输出: 0
 * 解释: 数组元素个数小于 2，因此返回 0。
 *
 * 说明：
 * - 你可以假设数组中所有元素都是非负整数，且数值在 32 位有符号整数范围内。
 * - 请尝试在线性时间复杂度和空间复杂度的条件下解决此问题。
 */
public class E164_Hard_MaximumGap {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(3, method.applyAsInt(new int[]{3,6,9,1}));
        assertEquals(0, method.applyAsInt(new int[]{10}));
        assertEquals(9999999, method.applyAsInt(new int[]{1,10000000}));
    }


    /**
     * 基数排序。
     *
     * LeetCode 耗时：33 ms - 82.11%
     *          内存消耗：52.6 MB - 33.77%
     */
    public int maximumGap(int[] nums) {
        if (nums.length < 2) {
            return 0;
        }

        // 计算 nums 中最大数的位数
        int max = 0;
        for (int num : nums) {
            max = Math.max(max, num);
        }
        int digit = 0;
        while (max > 0) {
            max /= 10;
            digit++;
        }

        // 基数排序
        int[] radixCount = new int[10], tmp = new int[nums.length];
        for (int d = 0, divisor = 1; d < digit; d++) {
            // 统计每个桶中的记录数
            for (int num : nums) {
                radixCount[num / divisor % 10]++;
            }
            // 每个桶更新为该位数在 nums 数组中的最大下标+1
            for (int i = 1; i < 10; i++) {
                radixCount[i] += radixCount[i - 1];
            }
            // 将所有桶中记录依次收集到tmp中
            for (int i = nums.length - 1; i >= 0; i--) {
                int k = nums[i] / divisor % 10;
                tmp[radixCount[k] - 1] = nums[i];
                radixCount[k]--;
            }
            // 将临时数组的内容复制到 nums 中
            System.arraycopy(tmp, 0, nums, 0, nums.length);
            divisor *= 10;
            // 清空 radixCount
            Arrays.fill(radixCount, 0);
        }

        int maxGap = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            maxGap = Math.max(maxGap, nums[i + 1] - nums[i]);
        }

        return maxGap;
    }

    @Test
    public void testMaximumGap() {
        test(this::maximumGap);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/maximum-gap/solution/zui-da-jian-ju-by-leetcode-solution/
     *
     * 桶排序。相对于基数排序，此方法也可以对负数使用。
     *
     * LeetCode 耗时：42 ms - 62.57%
     *          内存消耗：49.8 MB - 82.33%
     */
    public int bucketSortMethod(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return 0;
        }
        int minVal = Arrays.stream(nums).min().getAsInt();
        int maxVal = Arrays.stream(nums).max().getAsInt();
        int d = Math.max(1, (maxVal - minVal) / (n - 1));
        int bucketSize = (maxVal - minVal) / d + 1;

        int[][] bucket = new int[bucketSize][2];
        for (int i = 0; i < bucketSize; ++i) {
            Arrays.fill(bucket[i], -1); // 存储 (桶内最小值，桶内最大值) 对， (-1, -1) 表示该桶是空的
        }
        for (int num : nums) {
            int idx = (num - minVal) / d;
            if (bucket[idx][0] == -1) {
                bucket[idx][0] = bucket[idx][1] = num;
            } else {
                bucket[idx][0] = Math.min(bucket[idx][0], num);
                bucket[idx][1] = Math.max(bucket[idx][1], num);
            }
        }

        int ret = 0;
        int prev = -1;
        for (int i = 0; i < bucketSize; i++) {
            if (bucket[i][0] == -1) {
                continue;
            }
            if (prev != -1) {
                ret = Math.max(ret, bucket[i][0] - bucket[prev][1]);
            }
            prev = i;
        }
        return ret;
    }

    @Test
    public void testBucketSortMethod() {
        test(this::bucketSortMethod);
    }
}
