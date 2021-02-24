package learn.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.ArrayUtil.setEquals;

/**
 * 给定两个数组，编写一个函数来计算它们的交集。
 * <p>
 * 例 1：
 * Input: nums1 = [1,2,2,1], nums2 = [2,2]
 * Output: [2]
 * <p>
 * 例 2：
 * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 * Output: [9,4]
 * <p>
 * 注意：
 * - 结果中的每个元素都必须是唯一的。
 * - 结果可以是任何顺序。
 */
public class IntersectionOfTwoArrays {

    static void test(BiFunction<int[], int[], int[]> method) {
        assertTrue(setEquals(method.apply(new int[]{1, 2, 2, 1}, new int[]{2, 2}),
                new int[]{2}));

        assertTrue(setEquals(method.apply(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4}),
                new int[]{9, 4}));

        assertTrue(setEquals(method.apply(new int[]{4, 9, 5}, new int[]{3, 7, 6}),
                new int[]{}));
    }

    /**
     * 使用内置数据结构。
     *
     * LeetCode 耗时：4ms - 31.51%
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        HashSet<Integer> nums1Set = new HashSet<>(nums1.length);
        for (int i : nums1)
            nums1Set.add(i);
        HashSet<Integer> nums2Set = new HashSet<>(nums2.length);
        for (int i : nums2)
            nums2Set.add(i);
        nums1Set.retainAll(nums2Set);

        return nums1Set.stream().mapToInt(i -> i).toArray();
    }

    @Test
    public void testIntersection() {
        test(this::intersection);
    }


    /**
     * LeetCode 耗时：1ms - 99.74%
     */
    public int[] betterSetMethod(int[] nums1, int[] nums2) {
        if (nums1.length == 0 || nums2.length == 0)
            return new int[0];
        HashSet<Integer> set = new HashSet<>();
        for (int i : nums1)
            set.add(i);
        int idx = 0;
        for (int i : nums2) {
            if (set.contains(i)) {
                // 使用 nums1，节省空间
                nums1[idx++] = i;
                set.remove(i);
            }
        }

        return Arrays.copyOf(nums1, idx);
    }

    @Test
    public void testBetterSetMethod() {
        test(this::betterSetMethod);
    }


    /**
     * 基数方法，此方法只适用于数字最大值不会很大，且没有负数的情况。
     *
     * LeetCode 耗时：0ms - 100%
     */
    public int[] radixMethod(int[] nums1, int[] nums2) {
        if (nums1.length == 0 || nums2.length == 0)
            return new int[0];
        int max = Integer.MIN_VALUE;
        for (int i : nums1) {
            if (i > max)
                max = i;
        }
        int[] freq = new int[max + 1];
        int idx = 0;
        int[] temp = new int[Math.min(max, nums2.length)];
        for (int i : nums1) {
            freq[i] = 1;
        }
        for (int i : nums2) {
            if (i <= max && freq[i] != 0) {
                freq[i] = 0;
                temp[idx++] = i;
            }
        }

        return Arrays.copyOf(temp, idx);
    }

    @Test
    public void testRadixMethod() {
        test(this::radixMethod);
    }
}
