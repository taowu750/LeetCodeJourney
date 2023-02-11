package util.algorithm;

public class MathUtil {

    /**
     * 找到小于等于 n 的最大质数
     */
    public static long floorPrime(long n) {
        long i = n;
        for (; i > 1; i--) {
            boolean isPrime = true;
            for (long j = 2; j * j <= i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                break;
            }
        }

        return i;
    }

    public static long ceilPrime(long n) {
        for (long i = n; i >= n; i++) {
            boolean isPrime = true;
            for (long j = 2; j * j <= i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                return i;
            }
        }
        return -1;
    }
}
