package training.math;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.IntSupplier;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 已有方法 rand7 可生成 1 到 7 范围内的均匀随机整数，试写一个方法 rand10 生成 1 到 10 范围内的均匀随机整数。
 * 不要使用系统的 Math.random() 方法。
 *
 * rand7()调用次数的「期望值」是多少? 你能否尽量少调用 rand7()?
 *
 * 例 1：
 * 输入: 1
 * 输出: [7]
 *
 * 例 2：
 * 输入: 2
 * 输出: [8,4]
 *
 * 例 3：
 * 输入: 3
 * 输出: [8,1,10]
 *
 * 约束：
 * - rand7 已定义。
 * - 传入参数: n 表示 rand10 的调用次数。
 */
public class E470_Easy_ImplementRand10UsingRand7 {

    static void test(IntSupplier method) {
        int[] cnt = new int[11];
        for (int i = 0; i < 10000; i++) {
            int r = method.getAsInt();
            assertTrue(r >= 1 && r <= 10);
            cnt[r]++;
        }

        for (int i = 1; i <= 10; i++) {
            assertTrue(cnt[i] > 900);
        }
    }

    /**
     * 算法思想参见：
     * https://leetcode-cn.com/problems/implement-rand10-using-rand7/solution/xiang-xi-si-lu-ji-you-hua-si-lu-fen-xi-zhu-xing-ji/
     *
     * LeetCode 耗时：7 ms - 99.51%
     *          内存消耗：43.5 MB - 63.32%
     */
    public int rand10() {
        while (true) {
            // 采样 [1，49] 之间的数字，每个数字都是等比例的
            int sample = (rand7() - 1) * 7 + rand7();
            if (sample <= 40)
                return 1 + sample % 10;
            // 将剩余 9 的数字再次进行采样，采样到 [1,63]
            sample = (sample - 41) * 7 + rand7();
            if (sample <= 60)
                return 1 + sample % 10;
            // 将剩余 3 个数字再次进行采样，采样到 [1,21]
            sample = (sample - 61) * 7 + rand7();
            if (sample <= 20)
                return 1 + sample % 10;
        }
    }

    private Random random = new Random();

    int rand7() {
        return random.nextInt(7) + 1;
    }

    @Test
    void testRand10() {
        test(this::rand10);
    }
}
