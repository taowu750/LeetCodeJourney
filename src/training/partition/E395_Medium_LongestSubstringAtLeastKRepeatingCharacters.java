package training.partition;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 395. 至少有 K 个重复字符的最长子串: https://leetcode-cn.com/problems/longest-substring-with-at-least-k-repeating-characters/
 *
 * 给你一个字符串 s 和一个整数 k ，请你找出 s 中的最长子串，要求该子串中的每一字符出现次数都「不少于 k」。
 * 返回这一子串的长度。
 *
 * 例 1：
 * 输入：s = "aaabb", k = 3
 * 输出：3
 * 解释：最长子串为 "aaa" ，其中 'a' 重复了 3 次。
 *
 * 例 2：
 * 输入：s = "ababbc", k = 2
 * 输出：5
 * 解释：最长子串为 "ababb" ，其中 'a' 重复了 2 次， 'b' 重复了 3 次。
 *
 * 说明：
 * - 1 <= s.length <= 10^4
 * - s 仅由小写英文字母组成
 * - 1 <= k <= 10^5
 */
public class E395_Medium_LongestSubstringAtLeastKRepeatingCharacters {

    public static void test(ToIntBiFunction<String, Integer> method) {
        assertEquals(3, method.applyAsInt("aaabb", 3));
        assertEquals(5, method.applyAsInt("ababbc", 2));
        assertEquals(0, method.applyAsInt("bcb", 2));
        assertEquals(2, method.applyAsInt("bcbb", 2));
        assertEquals(0, method.applyAsInt("ababacb", 3));
        assertEquals(3, method.applyAsInt("bbaaacbd", 3));
        assertEquals(3, method.applyAsInt("baaabcb", 3));
        assertEquals(0, method.applyAsInt("baabacb", 3));
    }

    /**
     * 暴力解法。
     *
     * LeetCode 耗时：193 ms - 5.04%
     *          内存消耗：36.4 MB - 62.11%
     */
    public int longestSubstring(String s, int k) {
        int n = s.length();
        if (n < k) {
            return 0;
        }
        if (k == 1) {
            return n;
        }

        int[] count = new int[128];
        int result = 0, distinct = 0, greater = 0;
        for (int i = n - 2; i >= 0; i--) {
            count[s.charAt(i)]++;
            distinct++;
            for (int j = i + 1; j < n; j++) {
                count[s.charAt(j)]++;
                if (count[s.charAt(j)] == 1) {
                    distinct++;
                } else if (count[s.charAt(j)] == k) {
                    greater++;
                }

                if (distinct == greater && result < j - i + 1) {
                    result = j - i + 1;
                }
            }

            for (int l = 'a'; l <= 'z'; l++) {
                count[l] = 0;
            }
            distinct = 0;
            greater = 0;
        }

        return result;
    }

    @Test
    public void testLongestSubstring() {
        test(this::longestSubstring);
    }


    /**
     * 分治算法。
     *
     * 对于字符串 s，如果存在某个字符 ch，它的出现次数大于 0 且小于 k，则任何包含 ch 的子串都不可能满足要求。
     * 也就是说，我们将字符串按照 ch 切分成若干段，则满足要求的最长子串一定出现在某个被切分的段内，
     * 而不能跨越一个或多个段。因此，可以考虑分治的方式求解本题。
     *
     * 注意，分治算法不是一定要切分成两段，要结合实际情况考虑。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.4 MB - 64.78%
     */
    public int partitionMethod(String s, int k) {
        if (k > s.length()) {
            return 0;
        } else if (k == 1) {
            return s.length();
        }

        return partition(s, k, 0, s.length());
    }

    private int partition(String s, int k, int lo, int hi) {
        if (lo >= hi) {
            return 0;
        }

        // 先统计字符个数
        char[] ch2cnt = new char[26];
        for (int i = lo; i < hi; i++) {
            ch2cnt[s.charAt(i) - 'a']++;
        }

        // 循环，找到小于 k 的字符，就递归 [last, i) 的子串
        int result = 0, last = lo;
        for (int i = lo; i < hi; i++) {
            if (ch2cnt[s.charAt(i) - 'a'] < k) {
                result = Math.max(result, partition(s, k, last, i));
                last = i + 1;
            }
        }
        // 如果 [lo, hi) 都大于 k
        if (last == lo) {
            result = hi - lo;
        } else {  // 否则处理最后一个子串
            result = Math.max(result, partition(s, k, last, hi));
        }

        return result;
    }

    @Test
    public void testPartitionMethod() {
        test(this::partitionMethod);
    }


