package training.partition;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 1982. 从子集的和还原数组: https://leetcode-cn.com/problems/find-array-given-subset-sums/
 *
 * 存在一个未知数组需要你进行还原，给你一个整数 n 表示该数组的长度。另给你一个数组 sums ，
 * 由未知数组中全部 2^n 个「子集的和」组成（子集中的元素没有特定的顺序）。
 *
 * 返回一个长度为 n 的数组 ans 表示还原得到的未知数组。如果存在「多种」答案，只需返回其中「任意一个」。
 *
 * 如果可以由数组 arr 删除部分元素（也可能不删除或全删除）得到数组 sub ，那么数组 sub 就是数组 arr 的一个「子集」。
 * sub 的元素之和就是 arr 的一个「子集的和」。一个空数组的元素之和为 0。
 *
 * 注意：生成的测试用例将保证至少存在一个正确答案。
 *
 * 例 1：
 * 输入：n = 3, sums = [-3,-2,-1,0,0,1,2,3]
 * 输出：[1,2,-3]
 * 解释：[1,2,-3] 能够满足给出的子集的和：
 * - []：和是 0
 * - [1]：和是 1
 * - [2]：和是 2
 * - [1,2]：和是 3
 * - [-3]：和是 -3
 * - [1,-3]：和是 -2
 * - [2,-3]：和是 -1
 * - [1,2,-3]：和是 0
 * 注意，[1,2,-3] 的任何排列和 [-1,-2,3] 的任何排列都会被视作正确答案。
 *
 * 例 2：
 * 输入：n = 2, sums = [0,0,0,0]
 * 输出：[0,0]
 * 解释：唯一的正确答案是 [0,0] 。
 *
 * 例 3：
 * 输入：n = 4, sums = [0,0,5,5,4,-1,4,9,9,-1,4,3,4,8,3,8]
 * 输出：[0,-1,4,5]
 * 解释：[0,-1,4,5] 能够满足给出的子集的和。
 *
 * 说明：
 * - 1 <= n <= 15
 * - sums.length == 2^n
 * - -10^4 <= sums[i] <= 10^4
 */
public class E1982_Hard_FindArrayGivenSubsetSums {

    public static void assertSubsetSums(int[] sums, int[] actual) {
        List<Integer> actualSums = new ArrayList<>(1 << actual.length);
        subsetSums(actualSums, actual, 0, 0);
        actualSums.sort(null);

        List<Integer> expectedSums = Arrays.stream(sums).sorted().boxed().collect(Collectors.toList());

        if (!actualSums.equals(expectedSums)) {
            throw new AssertionError("actual=" + Arrays.toString(actual));
        }
    }

    private static void subsetSums(List<Integer> sums, int[] origin, int i, int path) {
        if (i == origin.length) {
            sums.add(path);
            return;
        }
        subsetSums(sums, origin, i + 1, path);
        subsetSums(sums, origin, i + 1, path + origin[i]);
    }

    public static void test(BiFunction<Integer, int[], int[]> method) {
        int[] sums = {-3,-2,-1,0,0,1,2,3};
        assertSubsetSums(sums, method.apply(3, sums));

        sums = new int[]{0,0,0,0};
        assertSubsetSums(sums, method.apply(2, sums));

        sums = new int[]{0,0,5,5,4,-1,4,9,9,-1,4,3,4,8,3,8};
        assertSubsetSums(sums, method.apply(4, sums));
    }

