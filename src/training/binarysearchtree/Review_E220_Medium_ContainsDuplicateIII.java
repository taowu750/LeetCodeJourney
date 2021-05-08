package training.binarysearchtree;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriPredicate;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个整数数组 nums 和两个整数 k 和 t，如果数组中有两个不同的索引 i 和 j，
 * 使得 abs(nums[i]-nums[j])<=t && abs(i-j)<=k，则返回 true。
 * <p>
 * 例 1：
 * Input: nums = [1,2,3,1], k = 3, t = 0
 * Output: true
 * <p>
 * 例 2：
 * Input: nums = [1,0,1,1], k = 1, t = 2
 * Output: true
 * <p>
 * 例 3：
 * Input: nums = [1,5,9,1,5,9], k = 2, t = 3
 * Output: false
 * <p>
 * 约束：
 * - 0 <= nums.length <= 2 * 10**4
 * - -2**31 <= nums[i] <= 2**31 - 1
 * - 0 <= k <= 10**4
 * - 0 <= t <= 2**31 - 1
 * - i != j
 */
public class Review_E220_Medium_ContainsDuplicateIII {

    static void test(TriPredicate<int[], Integer, Integer> method) {
        assertTrue(method.test(new int[]{1, 2, 3, 1}, 3, 0));

        assertTrue(method.test(new int[]{1, 0, 1, 1}, 1, 2));

        assertFalse(method.test(new int[]{1, 5, 9, 1, 5, 9}, 2, 3));
    }

    /**
     * 类似于基数排序，使用区间桶来划分数字。假设我们有连续的桶覆盖 nums 的范围，每个桶的宽度为 t+1。
     * 如果有两项差值 <=t，则会出现以下两种情况之一：
     * - 这两项在同一个桶中
     * - 这两项在相邻的桶中
     *
     * LeetCode 耗时：12ms - 92.72%
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        if (nums.length <= 1 || k == 0)
            return false;

        long width = (long)t + 1;
        // 使用 HashMap 作为桶
        Map<Long, Long> buckets = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            long bucketID = getBucketID(nums[i], width);
            if (buckets.containsKey(bucketID))
                return true;
            Long value;
            if ((value = buckets.get(bucketID - 1)) != null && Math.abs(nums[i] - value) < width)
                return true;
            if ((value = buckets.get(bucketID + 1)) != null && Math.abs(nums[i] - value) < width)
                return true;
            buckets.put(bucketID, (long)nums[i]);
            if (i >= k)
                buckets.remove(getBucketID(nums[i - k], width));
        }

        return false;
    }

    private long getBucketID(long num, long width) {
        // 假设 width = 3，如果负数也直接 num/width，那么 -1、-2 也会被映射到 0 桶中；
        // 如果只是 num/width - 1，那么 -3 就会被映射到 -2 桶中。
        // 因此，只有当 (num+1)/width - 1 才能保证正确。
        // 相当于先向右偏移 num，再向左偏移 id。
        return num < 0 ? (num + 1) / width - 1 : num / width;
    }

    @Test
    public void testContainsNearbyAlmostDuplicate() {
        test(this::containsNearbyAlmostDuplicate);
    }


    /**
     * 使用 TreeSet 的算法，时间复杂度为 O(N logK)。
     *
     * LeetCode 耗时：22ms - 71.24%
     */
    public boolean treeMethod(int[] nums, int k, int t) {
        if (nums.length <= 1 || k == 0)
            return false;

        TreeSet<Long> treeSet = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            Long min = treeSet.ceiling((long)nums[i] - t);
            // 不需要 flooring((long)nums[i] + t)，因为下面的判断包含了这种情况
            if (min != null && min <= (long)nums[i] + t)
                return true;
            treeSet.add((long) nums[i]);
            if (i >= k)
                treeSet.remove((long) nums[i - k]);
        }

        return false;
    }

    @Test
    public void testTreeMethod() {
        test(this::treeMethod);
    }
}
