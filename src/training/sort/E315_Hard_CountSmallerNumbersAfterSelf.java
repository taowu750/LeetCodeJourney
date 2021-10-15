package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 315. 计算右侧小于当前元素的个数: https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/
 *
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts。数组 counts 有该性质： counts[i] 的值是
 * nums[i] 右侧小于 nums[i] 的元素的数量。
 *
 * 例 1：
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧仅有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 *
 * 例 2：
 * 输入：nums = [-1]
 * 输出：[0]
 *
 * 例 3：
 * 输入：nums = [-1,-1]
 * 输出：[0,0]
 *
 * 约束：
 * 1 <= nums.length <= 10^5
 * -10^4 <= nums[i] <= 10^4
 */
public class E315_Hard_CountSmallerNumbersAfterSelf {

    public static void test(Function<int[], List<Integer>> method) {
        assertEquals(Arrays.asList(2,1,1,0), method.apply(new int[]{5,2,6,1}));
        assertEquals(singletonList(0), method.apply(new int[]{-1}));
        assertEquals(Arrays.asList(0,0), method.apply(new int[]{-1, -1}));
        assertEquals(Arrays.asList(4,1,1,1,0), method.apply(new int[]{5,2,3,3,1}));
    }

    /**
     * 超时。
     */
    public List<Integer> countSmaller(int[] nums) {
        Integer[] result = new Integer[nums.length];
        result[nums.length - 1] = 0;

        TreeMap<Integer, Integer> tree = new TreeMap<>();
        tree.put(nums[nums.length - 1], 1);
        for (int i = nums.length - 2; i >= 0; i--) {
            tree.merge(nums[i], 1, Integer::sum);
            result[i] = 0;
            for (int cnt : tree.headMap(nums[i]).values()) {
                result[i] += cnt;
            }
        }

        return Arrays.asList(result);
    }

    @Test
    public void testCountSmaller() {
        test(this::countSmaller);
    }


    private int[] nums;
    private Integer[] result;

    /**
     * 归并排序方法，参见 {@link training.sort.Offer51_Hard_InverseOrderPairsInArrays}。
     *
     * LeetCode 耗时：48 ms - 85.25%
     *          内存消耗：59.9 MB - 5.09%
     */
    public List<Integer> mergeSortMethod(int[] nums) {
        this.nums = nums;
        result = new Integer[nums.length];
        Arrays.fill(result, 0);

        // 排序下标
        int[] indices = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            indices[i] = i;
        }
        int[] aux = indices.clone();
        mergeSort(aux, indices, 0, nums.length - 1);

