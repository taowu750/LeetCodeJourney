package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个整数数组 nums 和一个整数 target，返回两个数字的索引，使它们相加等于 target。
 *
 * 给定的每个输入正好有一个解决方案，并且不能两次使用同一个元素。按照从小到大的顺序返回下标。
 *
 * 你可以只使用 O(n) 空间复杂度，O(n) 时间复杂度吗？
 *
 * 例 1：
 * Input: nums = [2,7,11,15], target = 9
 * Output: [0,1]
 *
 * 例 2：
 * Input: nums = [3,2,4], target = 6
 * Output: [1,2]
 *
 * 例 3：
 * Input: nums = [3,3], target = 6
 * Output: [0,1]
 *
 * 约束：
 * - 2 <= nums.length <= 10**3
 * - -10**9 <= nums[i] <= 10**9
 * - -10**9 <= target <= 10**9
 * - 只有一个答案
 */
public class E1_Easy_TwoSum {

    static void test(BiFunction<int[], Integer, int[]> method) {
        assertArrayEquals(method.apply(new int[]{2,7,11,15}, 9), new int[]{0,1});

        assertArrayEquals(method.apply(new int[]{3,2,4}, 6), new int[]{1,2});

        assertArrayEquals(method.apply(new int[]{3,3}, 6), new int[]{0,1});
    }

    /**
     * LeetCode 耗时：0ms - 100%
     *          内存消耗：38.9MB - 92.80%
     */
    public int[] twoSum(int[] nums, int target) {
        // 不要一开始就把所有元素添加到 map 中
        Map<Integer, Integer> map = new HashMap<>((int) (nums.length / 0.75) + 1);
        for (int i = 0; i < nums.length; i++) {
            int j = map.getOrDefault(target - nums[i], i);
            if (j != i)
                return new int[]{Math.min(i, j), Math.max(i, j)};
            else
                map.put(nums[i], i);
        }
        return null;
    }

    @Test
    public void testTwoSum() {
        test(this::twoSum);
    }
}
