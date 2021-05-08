package training.binarysearch;

/**
 * 找到第一个（最左边）匹配 target 的元素下标。
 */
public class BinarySearchII {

    /**
     * 循环不变式：
     * - 如果 target 存在初始数组 [0, n-1]，那么它一定在 [lo, hi] 中。
     * - 如果存在，则有 nums[lo] <= target && nums[hi] >= target
     *
     * 初始化：
     * - 第一轮循环开始之前，处理的数组是[0, n-1]，这时显然成立。
     *
     * 保持：
     * - 如果 nums[mid] < target，那么 target 只可能存在于 [mid + 1, hi] 中。
     *   此时数组大小减少 mid + 1 - lo，至少减少了 1。
     * - 否则 nums[mid] >= target，nums[mid] 是 [mid, hi] 中第一个大于等于
     *   target 的元素。此时数组大小减少 hi - mid。根据 while 的条件，
     *   只有当 hi==mid 时减少量才为 0，此时 hi==left，循环结束。
     *
     * 终止：
     * - 此时 lo == hi，由循环不变式，如果存在，则 target <= num[lo] <= target，
     *   则 nums[lo] == target。否则，target 不存在于数组中。
     */
    public int search(int[] nums, int target) {
        if(nums == null || nums.length == 0)
            return -1;

        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (nums[mid] < target)
                lo = mid + 1;
            else
                hi = mid;
        }
        return nums[lo] == target ? lo : -1;
    }
}
