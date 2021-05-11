package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 传送带上的包裹必须在 D 天内从一个港口运送到另一个港口。
 *
 * 传送带上的第 i 个包裹的重量为 weights[i]。每一天，我们都会「按给出重量的顺序」往传送带上装载包裹。
 * 我们装载的重量不会超过船的最大运载重量。
 *
 * 返回能在 D 天内将传送带上的所有包裹送达的船的最低运载能力。
 *
 * 例 1：
 * 输入：weights = [1,2,3,4,5,6,7,8,9,10], D = 5
 * 输出：15
 * 解释：
 * 船舶最低载重 15 就能够在 5 天内送达所有包裹，如下所示：
 * 第 1 天：1, 2, 3, 4, 5
 * 第 2 天：6, 7
 * 第 3 天：8
 * 第 4 天：9
 * 第 5 天：10
 *
 * 例 2：
 * 输入：weights = [3,2,2,4,1,4], D = 3
 * 输出：6
 * 解释：
 * 船舶最低载重 6 就能够在 3 天内送达所有包裹，如下所示：
 * 第 1 天：3, 2
 * 第 2 天：2, 4
 * 第 3 天：1, 4
 *
 * 例 3：
 * 输入：weights = [1,2,3,1,1], D = 4
 * 输出：3
 * 解释：
 * 第 1 天：1
 * 第 2 天：2
 * 第 3 天：3
 * 第 4 天：1, 1
 *
 * 约束：
 * - 1 <= D <= weights.length <= 5 * 10**4
 * - 1 <= weights[i] <= 500
 */
public class E1011_Medium_CapacityToShipPackagesWithinDays {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(15, method.applyAsInt(new int[]{1,2,3,4,5,6,7,8,9,10}, 5));
        assertEquals(6, method.applyAsInt(new int[]{3,2,2,4,1,4}, 3));
        assertEquals(3, method.applyAsInt(new int[]{1,2,3,1,1}, 4));
    }

    /**
     * 1011. 在 D 天内送达包裹的能力: https://leetcode-cn.com/problems/capacity-to-ship-packages-within-d-days/
     *
     * LeetCode 耗时：8 ms - 97.63%
     *          内存消耗：41.7 MB - 16.31%
     */
    public int shipWithinDays(int[] weights, int D) {
        int max = 0, sum = 0;
        for (int weight : weights) {
            if (max < weight)
                max = weight;
            sum += weight;
        }
        if (D == 1)
            return sum;
        if (D == weights.length)
            return max;

        int lo = max, hi = sum;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (canTransfer(weights, D, mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return lo;
    }

    private boolean canTransfer(int[] weights, int D, int capacity) {
        int load = 0;
        int i = 0;
        for (int j = 0; j < D; j++) {
            for (; i < weights.length && load + weights[i] <= capacity; i++) {
                load += weights[i];
            }
            if (i == weights.length)
                break;
            load = 0;
        }

        return i == weights.length;
    }

    @Test
    public void testShipWithinDays() {
        test(this::shipWithinDays);
    }
}
