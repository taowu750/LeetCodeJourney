package acguide._0x10_basicdatastructure._0x12_queue;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.*;
import java.util.function.BiFunction;

/**
 * 小组队列: https://www.acwing.com/problem/content/134/
 *
 * 有 n 个小组要排成一个队列，每个小组中有若干人。
 * 当一个人来到队列时，如果队列中已经有了自己小组的成员，他就直接插队排在自己小组成员的后面，否则就站在队伍的最后面。
 * 请你编写一个程序，模拟这种小组队列。
 *
 * 输入格式：
 * - 输入将包含一个或多个测试用例。对于每个测试用例，第一行输入小组数量 t。
 * - 接下来 t 行，每行输入一个小组描述，第一个数表示这个小组的人数，接下来的数表示这个小组的人的编号。
 *   编号是 0 到 999999 范围内的整数。
 * - 一个小组最多可包含 1000 个人。
 * - 最后，命令列表如下。 有三种不同的命令：
 *   - ENQUEUE x - 将编号是 x 的人插入队列；
 *   - DEQUEUE - 让整个队列的第一个人出队；
 *   - STOP - 测试用例结束
 * - 每个命令占一行。
 * - 当输入用例 t=0 时，代表停止输入。
 * - 需注意：测试用例最多可包含 200000（20 万）个命令，因此小组队列的实现应该是高效的：入队和出队都需要使用常数时间。
 *
 * 输出样例:
 * - 对于每个测试用例，首先输出一行 Scenario #k，其中 k 是测试用例的编号。
 * - 然后，对于每个 DEQUEUE 命令，输出出队的人的编号，每个编号占一行。
 * - 在每个测试用例（包括最后一个测试用例）输出完成后，输出一个空行。
 *
 * 数据范围
 * - 1 ≤ t ≤ 1000
 *
 *
 * 例 1：
 * 输入：
 * 2
 * 3 101 102 103
 * 3 201 202 203
 * ENQUEUE 101
 * ENQUEUE 201
 * ENQUEUE 102
 * ENQUEUE 202
 * ENQUEUE 103
 * ENQUEUE 203
 * DEQUEUE
 * DEQUEUE
 * DEQUEUE
 * DEQUEUE
 * DEQUEUE
 * DEQUEUE
 * STOP
 * 2
 * 5 259001 259002 259003 259004 259005
 * 6 260001 260002 260003 260004 260005 260006
 * ENQUEUE 259001
 * ENQUEUE 260001
 * ENQUEUE 259002
 * ENQUEUE 259003
 * ENQUEUE 259004
 * ENQUEUE 259005
 * DEQUEUE
 * DEQUEUE
 * ENQUEUE 260002
 * ENQUEUE 260003
 * DEQUEUE
 * DEQUEUE
 * DEQUEUE
 * DEQUEUE
 * STOP
 * 0
 * 输出：
 * Scenario #1
 * 101
 * 102
 * 103
 * 201
 * 202
 * 203
 *
 * Scenario #2
 * 259001
 * 259002
 * 259003
 * 259004
 * 259005
 * 260001
 */
public class G45_TeamQueue {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x12_queue/data/G045_input.txt",
                "acguide/_0x10_basicdatastructure/_0x12_queue/data/G045_output.txt");
    }

    public static abstract class AbstractQueue {

        protected Map<Integer, Integer> no2team;
        protected int[] teamSize;

        public AbstractQueue(Map<Integer, Integer> no2team, int[] teamSize) {
            this.no2team = no2team;
            this.teamSize = teamSize;
        }

        public abstract void enqueue(int no);

        public abstract int dequeue();
    }

    public void execute(BiFunction<Map<Integer, Integer>, int[], AbstractQueue> factory) {
        Scanner in = new Scanner(System.in);
        int caseNo = 1;
        for (;; caseNo++) {
            int t = in.nextInt();
            if (t == 0) {
                break;
            }

            Map<Integer, Integer> no2team = new HashMap<>();
            int[] teamSize = new int[t];
            for (int i = 0; i < t; i++) {
                int cnt = in.nextInt();
                teamSize[i] = cnt;
                for (int j = 0; j < cnt; j++) {
                    no2team.put(in.nextInt(), i);
                }
            }
            AbstractQueue queue = factory.apply(no2team, teamSize);

            System.out.println("Scenario #" + caseNo);
            // 跳过换行符
            in.nextLine();
            for (String line = in.nextLine(); !line.equals("STOP"); line = in.nextLine()) {
                if (line.startsWith("ENQUEUE")) {
                    queue.enqueue(Integer.parseInt(line.split(" ")[1]));
                } else {
                    System.out.println(queue.dequeue());
                }
            }
            System.out.println();
        }
    }

    public static class Queue extends AbstractQueue {

        private final Deque<Integer>[] team2queue;
        private final Deque<Deque<Integer>> queue;

        public Queue(Map<Integer, Integer> team, int[] teamSize) {
            super(team, teamSize);
            team2queue = new Deque[teamSize.length];
            for (int i = 0; i < teamSize.length; i++) {
                team2queue[i] = new ArrayDeque<>(teamSize[i]);
            }
            queue = new ArrayDeque<>(teamSize.length);
        }

        @Override
        public void enqueue(int no) {
            Deque<Integer> q = team2queue[no2team.get(no)];
            if (q.isEmpty()) {
                queue.add(q);
            }
            q.add(no);
        }

        @Override
        public int dequeue() {
            Deque<Integer> q = queue.element();
            int no = q.remove();
            if (q.isEmpty()) {
                queue.remove();
            }

            return no;
        }
    }

    @Test
    public void testQueue() {
        test(() -> execute(Queue::new));
    }
}
