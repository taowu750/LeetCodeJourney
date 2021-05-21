package training.design;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 855. 考场就座: https://leetcode-cn.com/problems/exam-room/
 *
 * 在考场里，一排有 N 个座位，分别编号为 0, 1, 2, ..., N-1。
 *
 * 当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，
 * 他会坐在编号最小的座位上。(另外，如果考场里没有人，那么学生就坐在 0 号座位上。)
 *
 * 返回 ExamRoom(int N) 类，它有两个公开的函数：
 * - 其中，函数 ExamRoom.seat() 会返回一个 int（整型数据），代表学生坐的位置；
 * - 函数 ExamRoom.leave(int p) 代表坐在座位 p 上的学生现在离开了考场。每次调用
 *   ExamRoom.leave(p) 时都保证有学生坐在座位 p 上。
 *
 * 例 1：
 * 输入：["ExamRoom","seat","seat","seat","seat","leave","seat"], [[10],[],[],[],[],[4],[]]
 * 输出：[null,0,9,4,2,null,5]
 * 解释：
 * ExamRoom(10) -> null
 * seat() -> 0，没有人在考场里，那么学生坐在 0 号座位上。
 * seat() -> 9，学生最后坐在 9 号座位上。
 * seat() -> 4，学生最后坐在 4 号座位上。
 * seat() -> 2，学生最后坐在 2 号座位上。
 * leave(4) -> null
 * seat() -> 5，学生最后坐在 5 号座位上。
 *
 * 约束：
 * - 1 <= N <= 10^9
 * - 在所有的测试样例中 ExamRoom.seat() 和 ExamRoom.leave() 最多被调用 10^4 次。
 * - 保证在调用 ExamRoom.leave(p) 时有学生正坐在座位 p 上。
 */
public class E855_Medium_ExamRoom {

    static void test(IntFunction<IExamRoom> factory) {
        IExamRoom examRoom = factory.apply(10);
        assertEquals(0, examRoom.seat());
        assertEquals(9, examRoom.seat());
        assertEquals(4, examRoom.seat());
        assertEquals(2, examRoom.seat());
        examRoom.leave(4);
        assertEquals(5, examRoom.seat());

        examRoom = factory.apply(10);
        assertEquals(0, examRoom.seat());
        assertEquals(9, examRoom.seat());
        assertEquals(4, examRoom.seat());
        examRoom.leave(0);
        examRoom.leave(4);
        assertEquals(0, examRoom.seat());
        assertEquals(4, examRoom.seat());
        assertEquals(2, examRoom.seat());
        assertEquals(6, examRoom.seat());
        assertEquals(1, examRoom.seat());
        assertEquals(3, examRoom.seat());
        assertEquals(5, examRoom.seat());
        assertEquals(7, examRoom.seat());
        assertEquals(8, examRoom.seat());
        examRoom.leave(0);
        examRoom.leave(4);
        assertEquals(0, examRoom.seat());
        assertEquals(4, examRoom.seat());
        examRoom.leave(7);
        assertEquals(7, examRoom.seat());
        examRoom.leave(3);
        assertEquals(3, examRoom.seat());
        examRoom.leave(0);
        examRoom.leave(8);
        assertEquals(0, examRoom.seat());
        assertEquals(8, examRoom.seat());
    }

    @Test
    public void testExamRoom() {
        test(ExamRoom::new);
    }

    @Test
    public void testBetterExamRoom() {
        test(BetterExamRoom::new);
    }
}


interface IExamRoom {

    int seat();

    void leave(int p);
}

/**
 * LeetCode 耗时：33 ms - 44.26%
 *          内存消耗：39.4 MB - 21.31%
 */
class ExamRoom implements IExamRoom {

    private TreeSet<Integer> seats;
    private int n;

    public ExamRoom(int n) {
        this.n = n;
        seats = new TreeSet<>();
    }

