package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 767. 重构字符串: https://leetcode-cn.com/problems/reorganize-string/
 *
 * 给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。
 *
 * 若可行，输出任意可行的结果。若不可行，返回空字符串。
 *
 * 例 1：
 * 输入: S = "aab"
 * 输出: "aba"
 *
 * 例 2：
 * 输入: S = "aaab"
 * 输出: ""
 *
 * 说明：
 * - S 只包含小写字母并且长度在[1, 500]区间内。
 */
public class E767_Medium_ReorganizeString {

    public static void assertValid(String actual, String origin, boolean can) {
        if (can) {
            char[] ac = actual.toCharArray();
            Arrays.sort(ac);
            char[] or = origin.toCharArray();
            Arrays.sort(or);
            if (!String.valueOf(or).equals(String.valueOf(ac))) {
                throw new AssertionError("actual not same as origin: " + actual);
            }

            for (int i = 1; i < actual.length(); i++) {
                if (actual.charAt(i - 1) == actual.charAt(i)) {
                    throw new AssertionError("invalid result: " + actual);
                }
            }
        } else {
            assertEquals("", actual);
        }
    }

    public static void test(UnaryOperator<String> method) {
        assertValid(method.apply("aab"), "aab", true);
        assertValid(method.apply("aaab"), "aaab", false);
        assertValid(method.apply("aabbcc"), "aabbcc", true);
        assertValid(method.apply("vvvlo"), "vvvlo", true);
        assertValid(method.apply("gpneqthatplqrofqgwwfmhzxjddhyupnluzkkysofgqawjyrwhfgdpkhiqgkpupgdeonipvptkfqluytogoljiaexrnxckeofqojltdjuujcnjdjohqbrzzzznymyrbbcjjmacdqyhpwtcmmlpjbqictcvjgswqyqcjcribfmyajsodsqicwallszoqkxjsoskxxstdeavavnqnrjelsxxlermaxmlgqaaeuvneovumneazaegtlztlxhihpqbajjwjujyorhldxxbdocklrklgvnoubegjrfrscigsemporrjkiyncugkksedfpuiqzbmwdaagqlxivxawccavcrtelscbewrqaxvhknxpyzdzjuhvoizxkcxuxllbkyyygtqdngpffvdvtivnbnlsurzroxyxcevsojbhjhujqxenhlvlgzcsibcxwomfpyevumljanfpjpyhsqxxnaewknpnuhpeffdvtyjqvvyzjeoctivqwann"),
                "gpneqthatplqrofqgwwfmhzxjddhyupnluzkkysofgqawjyrwhfgdpkhiqgkpupgdeonipvptkfqluytogoljiaexrnxckeofqojltdjuujcnjdjohqbrzzzznymyrbbcjjmacdqyhpwtcmmlpjbqictcvjgswqyqcjcribfmyajsodsqicwallszoqkxjsoskxxstdeavavnqnrjelsxxlermaxmlgqaaeuvneovumneazaegtlztlxhihpqbajjwjujyorhldxxbdocklrklgvnoubegjrfrscigsemporrjkiyncugkksedfpuiqzbmwdaagqlxivxawccavcrtelscbewrqaxvhknxpyzdzjuhvoizxkcxuxllbkyyygtqdngpffvdvtivnbnlsurzroxyxcevsojbhjhujqxenhlvlgzcsibcxwomfpyevumljanfpjpyhsqxxnaewknpnuhpeffdvtyjqvvyzjeoctivqwann", true);
    }

    /**
     * LeetCode 耗时：2 ms - 57.66%
     *          内存消耗：36.7 MB - 27.96%
     */
    public String reorganizeString(String s) {
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }

        PriorityQueue<Tuple> pq = new PriorityQueue<>(26);
        for (int i = 0; i < count.length; i++) {
            if (count[i] != 0) {
                // 当某个字符数量过半后就肯定不能组成合法结果
                if (count[i] > (s.length() + 1) / 2) {
                    return "";
                }
                pq.add(new Tuple((char) (i + 'a'), count[i]));
            }
        }

        StringBuilder result = new StringBuilder(s.length());
        while (pq.size() > 1) {
            // 注意是每次只使用一个字符
            Tuple a = pq.remove(), b = pq.remove();
            result.append(a.c).append(b.c);
            a.cnt--;
            if (a.cnt > 0) {
                pq.offer(a);
            }
            b.cnt--;
            if (b.cnt > 0) {
                pq.offer(b);
            }
        }

        if (pq.size() == 1) {
            Tuple t = pq.remove();
            if (t.cnt == 1) {
                result.append(t.c);
            } else {
                return "";
            }
        }

        return result.toString();
    }

    public static class Tuple implements Comparable<Tuple> {
        public char c;
        public int cnt;

        public Tuple(char c, int cnt) {
            this.c = c;
            this.cnt = cnt;
        }

        @Override
        public int compareTo(Tuple o) {
            return -Integer.compare(cnt, o.cnt);
        }
    }

    @Test
    public void testReorganizeString() {
        test(this::reorganizeString);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/reorganize-string/solution/zhong-gou-zi-fu-chuan-by-leetcode-solution/
     *
     * LeetCode 耗时：1 ms - 76.56%
     *          内存消耗：36.5 MB - 55.26%
     */
    public String greedyMethod(String s) {
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }

        for (int cnt : count) {
            // 当某个字符数量过半后就肯定不能组成合法结果
            if (cnt > (s.length() + 1) / 2) {
                return "";
            }
        }

        char[] result = new char[s.length()];
        int evenIdx = 0, oddIdx = 1, halfLen = s.length() / 2;
        for (int i = 0; i < 26; i++) {
            char c = (char) (i + 'a');
            while (count[i] > 0 && count[i] <= halfLen && oddIdx < s.length()) {
                result[oddIdx] = c;
                count[i]--;
                oddIdx += 2;
            }
            while (count[i] > 0) {
                result[evenIdx] = c;
                count[i]--;
                evenIdx += 2;
            }
        }

        return new String(result);
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
