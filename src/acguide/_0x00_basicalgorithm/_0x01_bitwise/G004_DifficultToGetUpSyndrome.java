package acguide._0x00_basicalgorithm._0x01_bitwise;

import org.junit.jupiter.api.Test;
import util.datastructure.function.ToIntTriFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 起床困难综合症：https://ac.nowcoder.com/acm/contest/996/E
 *
 * 一个boss的防御战线由 n 扇防御门组成，其中第 i 扇防御门的属性包括一个运算 op_i 和一个参数 t_i，
 * 运算一定是 OR、XOR、AND 中的一种，参数是非负整数。
 *
 * 在未通过这扇防御门时攻击力为 x，则通过这扇防御门后攻击力将变为 x op_i t_i。
 * 最终boss受到的伤害玩家为初始的攻击力 x_0，依次经过所有 n 扇防御门后得到的攻击力。
 *
 * 由于水平有限，玩家的初始攻击力只能为 [0,m] 之间的一个整数。玩家希望通过选择合适的初始攻击力，
 * 使他的攻击能造成最大的伤害，求这个伤害值。
 *
 * 例 1：
 * 输入：
 * op and t = [[AND, 5], [OR, 6], [XOR, 7]]
 * m = 10
 * 输出：
 * 1
 * 解释：
 * atm 可以选择的初始攻击力为 0,1, … ,10。
 * 假设初始攻击力为 4，最终攻击力经过了如下计算
 * 4 AND 5 = 4
 * 4 OR 6 = 6
 * 6 XOR 7 = 1
 * 类似的，我们可以计算出初始攻击力为 1,3,5,7,9 时最终攻击力为 0，初始攻击力为 0,2,4,6,8,10 时最终攻击力为 1，
 * 因此atm 的一次攻击最多使 drd 受到的伤害值为 1。
 *
 * 说明：
 * - 2 <= n <= 10^5，0 <= m,t_i <= 10^9
 */
public class G004_DifficultToGetUpSyndrome {

    public static void test(ToIntTriFunction<String[], int[], Integer> method) {
        assertEquals(1, method.applyAsInt(new String[]{"AND", "OR", "XOR"}, new int[]{5, 6, 7}, 10));
    }

    /**
     * 超时
     */
    public int attack(String[] op, int[] t, int m) {
        // 我们可以分别计算出 op 和 t 对二进制每一位为 1 和 0 时求得的结果
        final int n = op.length;
        // 10^9 有 30 位
        int zeroBits = 0;
        int oneBits = 0x3FFFFFFF;
        for (int i = 0; i < n; i++) {
            switch (op[i]) {
                case "AND":
                    zeroBits &= t[i];
                    oneBits &= t[i];
                    break;

                case "OR":
                    zeroBits |= t[i];
                    oneBits |= t[i];
                    break;

                case "XOR":
                    zeroBits ^= t[i];
                    oneBits ^= t[i];
            }
        }

        int result = zeroBits;
        for (int i = 1; i <= m; i++) {
            int x = 0;
            // 注意，t 中数字的位数可能大于 x，所以需要 bit < 30 的条件
            for (int bit = 0, x0 = i; bit < 30; bit++, x0 >>= 1) {
                x += ((x0 & 1) == 1 ? oneBits : zeroBits) & 1 << bit;
            }
            result = Math.max(result, x);
        }

        return result;
    }

    @Test
    public void testAttack() {
        test(this::attack);
    }


    /**
     * 作者的方法，和上面的方法类似，都注意到了位运算在二进表示制下不进位，因此每一位都是独立的。
     * 但是此方法使用 op、t 反推出最佳的 x0，这样复杂度就是 O(n)，小于 O(m)。
     */
    public int betterMethod(String[] op, int[] t, int m) {
        int x0 = 0, ans = 0;
        for (int bit = 29; bit >= 0; bit--) {
            int res0 = calc(op, t, bit, 0);
            int res1 = calc(op, t, bit, 1);
            if (x0 + (1 << bit) <= m && res0 < res1) {
                x0 += 1 << bit;
                ans += res1 << bit;
            } else {
                ans += res0 << bit;
            }
        }

        return ans;
    }

    /**
     * 计算某个 bit 位上的数字 now 和 op、t 进行运算后得到的位。
     */
    private int calc(String[] op, int[] t, int bit, int now) {
        for (int i = 0; i < op.length; i++) {
            int x = t[i] >> bit & 1;
            if ("AND".equals(op[i])) {
                now &= x;
            } else if ("OR".equals(op[i])) {
                now |= x;
            } else {
                now ^= x;
            }
        }
        return now;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
