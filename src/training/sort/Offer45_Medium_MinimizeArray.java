package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 剑指 Offer 45. 把数组排成最小的数: https://leetcode-cn.com/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof/
 *
 * 输入一个「非负整数」数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。
 * <p>
 * 例 1：
 * 输入: [10,2]
 * 输出: "102"
 * <p>
 * 例 2：
 * 输入: [3,30,34,5,9]
 * 输出: "3033459"
 * <p>
 * 约束：
 * - 0 < nums.length <= 100
 * - 输出结果可能非常大，所以你需要返回一个字符串而不是整数
 * - 拼接起来的数字可能会有前导 0，最后结果不需要去掉前导 0
 */
public class Offer45_Medium_MinimizeArray {

    static void test(Function<int[], String> method) {
        assertEquals("102", method.apply(new int[]{10, 2}));
        assertEquals("3033459", method.apply(new int[]{3, 30, 34, 5, 9}));
        assertEquals("12112", method.apply(new int[]{12, 121}));
        assertEquals("22281", method.apply(new int[]{2281, 2}));
        assertEquals("1399439856075703697382478249389609",
                method.apply(new int[]{824, 938, 1399, 5607, 6973, 5703, 9609, 4398, 8247}));
    }

    /**
     * LeetCode 耗时：4 ms - 99.30%
     *          内存消耗：37.8 MB - 81.79%
     */
    public String minNumber(int[] nums) {
        // 将数字转为字符串，方便后面的排序
        String[] strings = new String[nums.length];
        int totalLength = 0;
        for (int i = 0; i < nums.length; i++) {
            strings[i] = Integer.toString(nums[i]);
            totalLength += strings[i].length();
        }

        Arrays.sort(strings, (s1, s2) -> strCmp(s1, 0, s1.length(), s2, 0, s2.length()));

        StringBuilder result = new StringBuilder(totalLength);
        for (String s : strings) {
            result.append(s);
        }

        return result.toString();
    }

    private int strCmp(String s1, int l1, int h1,
                       String s2, int l2, int h2) {
        int len1 = h1 - l1, len2 = h2 - l2;
        int minLength = Math.min(len1, len2);
        // 从最高位到最低位比较排序
        for (int i = 0; i < minLength; i++) {
            int cmp = s1.charAt(l1 + i) - s2.charAt(l2 + i);
            if (cmp != 0) {
                return cmp;
            }
        }
        // 如果前面的位数都相等，则递归比较相同的部分和后面不同的部分。
        if (len1 == len2) {
            return 0;
        } else if (len1 > minLength) {
            return strCmp(s1, l1 + minLength, h1, s1, l1, l1 + minLength);
        } else {
            return strCmp(s2, l2, l2 + minLength, s2, l2 + minLength, h2);
        }
    }

    @Test
    public void testMinNumber() {
        test(this::minNumber);
    }
}
