package training.math;

import org.junit.jupiter.api.Test;
import util.Util;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 380. 常数时间插入、删除和获取随机元素: https://leetcode-cn.com/problems/insert-delete-getrandom-o1/
 *
 * 设计一个支持在平均时间复杂度 O(1)下，执行以下操作的数据结构。
 * - insert(val)：当元素 val 不存在时，向集合中插入该项。
 * - remove(val)：元素 val 存在时，从集合中移除该项。
 * - getRandom：随机返回现有集合中的一项。每个元素应该有相同的概率被返回。
 *
 * 例 1：
 * // 初始化一个空的集合。
 * RandomizedSet randomSet = new RandomizedSet();
 * // 向集合中插入 1 。返回 true 表示 1 被成功地插入。
 * randomSet.insert(1);
 * // 返回 false ，表示集合中不存在 2 。
 * randomSet.remove(2);
 * // 向集合中插入 2 。返回 true 。集合现在包含 [1,2] 。
 * randomSet.insert(2);
 * // getRandom 应随机返回 1 或 2 。
 * randomSet.getRandom();
 * // 从集合中移除 1 ，返回 true 。集合现在包含 [2] 。
 * randomSet.remove(1);
 * // 2 已在集合中，所以返回 false 。
 * randomSet.insert(2);
 * // 由于 2 是集合中唯一的数字，getRandom 总是返回 2 。
 * randomSet.getRandom();
 */
public class E380_Medium_InsertDeleteGetRandom {

    static void test(Supplier<IRandomizedSet> factory) {
        IRandomizedSet randomSet = factory.get();
        assertTrue(randomSet.insert(1));
        assertFalse(randomSet.remove(2));
        assertTrue(randomSet.insert(2));
        assertTrue(Util.in(randomSet.getRandom(), 1, 2));
        assertTrue(randomSet.remove(1));
        assertFalse(randomSet.insert(2));
        assertEquals(2, randomSet.getRandom());

        randomSet = factory.get();
        assertTrue(randomSet.insert(0));
        assertTrue(randomSet.insert(1));
        assertTrue(randomSet.remove(0));
        assertTrue(randomSet.insert(2));
        assertTrue(randomSet.remove(1));
        assertEquals(2, randomSet.getRandom());
    }

    @Test
    public void testRandomizedSet() {
        test(RandomizedSet::new);
    }
}

interface IRandomizedSet {

    boolean insert(int val);

    boolean remove(int val);

    int getRandom();
}

/**
 * LeetCode 耗时：12 ms - 83.25%
 *          内存消耗：43.5 MB - 34.89%
 */
class RandomizedSet implements IRandomizedSet {

    private List<Integer> numbers;
    private Map<Integer, Integer> numToIdx;
    private Random random;

    public RandomizedSet() {
        numbers = new ArrayList<>();
        numToIdx = new HashMap<>();
        random = new Random();
    }

    @Override
    public boolean insert(int val) {
        if (numToIdx.containsKey(val))
            return false;
        numbers.add(val);
        numToIdx.put(val, numbers.size() - 1);
        return true;
    }

    @Override
    public boolean remove(int val) {
        int idx = numToIdx.getOrDefault(val, -1);
        if (idx != -1) {
            // 将待删除元素交换到数组末尾，然后删除
            int endElement = numbers.get(numbers.size() - 1);
            numbers.set(idx, endElement);
            numToIdx.put(endElement, idx);
            numbers.remove(numbers.size() - 1);
            numToIdx.remove(val);
            return true;
        }
        return false;
    }

    @Override
    public int getRandom() {
        return numbers.get(random.nextInt(numbers.size()));
    }
}
