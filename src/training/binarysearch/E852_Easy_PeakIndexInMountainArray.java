package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 852. 山脉数组的峰顶索引: https://leetcode-cn.com/problems/peak-index-in-a-mountain-array/
 *
 * 符合下列属性的数组 arr 称为山脉数组：
 * - arr.length >= 3
 * - 存在 i（0 < i < arr.length - 1）使得：
 *   - arr[0] < arr[1] < ... arr[i-1] < arr[i]
 *   - arr[i] > arr[i+1] > ... > arr[arr.length - 1]
 *
 * 给你由整数组成的山脉数组 arr ，返回下标 i 。
 *
 * 例 1：
 * 输入：arr = [0,1,0]
 * 输出：1
 *
 * 例 2：
 * 输入：arr = [0,2,1,0]
 * 输出：1
 *
 * 例 3：
 * 输入：arr = [0,10,5,2]
 * 输出：1
 *
 * 例 4：
 * 输入：arr = [3,4,5,1]
 * 输出：2
 *
 * 例 5：
 * 输入：arr = [24,69,100,99,79,78,67,36,26,19]
 * 输出：2
 *
 * 说明：
 * - 3 <= arr.length <= 10^4
 * - 0 <= arr[i] <= 10^6
 * - 题目数据保证 arr 是一个山脉数组
 */
public class E852_Easy_PeakIndexInMountainArray {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(1, method.applyAsInt(new int[]{0,1,0}));
        assertEquals(1, method.applyAsInt(new int[]{0,2,1,0}));
        assertEquals(1, method.applyAsInt(new int[]{0,10,5,2}));
        assertEquals(2, method.applyAsInt(new int[]{3,4,5,1}));
        assertEquals(2, method.applyAsInt(new int[]{24,69,100,99,79,78,67,36,26,19}));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：41.9 MB - 5.05%
     */
    public int peakIndexInMountainArray(int[] arr) {
        int lo = 0, hi = arr.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (mid > 0 && mid < arr.length - 1) {
                if (arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]) {
                    return mid;
                } else if (arr[mid] > arr[mid - 1]) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            } else if (mid == 0) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return -1;
    }

    @Test
    public void testPeakIndexInMountainArray() {
        test(this::peakIndexInMountainArray);
    }


    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：41.9 MB - 5.05%
     */
    public int betterMethod(int[] arr) {
        final int n = arr.length;
        int l = 0, r = n - 1;
        // 不断缩小范围，找最大的数即可
        while (l < r) {
            int mid = (l + r) >>> 1;
            if (arr[mid] < arr[mid + 1]) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }

        return l;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }


    /**
     * 三分法。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：41.9 MB - 5.05%
     */
    public int triMethod(int[] arr) {
        int lo = 1, hi = arr.length - 2;
        while (lo < hi) {
            int lmid = (lo + hi) >>> 1, rmid = lmid + 1;
            if (arr[lmid] <= arr[rmid]) {
                lo = rmid;
            } else {
                hi = lmid;
            }
        }

        return lo;
    }

    @Test
    public void testTriMethod() {
        test(this::triMethod);
    }
}
