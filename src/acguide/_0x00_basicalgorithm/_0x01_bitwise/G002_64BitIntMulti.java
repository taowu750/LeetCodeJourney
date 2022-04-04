package acguide._0x00_basicalgorithm._0x01_bitwise;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 64 位整数乘法：https://ac.nowcoder.com/acm/contest/996/C
 *
 * 求 a 乘 b 对 p 取模的值，其中 1 <= a,b,p <= 10^18
 */
public class G002_64BitIntMulti {

    public static void test(TriFunction<Long, Long, Long, Long> method) {
        assertEquals(6, method.apply(2L, 3L, 9L));
    }

    public long multi(long a, long b, long p) {
        long aMultiB = 0;
        for (; b > 0; b >>= 1, a = (a * 2) % p) {
            if ((b & 1) == 1) {
                aMultiB = (aMultiB + a) % p;
            }
        }

        return aMultiB;
    }

    @Test
    public void testMulti() {
        test(this::multi);
    }
}
