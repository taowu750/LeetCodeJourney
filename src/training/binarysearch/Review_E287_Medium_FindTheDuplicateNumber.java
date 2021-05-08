package training.binarysearch;

import org.junit.jupiter.api.Test;
import training.linkedlist.Review_E142_Medium_LinkedListCycleHead;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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
public class Review_E287_Medium_FindTheDuplicateNumber {

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
     * 换句话说，我们可以构造一个一一映射 f(index) = num。
     *
     * 例如，nums = [2, 1, 3]，它有映射 0->2, 1->1, 2->3。如果我们从索引 0 开始，
     * 我们可以根据此映射函数获得一个值，然后将该值用作新索引，然后再次根据该新索引获得另一个新值。
     * 我们重复此过程，直到索引超出数组为止。实际上，通过这样做，我们可以获得一个序列：
     * 0->2->3。
     *
     * 但是，如果数组中有重复项，则映射是多对一的，并且索引不会超出数组。
     * 例如，nums = [2,3,1,1]，它有映射 0->2, 1->3, {2,3}->1。那么我们得到的序列肯定有一个环：
     * 0->2->1->3->1->3->1->...。此环的起点是重复数字。
     *
     * 注意到 nums[0] 始终是具有重复数字的循环的入口，因为没有值可以跳回 nums[0]。
     *
     * 那么，解决问题的方法就和 {@link Review_E142_Medium_LinkedListCycleHead} 一样。
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
