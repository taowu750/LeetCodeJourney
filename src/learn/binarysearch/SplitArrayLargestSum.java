package learn.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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
public class SplitArrayLargestSum {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(method.applyAsInt(new int[]{7, 2, 5, 10, 8}, 2), 18);

        assertEquals(method.applyAsInt(new int[]{1, 2, 3, 4, 5}, 2), 9);

        assertEquals(method.applyAsInt(new int[]{1, 4, 4}, 3), 4);

        assertEquals(method.applyAsInt(new int[]{10, 5, 13, 4, 8, 4, 5, 11, 14, 9, 16, 10, 20, 8}, 8), 25);
    }

    /**
     * 答案是在输入数组数的最大值和这些数的和之间。
     * 使用二分查找搜索正确答案。令 lo=最大值, hi=数组和。每次我们令 mid=(lo+hi)/2。
     *
     * 每次缩小左右边界：
     * 1. 从左到右切分数组，尽可能切分 m 次。
     * 2. 尽可能确保每两次切割之间的数字总和足够大，但仍小于 mid。
     * 3. 切分将得到两个结果：要么我们可以将数组分成 m 个以上的子数组，要么我们不能。
     *    - 如果我们可以，这意味着我们选择的 mid 太小，因为我们已经尽了最大努力确保每个部分包含尽可能多的数字，
     *      但我们仍然有剩余的数字。因此，不可能将数组分成 m 个部分，并且确保每个部分都不大于 mid，我们应该增加 m，
     *      也就是 lo=mid+1；
     *    - 如果我们不能，要么我们成功地将数组分成 m 个部分，每个部分的和小于 mid，要么我们在到达 m 之前用完了所有的数字。
     *      这都意味着我们应该降低 mid，因为我们需要找到最小的一个最大和。这导致 hi=mid-1（或 hi=mid，根据结束条件决定）。
     *
     * 为了更好的理解边界条件，请做如下思考：
     * 在使用二分查找时，像是在 FFFFTTTTTTTTTTTT 中找到第一个 T（T 表示 true，而 F 表示 false）。
     * 1. 当使用 lo <= hi 时
     *    - 当 mid 是 F 时，第一个 T 肯定在右边, 所以 lo = mid+1。
     *    - 当 mid 是 T 时，就像{@link BinarySearch}中的那样，令 hi = mid-1。
     *    结束时，会得到如下结果：
     *    FFFFFTTTTTT
     *    ....↑↑
     *    ....rl
     *    因此，我们返回 l
     * 2. 当使用 lo < hi 时
     *    - 当 mid 是 F 时，第一个 T 肯定在右边, 所以 lo = mid+1。
     *    - 当 mid 是 T 时，就像{@link BinarySearchII}中的那样，令 hi = mid。
     *    结束时，会得到如下结果：
     *    FFFFTTTTTTT
     *    ....↑
     *    ....l&&r
     *
     * LeetCode 耗时：0ms - 100%
     */
    public int splitArray(int[] nums, int m) {
        int sum = 0;
        for (int i = 0; i < nums.length; i++)
            sum += nums[i];
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > max)
                max = nums[i];
        }
        if (m == 1)
            return sum;
        if (m == nums.length)
            return max;

        int lo = max, hi = sum;
        // 改成 lo < hi, hi = mid 也可以
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (valid(nums, mid, m))
                hi = mid - 1;
            else
                lo = mid + 1;
        }

        return lo;
    }

    // 如果不能将数组分成 m 个以上的子数组，返回 true
    private boolean valid(int[] nums, int target, int m) {
        int cnt = 1, sum = 0;
        for (int num : nums) {
            sum += num;
            if (sum > target) {
                sum = num;
                cnt++;
                if (cnt > m)
                    return false;
            }
        }
        return true;
    }

    @Test
    public void testSplitArray() {
        test(this::splitArray);
    }
}
