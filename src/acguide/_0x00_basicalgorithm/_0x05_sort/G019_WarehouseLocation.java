package acguide._0x00_basicalgorithm._0x05_sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 货仓选址：https://www.acwing.com/problem/content/106/
 *
 * 在一条数轴上有 N 家商店，它们的坐标分别为 A_1∼A_N。
 * 现在需要在数轴上建立一家货仓，每天清晨，从货仓到每家商店都要运送一车商品。
 *
 * 为了提高效率，求把货仓建在何处，可以使得货仓到每家商店的距离之和最小。
 * 输出一个整数，表示距离之和的最小值。
 *
 * 例 1：
 * 输入：
 * loc=[6, 2, 9, 1]
 * 输出：
 * 12
 *
 * 说明：
 * - 1 ≤ N ≤ 100000,
 * - 0 ≤ A_i ≤ 40000
 */
public class G019_WarehouseLocation {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(12, method.applyAsInt(new int[]{6, 2, 9, 1}));
        assertEquals(800160, method.applyAsInt(new int[]{41, 18467, 6334, 26500, 19169, 15724, 11478, 29358, 26962,
                24464, 5705, 28145, 23281, 16827, 9961, 491, 2995, 11942, 4827, 5436, 32391, 14604, 3902, 153, 292, 12382,
                17421, 18716, 19718, 19895, 5447, 21726, 14771, 11538, 1869, 19912, 25667, 26299, 17035, 9894, 28703, 23811,
                31322, 30333, 17673, 4664, 15141, 7711, 28253, 6868, 25547, 27644, 32662, 32757, 20037, 12859, 8723, 9741, 27529,
                778, 12316, 3035, 22190, 1842, 288, 30106, 9040, 8942, 19264, 22648, 27446, 23805, 15890, 6729, 24370, 15350,
                15006, 31101, 24393, 3548, 19629, 12623, 24084, 19954, 18756, 11840, 4966, 7376, 13931, 26308, 16944, 32439,
                24626, 11323, 5537, 21538, 16118, 2082, 22929, 16541}));
    }

    public int minDistance(int[] locations) {
        Arrays.sort(locations);
        int house = locations[(locations.length + 1) / 2 - 1];
        int ans = 0;
        for (int location : locations) {
            ans += Math.abs(house - location);
        }

        return ans;
    }

    @Test
    public void testMinDistance() {
        test(this::minDistance);
    }
}
