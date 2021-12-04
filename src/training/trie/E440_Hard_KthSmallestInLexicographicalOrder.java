package training.trie;

import org.junit.jupiter.api.Test;

import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 440. 字典序的第K小数字: https://leetcode-cn.com/problems/k-th-smallest-in-lexicographical-order/
 *
 * 给定整数 n 和 k，找到 1 到 n 中字典序第 k 小的数字。
 * 注意：1 ≤ k ≤ n ≤ 10^9。
 *
 * 例 1：
 * 输入:
 * n: 13   k: 2
 * 输出:
 * 10
 * 解释:
 * 字典序的排列是 [1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7, 8, 9]，所以第二小的数字是 10。
 */
public class E440_Hard_KthSmallestInLexicographicalOrder {

    public static void test(IntBinaryOperator method) {
        assertEquals(10, method.applyAsInt(13, 2));
        assertEquals(2, method.applyAsInt(10, 3));
    }

    /**
     * 超时
     */
    public int findKthNumber(int n, int k) {
        /*
        n=3xxxx
        1 10 100 1000 10000
        10001 10002 ... 10009
        1001 10010 10011 ... 10019
        1002 10020 10021 ... 10029
        ..
        1009 10090 10091 ... 10099
        101 1010 10100 10101 ... 10109
        1011 10110 10111 ... 10119
        1012 10120 10121 ... 10129
        ...

        观察上面的数字可以发现规律：
        1. 设数字为 i，如果 i * 10 小于等于 n，那么 i *= 10；
        2. 否则如果 i + 1 小于等于 n，那么 i += 1，然后去掉末尾的 0，下次走第 1 步
        3. 否则 i /= 10，然后 i += 1，然后去掉末尾的 0，下次走第 1 步
         */

        long result = 1;
        for (int i = 1; i < k; i++) {
            if (result * 10 <= (long) n) {
                result *= 10;
            } else {
                if (result + 1 > (long) n) {
                    result /= 10;
                }
                result += 1;
                while (result % 10 == 0) {
                    result /= 10;
                }
            }
        }

        return (int) result;
    }

    @Test
    public void testFindKthNumber() {
        test(this::findKthNumber);
    }


    /**
     *
     * 字典树，参见：
     * https://leetcode-cn.com/problems/k-th-smallest-in-lexicographical-order/solution/ben-ti-shi-shang-zui-wan-zheng-ju-ti-de-shou-mo-sh/
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.1 MB - 61.09%
     */
    public int dictTreeMethod(int n, int k) {
        // 作为一个指针，指向当前所在位置，当p==k时，也就是到了排位第k的数
        int p = 1;
        int prefix = 1;
        while (p < k) {
            // 获得当前前缀下所有子节点的和
            int count = getCount(prefix, n);
            // 第k个数在当前前缀下
            if (p + count > k) {
                // 到子树下面去
                prefix *= 10;
                // 把指针指向了第一个子节点的位置，比如11乘10后变成110，指针从11指向了110
                p++;
            } else {  // 第k个数不在当前前缀下
                // 移到右兄弟
                prefix++;
                // 把指针指向了下一前缀的起点
                p += count;
            }
        }

        return prefix;
    }

    /**
     * 查找当前前缀下的子树节点数量，这也就是 prefix + 1（和 prefix 相邻的右兄弟）下的起点（最小数）减去 prefix 的起点
     */
    private int getCount(int prefix, int n) {
        // 使用 long 防止溢出
        long cur = prefix, next = prefix + 1, count = 0;
        while (cur <= n) {
            // 下一个前缀的起点减去当前前缀的起点
            count += Math.min(n + 1, next) - cur;
            cur *= 10;
            next *= 10;
            // 如果说刚刚prefix是1，next是2，那么现在分别变成10和20
            // 1为前缀的子节点增加10个，十叉树增加一层, 变成了两层

            // 如果说现在prefix是10，next是20，那么现在分别变成100和200，
            // 1为前缀的子节点增加100个，十叉树又增加了一层，变成了三层

            // 这是10叉树，每增加一层乘以10，和二叉树没增加一层乘以2类似
        }

        return (int) count;
    }

    @Test
    public void testDictTreeMethod() {
        test(this::dictTreeMethod);
    }
}
