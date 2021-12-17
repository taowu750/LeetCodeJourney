package training.math;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 202. 快乐数: https://leetcode-cn.com/problems/happy-number/
 *
 * 编写一个算法来判断一个数 n 是不是快乐数。
 *
 * 「快乐数」定义为：
 * - 对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和。
 * - 然后重复这个过程直到这个数变为 1，也可能是「无限循环」但始终变不到 1。
 * - 如果可以变为 1，那么这个数就是快乐数。
 * - 如果 n 是快乐数就返回 true ；不是，则返回 false 。
 *
 * 例 1：
 * 输入：n = 19
 * 输出：true
 * 解释：
 * 1^2 + 9^2 = 82
 * 8^2 + 2^2 = 68
 * 6^2 + 8^2 = 100
 * 1^2 + 0^2 + 0^2 = 1
 *
 * 例 2：
 * 输入：n = 2
 * 输出：false
 *
 * 说明：
 * - 1 <= n <= 2^31 - 1
 */
public class E202_Easy_HappyNumber {

    public static void test(IntPredicate method) {
        assertTrue(method.test(19));
        assertFalse(method.test(2));
        assertTrue(method.test(10));
    }

    /**
     * 参见：https://leetcode-cn.com/problems/happy-number/solution/kuai-le-shu-by-leetcode-solution/
     *
     * 对于快乐数，有以下三种情况：
     * 1. 最终会得到 1。
     * 2. 最终会进入循环。
     * 3. 值会越来越大，最后接近无穷大。
     *
     * 第三个情况比较难以检测和处理。我们怎么知道它会继续变大，而不是最终得到 1 呢？我们可以仔细想一想，
     * 每一位数的最大数字的下一位数是多少。
     *
     * Digits	Largest	       Next
     * 1	    9	           81
     * 2	    99	           162
     * 3	    999	           243
     * 4	    9999	       324
     * 13	    9999999999999  1053
     *
     * 对于 3 位数的数字，它不可能大于 243。这意味着它要么被困在 243 以下的循环内，要么跌到 1。
     * 4 位或 4 位以上的数字在每一步都会丢失一位，直到降到 3 位为止。所以我们知道，最坏的情况下，
     * 算法可能会在 243 以下的所有数字上循环，然后回到它已经到过的一个循环或者回到 1。但它不会无限期地进行下去，
     * 所以我们排除第三种选择。
     *
     * LeetCode 耗时：1 ms - 91.71%
     *          内存消耗：35.7 MB - 10.88%
     */
    public boolean isHappy(int n) {
        for (Set<Integer> set = new HashSet<>(); n != 1 && !set.contains(n);) {
            set.add(n);
            n = getNext(n);
        }

        return n == 1;
    }

    private int getNext(int n) {
        int totalSum = 0;
        while (n > 0) {
            int d = n % 10;
            n = n / 10;
            totalSum += d * d;
        }
        return totalSum;
    }

    @Test
    public void testIsHappy() {
        test(this::isHappy);
    }


    /**
     * 类似于判断循环链表的方法。
     *
     * LeetCode 耗时：1 ms - 91.71%
     *          内存消耗：35.4 MB - 53.49%
     */
    public boolean slowFastPointMethod(int n) {
        int slow = n, fast = n;
        do {
            slow = getNext(slow);
            fast = getNext(getNext(fast));
        } while (slow != fast && slow != 1);

        return slow == 1;
    }

    @Test
    public void testSlowFastPointMethod() {
        test(this::slowFastPointMethod);
    }
}
