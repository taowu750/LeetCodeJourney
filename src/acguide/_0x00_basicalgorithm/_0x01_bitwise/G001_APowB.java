package acguide._0x00_basicalgorithm._0x01_bitwise;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriIntOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * a^b%p：https://ac.nowcoder.com/acm/contest/996/A
 *
 * 求 a 的 b 次方对 p 取模的值，其中 0 <= a,b,p <= 10^9, p > 0
 */
public class G001_APowB {

    public static void test(TriIntOperator method) {
        assertEquals(8, method.applyAsInt(2, 3, 9));
        assertEquals(0, method.applyAsInt(1, 1, 1));
    }

    public int power(int a, int b, int p) {
        long aPowB = 1 % p;
        for (long aPow = a; b > 0; b >>= 1, aPow = aPow * aPow % p) {
            if ((b & 1) == 1) {
                aPowB = aPowB * aPow % p;
            }
        }

        return (int) aPowB;
    }

    @Test
    public void testAPowB() {
        test(this::power);
    }
}
