package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.ArrayUtil.equalsIgnoreOrder;

/**
 * 给定两个数组，编写一个函数来计算它们的交集。结果中的每个元素数量应与它出现在两个数组中的共同次数相等。
 * 结果可以是任何顺序。
 * <p>
 * 例 1：
 * Input: nums1 = [1,2,2,1], nums2 = [2,2]
 * Output: [2,2]
 * <p>
 * 例 2：
 * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 * Output: [4,9]
 */
public class E350_Easy_IntersectionOfTwoArraysII {

    static void test(BiFunction<int[], int[], int[]> method) {
        assertTrue(equalsIgnoreOrder(method.apply(new int[]{1, 2, 2, 1}, new int[]{2, 2}),
                new int[]{2, 2}));

        assertTrue(equalsIgnoreOrder(method.apply(new int[]{4,9,5}, new int[]{9,4,9,8,4}),
                new int[]{4,9}));

        assertTrue(equalsIgnoreOrder(method.apply(new int[]{4,9,5}, new int[]{7,3,6}),
                new int[0]));
    }

    /**
     * LeetCode 耗时：2ms - 95.60%
     */
    public int[] intersect(int[] nums1, int[] nums2) {
        if (nums1.length == 0 || nums2.length == 0)
            return new int[0];

        Map<Integer, Integer> freq = new HashMap<>(nums1.length);
        for (int i : nums1)
            freq.merge(i, 1, Integer::sum);
        int idx = 0;
        for (int i : nums2) {
            // 这比使用 get 加 remove 判断快 1 ms
            if (freq.containsKey(i)) {
                int f = freq.get(i);
                if (f != 0) {
                    freq.put(i, f - 1);
                    nums1[idx++] = i;
                }
            }
        }

        return Arrays.copyOf(nums1, idx);
    }

    @Test
    public void testIntersect() {
        test(this::intersect);
    }


    /**
     * LeetCode 耗时：1ms - 100%
     */
    public int[] sortMethod(int[] nums1, int[] nums2) {
        if (nums1.length == 0 || nums2.length == 0)
            return new int[0];

        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int[] intersection = new int[Math.min(nums1.length, nums2.length)];
        int idx = 0;
        for (int i = 0, j = 0; i < nums1.length && j < nums2.length;) {
            if (nums1[i] == nums2[j]) {
                intersection[idx++] = nums1[i];
                i++;
                j++;
            } else if (nums1[i] < nums2[j])
                i++;
            else
                j++;
        }

        return Arrays.copyOf(intersection, idx);
    }

    @Test
    public void testSortMethod() {
        test(this::sortMethod);
    }
}
