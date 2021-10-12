package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 410. 分割数组的最大值: https://leetcode-cn.com/problems/split-array-largest-sum/
 *
 * 给定由非负整数组成的数组 nums 和一个整数 m，需要将数组拆分为 m 个非空连续子数组。
 * 写一个算法来最小化这 m 个子数组中的最大和。
 * <p>
 * 例 1：
 * Input: nums = [7,2,5,10,8], m = 2
 * Output: 18
 * Explanation:
 * 有四种方法可以将 nums 拆分为两个子数组（注意连续性）。最好的办法是把它分成 [7,2,5] 和 [10,8] 两部分，
 * 其中两个数组中最大的和为 18。
 * <p>
 * 例 2：
 * Input: nums = [1,2,3,4,5], m = 2
 * Output: 9
 * <p>
 * 例 3：
 * Input: nums = [1,4,4], m = 3
 * Output: 4
 * <p>
 * 约束：
 * - 1 <= nums.length <= 1000
 * - 0 <= nums[i] <= 10**6
 * - 1 <= m <= min(50, nums.length)
 */
public class E410_Hard_SplitArrayLargestSum {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(method.applyAsInt(new int[]{7, 2, 5, 10, 8}, 2), 18);

        assertEquals(method.applyAsInt(new int[]{1, 2, 3, 4, 5}, 2), 9);

        assertEquals(method.applyAsInt(new int[]{1, 4, 4}, 3), 4);

        assertEquals(method.applyAsInt(new int[]{10, 5, 13, 4, 8, 4, 5, 11, 14, 9, 16, 10, 20, 8}, 8), 25);
    }

    /**
     * LeetCode 耗时：0ms - 100%
     *          内存消耗：35.7 MB - 95.31%
     */
    public int splitArray(int[] nums, int m) {
        int lo = 0, hi = 0;
        for (int num : nums) {
            if (lo < num) {
                lo = num;
            }
            hi += num;
        }

        /*
        下面的循环和 README 中的“寻找右侧边界的二分查找”很类似，只不过 high 取在数值范围内。
        因为所需要寻找的下标范围就在 [max, sum] 中，所以 high 需要这样选取。

        mid 足够切分时，mid 还有可能是最后的目标，所以 high = mid；
        mid 不能切分时，mid 不可能是最后的目标了，因此 low = mid + 1

        这种模式的核心想法就是 mid 这个元素在左半边和右半边还用得上吗？
         */
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (check(nums, m, mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return lo;
    }

    // 如果不能将数组分成 m 个以上的子数组，返回 true
    private boolean check(int[] nums, int m, int limit) {
        int cur = 0;
        for (int num : nums) {
            if (cur + num > limit) {
                if (--m == 0) {
                    return false;
                }
                cur = num;
            } else {
                cur += num;
            }
        }

        return true;
    }

    @Test
    public void testSplitArray() {
        test(this::splitArray);
    }
}
