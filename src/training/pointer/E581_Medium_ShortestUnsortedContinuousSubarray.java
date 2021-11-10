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

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(5, method.applyAsInt(new int[]{2,6,4,8,10,9,15}));
        assertEquals(0, method.applyAsInt(new int[]{1,2,3,4}));
        assertEquals(0, method.applyAsInt(new int[]{1}));
        assertEquals(3, method.applyAsInt(new int[]{2,3,3,2,4}));
        assertEquals(0, method.applyAsInt(new int[]{1,2,3,3,3}));
        assertEquals(3, method.applyAsInt(new int[]{1,2,4,5,3}));
        assertEquals(5, method.applyAsInt(new int[]{1,3,11,4,5,6,7}));
        assertEquals(4, method.applyAsInt(new int[]{1,3,5,2,4}));
        assertEquals(5, method.applyAsInt(new int[]{5,4,3,2,1}));
        assertEquals(4, method.applyAsInt(new int[]{1,3,5,4,2}));
    }

    /**
     * 利用单调栈和逆序对思想。
     *
     * LeetCode 耗时：6 ms - 53.50%
     *          内存消耗：39.3MB - 79.43%
     */
    public int findUnsortedSubarray(int[] nums) {
        // 找到所有不重叠逆序对区间，最前面逆序对的头和最后面逆序对的尾、连起来的数组就是最短无序连续子数组。

        // 递减栈，使用它找到最后面逆序对的尾
        Deque<Integer> stack = new LinkedList<>();
        // 需排序区域头尾（其中 sortEnd 是实际下标+1）
        int sortStart = -1, sortEnd = -1, min = Integer.MAX_VALUE;
        // 从后往前遍历，并记录最小值；如果出现逆序对，则需要更新 sortStart、sortEnd
        // 从栈顶到栈底就是数组尾部的递增序列
        for (int i = nums.length - 1; i >= 0; i--) {
            // 当 nums[i] 大于递增栈顶，表明出现逆序对，此时更新 sortEnd
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                sortEnd = Math.max(sortEnd, stack.remove() + 1);
            }
            stack.push(i);
            // 如果 nums[i] 大于右边的最小值，那么表示有一个逆序对，此时更新 sortStart
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
     * 这样最后一个使得不等式不成立的 i（注意上面的不等式里是 i+1）即为 left。
     *
     * 同理有 numsC 中每一个数 nums_i >= max(nums[0,i-1])。
     * 我们可以从小到大枚举 i，用一个变量 maxn 记录 max(nums[0,i-1])，
     * 这样最后一个使得不等式成立的 i 即为 right。
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
        // 注意，下面 maxn 从左到右遍历，minn 从右到左遍历
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


    /**
     * 自己想出来的一个易懂直接的指针方法。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：39.8 MB - 37.49%
     */
    public int selfDirectMethod(int[] nums) {
        int prefixEnd = 0, suffixStart = nums.length - 1;
        // 找到左边开始最长的递增序列
        while (prefixEnd < nums.length - 1 && nums[prefixEnd] <= nums[prefixEnd + 1]) {
            prefixEnd++;
        }
        // 找到右边开始最长的递减序列
        while (suffixStart > 0 && nums[suffixStart] >= nums[suffixStart - 1]) {
            suffixStart--;
        }
        // 单增序列，无需排序
        if (prefixEnd >= suffixStart) {
            return 0;
        }

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        if (prefixEnd < suffixStart - 1) {
            // 计算中间区域的最小、最大值
            for (int i = prefixEnd + 1; i < suffixStart; i++) {
                if (min > nums[i]) {
                    min = nums[i];
                }
                if (max < nums[i]) {
                    max = nums[i];
                }
            }
        } else {
            // 没有中间区域，则将 prefixEnd 和 suffixStart 作为中间区域
            min = nums[suffixStart];
            max = nums[prefixEnd];
            prefixEnd--;
            suffixStart++;
        }

        // 中间区域向两边扩展，只有当中间区域的最小值大于等于左边的最大值、
        // 中间区域的最大值小于等于右边区域的最小值时，我们就找到了最小的需排序子数组
        while (prefixEnd >= 0 || suffixStart < nums.length) {
            if (prefixEnd < 0) {
                if (max > nums[suffixStart]) {
                    suffixStart++;
                } else {
                    break;
                }
            } else if (suffixStart >= nums.length) {
                if (min < nums[prefixEnd]) {
                    prefixEnd--;
                } else {
                    break;
                }
            } else {
                if (max <= nums[suffixStart] && min >= nums[prefixEnd]) {
                    break;
                }
                if (max > nums[suffixStart]) {
                    suffixStart++;
                }
                if (min < nums[prefixEnd]) {
                    prefixEnd--;
                }
                // 注意更新中间区域最大最小值
                max = Math.max(max, nums[prefixEnd + 1]);
                min = Math.min(min, nums[suffixStart - 1]);
            }
        }

        return suffixStart - prefixEnd - 1;
    }

    @Test
    public void testSelfDirectMethod() {
        test(this::selfDirectMethod);
    }
}
