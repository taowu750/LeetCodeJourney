package training.math;

import training.linkedlist.ListNode;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Function;

import static training.linkedlist.ListNode.newList;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 382. 链表随机节点: https://leetcode-cn.com/problems/linked-list-random-node/
 *
 * 给定一个单链表，随机选择链表的一个节点，并返回相应的节点值。保证每个节点被选的概率一样。
 *
 * 例 1：
 * // 初始化一个单链表 [1,2,3].
 * ListNode head = new ListNode(1);
 * head.next = new ListNode(2);
 * head.next.next = new ListNode(3);
 * Solution solution = new Solution(head);
 *
 * // getRandom()方法应随机返回1,2,3中的一个，保证每个元素被返回的概率相等。
 * solution.getRandom();
 */
public class E382_Medium_LinkedListRandomNode {

    static void test(Function<ListNode, LinkedListRandomNode> factory) {
        ListNode head = newList(-1,1,2,3);
        LinkedListRandomNode solution = factory.apply(head);

        int[] cnt = new int[4];
        for (int i = 0; i < 30000; i++) {
            int r = solution.getRandom();
            assertTrue(r >= 1 && r <= 3);
            cnt[r]++;
        }

        for (int i = 1; i < cnt.length; i++)
            assertTrue(cnt[i] > 9500);
    }

    @Test
    public void testSolution() {
        test(Solution::new);
    }
}


abstract class LinkedListRandomNode {

    protected ListNode head;

    public LinkedListRandomNode(ListNode head) {
        this.head = head;
    }

    public abstract int getRandom();
}

/**
 * 蓄水池抽样算法。参见：https://www.jianshu.com/p/7a9ea6ece2af
 */
class Solution extends LinkedListRandomNode {

    private Random random;

    public Solution(ListNode head) {
        super(head);
        random = new Random();
    }

    @Override
    public int getRandom() {
        if (head == null)
            return 0;

        int result = head.val, i = 1;
        for (ListNode p = head.next; p != null; p = p.next, i++) {
            int d = random.nextInt(i + 1);
            if (d < 1)
                result = p.val;
        }

        return result;
    }
}
