package learn.array;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个整数数组 nums，返回十进制位数为偶数的整数的个数。
 * <p>
 * 例子：
 * Input: nums = [12,345,2,6,7896]
 * Output: 2
 * Explanation:
 * 12 包含 2 个数字
 * 345 包含 3 个数字
 * 2 包含 1 个数字
 * 6 包含 1 个数字
 * 7896 包含 4 个数字
 * 因此只有 12 和 7896 有偶数个数字
 * <p>
 * 注意：
 * - 1 <= nums.length <= 500
 * - 1 <= nums[i] <= 10^5
 */
public class FindEvenNumberOfDigits {

    public int findNumbers(int[] nums) {
        int cnt = 0;
        for (int n : nums) {
            int curCnt = 0;
            while (n != 0) {
                curCnt++;
                n /= 10;
            }
            if ((curCnt & 1) == 0)
                cnt++;
        }

        return cnt;
    }

    void test(ToIntFunction<int[]> function) {
        int[] a = {12, 345, 2, 6, 7896};
        assertEquals(function.applyAsInt(a), 2);

        int[] a2 = {555, 901, 482, 1771};
        assertEquals(function.applyAsInt(a2), 1);
    }

    @Test
    public void testFindNumbers() {
        test(this::findNumbers);
    }



    private static final int[] TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999,
            999999999};

    /**
     * 使用表驱动法改进
     */
    public int tableDriverFindNumbers(int[] nums) {
        int cnt = 0;
        for (int n : nums) {
            int curCnt = 1;
            for (int i = 0; i < TABLE.length; i++) {
                if (n > TABLE[i])
                    curCnt++;
                else
                    break;
            }
            if ((curCnt & 1) == 0)
                cnt++;
        }

        return cnt;
    }

    @Test
    public void testTableDriverFindNumbers() {
        test(this::tableDriverFindNumbers);
    }


    /**
     * 针对输入的限制条件硬编码
     */
    public int pointAtConstraint(int[] nums) {
        int cnt = 0;
        for (int n : nums) {
            if ((n >= 10 && n < 100) || (n >= 1000 && n < 10000) || n == 100000)
                cnt++;
        }

        return cnt;
    }

    @Test
    public void testPointAtConstraint() {
        test(this::pointAtConstraint);
    }
}
