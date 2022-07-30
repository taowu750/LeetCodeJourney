package training.binarysearch;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 658. 找到 K 个最接近的元素: https://leetcode-cn.com/problems/find-k-closest-elements/
 *
 * 给定一个排序的整数数组 arr，两个整数 k 和 x，返回数组中最接近 x 的 k 个整数。
 * 结果也应按升序排序。
 * <p>
 * 在下列情况下，整数 a 比整数 b 更接近 x：
 * - |a - x| < |b - x|
 * - |a - x| == |b - x| and a < b
 * <p>
 * 例 1：
 * Input: arr = [1,2,3,4,5], k = 4, x = 3
 * Output: [1,2,3,4]
 * <p>
 * 例 2：
 * Input: arr = [1,2,3,4,5], k = 4, x = -1
 * Output: [1,2,3,4]
 * <p>
 * 约束：
 * - 1 <= k <= arr.length
 * - 1 <= arr.length <= 10**4
 * - arr 以升序排序
 * - -10**4 <= arr[i], x <= 10**4
 */
public class E658_Medium_FindKClosestElements {

    public static void test(TriFunction<int[], Integer, Integer, List<Integer>> method) {
        assertEquals(method.apply(new int[]{1, 2, 3, 4, 5}, 4, 3), asList(1, 2, 3, 4));

        assertEquals(method.apply(new int[]{1, 2, 3, 4, 5}, 4, -1), asList(1, 2, 3, 4));

        assertEquals(method.apply(new int[]{1, 2, 2, 3, 3, 3, 4, 6, 7, 8, 8}, 3, -10),
                asList(1, 2, 2));

        assertEquals(method.apply(new int[]{1, 2, 2, 3, 3, 3, 4, 6, 7, 8, 8}, 5, 1),
                asList(1, 2, 2, 3, 3));

        assertEquals(method.apply(new int[]{1, 2, 2, 3, 3, 3, 4, 6, 7, 8, 8}, 4, 2),
                asList(1, 2, 2, 3));

        assertEquals(method.apply(new int[]{1, 2, 2, 3, 3, 3, 4, 6, 7, 8, 8}, 4, 3),
                asList(2, 3, 3, 3));

        assertEquals(method.apply(new int[]{1, 2, 2, 3, 3, 3, 4, 6, 7, 8, 8}, 4, 5),
                asList(3, 3, 4, 6));

        assertEquals(method.apply(new int[]{1, 2, 2, 3, 3, 3, 4, 6, 7, 8, 8}, 4, 8),
                asList(6, 7, 8, 8));

        assertEquals(method.apply(new int[]{1, 2, 2, 3, 3, 3, 4, 6, 7, 8, 8}, 4, 10),
                asList(6, 7, 8, 8));
    }

    /**
     * LeetCode 耗时：3 ms - 99.62%
     *          内存消耗：40.1 MB - 48.83%
     */
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        int idx = Arrays.binarySearch(arr, x), left, right;
        if (idx >= 0) {
            left = idx - 1;
            right = idx + 1;
        } else {
            idx = -idx - 1;
            left = idx - 1;
            right = idx;
        }

        while (right - left - 1 < k) {
            if (left < 0 || (right < arr.length && Math.abs(x - arr[left]) > Math.abs(x - arr[right]))) {
                right++;
            } else {
                left--;
            }
        }

        // 这比用 ArrayList 少了 1ms
        Integer[] result = new Integer[k];
        for (int i = left + 1; i < right; i++) {
            result[i - left - 1] = arr[i];
        }

        return Arrays.asList(result);
    }

    @Test
    public void testFindClosestElements() {
        test(this::findClosestElements);
    }


    /**
     * 二分查找结合滑动窗口。
     *
     * LeetCode 耗时：1ms - 100%
     */
    public List<Integer> betterMethod(int[] arr, int k, int x) {
        /*
        假设 A[mid] ~ A[mid + k] 是滑动窗口

        case 1: x - A[mid] < A[mid + k] - x, 需要向左移动窗口
        -------x----A[mid]-----------------A[mid + k]----------

        case 2: x - A[mid] < A[mid + k] - x, 需要向左移动窗口
        -------A[mid]----x-----------------A[mid + k]----------

        case 3: x - A[mid] > A[mid + k] - x, 需要向右移动窗口
        -------A[mid]------------------x---A[mid + k]----------

        case 4: x - A[mid] > A[mid + k] - x, 需要向右移动窗口
        -------A[mid]---------------------A[mid + k]----x------
        */
        int lo = 0, hi = arr.length - k;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (x - arr[mid] > arr[mid + k] - x)
                lo = mid + 1;
            else
                hi = mid;
        }
        List<Integer> result = new ArrayList<>(k);
        for (int i = lo; i < lo + k; i++)
            result.add(arr[i]);
        return result;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
