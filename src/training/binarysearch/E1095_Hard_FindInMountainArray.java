package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1095. 山脉数组中查找目标值: https://leetcode-cn.com/problems/find-in-mountain-array/
 *
 * 给你一个「山脉数组」mountainArr，请你返回能够使得 mountainArr.get(index) 等于 target 最小的下标 index 值。
 *
 * 何为山脉数组？如果数组 A 是一个山脉数组的话，那它满足如下条件：
 * - 首先，A.length >= 3
 * - 其次，在 0 < i < A.length - 1 条件下，存在 i 使得：
 *   - A[0] < A[1] < ... A[i-1] < A[i]
 *   - A[i] > A[i+1] > ... > A[A.length - 1]
 *
 * 你将不能直接访问该山脉数组，必须通过 MountainArray 接口来获取数据：
 * - MountainArray.get(k) - 会返回数组中索引为 k 的元素（下标从 0 开始）
 * - MountainArray.length() - 会返回该数组的长度
 *
 * 注意，对 MountainArray.get 发起超过 100 次调用的提交将被视为错误答案。
 *
 * 例 1：
 * 输入：array = [1,2,3,4,5,3,1], target = 3
 * 输出：2
 * 解释：3 在数组中出现了两次，下标分别为 2 和 5，我们返回最小的下标 2。
 *
 * 例 2：
 * 输入：array = [0,1,2,4,2,1], target = 3
 * 输出：-1
 * 解释：3 在数组中没有出现，返回 -1。
 *
 * 说明：
 * - 3 <= mountain_arr.length() <= 10000
 * - 0 <= target <= 10^9
 * - 0 <= mountain_arr.get(index) <= 10^9
 */
public class E1095_Hard_FindInMountainArray {

    public static class MountainArray {

        private final int[] array;

        public MountainArray(int[] array) {
            this.array = array;
        }

        public int get(int k) {
            return array[k];
        }

        public int length() {
            return array.length;
        }
    }

    public static void test(ToIntBiFunction<Integer, MountainArray> method) {
        assertEquals(2, method.applyAsInt(3, new MountainArray(new int[]{1,2,3,4,5,3,1})));
        assertEquals(-1, method.applyAsInt(3, new MountainArray(new int[]{0,1,2,4,2,1})));
        assertEquals(3, method.applyAsInt(1, new MountainArray(new int[]{0,5,3,1})));
        assertEquals(4, method.applyAsInt(0, new MountainArray(new int[]{3,5,3,2,0})));
    }

    /**
     * LeetCode 耗时：1 ms - 7.35%
     *          内存消耗：38.3 MB - 44.58%
     */
    public int findInMountainArray(int target, MountainArray mountainArr) {
        int n = mountainArr.length();
        // 首先找到最高峰
        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int elem = mountainArr.get(mid);
            // 注意下标
            if (mid - 1 >= 0 && elem > mountainArr.get(mid - 1)) {
                if (elem > mountainArr.get(mid + 1)) {
                    lo = mid;
                    break;
                } else {
                    lo = mid + 1;
                }
            } else {
                hi = mid - 1;
            }
        }

        // 写两个搜索函数会快 1ms
        int result = binarySearch(target, mountainArr, 0, lo, (mid, tar) -> mid < tar);
        return result != -1 ? result :
                binarySearch(target, mountainArr, lo + 1, n - 1, (mid, tar) -> mid > tar);
    }

    private int binarySearch(int target, MountainArray mountainArr, int lo, int hi,
                             BiPredicate<Integer, Integer> cmp) {
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int elem = mountainArr.get(mid);
            if (elem == target) {
                return mid;
            } else if (cmp.test(elem, target)) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return -1;
    }

    @Test
    public void testFindInMountainArray() {
        test(this::findInMountainArray);
    }
}
