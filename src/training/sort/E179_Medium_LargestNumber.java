package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 179. 最大数: https://leetcode-cn.com/problems/largest-number/
 *
 * 给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
 * 注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
 *
 * 例 1：
 * 输入：nums = [10,2]
 * 输出："210"
 *
 * 例 2：
 * 输入：nums = [3,30,34,5,9]
 * 输出："9534330"
 *
 * 例 3：
 * 输入：nums = [1]
 * 输出："1"
 *
 * 例 4：
 * 输入：nums = [10]
 * 输出："10"
 *
 * 约束：
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 10^9
 */
public class E179_Medium_LargestNumber {

    public static void test(Function<int[], String> method) {
        assertEquals("210", method.apply(new int[]{10, 2}));
        assertEquals("9534330", method.apply(new int[]{3,30,34,5,9}));
        assertEquals("1", method.apply(new int[]{1}));
        assertEquals("10", method.apply(new int[]{10}));
        assertEquals("83088308830", method.apply(new int[]{8308,8308,830}));
        assertEquals("0", method.apply(new int[]{0, 0}));
    }

    /**
     * LeetCode 耗时：4 ms - 91.38%
     *          内存消耗：36.2 MB - 98.50%
     */
    public String largestNumber(int[] nums) {
        Integer[] ints = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            ints[i] = nums[i];
        }
        Arrays.sort(ints, this::compare);

        if (ints[0] == 0) {
            return "0";
        } else {
            StringBuilder sb = new StringBuilder(ints.length * 3);
            for (int num : ints) {
                sb.append(num);
            }

            return sb.toString();
        }
    }

    final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE };
    final static int[] radix = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    static int size(int x) {
        for (int i=0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    private int compare(int a, int b) {
        return compare(a, b, size(a), size(b));
    }

    private int compare(int a, int b, int aSize, int bSize) {
        if (aSize == bSize) {
            return -Integer.compare(a, b);
        }

        if (a < b) {
            return -compare(b, a, bSize, aSize);
        }

        for (int i = 0, bcopy = b, ar = aSize - 1, br = bSize - 1; i < bSize; i++) {
            int ah = a / radix[ar], bh = bcopy / radix[br];
            int cmp = -Integer.compare(ah, bh);
            if (cmp != 0) {
                return cmp;
            }
            a -= ah * radix[ar];
            bcopy -= bh * radix[br];

            ar--;
            br--;
        }

        return compare(a, b, aSize - bSize, bSize);
    }

    @Test
    public void testLargestNumber() {
        test(this::largestNumber);
    }
}
