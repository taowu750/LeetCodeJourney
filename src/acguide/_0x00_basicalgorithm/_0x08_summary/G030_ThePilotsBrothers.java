package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * 飞行员兄弟: https://www.acwing.com/problem/content/118/
 *
 * “飞行员兄弟”这个游戏，需要玩家顺利的打开一个拥有 16 个把手的冰箱。
 * 已知每个把手可以处于以下两种状态之一：打开或关闭。只有当所有把手都打开时，冰箱才会打开。
 *
 * 把手可以表示为一个 4×4 的矩阵，您可以「翻转」任何一个位置 [i,j] 上把手的状态。
 * 但是，这也会使得第 i 行和第 j 列上的所有把手的状态也随着翻转。
 *
 * 请你求出打开冰箱所需的切换把手的次数最小值是多少。
 *
 * 例 1：
 * 输入：
 * -+--
 * ----
 * ----
 * -+--
 * 输出：
 * 6
 * 1 1
 * 1 3
 * 1 4
 * 4 1
 * 4 3
 * 4 4
 * 解释：
 * 符号 + 表示把手处于闭合状态，而符号 - 表示把手处于打开状态。至少一个手柄的初始状态是关闭的。
 * 第一行输出一个整数 N，表示所需的最小切换把手次数。
 * 接下来 N 行描述切换顺序，每行输出两个整数，代表被切换状态的把手的行号和列号，数字之间用空格隔开。
 * 如果存在多种打开冰箱的方式，则按照优先级整体从上到下，同行从左到右打开。
 */
public class G030_ThePilotsBrothers {

    public static void test(Function<char[][], List<int[]>> method) {
        List<int[]> res = method.apply(new char[][]{
                "-+--".toCharArray(),
                "----".toCharArray(),
                "----".toCharArray(),
                "-+--".toCharArray(),
        });
        CollectionUtil.equalsIntArrays(Arrays.asList(new int[]{1, 1}, new int[]{1, 3}, new int[]{1, 4}, new int[]{4, 1},
                new int[]{4, 3}, new int[]{4, 4}), res);

    }

    public List<int[]> minSwitchTimes(char[][] refrigerator) {
        // 把开关的状态转化为一个 16 位的二进制整数
        int state = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (refrigerator[i][j] == '-') {
                    state |= 1 << (i * 4 + j);
                }
            }
        }

        ans = null;
        tmp = new ArrayList<>();
        dfs(0, 0, state);

        return ans;
    }

    ArrayList<int[]> ans, tmp;

    private void dfs(int r, int c, int state) {
        // 当已经遍历完开关时
        if (r == 4) {
            // 如果所有开关均打开，且次数小于上次记录的次数
            if (state == 0xFFFF && (ans == null || ans.size() > tmp.size())) {
                ans = (ArrayList<int[]>) tmp.clone();
            }
            return;
        }
        // 移动到下一个位置
        int nr = r, nc = c;
        if (++nc == 4) {
            nc = 0;
            nr++;
        }
        // 尝试翻转
        tmp.add(new int[]{r + 1, c + 1});
        dfs(nr, nc, flip(state, r, c));
        tmp.remove(tmp.size() - 1);
        // 尝试不翻转
        dfs(nr, nc, state);
    }

    private int flip(int state, int r, int c) {
        // 翻转行
        int nextState = state ^ (0xF << r * 4);
        // 翻转列
        for (int j = 0; j < 4; j++) {
            if (j == r) {
                continue;
            }
            nextState ^= 1 << j * 4 + c;
        }

        return nextState;
    }

    @Test
    public void testMinSwitchTimes() {
        test(this::minSwitchTimes);
    }


    /**
     * 数学证明方法，参见 https://www.acwing.com/solution/content/44927/
     *
     * 在判断是否要对 (i, j) 位置的把手进行切换时，只需要计算一下第 i 行和第 j 列总共 7 个把手中闭合的把手数目，
     * 如果是奇数个就进行切换，偶数个就不进行切换。(奇数个是该位置的把手进行过切换的充要条件)
     *
     * 因此我们从上到下从左到右顺次的对 16 个把手进行上述判断。如果判断结果是奇数个那么说明该位置被切换过，进行记录即可。
     * 可以在 O(16 * 7) 的时间复杂度给出结果。
     *
     * 先证明充分性：如果 (i, j) 位置的把手被切换过（被别的位置翻转过），其对应十字的所有 7 个把手中闭合的把手数目一直都将是奇数个。(切换过 =推出=> 奇数个)
     * 1. 因为「先后切换的顺序不影响最后的状态」，倘若 (i, j) 对应十字有奇数个闭合的把手，我们可以把 (i, j) 看成是第一次切换过的即可。
     * 2. 如果一开始所有把手都是打开的。那么对 (i, j) 进行切换，其对应的 7 个把手都变成闭合，是奇数个(初始状态是奇数个)，也就可以看做切换过。
     * 3. 假设后来在 (x, y) 的位置（(x, y)与(i, j)不重合）又进行了把手切换
     *    - 如果 (x, y) 在第 i 行(或者第 j 列)，那么这次切换将会改变第 i 行(或第 j 列)上所有的把手状态。
     *      第 i 行中如有 a 个闭合则切换后有 4 - a 个闭合(a 取 0，1，2，3，4)。不管 a 的取值是多少，
     *      从 a 变成 4 - a 不改变奇偶性，(i, j) 对应的 7 个把手中闭合的把手数仍然是奇数个。
     *    - 如果 (x, y) 不在第 i 行也不在第 j 列，那么这次切换将会改变 (i, y) 和 (x, j) 2个位置的把手的状态，
     *      这 2 个把手的状态是 (0, 0)、(0, 1)、(1, 0)、(1, 1). 无论是哪种情况，在进行切换之后奇偶性依然是没有改变的。
     *      这 7 个把手中仍然是奇数个把手闭合。
     * 4. 也就是说，「对 (x,y) 切换，不会更改 (i,j) 的奇偶性」
     *
     * 再证明必要性：如果是奇数个，那么一定是被切换过。(奇数个 =推出=> 切换过)
     * 1. 这个可能不好证，但是如果我们证明了没有切换的把手对应的十字中闭合的把手一直将是偶数个(没切换=>偶数个)，那同样可以得出奇数个=>切换过。
     * 2. 所以我们转而证明：没切换=>偶数个，这个证明思路就和上面的一模一样了。
     *
     * 因此我们说明了对应十字上有奇数个闭合开关是切换过的充要条件。所以在每个把手至多切换一次的情况下，只存在一个解法，就是把所有奇数把手切换掉
     *
     * 由上述证明不难发现，此法可推广至边长为偶数的正方形或者长和宽均为偶数的长方形。
     * 边长为奇数的正方形会由于上述证明中 a 和 4 - a 奇偶性不同而不满足此性质。
     */
    public List<int[]> mathMethod(char[][] refrigerator) {
        int[][] closedCnts = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if (refrigerator[i][k] == '+') {
                        closedCnts[i][j]++;
                    }
                    if (refrigerator[k][j] == '+') {
                        closedCnts[i][j]++;
                    }
                }
                if (refrigerator[i][j] == '+') {
                    closedCnts[i][j]--;
                }
            }
        }

        List<int[]> ans = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((closedCnts[i][j] & 1) == 1) {
                    ans.add(new int[]{i + 1, j + 1});
                }
            }
        }

        return ans;
    }

    @Test
    public void testMathMethod() {
        test(this::mathMethod);
    }
}