    /**
     * 参见：
     * https://leetcode-cn.com/problems/find-array-given-subset-sums/solution/cong-zi-ji-de-he-huan-yuan-shu-zu-by-lee-aj8o/
     *
     * 思考一：
     * 对于 n 个数构成的长度为 2^n 的子集和数组 sums，设 sums 中的最小值为 x，次小值为 y（x 和 y 可以相等），
     * 那么 x-y 和 y-x 二者中至少有一个数出现在这 n 个数中。
     *
     * 显然，sums 中的最小值 x 等于这 n 个数中所有负数之和。如果没有负数，x=0。
     * 那么次小值 y 应该等于哪些数之和呢？我们可以知道次小值应该是下面两种情况中的一种：
     * - 在最小值的基础上，额外选择了一个最小的非负数；
     * - 在最小值的基础上，移除了一个最大的负数。
     *
     * 对于第一种「选择非负数」的情况，这个数的值即为 y-x；对于第二种「移除负数」的情况，这个数的值即为 x-y。
     * 因此 x−y 和 y−x 二者中至少有一个数出现在这 n 个数中。
     *
     *
     * 思考二：
     * 记 d=y−x。
     *
     * 我们可以将这 2^n 个子集和分成两部分 S 和 T，每一部分的长度均为 2^{n-1}，
     * 并且对于任意一个在 S 中出现的子集和 x_0，在 T 中一定出现了 x_0+d；
     * 同时对于任意一个在 T 中出现的子集和 y_0，在 S 中一定出现了 y_0-d。
     *
     * 对于第一种「选择非负数」的情况，这个数的值即为 d。我们考虑两种不同类型的子集和：
     * - 第一种类型的子集和不包含 d，那么我们需要在剩余的 n-1 个数中进行选择，一共有 2^{n-1} 种选择方法，
     *   对应的 2^{n-1} 个子集和的集合记为 S；
     * - 第二种类型的子集和包含 d，除了 d 以外，我们同样需要在剩余的 n−1 个数中进行选择，一共有 2^{n-1} 种选择方法，
     *   对应 2^{n-1} 个子集和的集合记为 T。由于 T 中的每个子集和相当于在 S 中选择了一个子集和再加上 d，
     *   因此是满足思考二的。
     *
     * 对于第二种「移除负数」的情况，这个数的值即为 −d，讨论是类似的：
     * - 第一种类型的子集和包含 −d，对应的 2^{n-1} 个子集和的集合记为 S；
     * - 第二种类型的子集和不包含 −d，由于 T 中的每个子集和相当于在 S 中选择了一个子集和再减去 −d，
     *   因此同样是满足思考二的。
     *
     * 对于给定的 2^n 个子集和，要想在实际的代码中得到 S 和 T，我们可以将子集和进行升序排序，
     * 随后选择最小的子集和 x_0 放入 S，那么 T 中就对应着有一个子集和 x_0+d。
     * 我们使用双指针或哈希表等数据结构将这两个子集和移除，总计进行 2^{n-1} 次选择即可得到 S 和 T。
     *
     *
     * 根据思考一和思考二，我们就可以设计出还原数组的算法了。
     *
     * 对于 n 个数构成的长度为 2^n 的子集和数组 sums，设 sums 中的最小值为 x，次小值为 y。记 d=y−x，
     * 我们使用提示 2 中的方法将 sums 分成两个长度为 2^{n-1} 的数组 S 和 T。
     *
     * 由于我们并不知道子集和中包含的是 d 还是 −d，因此我们需要对 S 和 T 分别进行尝试：
     * - 如果子集和中包含 d，那么 S 中是除了 d 以外的 n−1 个数的子集和，因此我们递归地求解 (n−1,S) 这一子问题；
     * - 如果子集和中包含 −d，那么 T 中是除了 −d 以外的 n−1 个数的子集和，因此我们递归地求解 (n−1,T) 这一子问题。
     *
     * 当我们递归到 n=1 时，sums 中包含两个整数，其中一个必然为 0，另一个就是我们剩下的最后一个数。
     * 如果 sums 满足该要求，说明我们递归求解该问题成功，否则失败。
     *
     * LeetCode 耗时：59 ms - 64.29%
     *          内存消耗：49.8 MB - 14.29%
     */
    public int[] recoverArray(int n, int[] sums) {
        Arrays.sort(sums);
        List<Integer> list = recover(n, n, sums);
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = list.get(i);
        }

