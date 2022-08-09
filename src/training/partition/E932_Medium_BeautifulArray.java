package training.partition;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * 932. 漂亮数组: https://leetcode-cn.com/problems/beautiful-array/
 *
 * 对于某些固定的 N，如果数组 A 是整数 1, 2, ..., N 组成的排列，使得：
 * 对于每个 i < j，都不存在 k 满足 i < k < j 使得 A[k] * 2 = A[i] + A[j]。
 * 那么数组 A 是漂亮数组。
 *
 * 给定 N，返回任意漂亮数组 A（保证存在一个）。
 *
 * 例 1：
 * 输入：4
 * 输出：[2,1,4,3]
 *
 * 例 2：
 * 输入：5
 * 输出：[3,1,2,5,4]
 *
 * 说明：
 * - 1 <= N <= 1000
 */
public class E932_Medium_BeautifulArray {

    public static void assertBeautifulArray(int[] arr) {
        final int n = arr.length;
        // 检查 arr 是否由 1 到 N 的数字组成
        boolean[] visited = new boolean[n];
        for (int i : arr) {
            if (i < 1 || i > n || visited[i - 1]) {
                throw new AssertionError("not N sequence: " + Arrays.toString(arr));
            }
            visited[i - 1] = true;
        }
        // 检查 arr 是不是漂亮数组
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 2; j < n; j++) {
                for (int k = i + 1; k < j; k++) {
                    if (arr[k] * 2 == arr[i] + arr[j]) {
                        throw new AssertionError("not beautiful array: " + Arrays.toString(arr));
                    }
                }
            }
        }
    }

    public static void test(IntFunction<int[]> method) {
        assertBeautifulArray(method.apply(4));
        assertBeautifulArray(method.apply(5));
        assertBeautifulArray(method.apply(1));
        assertBeautifulArray(method.apply(2));
    }

    /**
     * 参见：
     * https://leetcode-cn.com/problems/beautiful-array/solution/c-fen-zhi-fa-dai-tu-pian-jie-shi-by-avphn4vwuo/
     *
     * 首先注意到两个事实:
     * - 奇数 + 偶数 = 奇数
     * - 如果{ X, Y, Z }是一个漂亮数组，则{ k * X + b, k * Y + b, k * Z + b } 也一定是漂亮数组。
     *   因为如果 2∗Y ≠ X+Z，则 2∗(k∗Y+b) ≠ k∗X+b+k∗Z+b 一定成立
     *
     * 对于一个正整数 N, 我们将其等分为两部分，left和right， 如果left部分是漂亮数组，right部分也是漂亮数组，
     * 同时left部分全部是奇数，right部分全部是偶数，那么此时left + right组成的数组一定也是一个漂亮数组。
     * 1. 所以可以采用分治算法，自顶向下
     * 2. 先将数组分为两部分，将奇数放在left，偶数放在right
     * 3. 同时保证left和right都是漂亮数组
     * 4. 递归...
     *
     * 如果我现在知道了整数 N 的漂亮数组，那么通过k * N + b的变换可以让N变成2N的奇部（前半部分left),
     * 同样通过 k * N + b的变换可以让N变成 2N的偶部（后半部分right)，只不过k和b可能取不同的值。
     *
     * 因此我们将所有的奇数放在 left 部分，所有的偶数放在 right 部分，这样可以保证等式恒不成立。
     * 对于 [1..N] 的排列，left 部分包括 (N + 1) / 2 个奇数，right 部分包括 N / 2 个偶数。
     * 对于 left 部分，我们进行 k = 1/2, b = 1/2 的仿射变换，把这些奇数一一映射到不超过 (N + 1) / 2 的整数。
     * 对于 right 部分，我们进行 k = 1/2, b = 0 的仿射变换，把这些偶数一一映射到不超过 N / 2 的整数。
     * 经过映射，left 和 right 部分变成了和原问题一样，但规模减少一半的子问题，这样就可以使用分治算法解决了。
     * 回溯的时候再将left、right变回来，拼接在一起就能得到最后的结果。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：39.7 MB - 82.14%
     */
    public int[] beautifulArray(int n) {
        return partition(n);
    }

    private static final Map<Integer, int[]> n2beauti = new HashMap<>();

    private int[] partition(int n) {
        if (n2beauti.containsKey(n)) {
            return n2beauti.get(n);
        }

        int[] result = new int[n];
        if (n == 1) {
            result[0] = 1;
        } else {
            int i = 0;
            // 奇数
            for (int x: partition((n + 1) / 2)) {
                // 反向变换
                result[i++] = 2 * x - 1;
            }
            // 偶数
            for (int x: partition(n / 2)) {
                result[i++] = 2 * x;
            }
        }
        n2beauti.put(n, result);

        return result;
    }

    @Test
    public void testBeautifulArray() {
        test(this::beautifulArray);
    }
}
