package training.binarysearch;

import org.junit.jupiter.api.Test;
import training.linkedlist.Review_E142_Medium_LinkedListCycleHead;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 287. 寻找重复数: https://leetcode-cn.com/problems/find-the-duplicate-number/
 *
 * 给定一个整数数组 nums 包含 n + 1 个整数，其中每个整数都在 [1，n] 范围内。
 * nums 中只有一个重复的数字，请返回此重复的数字。
 * <p>
 * 你可以不修改 nums 数组解决问题吗？
 * 你可以仅使用常数 O(1) 的额外空间来解决问题吗？
 * 你可以写出时间复杂度小于 O(n**2) 的实现吗？
 * <p>
 * 例 1：
 * Input: nums = [1,3,4,2,2]
 * Output: 2
 * <p>
 * 例 2：
 * Input: nums = [3,1,3,4,2]
 * Output: 3
 * <p>
 * 例 3：
 * Input: nums = [1,1]
 * Output: 1
 * <p>
 * 例 4：
 * Input: nums = [1,1,2]
 * Output: 1
 * <p>
 * 约束：
 * - 2 <= n <= 3 * 10**4
 * - nums.length == n + 1
 * - 1 <= nums[i] <= n
 * - 所有在 nums 中的整数仅出现一次，但唯独会有一个整数出现两次或多次。
 */
public class E287_Medium_FindTheDuplicateNumber {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{1, 3, 4, 2, 2}), 2);

        assertEquals(method.applyAsInt(new int[]{3, 1, 3, 4, 2}), 3);

        assertEquals(method.applyAsInt(new int[]{1, 1}), 1);

        assertEquals(method.applyAsInt(new int[]{2, 1, 1}), 1);

        assertEquals(method.applyAsInt(new int[]{2, 2, 2, 2, 2}), 2);
    }

    /**
     * 此题的解题思路和 {@link Review_E142_Medium_LinkedListCycleHead}（寻找环开头）类似。
     * 如果数组中没有重复项，我们可以将每个索引映射到该数组中的每个数字。
     *
     * 参见：
     * https://leetcode-cn.com/problems/find-the-duplicate-number/solution/kuai-man-zhi-zhen-de-jie-shi-cong-damien_undoxie-d/
     *
     * LeetCode 耗时：0ms - 100%
     */
    public int findDuplicate(int[] nums) {
        int slow = nums[0], fast = nums[nums[0]];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[nums[fast]];
        }
        slow = 0;
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }

        return slow;
    }

    @Test
    public void testFindDuplicate() {
        test(this::findDuplicate);
    }


    /**
     * 二分查找方法。令 count 为 1...mid 范围内的元素数。
     *
     * 如果 cnt > mid，那么在 lo...mid 范围内有不止 mid 个元素，因此该范围包含重复项。
     * 如果 cnt <= mid，则在 mid+1...hi 范围内有不止 n-mid 个元素，因此该范围包含重复项。
     */
    public int binarySearch(int[] nums) {
        /*
        下面的循环和 README 中的“寻找右侧边界的二分查找”很类似，只不过 lo、hi 取在数值范围内。
        因为所需要寻找的目标范围就在 [1, n] 中，所以 lo、hi 需要这样选取。

        重复数在左半部分的时候，mid 还有可能是最后的目标，所以 hi = mid；
        最小值在右半部分的时候，mid 不可能是最后的目标了，因此 low = mid + 1

        这种模式的核心想法就是 mid 这个元素在左半边和右半边还用得上吗？
         */
        int lo = 1, hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            int cnt = 0;
            for (int num : nums) {
                if (num <= mid)
                    cnt++;
            }
            if (cnt <= mid)
                lo = mid + 1;
            else
                hi = mid;
        }

        return lo;
    }

    @Test
    public void testBinarySearch() {
        test(this::binarySearch);
    }
}
