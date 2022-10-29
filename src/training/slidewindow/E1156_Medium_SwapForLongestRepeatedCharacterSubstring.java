package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1156. 单字符重复子串的最大长度: https://leetcode-cn.com/problems/swap-for-longest-repeated-character-substring/
 *
 * 如果字符串中的所有字符都相同，那么这个字符串是单字符重复的字符串。
 *
 * 给你一个字符串 text，你只能交换其中两个字符一次或者什么都不做，然后得到一些单字符重复的子串。返回其中最长的子串的长度。
 *
 * 例 1：
 * 输入：text = "ababa"
 * 输出：3
 *
 * 例 2：
 * 输入：text = "aaabaaa"
 * 输出：6
 *
 * 例 3：
 * 输入：text = "aaabbaaa"
 * 输出：4
 *
 * 例 4：
 * 输入：text = "aaaaa"
 * 输出：5
 *
 * 例 5：
 * 输入：text = "abcdef"
 * 输出：1
 *
 * 说明：
 * - 1 <= text.length <= 20000
 * - text 仅由小写英文字母组成。
 */
public class E1156_Medium_SwapForLongestRepeatedCharacterSubstring {

    public static void test(ToIntFunction<String> method) {
        assertEquals(3, method.applyAsInt("ababa"));
        assertEquals(6, method.applyAsInt("aaabaaa"));
        assertEquals(4, method.applyAsInt("aaabbaaa"));
        assertEquals(5, method.applyAsInt("aaaaa"));
        assertEquals(1, method.applyAsInt("abcdef"));
        assertEquals(4, method.applyAsInt("acbaaa"));
    }

    /**
     * LeetCode 耗时：8 ms - 44.39%
     *          内存消耗：41.1 MB - 17.35%
     */
    public int maxRepOpt1(String text) {
        final int n = text.length();
        // 记录所有字符的单字符子串区间
        List<int[]>[] c2intervals = new List[26];
        for (int i = 0; i < n; i++) {
            int c = text.charAt(i) - 'a';
            if (c2intervals[c] == null) {
                c2intervals[c] = new ArrayList<>(4);
                c2intervals[c].add(new int[]{i, i});
            } else {
                int[] lastInterval = c2intervals[c].get(c2intervals[c].size() - 1);
                if (lastInterval[1] == i - 1) {
                    lastInterval[1] = i;
                } else {
                    c2intervals[c].add(new int[]{i, i});
                }
            }
        }

        int ans = 0;
        for (List<int[]> intervals : c2intervals) {
            if (intervals == null) {
                continue;
            }
            for (int i = 0; i < intervals.size() - 1; i++) {
                int[] prev = intervals.get(i), next = intervals.get(i + 1);
                // 如果两个子串之间只隔着一个字符，则可以连起来
                if (prev[1] == next[0] - 2) {
                    int len = prev[1] - prev[0] + next[1] - next[0] + 2;
                    // 如果有多余两个子串，则可以使用第三个子串字符
                    if (intervals.size() > 2) {
                        len++;
                    }
                    ans = Math.max(ans, len);
                } else {
                    // 注意这里是+2，因为还可以交换一个字符
                    ans = Math.max(ans, prev[1] - prev[0] + 2);
                }
            }
            int[] end = intervals.get(intervals.size() - 1);
            int len = end[1] - end[0] + 1;
            // 如果有多余一个子串，则可以交换一个字符
            if (intervals.size() > 1) {
                len++;
            }
            ans = Math.max(ans, len);
        }

        return ans;
    }

    @Test
    public void testMaxRepOpt1() {
        test(this::maxRepOpt1);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/swap-for-longest-repeated-character-substring/solution/hen-e-xin-de-hua-dong-chuang-kou-lao-mei-u312/
     *
     * LeetCode 耗时：4 ms - 71.96%
     *          内存消耗：39.9 MB - 33.64%
     */
    public int slideWindowMethod(String text) {
        // 记录字符出现的最早位置
        int[] prev = new int[26];
        // 记录字符出现的最晚位置
        int[] after = new int[26];
        Arrays.fill(prev, -1);
        Arrays.fill(after, -1);

        final int n = text.length();
        for (int i = 0; i < n; i++) {
            int idx = text.charAt(i) - 'a';
            if (prev[idx] == -1) {
                prev[idx] = i;
            }
            after[idx] = i;
        }

        // windowNum 表示当前窗口 [left, right) 内的主要字符
        int result = 0, left = 0, right = 0, windowNum = text.charAt(0) - 'a';
        // windowUnMatchCnt 表示窗口扩充时，和 windowNum 不一样的字符个数
        // windowUnMatchPos 记录第一次遇到不一样字符的位置
        // windowSwapPos 表示使用窗口外字符进行填补，该字符的位置
        // windowCanSwapUseOutOf 表示使用窗口外字符的个数。由于只能交换一次，因此它只有 0 和 1 两个值
        int windowUnMatchCnt = 0, windowUnMatchPos = 0, windowSwapPos = -1, windowCanSwapUseOutOf = 0;
        while (right < n) {
            int num = text.charAt(right) - 'a';
            // 向右扩充时遇到了不一样的字符，也就是失配
            if (num != windowNum) {
                // 如果失配字符数量大于 1
                if (++windowUnMatchCnt > 1) {
                    // 需要重置窗口，定位到第一次失配的位置
                    left = right = windowUnMatchPos;
                    windowNum = text.charAt(left) - 'a';
                    windowUnMatchCnt = 0;
                    windowCanSwapUseOutOf = 0;
                    windowSwapPos = -1;
                } else {  // 否则首次出现失配
                    // 记录第一次失配位置
                    windowUnMatchPos = right;
                    // 如果可以用窗口外的字符填补
                    if (prev[windowNum] < left || after[windowNum] > right) {
                        // 注意 windowCanSwapUseOutOf 只有在这里才会赋值为 1，因为 windowNum 可能只有一段重复子串
                        windowCanSwapUseOutOf = 1;
                        windowSwapPos = prev[windowNum] < left ? prev[windowNum] : after[windowNum];
                        result = Math.max(result, right - left + 1);
                    }
                }
            } else {
                // 如果失配，是不是使用窗口内的字符填补的
                int unMatchHappenInsideWindow = windowSwapPos >= left && windowSwapPos <= right ? 1 : 0;
                // 当前单字符重复子串长度 = 窗口长度 - 失配字符数量 + 使用窗口外字符填补数量 - 使用窗口内的字符填补数量
                result = Math.max(result, right - left + 1 - windowUnMatchCnt + windowCanSwapUseOutOf - unMatchHappenInsideWindow);
            }
            right++;
        }
        // 运行结束时，我们还可以从窗口外获取一个字符增加长度
        windowCanSwapUseOutOf = prev[windowNum] < left || after[windowNum] > right ? 1 : 0;
        return Math.max(result, right - left - windowUnMatchCnt + windowCanSwapUseOutOf);
    }

    @Test
    public void testSlideWindowMethod() {
        test(this::slideWindowMethod);
    }
}