        return Arrays.asList(result);
    }

    private void mergeSort(int[] src, int[] dst, int lo, int hi) {
        if (lo < hi) {
            int mid = (lo + hi) >>> 1;
            mergeSort(dst, src, lo, mid);
            mergeSort(dst, src, mid + 1, hi);
            merge(src, dst, lo, mid, hi);
        }
    }

    private void merge(int[] src, int[] dst, int lo, int mid, int hi) {
        int i, j, k = lo;
        for (i = lo, j = mid + 1; i <= mid && j <= hi;) {
            if (nums[src[i]] <= nums[src[j]]) {
                dst[k++] = src[i];
                /*
                之所以在这里加、而且加 j - mid - 1，是因为:
                1. 只有在这里，才能确定 nums[src[i]] 大于多少元素，也就是 j - mid - 1 个元素
                2. 在归并的时候，lo..mid 属于左半部分，肯定是在 mid+1..hi 的左边，
                   因此这 j - mid - 1 个元素在原数组中才肯定是在 nums[src[i]] 的右边
                 */
                result[src[i]] += j - mid - 1;
                i++;
            } else {
                dst[k++] = src[j++];
            }
        }
        if (i <= mid) {
            for (; i <= mid; i++) {
                dst[k++] = src[i];
                result[src[i]] += j - mid - 1;
            }
        } else if (j <= hi)  {
            for (; j <= hi; j++) {
                dst[k++] = src[j];
            }
        }
    }

    @Test
    public void testMergeSortMethod() {
        test(this::mergeSortMethod);
    }


    private int[] tree;
    // 离散化数组
    private int[] discretization;

    /**
     * 树型数组+离散化数组方法，参见：
     * https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/solution/ji-suan-you-ce-xiao-yu-dang-qian-yuan-su-de-ge-s-7/
     *
     * 树型数组参见 {@link training.treearray.E308_Hard_RangeSumQuery2DMutable}。
     *
     * 记题目给定的序列为 a，我们规定 ai 的取值集合为 a 的「值域」。我们用桶来表示值域中的每一个数，
     * 桶中记录这些数字出现的次数。假设 a={5,5,2,3,6}，那么遍历这个序列得到的桶是这样的：
     *      index  ->  1 2 3 4 5 6 7 8 9
     *      value  ->  0 1 1 0 2 1 0 0 0
     *
     * 记 value 序列为 v，我们可以看出它第 i−1 位的前缀和表示「有多少个数比 i 小」。那么我们可以从后往前遍历序列 a，
     * 记当前遍历到的元素为 a_i，我们把 a_i 对应的桶的值自增 1，记 a_i = p，把 v 序列 p−1 位置的前缀和加入到答案中。
     * 为什么这么做是对的呢，因为我们在循环的过程中，我们把原序列分成了两部分，后半部部分已经遍历过（已入桶），
     * 前半部分是待遍历的（未入桶），那么我们求到的 p−1 位置的前缀和就是「已入桶」的元素中比 p 小的元素的个数总和。
     * 这种动态维护前缀和的问题我们可以用「树状数组」来解决。
     *
     * 我们显然可以用数组来实现这个桶，可问题是如果 a_i 中有很大的元素，内存中是存不下的。这个桶数组中很多位置是 0，
     * 有效位置是稀疏的，我们要想一个办法让有效的位置全聚集到一起，减少无效位置的出现，这个时候我们就需要用到一个
     * 方法——离散化。
     *
     * 离散化的方法有很多，但是目的是一样的，即把原序列的值域映射到一个连续的整数区间，并保证它们的偏序关系不变。
     * 这里我们将原数组去重后排序，原数组每个数映射到去重排序后这个数对应位置的下标，我们称这个下标为这个对应数字的 id。
     * 已知数字获取 id 可以在去重排序后的数组里面做二分查找，已知 id 获取数字可以直接把 id 作为下标访问去
     * 重排序数组的对应位置。
     *
     * LeetCode 耗时：106 ms - 23.68%
     *          内存消耗：54.6 MB - 62.19%
     */
    public List<Integer> treeArrayMethod(int[] nums) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        for (int num : nums) {
            treeSet.add(num);
        }
        tree = new int[treeSet.size() + 1];
        discretization = new int[treeSet.size()];
        int i = 0;
        for (int num : treeSet) {
            discretization[i++] = num;
        }

        Integer[] result = new Integer[nums.length];
        for (i = nums.length - 1; i >= 0; i--) {
            int idx = idx(nums[i]);
            result[i] = prefixSum(idx - 1);
            count(idx);
        }

        return Arrays.asList(result);
    }

    private int prefixSum(int idx) {
        int res = 0;
        for (int i = idx; i > 0; i -= lowbit(i)) {
            res += tree[i];
        }

        return res;
    }

    private void count(int idx) {
        for (int i = idx; i < tree.length; i += lowbit(i)) {
            tree[i] += 1;
        }
    }

    private int lowbit(int i) {
        return i & -i;
    }

    private int idx(int x) {
        // 因为 tree 下标从 1 开始，所以这里加 1
        return Arrays.binarySearch(discretization, x) + 1;
    }

    @Test
    public void testTreeArrayMethod() {
        test(this::treeArrayMethod);
    }
}
