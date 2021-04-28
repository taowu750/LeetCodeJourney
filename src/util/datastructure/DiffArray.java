package util.datastructure;

/**
 * 差分数组，适用场景是频繁对原始数组的某个区间的元素进行增减。
 */
public class DiffArray {

    private int[] diff;

    public DiffArray(int[] nums) {
        diff = new int[nums.length];
        diff[0] = nums[0];
        // 构造差分数组，diff[i] 就是 nums[i] 和 nums[i-1] 之差
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    // 使用差分数组反推出原始数组
    public int[] result() {
        int[] res = new int[diff.length];
        res[0] = diff[0];
        // 根据差分数组构造结果数组
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i - 1] + diff[i];
        }

        return res;
    }

    // 给闭区间 [i,j] 增加 val（可以是负数）
    public void add(int i, int j, int val) {
        // diff[i]+=val 意味着给 nums[i..] 所有的元素都加了 val
        diff[i] += val;
        if (j + 1 < diff.length)
            // diff[j+1]-=val 又意味着对于 nums[j+1..] 所有元素再减 val
            diff[j + 1] -= val;
        // 综合起来，就是对 nums[i..j] 中的所有元素都加 val
    }
}
