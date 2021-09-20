package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 134. 加油站: https://leetcode-cn.com/problems/gas-station/
 *
 * 在一条环路上有 N 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
 *
 * 你有一辆油箱容量无限的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
 * 你从其中的一个加油站出发，开始时油箱为空。
 *
 * 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
 *
 * 例 1：
 * 输入:
 * [1,3,6,10,15]
 * [3,7,12,13,15]
 *
 * [15,14,12,9,5]
 * [15,12,8,3,2]
 *
 * gas  = [1,2,3,4,5]
 * cost = [3,4,5,1,2]
 * 输出: 3
 * 解释:
 * 从 3 号加油站(索引为 3 处)出发，可获得 4 升汽油。此时油箱有 = 0 + 4 = 4 升汽油
 * 开往 4 号加油站，此时油箱有 4 - 1 + 5 = 8 升汽油
 * 开往 0 号加油站，此时油箱有 8 - 2 + 1 = 7 升汽油
 * 开往 1 号加油站，此时油箱有 7 - 3 + 2 = 6 升汽油
 * 开往 2 号加油站，此时油箱有 6 - 4 + 3 = 5 升汽油
 * 开往 3 号加油站，你需要消耗 5 升汽油，正好足够你返回到 3 号加油站。
 * 因此，3 可为起始索引。
 *
 * 例 2：
 * 输入:
 * gas  = [2,3,4]
 * cost = [3,4,3]
 * 输出: -1
 * 解释:
 * 你不能从 0 号或 1 号加油站出发，因为没有足够的汽油可以让你行驶到下一个加油站。
 * 我们从 2 号加油站出发，可以获得 4 升汽油。 此时油箱有 = 0 + 4 = 4 升汽油
 * 开往 0 号加油站，此时油箱有 4 - 3 + 2 = 3 升汽油
 * 开往 1 号加油站，此时油箱有 3 - 3 + 3 = 3 升汽油
 * 你无法返回 2 号加油站，因为返程需要消耗 4 升汽油，但是你的油箱只有 3 升汽油。
 * 因此，无论怎样，你都不可能绕环路行驶一周。
 *
 * 约束：
 * - 如果题目有解，该答案即为唯一答案。
 * - 输入数组均为非空数组，且长度相同。
 * - 输入数组中的元素均为非负数。
 */
public class E134_Medium_GasStation {

    static void test(ToIntBiFunction<int[], int[]> method) {
        assertEquals(3, method.applyAsInt(new int[]{1,2,3,4,5}, new int[]{3,4,5,1,2}));
        assertEquals(-1, method.applyAsInt(new int[]{2,3,4}, new int[]{3,4,3}));
    }

    /**
     * 暴力法。
     *
     * LeetCode 耗时：50 ms - 20.16%
     *          内存消耗：38.4 MB - 87.05%
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int sumGas = 0, sumCost = 0;
        for (int i = 0; i < gas.length; i++) {
            sumGas += gas[i];
            sumCost += cost[i];
        }
        if (sumGas < sumCost) {
            return -1;
        }

        for (int i = 0; i < gas.length; i++) {
            int totalGas = 0, len = 0;
            while (len < gas.length && totalGas + gas[(i + len) % gas.length] >= cost[(i + len) % gas.length]) {
                totalGas += gas[(i + len) % gas.length] - cost[(i + len) % gas.length];
                len++;
            }
            if (len == gas.length) {
                return i;
            }
        }

        return -1;
    }

    @Test
    public void testCanCompleteCircuit() {
        test(this::canCompleteCircuit);
    }


    /**
     * 数学证明法，参见：https://leetcode-cn.com/problems/gas-station/solution/jia-you-zhan-by-leetcode-solution/
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.7 MB - 48.96%
     */
    public int mathMethod(int[] gas, int[] cost) {
        int n = gas.length;
        for (int i = 0; i < n;) {
            int sumOfGas = 0, sumOfCost = 0;
            int cnt = 0;
            while (cnt < n) {
                int j = (i + cnt) % n;
                sumOfGas += gas[j];
                sumOfCost += cost[j];
                if (sumOfCost > sumOfGas) {
                    break;
                }
                cnt++;
            }
            if (cnt == n) {
                return i;
            } else {
                i = i + cnt + 1;
            }
        }

        return -1;
    }

    @Test
    public void testMathMethod() {
        test(this::mathMethod);
    }
}
