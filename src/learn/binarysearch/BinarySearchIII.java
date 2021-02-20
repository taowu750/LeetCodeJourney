package learn.binarysearch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 找到最后一个（最右边）匹配 target 的元素下标。
 */
public class BinarySearchIII {

    /**
     * 错误写法如下：
     *     int lo = 0, hi = nums.length - 1;
     *     while (lo < hi) {
     *         int mid = (lo + hi) >>> 1;
     *         if (nums[mid] > target)
     *             hi = mid - 1;
     *         else
     *             lo = mid;
     *     }
     *     return nums[lo] == target ? lo : -1;
     * 需要注意的是，但 nums[mid] <= target，数组大小减以 mid-lo。
     * 而但 lo == mid 时，lo < hi 仍可以成立，但此时数组大小减少 0，
     * 条件并不会推进，导致死循环。
     *
     * 为了消除死循环，我们需要将循环改写为下面的形式：
     *     while (lo < hi) {
     *         int mid = (lo + hi) >>> 1;
     *         if (nums[mid] > target)
     *             hi = mid - 1;
     *         else if (nums[mid] == target) {
     *             if (lo == mid) {
     *                 if (nums[hi] == target)
     *                     return hi;
     *                 else
     *                     return lo;
     *             } else
     *                 left = mid;
     *         } else
     *             lo = mid + 1;
     *     }
     *
     *
     *
     * 下面是更加简洁的代码。
     * 循环不变式：
     * - 如果 target 存在初始数组 [0, n-1]，那么它一定在 [lo, hi] 中。
     * - 如果存在，则有 nums[lo] <= target && nums[hi] >= target
     *
     * 保持：
     * - nums[mid] <= target，那么 mid >= 最后等于 target 的元素的下标，
     *   目标元素存在于 [mid, hi] 中。数组大小减少 mid - lo。根据 while 条件，
     *   如果 lo == mid 减少量为 0，此时 hi - lo <= 1，循环结束。
     *   这样避免了死循环。
     * - nums[mid] > target，那么目标元素存在于 [lo, mid - 1] 中。
     *   数组大小减少 hi - mid + 1，至少减少 1。
     *
     * 终止：
     * - 此时 lo <= hi，如果存在，根据循环不变式，nums[hi] 或 nums[lo]
     *   是要找的元素。否则不存在。
     */
    public int search(int[] nums, int target) {
        if(nums == null || nums.length == 0)
            return -1;

        int lo = 0, hi = nums.length - 1;
        while (hi - lo > 1){
            int mid = (lo + hi) >>> 1;
            if (nums[mid] <= target)
                lo = mid;
            else
                hi = mid - 1;
        }

        if(nums[hi] == target) return hi;
        if(nums[lo] == target) return lo;

        return -1;
    }

    @Test
    public void testSearch() {
        int[] arr = new int[]{1,1,2,2,2,3,4,5,6,6,7,7,7,7,7,8,9,9};

        assertEquals(search(arr, 1), 1);
        assertEquals(search(arr, 2), 4);
        assertEquals(search(arr, 3), 5);
        assertEquals(search(arr, 4), 6);
        assertEquals(search(arr, 5), 7);
        assertEquals(search(arr, 6), 9);
        assertEquals(search(arr, 7), 14);
        assertEquals(search(arr, 8), 15);
        assertEquals(search(arr, 9), 17);
        assertEquals(search(arr, 0), -1);
        assertEquals(search(arr, -10), -1);
        assertEquals(search(arr, 10), -1);
        assertEquals(search(arr, 100), -1);
    }
}
