package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 581. 最短无序连续子数组: https://leetcode-cn.com/problems/shortest-unsorted-continuous-subarray/
 *
 * 给你一个整数数组 nums ，你需要找出一个「连续子数组」，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
 * 请你找出符合题意的「最短」子数组，并输出它的长度。
 *
 * 你可以设计一个时间复杂度为 O(n) 的解决方案吗？
 *
 * 例 1：
 * 输入：nums = [2,6,4,8,10,9,15]
 * 输出：5
 * 解释：你只需要对 [6, 4, 8, 10, 9] 进行升序排序，那么整个表都会变为升序排序。
 *
 * 例 2：
 * 输入：nums = [1,2,3,4]
 * 输出：0
 *
 * 例 3：
 * 输入：nums = [1]
 * 输出：0
 *
 * 约束：
 * - 1 <= nums.length <= 10^4
 * - -10^5 <= nums[i] <= 10^5
 */
public class E581_Medium_ShortestUnsortedContinuousSubarray {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(5, method.applyAsInt(new int[]{2,6,4,8,10,9,15}));
        assertEquals(0, method.applyAsInt(new int[]{1,2,3,4}));
        assertEquals(0, method.applyAsInt(new int[]{1}));
        assertEquals(3, method.applyAsInt(new int[]{2,3,3,2,4}));
        assertEquals(0, method.applyAsInt(new int[]{1,2,3,3,3}));
        assertEquals(3, method.applyAsInt(new int[]{1,2,4,5,3}));
        assertEquals(5, method.applyAsInt(new int[]{1,3,11,4,5,6,7}));
        assertEquals(4, method.applyAsInt(new int[]{1,3,5,2,4}));
        assertEquals(5, method.applyAsInt(new int[]{5,4,3,2,1}));
    }

    /**
     * LeetCode 耗时：6 ms - 53.50%
     *          内存消耗：39.3MB - 79.43%
     */
    public int findUnsortedSubarray(int[] nums) {
        // 找到所有不重叠逆序对区间，最前面逆序对的头和最后面逆序对的尾、连起来的数组就是最短无序连续子数组

        // 递减栈
        Deque<Integer> stack = new LinkedList<>();
        // 需排序区域头尾
        int sortStart = -1, sortEnd = -1, min = Integer.MAX_VALUE;
        // 从后往前遍历，并记录最小值；如果出现逆序对，则需要更新 sortStart、sortEnd
        for (int i = nums.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
                if (nums[stack.peek()] < nums[i]
                        // 当 nums[i] 等于递增栈顶（也就是 nums[i+1]），则需要看看递增栈顶的数是不是需排序区域头
                        || sortStart == stack.peek()) {
                    sortEnd = Math.max(sortEnd, stack.remove() + 1);
                    sortStart = i;
                } else {
                    break;
                }
            }
            stack.push(i);
            // 如果 nums[i] 大于右边的最小值，那么表示有一个逆序对
            if (nums[i] > min) {
                sortStart = i;
            }
            min = Math.min(min, nums[i]);
        }

        return sortEnd - sortStart;
    }

    @Test
    public void testFindUnsortedSubarray() {
        test(this::findUnsortedSubarray);
    }


    /**
     * 设数组有三段组成 numsA、numsB、numsC，其中 numsB 使我们要找最短无序连续子数组。
     * 设 numsB 范围为 [left, right]。
     *
     * 因此有 numsA 中每一个数 nums_i <= min(nums[i+1..n-1])。
     * 我们可以从大到小枚举 i，用一个变量 minn 记录 min(nums[i+1..n-1])，
     * 这样最后一个使得不等式不成立的 i 即为 left。
     *
     * 同理有 numsC 中每一个数 nums_i >= max(nums[0,i-1])。
     * 我们可以从小到大枚举 i，用一个变量 maxn 记录 max(nums[0,i-1])，
     * 这样最后一个使得不等式不成立的 i 即为 right。
     *
     * 参见：https://leetcode-cn.com/problems/shortest-unsorted-continuous-subarray/solution/zui-duan-wu-xu-lian-xu-zi-shu-zu-by-leet-yhlf/
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：39.7 MB - 58.56%
     */
    public int twoPointMethod(int[] nums) {
        int n = nums.length;
        int maxn = Integer.MIN_VALUE, right = -1;
        int minn = Integer.MAX_VALUE, left = -1;
        for (int i = 0; i < n; i++) {
            if (maxn > nums[i]) {
                right = i;
            } else {
                maxn = nums[i];
            }
            if (minn < nums[n - i - 1]) {
                left = n - i - 1;
            } else {
                minn = nums[n - i - 1];
            }
        }
        return right == -1 ? 0 : right - left + 1;
    }

    @Test
    public void testTwoPointMethod() {
        test(this::twoPointMethod);
    }
}