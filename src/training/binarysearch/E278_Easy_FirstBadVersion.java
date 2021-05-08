package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 你是产品经理，目前正在领导一个团队开发新产品。不幸的是，产品的最新版本未通过质量检查。
 * 由于每个版本都是基于先前版本开发的，因此错误版本之后的所有版本也都是错误的。
 * 假设有 n 个版本 [1、2，...，n]，并且想找出第一个不良版本。
 *
 * 给定一个 API bool isBadVersion(version)，该API返回版本是否错误。
 * 实现查找第一个错误版本的功能。您应该减少对 API 的调用次数。
 *
 * 例 1：
 * Input: n = 5, bad = 4
 * Output: 4
 *
 * 例 2：
 * Input: n = 1, bad = 1
 * Output: 1
 *
 * 约束：
 * - 1 <= bad <= n <= 2**31 - 1
 */
public class E278_Easy_FirstBadVersion {

    static int badVersion;

    static boolean isBadVersion(int version) {
        return version >= badVersion;
    }

    static void test(IntUnaryOperator method) {
        int n = 5;
        badVersion = 4;
        assertEquals(method.applyAsInt(n), badVersion);

        n = 1;
        badVersion = 1;
        assertEquals(method.applyAsInt(n), badVersion);

        n = 10_0000;
        for (int i = 1; i <= n; i++) {
            badVersion = i;
            assertEquals(method.applyAsInt(n), badVersion);
        }
    }

    /**
     * LeetCode 耗时：12ms - 98.06%
     */
    public int firstBadVersion(int n) {
        int lo = 1, hi = n;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (isBadVersion(mid))
                hi = mid;
            else
                lo = mid + 1;
        }

        return lo;
    }

    @Test
    public void testFirstBadVersion() {
        test(this::firstBadVersion);
    }
}
