package learn.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 给定一个整数数组 nums，有 1 <= nums[i] <= n（n 是数组大小）。有些元素出现两次，有些元素出现一次。
 * 查找[1，n]中所有不在此数组中出现的元素。
 * <p>
 * 在 O(1) 额外空间的情况下在 O(n)运行时间完成。返回的列表不算作额外空间。
 * <p>
 * 例 1：
 * Input:
 * [4,3,2,7,8,2,3,1]
 * Output:
 * [5,6]
 */
public class FindAllNumbersDisappearedInAnArray {

    static void test(Function<int[], List<Integer>> method) {
        int[] nums = {4, 3, 2, 7, 8, 2, 3, 1};
        Assertions.assertEquals(method.apply(nums), Stream.of(5, 6).collect(Collectors.toList()));
    }

    public List<Integer> findDisappearedNumbers(int[] nums) {
        int len = nums.length;
        List<Integer> list = new ArrayList<>(len < 4 ? len : len / 4);

        for (int i = 0; i < len; i++) {
            int elem = nums[i];
            // 不断交换位置不对的元素，直到元素正确防止或两个位置元素相同为止
            while (elem != i + 1 && nums[elem - 1] != elem) {
                int tmp = nums[elem - 1];
                nums[i] = tmp;
                nums[elem - 1] = elem;
                elem = tmp;
            }
        }
        for (int i = 0; i < len; i++) {
            if (nums[i] != i + 1)
                list.add(i + 1);
        }

        return list;
    }

    @Test
    public void testFindDisappearedNumbers() {
        test(this::findDisappearedNumbers);
    }



    public List<Integer> addLenMethod(int[] nums) {
        int len = nums.length;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            // 那些没有出现的元素位置上不会加 len
            // 此方法有数字溢出风险
            nums[(nums[i] - 1) % len] += len;
        }
        for (int i = 0; i < len; i++) {
            if (nums[i] <= len)
                list.add(i + 1);
        }

        return list;
    }

    @Test
    public void testAddLenMethod() throws Exception {
        test(this::addLenMethod);
    }



    /**
     * 此方法和{@link #addLenMethod(int[])}原理一致，
     * 都是将出现的元素位置上的元素进行某些操作以便将来区分，
     * 当此方法通过转换为负数避免了数字溢出
     */
    public List<Integer> negativeMethod(int[] nums) {
        int len = nums.length;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            int idx = Math.abs(nums[i]) - 1;
            nums[idx] = -Math.abs(nums[idx]);
        }
        for (int i = 0; i < len; i++) {
            if (nums[i] > 0)
                list.add(i + 1);
        }

        return list;
    }

    @Test
    public void testNegativeMethod() throws Exception {
        test(this::negativeMethod);
    }
}
