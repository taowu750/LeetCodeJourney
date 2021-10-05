package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个仅包含小写字母的排序字符字母列表 letters，并给定一个目标字母 target，
 * 请找到列表中大于给定 target 的最小元素。
 * <p>
 * 规定字母大小是环绕的。例如，如果 target='z'并且 letters=['a','b']，则返回 'a'。
 * <p>
 * 例 1：
 * Input:
 * letters = ["c", "f", "j"]
 * target = "a"
 * Output: "c"
 * <p>
 * 例 2：
 * Input:
 * letters = ["c", "f", "j"]
 * target = "c"
 * Output: "f"
 * <p>
 * 例 3：
 * Input:
 * letters = ["c", "f", "j"]
 * target = "d"
 * Output: "f"
 * <p>
 * 例 4：
 * Input:
 * letters = ["c", "f", "j"]
 * target = "g"
 * Output: "j"
 * <p>
 * 例 5：
 * Input:
 * letters = ["c", "f", "j"]
 * target = "k"
 * Output: "c"
 * <p>
 * 约束：
 * - letters 的长度范围为 [2, 10000]
 * - letters 仅由小写字母构成，并且至少有两个不同的字母
 * - target 是小写字母
 */
public class E744_Easy_FindSmallestLetterGreaterThanTarget {

    static void test(BiFunction<char[], Character, Character> method) {
        char[] letters = new char[]{'c', 'f', 'j'};
        assertEquals(method.apply(letters, 'a'), 'c');

        assertEquals(method.apply(letters, 'c'), 'f');

        assertEquals(method.apply(letters, 'd'), 'f');

        assertEquals(method.apply(letters, 'g'), 'j');

        assertEquals(method.apply(letters, 'k'), 'c');
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public char nextGreatestLetter(char[] letters, char target) {
        int lo = 0, hi = letters.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (letters[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo < letters.length ? letters[lo] : letters[0];
    }

    @Test
    public void testNextGreatestLetter() {
        test(this::nextGreatestLetter);
    }
}
