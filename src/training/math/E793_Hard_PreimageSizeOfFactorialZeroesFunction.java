package training.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 793. 阶乘函数后 K 个零：https://leetcode-cn.com/problems/preimage-size-of-factorial-zeroes-function/
 *
 * f(x) 是 x! 末尾是 0 的数量。（回想一下 x! = 1 * 2 * 3 * ... * x，且 0! = 1 ）
 *
 * 例如，f(3) = 0，因为 3! = 6 的末尾没有 0 ；而 f(11) = 2，因为 11!= 39916800 末端有 2 个 0 。
 * 给定 K，找出多少个非负整数 x，能满足 f(x) = K 。
 *
 * 例 1：
 * 输入：K = 0
 * 输出：5
 * 解释：0!, 1!, 2!, 3!, and 4! 均符合 K = 0 的条件。
 *
 * 例 2：
 * 输入：K = 5
 * 输出：0
 * 解释：没有匹配到这样的 x!，符合 K = 5 的条件。
 *
 * 约束：
 * - K 是范围在 [0, 10^9] 的整数。
 */
public class E793_Hard_PreimageSizeOfFactorialZeroesFunction {

    static void test(IntUnaryOperator method) {
        assertEquals(5, method.applyAsInt(0));
        assertEquals(0, method.applyAsInt(5));
        assertEquals(5, method.applyAsInt(25));
        assertEquals(5, method.applyAsInt(45));
        assertEquals(5, method.applyAsInt(71));
        assertEquals(0, method.applyAsInt(66550376));
        assertEquals(5, method.applyAsInt(1000000000));
    }

    /**
     * 参见 {@link E172_Easy_FactorialTrailingZeroes}。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.2 MB - 53.14%
     */
    public int preimageSizeFZF(int K) {
        int lo = 0, hi = K;
        // 使用二分法减少 trailingZeroes 调用次数
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            // 转成 long 防止溢出
            int zeroes = trailingZeroes(mid * 5L);
            if (zeroes == K)
                return 5;
            else if (zeroes < K)
                lo = mid + 1;
            else
                hi = mid - 1;
        }

        return 0;
    }

    private int trailingZeroes(long n) {
        int result = 0;
        while (n > 1) {
            result += n / 5;
            n /= 5;
        }
        return result;
    }

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testPreimageSizeFZF() {
        test(this::preimageSizeFZF);
    }
}
