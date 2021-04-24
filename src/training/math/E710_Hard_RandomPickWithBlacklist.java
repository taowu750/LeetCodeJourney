package training.math;

import org.junit.jupiter.api.Test;
import util.Util;

import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 710. 黑名单中的随机数: https://leetcode-cn.com/problems/random-pick-with-blacklist/
 *
 * 给定一个包含 [0，n) 中独特的整数的黑名单 B，写一个函数从 [0，n) 中返回一个不在 B 中的随机整数。
 * 对它进行优化使其尽量少调用系统方法 Math.random() 。
 *
 * 下面的例子中，输入是两个列表：调用成员函数名和调用的参数。Solution 的构造函数有两个参数，
 * N 和黑名单 B。pick 没有参数，输入参数是一个列表，即使参数为空，也会输入一个 [] 空列表。
 *
 * 例 1：
 * 输入:
 * ["Solution","pick","pick","pick"]
 * [[1,[]],[],[],[]]
 * 输出: [null,0,0,0]
 *
 * 例 2：
 * 输入:
 * ["Solution","pick","pick","pick"]
 * [[2,[]],[],[],[]]
 * 输出: [null,1,1,1]
 *
 * 例 3：
 * 输入:
 * ["Solution","pick","pick","pick"]
 * [[3,[1]],[],[],[]]
 * 输出: [null,0,0,2]
 *
 * 例 4：
 * 输入:
 * ["Solution","pick","pick","pick"]
 * [[4,[2]],[],[],[]]
 * 输出: [null,1,3,1]
 *
 * 约束：
 * - 1 <= N <= 1000000000
 * - 0 <= B.length < min(100000, N)
 */
public class E710_Hard_RandomPickWithBlacklist {

    static void test(BiFunction<Integer, int[], IRandomPickWithBlacklist> factory) {
        IRandomPickWithBlacklist solution = factory.apply(1, new int[0]);
        for (int i = 0; i < 10000; i++) {
            assertEquals(0, solution.pick());
        }

        solution = factory.apply(2, new int[0]);
        for (int i = 0; i < 10000; i++) {
            assertTrue(Util.in(solution.pick(), 0, 1));
        }

        solution = factory.apply(3, new int[]{1});
        for (int i = 0; i < 10000; i++) {
            assertTrue(Util.in(solution.pick(), 0, 2));
        }

        solution = factory.apply(4, new int[]{0, 2});
        for (int i = 0; i < 10000; i++) {
            assertTrue(Util.in(solution.pick(), 1, 3));
        }

        solution = factory.apply(4, new int[]{0, 3});
        for (int i = 0; i < 10000; i++) {
            assertTrue(Util.in(solution.pick(), 1, 2));
        }
    }

    @Test
    public void testRandomPickWithBlacklist() {
        test(RandomPickWithBlacklist::new);
    }

    @Test
    public void testRandomizedSetMethod() {
        test(RandomizedSetMethod::new);
    }
}


interface IRandomPickWithBlacklist {

    int pick();
}

/**
 * 超出时间限制
 */
class RandomPickWithBlacklist implements IRandomPickWithBlacklist {

    private List<Integer> numbers;
    private Random random;

    public RandomPickWithBlacklist(int N, int[] blacklist) {
        Set<Integer> blackSet = new HashSet<>((int) (blacklist.length / 0.75) + 1);
        for (int b : blacklist) {
            blackSet.add(b);
        }

        numbers = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (!blackSet.contains(i))
                numbers.add(i);
        }

        random = new Random();
    }

    @Override
    public int pick() {
        return numbers.get(random.nextInt(numbers.size()));
    }
}

/**
 * 此方法和 {@link E380_Medium_InsertDeleteGetRandom} 类似。
 * 将 [0, N) 视为一个数组，将 blacklist 中的元素视为要删除的值，
 * 将它们和数组末尾的值交换，最后在 [0, N-blacklist.len) 里面随机选择元素。
 *
 * LeetCode 耗时：54 ms - 96.84%
 *          内存消耗：51.6 MB - 19.82%
 */
class RandomizedSetMethod implements IRandomPickWithBlacklist {

    private Map<Integer, Integer> idxToVal;
    private int len;
    private Random random;

    public RandomizedSetMethod(int N, int[] blacklist) {
        idxToVal = new HashMap<>((int) (blacklist.length / 0.75) + 1);
        len = N - blacklist.length;
        random = new Random();

        // 先保存所有 black，值先随便填
        for (int black : blacklist) {
            idxToVal.put(black, 666);
        }

        int exchStart = len;
        for (int black : blacklist) {
            // 如果 black 已在数组末尾，则不需要保存它
            if (black >= len)
                continue;
            // 如果数组末尾的元素在 blacklist 中，跳过它
            while (idxToVal.containsKey(exchStart))
                exchStart++;
            // 原来 black 在“数组”中的位置被数组末尾的元素取代
            idxToVal.put(black, exchStart++);
        }
    }

    @Override
    public int pick() {
        int idx = random.nextInt(len);
        return idxToVal.getOrDefault(idx, idx);
    }
}
