package acguide._0x00_basicalgorithm._0x07_greedy;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 国王游戏：https://www.acwing.com/problem/content/116/
 *
 * 恰逢 H 国国庆，国王邀请 n 位大臣来玩一个有奖游戏。
 *
 * 首先，他让每个大臣在左、右手上面分别写下一个整数，国王自己也在左、右手上各写一个整数。
 * 然后，让这 n 位大臣排成一排，国王站在队伍的最前面。
 * 排好队后，所有的大臣都会获得国王奖赏的若干金币，每位大臣获得的金币数分别是:
 * 排在该大臣前面的所有人（不包括他自己）的左手上的数 a 的乘积除以他自己右手上的数 b，然后向下取整得到的结果。
 *
 * 国王不希望某一个大臣获得特别多的奖赏，所以他想请你帮他重新安排一下队伍的顺序，使得获得奖赏最多的大臣，所获奖赏尽可能的少。
 * 注意，国王的位置始终在队伍的最前面。结果输出重新排列后的队伍中获奖赏最多的大臣所获得的金币数。
 *
 * 例 1：
 * 输入：
 * hands=[[1,1],[2,3],[7,4],[4,6]]
 * 输出：
 * 2
 *
 * 说明：
 * - 1 ≤ n ≤ 1000
 * - 0 < a,b < 10000
 */
public class G028_KingGame {

    public static void test(Function<int[][], String> method) {
        assertEquals("2", method.apply(new int[][]{{1,1},{2,3},{7,4},{4,6}}));
        assertEquals("2166489661101032350678866897536628698296804147316726878162441737980268621335310233327258927458239967674879428851028800069063620140885606400000000000000000",
                method.apply(new int[][]{{70, 94}, {43, 9}, {92, 18}, {18, 9}, {86, 31}, {24, 32}, {46, 49}, {23, 69}, {40, 56}, {27, 75}, {28, 85}, {37, 29}, {99, 80}, {44, 70}, {14, 9}, {30, 38}, {46, 32}, {93, 87}, {42, 49}, {35, 60}, {99, 73}, {57, 8}, {38, 35}, {73, 33}, {6, 32}, {10, 36}, {78, 75}, {49, 98}, {50, 48}, {91, 78}, {18, 3}, {86, 24}, {18, 84}, {27, 28}, {83, 25}, {15, 95}, {38, 18}, {50, 89}, {79, 9}, {3, 17}, {1, 52}, {74, 32}, {76, 99}, {24, 36}, {9, 43}, {93, 7}, {65, 27}, {36, 84}, {75, 31}, {94, 44}, {33, 2}, {85, 5}, {42, 18}, {4, 33}, {45, 84}, {92, 87}, {86, 34}, {36, 44}, {61, 59}, {59, 28}, {1, 97}, {60, 23}, {9, 64}, {96, 47}, {57, 100}, {90, 7}, {54, 93}, {17, 30}, {71, 23}, {72, 32}, {14, 95}, {48, 40}, {27, 15}, {92, 78}, {52, 11}, {93, 21}, {56, 60}, {22, 47}, {21, 58}, {89, 11}, {29, 13}, {36, 14}, {95, 91}, {47, 12}, {16, 36}, {19, 80}, {19, 92}, {73, 68}, {66, 1}, {53, 97}, {13, 60}, {83, 5}, {63, 99}, {98, 37}, {2, 67}, {84, 95}, {26, 60}, {63, 33}, {2, 78}, {91, 38}, {9, 31}}));
    }

    public String reward(int[][] hands) {
        /*
        对每个大臣 i（从 1 开始），它的金币数 coin[i] = prod(left[0..i)) / right[i]，
        要求 min(max(coin[i])) i∈[1,n]

        暴力做法枚举所有排列，然后求 mx = max(coin[i])，最后求 min(mx)，时间复杂度 O(n! * n)

        我们从后往前安排，选择能使得当前位置 i 具有最小 coin 的大臣。每当安排好一个大臣，则贪心地确定了一个位置。
        安排第 n 个位置，设选择了第 i 个大臣，则有
            coin = prod_{j!=i}(left[j]) / right[i] = prod(left[j]) / (left[i] * right[i])
        所以可以按照乘积升序排序大臣，就是正确地序列
         */

        // 对大臣按照乘积升序排序
        Arrays.sort(hands, 1, hands.length, Comparator.comparingInt((int[] a) -> a[0] * a[1]));
        BigInteger prod = BigInteger.valueOf(hands[0][0]), max = BigInteger.valueOf(0);
        for (int i = 1; i < hands.length; i++) {
            /*coin = prod / hands[i][1];
            max = Math.max(max, coin);
            prod *= hands[i][0];*/
            max = max.max(prod.divide(BigInteger.valueOf(hands[i][1])));
            prod = prod.multiply(BigInteger.valueOf(hands[i][0]));
        }

        return max.toString();
    }

    @Test
    public void testReward() {
        test(this::reward);
    }
}
