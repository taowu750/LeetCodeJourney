package training.design;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 295. 数据流的中位数: https://leetcode-cn.com/problems/find-median-from-data-stream/
 *
 * 中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。
 * 例如，
 * - [2,3,4] 的中位数是 3
 * - [2,3] 的中位数是 (2 + 3) / 2 = 2.5
 *
 * 设计一个支持以下两种操作的数据结构：
 * - void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 * - double findMedian() - 返回目前所有元素的中位数。
 *
 * 如果数据流中所有整数都在 0 到 100 范围内，你将如何优化你的算法？
 * 如果数据流中 99% 的整数都在 0 到 100 范围内，你将如何优化你的算法？
 *
 * 例 1：
 * addNum(1)
 * addNum(2)
 * findMedian() -> 1.5
 * addNum(3)
 * findMedian() -> 2
 */
public class E295_Hard_FindMedianFromDataStream {

    static void test(Supplier<IMedianFinder> factory) {
        IMedianFinder medianFinder = factory.get();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        assertEquals(1.5, medianFinder.findMedian());
        medianFinder.addNum(3);
        assertEquals(2, medianFinder.findMedian());
        medianFinder.addNum(100);
        assertEquals(2.5, medianFinder.findMedian());
        medianFinder.addNum(3);
        assertEquals(3, medianFinder.findMedian());
    }

    @Test
    void testMedianFinder() {
        test(MedianFinder::new);
    }
}

interface IMedianFinder {

    void addNum(int num);

    double findMedian();
}

/**
 * 元素范围较小时可以尝试使用计数排序。
 *
 * LeetCode 耗时：69 ms - 69.88%
 *          内存消耗：49.4 MB - 86.32%
 */
class MedianFinder implements IMedianFinder {

    private PriorityQueue<Integer> leftHalf, rightHalf;

    public MedianFinder() {
        // 左半边最大堆
        leftHalf = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return -Integer.compare(o1, o2);
            }
        });
        // 右半边最小堆
        rightHalf = new PriorityQueue<>();
    }

    @Override
    public void addNum(int num) {
//        if (rightHalf.size() == 0)
//            rightHalf.add(num);
//        else {
//            if (num >= rightHalf.peek())
//                rightHalf.add(num);
//            else
//                leftHalf.add(num);
//
//            if (rightHalf.size() > leftHalf.size() + 1)
//                leftHalf.add(rightHalf.remove());
//            else if (leftHalf.size() > rightHalf.size())
//                rightHalf.add(leftHalf.remove());
//        }

        // 此实现比上面的代码快 6ms，推测是减少了判断，提高了 CPU 流水线工作效率
        rightHalf.add(num);
        leftHalf.add(rightHalf.remove());
        if (((leftHalf.size() + rightHalf.size()) & 1) != 0)
            rightHalf.add(leftHalf.remove());
    }

    @Override
    public double findMedian() {
        if (rightHalf.size() == 0)
            return 0;

        if (leftHalf.size() == rightHalf.size())
            return ((double) leftHalf.peek() + rightHalf.peek()) / 2;
        else
            return rightHalf.peek();
    }
}
