package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 我们正在玩猜猜游戏。游戏如下：
 * 我选择一个从 1 到 n 的数字。您必须猜测我选了哪个号码。
 * 每次您猜错了，我都会告诉您我选择的数字是高于还是低于您的猜测。
 * <p>
 * 您调用一个预定义的 API int guess(int num)，它将返回3种可能的结果：
 * - -1: 我选的数字比你的猜测低
 * - 0: 我选的数字高于你的猜测
 * - 1: 我选的数字等于你的猜测
 * <p>
 * 例 1：
 * Input: n = 10, pick = 6
 * Output: 6
 * <p>
 * 例 2：
 * Input: n = 1, pick = 1
 * Output: 1
 * <p>
 * 例 3：
 * Input: n = 2, pick = 1
 * Output: 1
 * <p>
 * 例 4：
 * Input: n = 2, pick = 2
 * Output: 2
 * <p>
 * 约束：
 * - 1 <= n <= 2**31 - 1
 * - 1 <= pick <= n
 */
public class E374_Easy_GuessNumberHigherOrLower {

    static int pick;

    int guess(int num) {
        return Integer.compare(pick, num);
    }

    static void test(IntUnaryOperator method) {
        int n = 10;
        pick = 6;
        assertEquals(method.applyAsInt(n), pick);

        n = 1;
        pick = 1;
        assertEquals(method.applyAsInt(n), pick);

        n = 2;
        pick = 1;
        assertEquals(method.applyAsInt(n), pick);

        n = 2;
        pick = 2;
        assertEquals(method.applyAsInt(n), pick);

        n = 100000;
        for (int i = 1; i <= n; i++) {
            pick = i;
            assertEquals(method.applyAsInt(n), pick);
        }
    }

    public int guessNumber(int n) {
        int lo = 1, hi = n;

        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            int r = guess(mid);
            // 先判断是不是相等
            if (r == 0)
                return mid;
            else if (r == 1)
                lo = mid + 1;
            else
                hi = mid - 1;
        }
        // 不会在这里返回
        return lo;
    }

    @Test
    public void testGuessNumber() {
        test(this::guessNumber);
    }
}
