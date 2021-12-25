package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.IntPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 633. 平方数之和: https://leetcode-cn.com/problems/sum-of-square-numbers/
 *
 * 给定一个非负整数 c ，你要判断是否存在两个整数 a 和 b，使得 a^2 + b^2 = c 。
 *
 * 例 1：
 * 输入：c = 5
 * 输出：true
 * 解释：1 * 1 + 2 * 2 = 5
 *
 * 例 2：
 * 输入：c = 3
 * 输出：false
 *
 * 例 3：
 * 输入：c = 4
 * 输出：true
 *
 * 例 4：
 * 输入：c = 2
 * 输出：true
 *
 * 例 5：
 * 输入：c = 1
 * 输出：true
 *
 * 说明：
 * - 0 <= c <= 2^31 - 1
 */
public class E633_Medium_SumOfSquareNumbers {

    public static void test(IntPredicate method) {
        assertTrue(method.test(5));
        assertFalse(method.test(3));
        assertTrue(method.test(4));
        assertTrue(method.test(2));
        assertTrue(method.test(1));
        assertFalse(method.test(Integer.MAX_VALUE));
        assertTrue(method.test(1000));
    }

    /**
     * LeetCode 耗时：2 ms - 98.56%
     *          内存消耗：35.1 MB - 59.56%
     */
    public boolean judgeSquareSum(int c) {
        int sqrt = (int) Math.sqrt(c), square = sqrt * sqrt;
        // 如果 c 是平方数
        if (square == c) {
            return true;
        }
        for(;;) {
            // 计算 c 和 square 的差是不是平方数
            int remain = c - square, rs = (int) Math.sqrt(remain);
            if (rs * rs == remain) {
                return true;
            } else if (remain > square) {  // 因为 square 是从最大值开始减少的，所以满足此条件的肯定就不行了
                return false;
            }
            sqrt -= 1;
            square = sqrt * sqrt;
        }
    }

    @Test
    public void testJudgeSquareSum() {
        test(this::judgeSquareSum);
    }


    /**
     * 双指针，参见：
     * https://leetcode-cn.com/problems/sum-of-square-numbers/solution/shuang-zhi-zhen-de-ben-zhi-er-wei-ju-zhe-ebn3/
     *
     * LeetCode 耗时：3 ms - 93.32%
     *          内存消耗：35.3 MB - 18.96%
     */
    public boolean twoPointMethod(int c) {
        long left = 0, right = (long) Math.sqrt(c);
        while (left <= right) {
            long sum = left * left + right * right;
            if (sum == c) {
                return true;
            } else if (sum < c) {
                left++;
            } else {
                right--;
            }
        }

        return false;
    }

    @Test
    public void testTwoPointMethod() {
        test(this::twoPointMethod);
    }


    /**
     * 费马平方和定理方法，参见：
     * https://leetcode-cn.com/problems/sum-of-square-numbers/solution/ping-fang-shu-zhi-he-by-leetcode-solutio-8ydl/
     */
    public boolean fermatMethod(int c) {
        for (int base = 2; base * base <= c; base++) {
            // 如果不是因子，枚举下一个
            if (c % base != 0) {
                continue;
            }

            // 计算 base 的幂
            int exp = 0;
            while (c % base == 0) {
                c /= base;
                exp++;
            }

            // 根据 Sum of two squares theorem 验证
            if (base % 4 == 3 && exp % 2 != 0) {
                return false;
            }
        }

        // 例如 11 这样的用例，由于上面的 for 循环里 base * base <= c ，base == 11 的时候不会进入循环体
        // 因此在退出循环以后需要再做一次判断
        return c % 4 != 3;
    }

    @Test
    public void testFermatMethod() {
        test(this::fermatMethod);
    }
}
