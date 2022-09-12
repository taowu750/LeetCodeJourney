package acguide._0x00_basicalgorithm._0x07_greedy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Radar Installation: https://www.acwing.com/problem/content/114/
 *
 * 假设海岸是一条无限长的直线，陆地位于海岸的一侧，海洋位于另外一侧。每个小岛都位于海洋一侧的某个点上。
 * 雷达装置均位于海岸线上，且雷达的监测范围为 d，当小岛与某雷达的距离不超过 d 时，该小岛可以被雷达覆盖。
 *
 * 我们使用笛卡尔坐标系，定义海岸线为 x 轴，海的一侧在 x 轴上方，陆地一侧在 x 轴下方。
 * 现在给出每个小岛的具体坐标以及雷达的检测范围，请你求出能够使所有小岛都被雷达覆盖所需的最小雷达数目。
 * 若没有解决方案则所需数目输出 −1。
 *
 * 例 1：
 * 输入：
 * islands=[[1,2],[-3,1],[2,1]], d=2
 * 输出：
 * 2
 *
 * 说明：
 * - 1 ≤ n ≤ 1000,
 * - −1000 ≤ x,y ≤ 1000
 */
public class G027_RadarInstallation {

    public static void test(ToIntBiFunction<int[][], Integer> method) {
        assertEquals(2, method.applyAsInt(new int[][]{{1,2},{-3,1},{2,1}}, 2));
        assertEquals(17, method.applyAsInt(new int[][]{{592, 72}, {602, 86}, {847, 42}, {645, 38}, {-297, 90}, {-963, 27}, {-477, 79}, {528, 48}, {392, 93}, {71, 34}, {-648, 2}, {-832, 96}, {140, 87}, {978, 47}, {800, 46}, {780, 68}, {-720, 64}, {143, 54}, {758, 52}, {-414, 47}, {-186, 78}, {456, 21}, {135, 1}, {-617, 15}, {222, 62}, {-943, 91}, {449, 36}, {437, 91}, {99, 6}, {666, 65}, {170, 21}, {-128, 75}, {-607, 36}, {-570, 3}, {-634, 99}, {102, 65}, {-635, 16}, {653, 58}, {-414, 47}, {-244, 62}, {-338, 11}, {656, 32}, {-778, 19}, {368, 66}, {13, 9}, {837, 68}, {-971, 98}, {468, 51}, {55, 61}, {-739, 2}, {-441, 28}, {120, 36}, {-480, 11}, {317, 88}, {-918, 6}, {-692, 57}, {865, 26}, {523, 92}, {-921, 58}, {-929, 28}, {-9, 67}, {131, 65}, {841, 29}, {-183, 40}, {552, 2}, {-828, 37}, {-146, 68}, {270, 71}, {288, 97}, {-248, 76}, {396, 59}, {572, 64}, {-891, 96}, {447, 45}, {978, 70}, {-297, 77}, {411, 40}, {881, 25}, {313, 89}, {692, 59}, {659, 50}, {956, 23}, {394, 42}, {606, 47}, {-470, 30}, {660, 29}, {-383, 62}, {428, 88}, {-102, 30}, {-569, 35}, {551, 58}, {-653, 97}, {320, 43}, {-896, 14}, {-97, 44}, {891, 26}, {537, 71}, {-100, 10}, {352, 72}, {-998, 84}, {-904, 87}, {-162, 51}, {166, 12}, {848, 87}, {411, 57}, {-407, 3}, {-982, 70}, {-453, 42}, {50, 87}, {-975, 1}, {230, 1}, {359, 95}, {749, 17}, {-521, 49}, {-338, 20}, {-18, 17}, {463, 22}, {345, 95}, {608, 71}, {31, 79}, {-500, 62}, {-577, 70}, {-992, 94}, {-613, 68}, {864, 59}, {730, 97}, {-554, 40}, {-209, 22}, {-218, 95}, {739, 45}, {-970, 22}, {254, 8}, {-56, 43}, {-311, 88}, {976, 38}, {179, 54}, {-854, 6}, {679, 48}, {-677, 54}, {896, 72}, {469, 21}, {-663, 91}, {-137, 2}, {-758, 89}, {-904, 38}, {588, 32}, {519, 63}, {-872, 31}, {-425, 80}, {185, 68}, {456, 69}, {-215, 79}, {229, 73}, {253, 31}, {-957, 52}, {-25, 71}, {-153, 42}, {374, 65}, {-214, 28}, {-586, 81}, {747, 11}, {517, 27}, {-174, 72}, {396, 46}, {840, 18}, {-144, 16}, {-124, 35}, {940, 3}, {746, 75}, {-903, 21}, {-749, 55}, {584, 4}, {209, 29}, {-240, 68}, {-63, 1}, {929, 0}, {833, 79}, {281, 71}, {181, 6}, {485, 58}, {787, 88}, {338, 22}, {455, 23}, {949, 49}, {715, 80}, {-630, 71}, {498, 29}, {848, 19}, {773, 1}, {347, 87}, {-780, 99}, {478, 76}, {-4, 64}, {-368, 41}, {-428, 83}, {-189, 40}, {120, 22}, {97, 41}, {569, 98}, {-960, 79}},
                100));
    }

    public int radarNums(int[][] islands, int d) {
        for (int[] island : islands) {
            if (island[1] > d) {
                return -1;
            }
        }

        /*
        所有小岛按 x 排序，求出所有小岛到 x 轴距离 <=d 的雷达位置范围。当有范围重叠时，则可以用一个雷达监听多个小岛。
        从左到右遍历，依次检查每个小岛和上一组覆盖区域是否重叠，是则它们公用一个雷达，并更新覆盖区域；否则得用新的雷达。
         */
        Arrays.sort(islands, Comparator.comparingInt((int[] a) -> a[0]));

        double lastRight = Double.NEGATIVE_INFINITY;
        int ans = 0;
        for (int[] island : islands) {
            int x = island[0], y = island[1];
            double offset = Math.sqrt(d * d - y * y);
            double left = x - offset, right = x + offset;
            if (left > lastRight) {
                ans++;
                lastRight = right;
            } else if (right < lastRight) {  // 可能会有两个岛的 x 坐标相同，y 坐标不一样的情况
                lastRight = right;
            }
        }

        return ans;
    }

    @Test
    public void testRadarNums() {
        test(this::radarNums);
    }
}
