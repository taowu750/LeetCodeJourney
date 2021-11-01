package training.pointer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

/**
 * 611. 有效三角形的个数: https://leetcode-cn.com/problems/valid-triangle-number/
 * <p>
 * 给定一个包含非负整数的数组，你的任务是统计其中可以组成三角形三条边的三元组个数。
 * <p>
 * 例 1：
 * 输入: [2,2,3,4]
 * 输出: 3
 * 解释:
 * 有效的组合是:
 * 2,3,4 (使用第一个 2)
 * 2,3,4 (使用第二个 2)
 * 2,2,3
 * <p>
 * 说明：
 * - 数组长度不超过1000。
 * - 数组里整数的范围为 [0, 1000]。
 */
public class E611_Medium_ValidTriangleNumber {

    public static void test(ToIntFunction<int[]> method) {
        Assertions.assertEquals(3, method.applyAsInt(new int[]{2, 2, 3, 4}));
        Assertions.assertEquals(0, method.applyAsInt(new int[]{7, 0, 0, 0}));
    }

    /**
     * LeetCode 耗时：75 ms - 56.89%
     * 内存消耗：37.9 MB - 92.90%
     */
    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);

        int n = nums.length, result = 0, i = 0;
        // 去掉 0
        while (i < n && nums[i] == 0) {
            i++;
        }
        // 先选择两个数，在从后面找到小于这两数之和的数
        for (; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int search = nums[i] + nums[j];
                int lo = j + 1, hi = n;
                while (lo < hi) {
                    int mid = (lo + hi) >>> 1;
                    if (nums[mid] < search) {
                        lo = mid + 1;
                    } else {
                        hi = mid;
                    }
                }
                /*
                当 lo 等于 n 时，表示 nums[i] + nums[j] 大于后面任意一个数，
                这样可以直接用等差数列求和公式算出后续结果
                 */
                if (lo == n) {
                    int num = n - j - 1, a1 = n - j - 1;
                    result += num * a1 - num * (num - 1) / 2;
                    break;
                } else if (lo > j + 1) {
                    result += lo - j - 1;
                }
            }
        }

        return result;
    }

    @Test
    public void testTriangleNumber() {
        test(this::triangleNumber);
    }


    /**
     * 双指针法。
     * <p>
     * LeetCode 耗时：26 ms - 100%
     * 内存消耗：38 MB - 91.20%
     */
    public int twoPointerMethod(int[] nums) {
        int count = 0;
        Arrays.sort(nums);
        for (int i = nums.length - 1; i >= 2; i--) {
            int target = nums[i];
            int left = 0, right = i - 1;
            while (left < right) {
                int sum = nums[left] + nums[right];
                if (sum <= target) {
                    left++;
                } else {
                    count += right - left;
                    right--;
                }
            }
        }
        return count;
    }

    @Test
    public void testTwoPointerMethod() {
        test(this::twoPointerMethod);
    }
}
