package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 421. 数组中两个数的最大异或值: https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/
 *
 * 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
 *
 * 进阶：你可以在 O(n) 的时间解决这个问题吗？
 *
 * 例 1：
 * 输入：nums = [3,10,5,25,2,8]
 * 输出：28
 * 解释：最大运算结果是 5 XOR 25 = 28.
 *
 * 例 2：
 * 输入：nums = [0]
 * 输出：0
 *
 * 例 3：
 * 输入：nums = [2,4]
 * 输出：6
 *
 * 例 4：
 * 输入：nums = [8,10,2]
 * 输出：10
 *
 * 例 5：
 * 输入：nums = [14,70,53,83,49,91,36,80,92,51,66,70]
 * 输出：127
 *
 * 说明：
 * - 1 <= nums.length <= 2 * 10^4
 * - 0 <= nums[i] <= 2^31 - 1
 */
public class E421_Medium_MaximumXOROfTwoNumbersInArray {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(28, method.applyAsInt(new int[]{3,10,5,25,2,8}));
        assertEquals(0, method.applyAsInt(new int[]{0}));
        assertEquals(6, method.applyAsInt(new int[]{2,4}));
        assertEquals(10, method.applyAsInt(new int[]{8,10,2}));
        assertEquals(127, method.applyAsInt(new int[]{14,70,53,83,49,91,36,80,92,51,66,70}));
    }

    /**
     * 假设我们在数组中选择了元素 ai 和 aj，使得它们达到最大的按位异或运算结果 x：
     *      x = ai ^ aj
     *
     * 根据按位异或运算的性质，上面的式子等价于 aj = x ^ ai。我们可以根据这一变换，设计一种
     * 「从高位到低位依次确定 x 二进制表示的每一位」的方法，以此得到 x 的值。该方法的精髓在于：
     * - 由于数组中的元素都在 [0, 2^31) 的范围内，那么我们可以将每一个数表示为一个长度为 31 位的二进制数
     * - 这 31 个二进制位从低位到高位依次编号为 0,1,⋯,30。我们从最高位第 30 个二进制位开始，
     *   依次确定 x 的每一位是 0 还是 1；
     * - 由于我们需要找出最大的 x，因此在枚举每一位时，我们先判断 x 的这一位是否能取到 1。如果能，
     *   我们取这一位为 1，否则我们取这一位为 0。
     *
     * 「判断 x 的某一位是否能取到 1」这一步骤并不容易。下面是一种方法，哈希表法，参见：
     * https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/solution/shu-zu-zhong-liang-ge-shu-de-zui-da-yi-h-n9m9/
     *
     * 哈希表法用到了「前缀异或」的思想，假设我们已经确定了 x 最高的若干个二进制位，当前正在确定第 k 个二进制位。根据「前言」部分的分析，
     * 我们希望第 k 个二进制位能够取到 1。
     *
     * 我们用 prek(x) 表示 x 从最高位第 30 个二进制位开始，到第 k 个二进制位为止的数，那么 aj = x ^ ai 蕴含着：
     *      prek(aj) = prek(x) ^ prek(ai)
     *
     * 由于 prek(x) 对我们来说是已知的，因此我们将所有的 prek(aj) 放入哈希表中，随后枚举 i 并计算 prek(x) ^ prek(ai)。
     * 如果其出现在哈希表中，那么说明第 k 个二进制位能够取到 1，否则第 k 个二进制位只能为 0。
     *
     * LeetCode 耗时：209 ms - 24.83%
     *          内存消耗：63 MB - 21.38%
     */
    public int findMaximumXOR(int[] nums) {
        int x = 0;
        for (int k = 30; k >= 0; k--) {
            // 将所有的 prek(aj) 放入哈希表中
            Set<Integer> set = new HashSet<>();
            for (int num : nums) {
                set.add(num >> k);
            }

            // 目前 x 包含从最高位开始到第 k+1 个二进制位为止的部分
            // 我们将 x 的第 k 个二进制位置为 1，即为 x = x*2+1
            int next = 2 * x + 1;
            boolean found = false;
            // 枚举 i
            for (int num : nums) {
                if (set.contains(next ^ (num >> k))) {
                    found = true;
                    break;
                }
            }

            if (found) {
                x = next;
            } else {
                // 如果没有找到满足等式的 a_i 和 a_j，那么 x 的第 k 个二进制位只能为 0
                // 即为 x = x*2
                x = next - 1;
            }
        }

        return x;
    }

    @Test
    public void testFindMaximumXOR() {
        test(this::findMaximumXOR);
    }


    /**
     * 和上面的思路类似，只不过这次使用的是 Trie，参见：
     * https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/solution/shu-zu-zhong-liang-ge-shu-de-zui-da-yi-h-n9m9/
     *
     * 根据 x = ai ^ aj，我们枚举 ai, 并将 a0, a1, ..., a(i-1) 作为 aj 放入 trie 中，希望找出使得 x 达到最大值的 aj。
     *
     * 如何求出 x 呢？我们可以从字典树的根节点开始进行遍历，遍历的「参照对象」为 ai。在遍历的过程中，我们根据 ai 的第 x 个二进制位是 0 还是 1，
     * 确定我们应当走向哪个子节点以继续遍历。假设我们当前遍历到了第 k 个二进制位：
     * - 如果 ai 的第 k 个二进制位为 0，那么我们应当往表示 1 的子节点走，这样 0^1=1，可以使得 x 的第 k 个二进制位为 1。
     *   如果不存在表示 1 的子节点，那么我们只能往表示 0 的子节点走，x 的第 k 个二进制位为 0；
     * - 如果 ai 的第 k 个二进制位为 1，那么我们应当往表示 0 的子节点走，这样 1^0=1，可以使得 x 的第 k 个二进制位为 1。
     *   如果不存在表示 0 的子节点，那么我们只能往表示 1 的子节点走，x 的第 k 个二进制位为 0；
     *
     * 当遍历完所有的 31 个二进制位后，我们也就得到了 ai 可以通过异或运算得到的最大 x。
     *
     * LeetCode 耗时：77 ms - 88.31%
     *          内存消耗：58.3 MB - 57.37%
     */
    public int trieMethod(int[] nums) {
        Trie root = new Trie();
        int x = 0;
        for (int i = 1; i < nums.length; i++) {
            root.insert(nums[i - 1]);
            x = Math.max(x, root.check(nums[i]));
        }

        return x;
    }

    public static class Trie {
        private Trie zero;
        private Trie one;

        public void insert(int num) {
            Trie cur = this;
            for (int k = 30; k >= 0; k--) {
                int bit = (num >> k) & 1;
                if (bit == 0) {
                    if (cur.zero == null) {
                        cur.zero = new Trie();
                    }
                    cur = cur.zero;
                } else {
                    if (cur.one == null) {
                        cur.one = new Trie();
                    }
                    cur = cur.one;
                }
            }
        }

        public int check(int num) {
            Trie cur = this;
            int x = 0;
            for (int k = 30; k >= 0; k--) {
                int bit = (num >> k) & 1;
                if (bit == 0) {
                    if (cur.one != null) {
                        cur = cur.one;
                        x = x * 2 + 1;
                    } else {
                        cur = cur.zero;
                        x = x * 2;
                    }
                } else {
                    if (cur.zero != null) {
                        cur = cur.zero;
                        x = x * 2 + 1;
                    } else {
                        cur = cur.one;
                        x = x * 2;
                    }
                }
            }

            return x;
        }
    }

    @Test
    public void testTrieMethod() {
        test(this::trieMethod);
    }
}
