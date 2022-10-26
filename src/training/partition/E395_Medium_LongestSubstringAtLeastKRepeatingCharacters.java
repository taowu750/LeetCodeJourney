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
     * 首先我们知道「答案子串的左边界左侧的字符以及右边界右侧的字符一定不会出现在子串中，否则就不会是最优解」。
     *
     * 但如果我们只从该性质出发的话，朴素解法应该是使用一个滑动窗口，不断的调整滑动窗口的左右边界，
     * 使其满足「左边界左侧的字符以及右边界右侧的字符一定不会出现在窗口中」，这实际上就是双指针解法，
     * 但是如果不先敲定（枚举）出答案所包含的字符数量的话，这里的双指针是不具有单调性的。
     *
     * 因此我们需要先利用字符数量有限性（可枚举）作为切入点，使得「答案子串的左边界左侧的字符以及右边界右侧的字符一定不会出现在子串中」
     * 这一性质在双指针的实现下具有单调性。也就是题解说的「让区间重新具有二段性质」。
     *
     * 然后遍历 26 种可能性（答案所包含的字符种类数量），对每种可能性应用滑动窗口（由上述性质确保正确），
     * 可以得到每种可能性的最大值（局部最优），由所有可能性的最大值可以得出答案（全局最优）。
     *
     * 当我们采用常规的分析思路发现无法进行时，要去关注一下数据范围中「数值小」的值。因为数值小其实是代表了「可枚举」，
     * 往往是解题或者降低复杂度的一个重要（甚至是唯一）的突破口。
     *
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