    /**
     * 枚举+滑动窗口方法，参见：
     * https://leetcode-cn.com/problems/longest-substring-with-at-least-k-repeating-characters/solution/xiang-jie-mei-ju-shuang-zhi-zhen-jie-fa-50ri1/
     *
     * 对于给定的字符种类数量 t（从 1 枚举到 26），我们维护滑动窗口的左右边界 l,r 滑动窗口内部每个字符出现的次数 cnt，
     * 以及滑动窗口内的字符种类数目 total。
     * 当 total>t 时，我们不断地右移左边界 l，并对应地更新 cnt 以及 total，直到 total≤t 为止。
     * 这样，对于任何一个右边界 r，我们都能找到最小的 l（记为 lmin），使得 s[lmin...r] 之间的字符种类数目不多于 t。
     *
     * 对于任何一组 lmin, r 而言，如果 s[lmin...r] 之间存在某个出现次数小于 k（且不为 0，下文不再特殊说明）的字符，
     * 我们可以断定：对于任何 l' ∈ (lmin,r) 而言，s[l'...r] 依然不可能是满足题意的子串，因为：
     * - 要么该字符的出现次数降为 0，此时子串内虽然少了一个出现次数小于 k 的字符，但字符种类数目也随之小于 t 了；
     * - 要么该字符的出现次数降为非 0 整数，此时该字符的出现次数依然小于 k。
     *
     * 根据上面的结论，我们发现：当限定字符种类数目为 t 时，满足题意的最长子串，就一定出自某个 s[lmin...r]。
     * 因此，在滑动窗口的维护过程中，就可以直接得到最长子串的大小。
     *
     * 我们可以维护一个计数器 less，代表当前出现次数小于 k 的字符的数量。注意到：每次移动滑动窗口的边界时，
     * 只会让某个字符的出现次数加一或者减一。
     *
     *
     *
     * 滑动窗口本质上来源于单调性，一般可以理解为，随着左端点位置的增加，其最优决策的右端点位置单调不减。
     * 也就是说，当滑动窗口不满足条件，需要收缩左端点时，右端点的位置不应该减少。
     * 例如 s=baaacbb, k=3，当滑动窗口为 [0,4] 遇到 c 时，需要收缩左端点，而左移右端点可以得到一个符合条件的子串 [1,3]，
     * 所以此题无法使用常规的滑动窗口。
     *
     * 进一步分析，此题为什么要用「枚举」的方法呢？因为这个题目不满足「二分」的性质。
     * 也就是假设有长度 t 的一段区间满足要求的话，t + 1 长度的区间是否「一定满足」或者「一定不满足」呢？
     * 显然并不一定，是否满足取决于 t + 1 个位置出现的字符在不在原有区间内，「这和 t + 1 的内容相关，因此不满足二分」。
     *
     * 假设我们已经画出来一段长度为 t 的区间满足要求（且此时 k > 1），那么当我们将长度扩成 t + 1 的时候
     * （无论是往左扩还是往右扩）：
     * - 如果新位置的字符在原有区间出现过，那必然还是满足出现次数大于 k，这时候 t + 1 的长度满足要求
     * - 如果新位置的字符在原有区间没出现过，那新字符的出现次数只有一次，这时候 t + 1 的长度不满足要求
     *
     * 因此我们无法是使用「二分」，相应的也无法直接使用「滑动窗口」思路的双指针。
     * 因为双指针其实也是利用了二段性质，当一个指针确定在某个位置，另外一个指针能够落在某个明确的分割点，
     * 使得左半部分满足，右半部分不满足。
     *
     * 题目说明了只包含小写字母（26 个，为有限数据），我们可以枚举最大长度所包含的字符类型数量，答案必然是 [1, 26]，
     * 即最少包含 1 个字母，最多包含 26 个字母。
     * 你会发现，当确定了长度所包含的字符种类数量时，区间重新具有了二段性质。当我们使用双指针的时候：
     * - 右端点往右移动必然会导致字符类型数量增加（或不变）
     * - 左端点往右移动必然会导致字符类型数量减少（或不变）
     *
     * LeetCode 耗时：5 ms - 39.08%
     *          内存消耗：36.6 MB - 57.41%
     */
    public int slideWindowMethod(String s, int k) {
        if (k > s.length()) {
            return 0;
        }
        if (k == 1) {
            return s.length();
        }

        int n = s.length(), result = 0;
        int[] count = new int[26];
        for (int t = 1; t <= 26; t++) {
            int total = 0, lessKCnt = 0, left = 0, right = 0;
            while (right < n) {
                int ci = s.charAt(right++) - 'a';
                // 第一次遇到，total 和 lessKCnt 都加 1
                if (++count[ci] == 1) {
                    total++;
                    lessKCnt++;
                } else if (count[ci] == k) {  // 有 k 个了，则 lessKCnt 减 1
                    lessKCnt--;
                }

                // 当 total 大于 t，收缩左边界
                while (total > t) {
                    int lci = s.charAt(left++) - 'a';
                    if (--count[lci] == 0) {
                        total--;
                        lessKCnt--;
                    } else if (count[lci] == k - 1) {
                        lessKCnt++;
                    }
                }

                if (lessKCnt == 0 && result < right - left) {
                    result = right - left;
                }
            }

            Arrays.fill(count, 0);
        }

        return result;
    }

    @Test
    public void testSlideWindowMethod() {
        test(this::slideWindowMethod);
    }
}
