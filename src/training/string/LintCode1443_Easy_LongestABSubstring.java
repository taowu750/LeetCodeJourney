package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给你一个只由字母'A'和'B'组成的字符串s，找一个最长的子串，
 * 要求这个子串里面'A'和'B'的数目相等，输出该子串的长度。
 *
 * 例 1：
 * 输入: s = "ABAAABBBA"
 * 输出: 8
 * 解释:
 * 子串 s[0,7] 和子串 s[1,8] 满足条件，长度为 8。
 *
 * 例 2：
 * 输入: s = "AAAAAA"
 * 输出: 0
 * 解释:
 * s 中除了空字串，不存在 'A' 和 'B' 数目相等的子串。
 *
 * 约束：
 * - 这个子串可以为空。
 * - s 的长度 n 满足 2<=n<=1000000。
 */
public class LintCode1443_Easy_LongestABSubstring {

    static void test(ToIntFunction<String> method) {
        assertEquals(method.applyAsInt("ABAAABBBA"), 8);
    }

    /**
     * LintCode 耗时：1823ms - 87.60%
     */
    public int getAns(String S) {
        int cntA = 0, cntB = 0;
        char[] chars = S.toCharArray();
        // 记录数到第 i 个数时 A / B 的数量
        int[] numsA = new int[chars.length + 1], numsB = new int[chars.length + 1];
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 'A')
                cntA++;
            else
                cntB++;
            numsA[i + 1] = cntA;
            numsB[i + 1] = cntB;
        }
        if (cntA == 0 || cntB == 0)
            return 0;

        int maxSubLen = Math.max(cntA, cntB);
        for (maxSubLen *= 2; maxSubLen > 0; maxSubLen -= 2) {
            for (int i = maxSubLen; i < chars.length + 1; i++) {
                // 如果从 i 到 i - maxSubLen 的子串中，A、B 数量相等，则最长子串长度为 maxSubLen
                if (numsA[i] - numsA[i - maxSubLen] == numsB[i] - numsB[i - maxSubLen])
                    return maxSubLen;
            }
        }

        return 0;
    }

    @Test
    public void testGetAns() {
        test(this::getAns);
    }
}