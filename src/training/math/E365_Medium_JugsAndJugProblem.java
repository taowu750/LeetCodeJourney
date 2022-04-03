package training.math;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriPredicate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 365. 水壶问题: https://leetcode-cn.com/problems/water-and-jug-problem/
 *
 * 有两个水壶，容量分别为 jug1Capacity 和 jug2Capacity 升。水的供应是无限的。
 * 确定是否有可能使用这两个壶准确得到 targetCapacity 升。
 *
 * 如果可以得到 targetCapacity 升水，最后请用以上水壶中的一或两个来盛放取得的 targetCapacity 升水。
 * 你可以：
 * - 装满任意一个水壶
 * - 清空任意一个水壶
 * - 从一个水壶向另外一个水壶倒水，直到装满或者倒空
 *
 * 例 1：
 * 输入: jug1Capacity = 3, jug2Capacity = 5, targetCapacity = 4
 * 输出: true
 * 解释：先装满 jug1，再把 jug1 倒入 jug2。再装满 jug1，再用 jug1 装满 jug2。
 * 此时 jug1 中有 1 升水。清空 jug2，把 jug1 的 1 升水倒入 jug2，最后装满 jug2，得到 4 升水
 *
 * 例 2：
 * 输入: jug1Capacity = 2, jug2Capacity = 6, targetCapacity = 5
 * 输出: false
 *
 * 例 3：
 * 输入: jug1Capacity = 1, jug2Capacity = 2, targetCapacity = 3
 * 输出: true
 *
 * 说明：
 * - 1 <= jug1Capacity, jug2Capacity, targetCapacity <= 10^6
 */
public class E365_Medium_JugsAndJugProblem {

    public static void test(TriPredicate<Integer, Integer, Integer> method) {
        assertTrue(method.test(3, 5, 4));
        assertFalse(method.test(2, 6, 5));
        assertTrue(method.test(1, 2, 3));
        assertFalse(method.test(1, 1, 12));
    }

    /**
     * LeetCode 耗时：305 ms - 47.76%
     *          内存消耗：137.4 MB - 29.05%
     */
    public boolean canMeasureWater(int jug1Capacity, int jug2Capacity, int targetCapacity) {
        if (jug1Capacity + jug2Capacity < targetCapacity) {
            return false;
        }

        this.jug1Capacity = jug1Capacity;
        this.jug2Capacity = jug2Capacity;
        this.targetCapacity = targetCapacity;
        Set<Jugs> visited = new HashSet<>();

        return dfs(visited, new Jugs(0, 0));
    }

    private int jug1Capacity, jug2Capacity, targetCapacity;

    private boolean dfs(Set<Jugs> visited, Jugs jugs) {
        if (visited.contains(jugs)) {
            return false;
        }
        if (jugs.jug1 + jugs.jug2 == targetCapacity) {
            return true;
        }
        visited.add(jugs);

        // 填满 jug1
        if (jugs.jug1 < jug1Capacity) {
            if (dfs(visited, new Jugs(jug1Capacity, jugs.jug2))) {
                return true;
            }
        }
        // 清空 jug1
        if (jugs.jug1 > 0) {
            if (dfs(visited, new Jugs(0, jugs.jug2))) {
                return true;
            }
        }
        // 填满 jug2
        if (jugs.jug2 < jug1Capacity) {
            if (dfs(visited, new Jugs(jugs.jug1, jug2Capacity))) {
                return true;
            }
        }
        // 清空 jug2
        if (jugs.jug2 > 0) {
            if (dfs(visited, new Jugs(jugs.jug1, 0))) {
                return true;
            }
        }
        // 使用 jug1 填 jug2
        if (jugs.jug1 > 0 && jugs.jug2 < jug2Capacity) {
            int trans = Math.min(jugs.jug1, jug2Capacity - jugs.jug2);
            if (dfs(visited, new Jugs(jugs.jug1 - trans, jugs.jug2 + trans))) {
                return true;
            }
        }
        // 使用 jug2 填 jug1
        if (jugs.jug2 > 0 && jugs.jug1 < jug1Capacity) {
            int trans = Math.min(jugs.jug2, jug1Capacity - jugs.jug1);
            if (dfs(visited, new Jugs(jugs.jug1 + trans, jugs.jug2 - trans))) {
                return true;
            }
        }

        return false;
    }