    @Override
    public int seat() {
        // 没有人做，就坐在 0 号位置上
        if (seats.size() == 0) {
            seats.add(0);
            return 0;
        }

        int result;
        // 最小位置离 0 号位置的距离，最大位置离 n-1 号位置的距离
        int distToStart = seats.first(), distToEnd = n - 1 - seats.last();

        // 遍历所有位置，选择它们中间隔最大的位置
        Iterator<Integer> iter = seats.iterator();
        int lastSeat = iter.next(), offset = 0, maxCenterDist = 0;
        while (iter.hasNext()) {
            int seat = iter.next(), curDist = (seat - lastSeat) / 2;
            if (curDist > maxCenterDist) {
                offset = lastSeat;
                maxCenterDist = curDist;
            }
            lastSeat = seat;
        }

        // 从最大间隔位置、0、n-1 中选择具有最大距离的位置
        // 注意，因为是选座位号最小的，所以下面的判断是 "<="
        result = offset + maxCenterDist;
        if (maxCenterDist <= distToStart) {
            result = 0;
            maxCenterDist = distToStart;
        }
        if (maxCenterDist < distToEnd) {
            result = n - 1;
        }
        seats.add(result);
        return result;
    }

    @Override
    public void leave(int p) {
        seats.remove(p);
    }
}

/**
 * 思想来源：https://labuladong.gitee.io/algo/5/42/
 *
 * 如果将每两个相邻的考生看做线段的两端点，新安排考生就是找最长的线段，然后让该考生在中间把这个线段「二分」，
 * 中点就是给他分配的座位。leave(p) 其实就是去除端点 p，使得相邻两个线段合并为一个。
 *
 * LeetCode 耗时：18 ms - 69.18%
 *          内存消耗：39.3 MB - 25.90%
 */
class BetterExamRoom implements IExamRoom {

    // 将端点 p 映射到以 p 为左端点的线段
    private Map<Integer, int[]> starts;
    // 将端点 p 映射到以 p 为右端点的线段
    private Map<Integer, int[]> ends;
    // 根据线段长度从小到大存放所有线段
    private TreeSet<int[]> lines;
    private int n;

    public BetterExamRoom(int n) {
        this.n = n;
        starts = new HashMap<>();
        ends = new HashMap<>();
        lines = new TreeSet<>((a, b) -> {
            int aDist = distance(a);
            int bDist = distance(b);
            // 两个距离相同，则座位号越小的越大
            if (aDist == bDist)
                return -Integer.compare(a[0], b[0]);
            return Integer.compare(aDist, bDist);
        });

        // 在有序集合中先放一个虚拟线段，使算法正常启动
        int[] initialLine = {-1, n};
        lines.add(initialLine);
    }

    @Override
    public int seat() {
        int result;
        int[] maxLine = lines.last();
        if (maxLine[0] == -1) {
            result = 0;
        } else if (maxLine[1] == n) {
            result = n - 1;
        } else {
            result = maxLine[0] + (maxLine[1] - maxLine[0]) / 2;
        }
        // 将最长的线段一分为二
        removeLine(maxLine);
        addLine(new int[]{maxLine[0], result});
        addLine(new int[]{result, maxLine[1]});

        return result;
    }

    @Override
    public void leave(int p) {
        int[] right = starts.get(p);
        int[] left = ends.get(p);

        // 合并两个线段为一个线段
        removeLine(right);
        removeLine(left);
        addLine(new int[]{left[0], right[1]});
    }

    // 增加一个线段
    private void addLine(int[] line) {
        lines.add(line);
        starts.put(line[0], line);
        ends.put(line[1], line);
    }

    // 去除一个线段
    private void removeLine(int[] line) {
        lines.remove(line);
        starts.remove(line[0]);
        ends.remove(line[1]);
    }

    // 计算线段长度
    private int distance(int[] line) {
        if (line[0] == -1) {
            return line[1];
        } else if (line[1] == n) {
            return n - line[0] - 1;
        } else {
            // 中点和端点之间的长度
            return (line[1] - line[0]) / 2;
        }
    }
}
