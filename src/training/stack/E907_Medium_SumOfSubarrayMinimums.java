package training.stack;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 907. 子数组的最小值之和: https://leetcode-cn.com/problems/sum-of-subarray-minimums/
 *
 * 给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
 * 就是求 arr 所有子数组中，每个子数组最小值的和。
 *
 * 由于答案可能很大，因此返回答案模 10^9 + 7。
 *
 * 例 1：
 * 输入：arr = [3,1,2,4]
 * 输出：17
 * 解释：
 * 子数组为 [3]，[1]，[2]，[4]，[3,1]，[1,2]，[2,4]，[3,1,2]，[1,2,4]，[3,1,2,4]。
 * 最小值为 3，1，2，4，1，1，2，1，1，1，和为 17。
 *
 * 例 2：
 * 输入：arr = [11,81,94,43,3]
 * 输出：444
 *
 * 说明：
 * - 1 <= arr.length <= 3 * 10^4
 * - 1 <= arr[i] <= 3 * 10^4
 */
public class E907_Medium_SumOfSubarrayMinimums {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(17, method.applyAsInt(new int[]{3,1,2,4}));
        assertEquals(444, method.applyAsInt(new int[]{11,81,94,43,3}));
    }

    /**
     * 超时，在递增数组上为 O(n^2)
     */
    public int sumSubarrayMins(int[] arr) {
        /*
        从左到右遍历，下标设为 i。每次遍历到一个 i，就加上以这个 i 结尾的所有子数组的最小值。
        从左到右遍历时，维持一个单增栈（从栈底到栈顶单增），记录下标，这样就能根据栈中的内容知道所有最小值
         */

        // 让队尾作为栈底，方便后面的遍历
        Deque<Integer> stack = new LinkedList<>();
        final int MOD = 1000000007;
        int result = 0;
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.getLast()] >= arr[i]) {
                stack.removeLast();
            }
            stack.addLast(i);

            int lastIdx = -1;
            // 栈中每个最小值下标 idx，是 (lastIdx, idx] 范围内以 idx 结尾的子数组的最小值
            for (int idx : stack) {
                result = (result + ((idx - lastIdx) * arr[idx]) % MOD) % MOD;
                lastIdx = idx;
            }
        }

        return result;
    }

    @Test
    public void testSumSubarrayMins() {
        test(this::sumSubarrayMins);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/sum-of-subarray-minimums/solution/dan-diao-zhan-zuo-you-liang-bian-di-yi-g-ww3n/
     *
     * 这道题的本质在于找到数组中的每一个数作为最小值的范围，比如对于某个数nums[i]能够最小值以这种形式表示：
     * 左边连续m个数比nums[i]大，右边连续n个数比nums[i]大。以 nums[i] 为最小值的子数组数量就等于：
     *      m * n + m + n + 1 = (m + 1) * (n + 1)
     *
     * 因此，当前目标是找到每一个数nums[i]的左右两边第一个小于等于的数nums[prev]，这两个数之间的数则均是大于nums[i]，
     * 这样便可以计算出m和n。「在一个数组中找左右两边第一个大于、小于、等于的数这种问题可以使用单调栈来解决」。
     *
     * 在这里，单调栈的具体使用如下：
     * - 当当前数nums[i] <= nums[stk.top()]，触发计算；
     *   - 因为此时栈顶比栈里栈顶下一个元素大，比当前数大，那么栈顶就是在这两个数之间作为最小值的一个范围
     *   - 栈顶下一个元素就是前一个比栈顶小的数，当前数就是后一个比栈顶小的元素
     * - 分别计算栈顶元素到前一个小的元素和到后一个小的元素「之间」有多少元素，这些元素肯定都比栈顶大
     * - 那么栈顶元素作为最小值的子数组数量就可以得知
     *
     * LeetCode 耗时：19 ms - 75.07%
     *          内存消耗：45.6 MB - 63.30%
     */
    public int rangeMethod(int[] arr) {
        // 单增栈（从栈底到栈顶单增）
        Deque<Integer> stack = new LinkedList<>();
        final int MOD = 1000000007;
        long result = 0;
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                int idx = stack.pop(), prev = stack.isEmpty() ? -1 : stack.peek();
                int m = idx - prev - 1, n = i - idx - 1;
                // 需要转成 long 防止数字溢出
                result = (result + ((m + 1) * (n + 1) * (long) arr[idx]) % MOD) % MOD;
            }
            stack.push(i);
        }
        // 计算剩余的元素
        while (!stack.isEmpty()) {
            int idx = stack.pop(), prev = stack.isEmpty() ? -1 : stack.peek();
            int m = idx - prev - 1, n = arr.length - idx - 1;
            // 需要转成 long 防止数字溢出
            result = (result + ((m + 1) * (n + 1) * (long) arr[idx]) % MOD) % MOD;
        }

        return (int) result;
    }

    @Test
    public void testRangeMethod() {
        test(this::rangeMethod);
    }
}