    public static class Jugs {
        public int jug1;
        public int jug2;

        public Jugs(int jug1, int jug2) {
            this.jug1 = jug1;
            this.jug2 = jug2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Jugs)) return false;
            Jugs jugs = (Jugs) o;
            return jug1 == jugs.jug1 && jug2 == jugs.jug2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(jug1, jug2);
        }
    }

    @Test
    public void testCanMeasureWater() {
        test(this::canMeasureWater);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/water-and-jug-problem/solution/shui-hu-wen-ti-by-leetcode-solution/
     *
     * 首先要了解裴蜀定理：
     * 若a,b是整数,且gcd(a,b)=d，那么对于任意的整数x,y,ax+by都一定是d的倍数，特别地，一定存在整数x,y，使ax+by=d成立。
     *
     * 我们认为，每次操作只会让桶里的水总量增加 x，增加 y，减少 x，或者减少 y。
     * 你可能认为这有问题：如果往一个不满的桶里放水，或者把它排空呢？那变化量不就不是 x 或者 y 了吗？
     * 接下来我们来解释这一点：
     * - 首先要清楚，在题目所给的操作下，两个桶不可能同时有水且不满。因为观察所有题目中的操作，
     *   操作的结果都至少有一个桶是空的或者满的；
     * - 其次，对一个不满的桶加水是没有意义的。因为如果另一个桶是空的，那么这个操作的结果等价于直接从初始状态给这个桶加满水；
     *   而如果另一个桶是满的，那么这个操作的结果等价于从初始状态分别给两个桶加满；
     * - 再次，把一个不满的桶里面的水倒掉是没有意义的。因为如果另一个桶是空的，那么这个操作的结果等价于回到初始状态；
     *   而如果另一个桶是满的，那么这个操作的结果等价于从初始状态直接给另一个桶倒满。
     *
     * 因此，我们可以认为每次操作只会给水的总量带来 x 或者 y 的变化量。因此我们的目标可以改写成：找到一对整数 a,b，使得
     *              ax + by = z
     *
     * 而只要满足 z≤x+y，且这样的 a,b 存在，那么我们的目标就是可以达成的。这是因为：
     * - 若 a≥0,b≥0，那么显然可以达成目标。
     * - 若 a<0，那么可以进行以下操作：
     *   1. 往 y 壶倒水；
     *   2. 把 y 壶的水倒入 x 壶；
     *   3. 如果 y 壶不为空，那么 x 壶肯定是满的，把 x 壶倒空，然后再把 y 壶的水倒入 x 壶。
     *   4. 重复以上操作直至某一步时 x 壶进行了 a 次倒空操作，y 壶进行了 b 次倒水操作。
     * - 若 b<0，方法同上，x 与 y 互换。
     *
     * 而裴蜀定理告诉我们，ax+by=z 有解当且仅当 z 是 x,y 的最大公约数的倍数。因此我们只需要找到 x,y 的最大公约数
     * 并判断 z 是否是它的倍数即可。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.3 MB - 68.31%
     */
    public boolean mathMethod(int jug1Capacity, int jug2Capacity, int targetCapacity) {
        return jug1Capacity + jug2Capacity >= targetCapacity
                && targetCapacity % gcd(jug1Capacity, jug2Capacity) == 0;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int tmp = b;
            b = a % b;
            a = tmp;
        }

        return a;
    }

    @Test
    public void testMathMethod() {
        test(this::mathMethod);
    }
}
