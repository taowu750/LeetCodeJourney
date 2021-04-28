package util.datastructure;

/**
 * 前缀和数组，它的主要适用的场景是原始数组不会被修改的情况下，频繁查询某个区间的累加和，例如 nums[i..j]。
 */
public class PrefixArray {

    private int[] prefix;

    public PrefixArray(int[] nums) {
        prefix = new int[nums.length + 1];
        // 计算 nums 的累加和
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
    }

    // 查询闭区间 [i, j] 的累加和
    public int query(int i, int j) {
        return prefix[j + 1] - prefix[i];
    }
}
