package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 剑指 Offer 66. 构建乘积数组：https://leetcode-cn.com/problems/gou-jian-cheng-ji-shu-zu-lcof/
 *
 * 给定一个数组 A[0,1,…,n-1]，请构建一个数组 B[0,1,…,n-1]，其中 B[i] 的值是数组 A 中除了下标 i 以外的元素的积,
 * 即 B[i]=A[0]×A[1]×…×A[i-1]×A[i+1]×…×A[n-1]。
 *
 * 「不能使用除法」。
 *
 * 例 1：
 * 输入: [1,2,3,4,5]
 * 输出: [120,60,40,30,24]
 *
 * 约束：
 * - 所有元素乘积之和不会溢出 32 位整数
 * - a.length <= 100000
 */
public class Offer66_Medium_BuildingProductArray {

    static void test(UnaryOperator<int[]> method) {
        assertArrayEquals(new int[]{120,60,40,30,24}, method.apply(new int[]{1,2,3,4,5}));
        assertArrayEquals(new int[]{3}, method.apply(new int[]{3}));
        assertArrayEquals(new int[]{2,5}, method.apply(new int[]{5,2}));
    }

    public int[] constructArr(int[] a) {
        int n = a.length;
        if (n <= 1) {
            return a;
        }

        /*
        构造前缀后缀数组。
        prefix[i] = a[0]*..*a[i-1]
        postfix[i] = a[i+1]*..*a[n-1]
        */
        int[] prefix = new int[n];
        int[] postfix = new int[n];
        prefix[0] = 1;
        postfix[n - 1] = 1;

        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] * a[i - 1];
            postfix[n - i - 1] = postfix[n - i] * a[n - i];
        }

        for (int i = 0; i < n; i++) {
            a[i] = prefix[i] * postfix[i];
        }

        return a;
    }

    @Test
    public void testConstructArr() {
        test(this::constructArr);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/gou-jian-cheng-ji-shu-zu-lcof/solution/mian-shi-ti-66-gou-jian-cheng-ji-shu-zu-biao-ge-fe/
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：51.1 MB - 52.79%
     */
    public int[] betterMethod(int[] a) {
        int n = a.length;
        if (n < 2) {
            return a;
        }

        int[] result = new int[n];
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * a[i - 1];
        }

        int post = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= post;
            post *= a[i];
        }

        return result;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
