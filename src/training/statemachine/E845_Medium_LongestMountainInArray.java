package training.statemachine;

import org.junit.jupiter.api.Test;
import training.pointer.E42_Hard_TrappingRainWater;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 845. 数组中的最长山脉: https://leetcode-cn.com/problems/longest-mountain-in-array/
 *
 * 把符合下列属性的数组 arr 称为山脉数组：
 * - arr.length >= 3
 * - 存在下标 i（0 < i < arr.length - 1），满足
 *   - arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
 *   - arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
 *
 * 进阶：
 * - 你可以仅用一趟扫描解决此问题吗？
 * - 你可以用 O(1) 空间解决此问题吗？
 *
 * 例 1：
 * 输入：arr = [2,1,4,7,3,2,5]
 * 输出：5
 * 解释：最长的山脉子数组是 [1,4,7,3,2]，长度为 5。
 *
 * 例 2：
 * 输入：arr = [2,2,2]
 * 输出：0
 * 解释：不存在山脉子数组。
 *
 * 说明：
 * - 1 <= arr.length <= 10^4
 * - 0 <= arr[i] <= 10^4
 */
public class E845_Medium_LongestMountainInArray {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(5, method.applyAsInt(new int[]{2,1,4,7,3,2,5}));
        assertEquals(0, method.applyAsInt(new int[]{2,2,2}));
        assertEquals(3, method.applyAsInt(new int[]{0, 2, 0, 2, 1, 2, 3, 4, 4, 1}));
    }

    /**
     * 动态规划方法，和 {@link E42_Hard_TrappingRainWater#dpMethod(int[])} 思想类似。
     *
     * 枚举山顶。参见：
     * https://leetcode-cn.com/problems/longest-mountain-in-array/solution/shu-zu-zhong-de-zui-chang-shan-mai-by-leetcode-sol/
     *
     * LeetCode 耗时：2 ms - 99.68%
     *          内存消耗：39.4 MB - 65.10%
     */
    public int longestMountain(int[] arr) {
        // left[i] 表示 i 左边上升序列的长度
        int[] left = new int[arr.length];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[i - 1]) {
                left[i] = left[i - 1] + 1;
            }
        }
        int result = 0;
        for (int i = arr.length - 2, right = 0; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {
                right++;
                if (left[i] > 0) {
                    result = Math.max(result, left[i] + right + 1);
                }
            } else {
                right = 0;
            }
        }

        return result;
    }

    @Test
    public void testLongestMountain() {
        test(this::longestMountain);
    }


    /**
     * 一次遍历，不使用额外空间。这个算法类似于一个状态机。
     *
     * LeetCode 耗时：2 ms - 99.68%
     *          内存消耗：39.5 MB - 48.22%
     */
    public int stateMachineMethod(int[] arr) {
        final int n = arr.length;
        // state: 0 初始化/水平；1 上升；2 下降
        int state = 0, ans = 0;
        for (int i = 1, len = 1; i < n; i++) {
            switch (state) {
                case 0:
                    if (arr[i] > arr[i - 1]) {
                        state = 1;
                        len++;
                    }
                    break;

                case 1:
                    if (arr[i] > arr[i - 1]) {
                        len++;
                    } else if (arr[i] == arr[i - 1]) {
                        state = 0;
                        len = 1;
                    } else {
                        state = 2;
                        ans = Math.max(ans, ++len);
                    }
                    break;

                case 2:
                    if (arr[i] > arr[i - 1]) {
                        len = 2;
                        state = 1;
                    } else if (arr[i] == arr[i - 1]) {
                        len = 1;
                        state = 0;
                    } else {
                        ans = Math.max(ans, ++len);
                    }
                    break;
            }
        }

        return ans;
    }

    @Test
    public void testStateMachineMethod() {
        test(this::stateMachineMethod);
    }


    /**
     * 双指针法，参见：
     * https://leetcode-cn.com/problems/longest-mountain-in-array/solution/shu-zu-zhong-de-zui-chang-shan-mai-by-leetcode-sol/
     *
     * 枚举山脚。
     *
     * 我们使用指针 left 指向左侧山脚，它的初始值为 0。每次当我们固定 left 时：
     * - 我们首先需要保证 left+2 < n，这是因为山脉的长度至少为 3；
     *   其次我们需要保证 arr[left] < arr[left+1]，否则 left 对应的不可能时左侧山脚；
     * - 我们将右侧山脚的 right 的初始值置为 left+1，随后不断地向右移动 right，
     *   直到不满足 arr[right] < arr[right+1] 为止，此时：
     *   - 如果 right=n−1，说明我们已经移动到了数组末尾，已经无法形成山脉了；
     *   - 否则，right 指向的可能是山顶。我们需要额外判断是有满足 arr[right]>arr[right+1]，这是因为如果两者相等，
     *     那么 right 指向的就不是山顶了。
     * - 如果 right 指向的确实是山顶，那么我们使用类似的方法，不断地向右移动 right，直到不满足 arr[right]>arr[right+1] 为止，
     *   此时，right 指向右侧山脚，arr[left] 到 arr[right] 就对应着一座山脉；
     * - 需要注意的是，右侧山脚有可能是下一个左侧山脚，因此我们需要将 right 的值赋予 left，以便与进行下一次枚举。
     *   在其它所有不满足要求的情况下，right 对应的位置都不可能是下一个左侧山脚，因此可以将 right+1 的值赋予 left。
     *
     * LeetCode 耗时：2 ms - 99.68%
     *          内存消耗：39.5 MB - 48.22%
     */
    public int twoPointerMethod(int[] arr) {
        int left = 0, right, n = arr.length, result = 0;
        while (left + 2 < n)  {
            if (arr[left] >= arr[left + 1]) {
                left++;
                continue;
            }
            right = left + 1;
            while (right < n - 1 && arr[right] < arr[right + 1]) {
                right++;
            }
            if (right == n - 1 || arr[right] == arr[right + 1]) {
                left = right + 1;
            } else {
                while (right < n - 1 && arr[right] > arr[right + 1]) {
                    right++;
                }
                result = Math.max(result, right - left + 1);
                left = right;
            }
        }

        return result;
    }

    @Test
    public void testTwoPointerMethod() {
        test(this::twoPointerMethod);
    }
}