        return ans;
    }

    private List<Integer> recover(int len, int n, int[] sums) {
        if (n == 1) {
            if (sums[0] == 0) {
                List<Integer> ans = new ArrayList<>(len);
                ans.add(sums[1]);
                return ans;
            }
            if (sums[1] == 0) {
                List<Integer> ans = new ArrayList<>(len);
                ans.add(sums[0]);
                return ans;
            }
            return Collections.emptyList();
        }

        int d = sums[1] - sums[0];
        int[] s = new int[1 << (n - 1)], t = new int[s.length];
        int i = 0, j = 0, left = 0, right = 0;
        boolean[] visited = new boolean[sums.length];
        while (true) {
            while (left < sums.length && visited[left]) {
                left++;
            }
            if (left == sums.length) {
                break;
            }
            s[i++] = sums[left];
            visited[left] = true;
            // right 指针找到 sums[left] + d
            while (visited[right] || sums[right] != sums[left] + d) {
                ++right;
            }
            t[j++] = sums[right];
            visited[right] = true;
        }

        List<Integer> ans = recover(len, n - 1, s);
        if (ans.size() > 0) {
            ans.add(d);
            return ans;
        }
        ans = recover(len, n - 1, t);
        if (ans.size() > 0) {
            ans.add(-d);
            return ans;
        }

        return Collections.emptyList();
    }

    @Test
    public void testRecoverArray() {
        test(this::recoverArray);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/find-array-given-subset-sums/solution/cong-zi-ji-de-he-huan-yuan-shu-zu-by-lee-aj8o/
     *
     * 在上一个方法中，有一步非常重要的决策是：由于我们并不知道子集和中包含的是 d 还是 −d，
     * 因此我们需要对 S 和 T 分别进行尝试。
     * 如果我们能够直接确定子集和中包含的是 d 还是 -d，就可以省去两路递归的过程了。
     *
     * 本质上来说，确定是 d 还是 −d，实际上就是要找出到底 S 和 T 中的哪一个是剩余的 n−1 个数的子集和。
     * 我们知道的是，这个子集和一定是包含 0 值的（因为我们可以不选择任何元素），而另一个子集和不一定包含 0 值，
     * 因为我们必须选择 d 或 −d。
     *
     * 因此，如果 S 和 T 中只有一个集合包含 0 值，我们就能直接确定其就是剩余的 n−1 个数的子集和。如果其为 S，
     * 原数组中就包含 d；如果其为 T，原数组中就包含 −d。
     *
     * 那么如果 S 和 T 均包含 0 值，我们应该如何进行选择呢？实际上，我们可以选择任意一个，这也是可以证明的：
     * - 我们规定当 S 和 T 均包含 0 值时，我们一定选择 S，并将 d 放入原数组中；
     * - 如果原数组确实是包含 d 的，那么我们「猜对了」，就不会有任何问题；
     * - 如果原数组不包含 d，那么一定包含 −d。此时，S 表示剩余的 n−1 个数的子集和（也就是 T）加上 −d，但 S 中又有 0 值，
     *   因此原数组中一定存在若干个我们还未还原的元素，记为 o_0,o_1,···,o_{k-1}，使得
     *                  o_0+o_1+···+o_{k-1}+o_k = 0
     *   这里多出的 o_k 就代表 −d。由于这 k+1 个元素之和为 0，那么我们将它们全部取相反数，记 o'_i = -o_i，
     *   那么所有 o'_i 的和仍然为 0，并且对于任意的子集和，如果其包含的在 o_0,···,o_k 内的元素的下标集合为 A：
     *                  sum(o_i) = a, i ∈ A
     *   那么在新的 o'_0,···, o'_k 中，我们只要选择下标的补集 A'={0,1,⋯,k}\A，那么：
     *                  sum(o'_i)_{i∈A'} = 0 - sum(o'_i)_{i∈A} = 0 - (-a) = a
     *   这样一来，o_0,···,o_k 在子集和中的选择与 o'_0,···,o'_k「一一对应」，即原数组中包含 o_0,···,o_k
     *   与包含 o'_0,···,o'_k 是等价的。而 o'_k = d，因此我们一定可以选择 d。
     *
     * LeetCode 耗时：55 ms - 85.71%
     *          内存消耗：49.2MB - 90%
     */
    public int[] betterMethod(int n, int[] sums) {
        Arrays.sort(sums);
        int[] ans = new int[n];
        int ai = 0;

        while (sums.length > 2) {
            int d = sums[1] - sums[0];
            int[] s = new int[sums.length >> 1], t = new int[s.length];
            int i = 0, j = 0, left = 0, right = 0;
            boolean[] visited = new boolean[sums.length];
            while (true) {
                while (left < sums.length && visited[left]) {
                    left++;
                }
                if (left == sums.length) {
                    break;
                }
                s[i++] = sums[left];
                visited[left] = true;
                // right 指针找到 sums[left] + d
                while (visited[right] || sums[right] != sums[left] + d) {
                    ++right;
                }
                t[j++] = sums[right];
                visited[right] = true;
            }
            if (Arrays.binarySearch(s, 0) >= 0) {
                ans[ai++] = d;
                sums = s;
            } else {
                ans[ai++] = -d;
                sums = t;
            }
        }
        // 迭代到 n=1 时，数组中必然一个为 0，另一个为剩下的最后一个数
        ans[ai] = sums[0] + sums[1];

        return ans;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
