package util;

public class IntUtil {

    private static int[] bitTable = new int[31];
    static {
        for (int i = 1; i < bitTable.length; i++) {
            bitTable[i] = 1 << i - 1;
        }
    }

    /**
     * 计算 num 的二进制位数有多少，比用 log2 计算要快。
     */
    public static int bitCnt(int num) {
        int lo = 0, hi = bitTable.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (bitTable[mid] == num) {
                return mid;
            } else if (bitTable[mid] < num) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return hi;
    }
}
