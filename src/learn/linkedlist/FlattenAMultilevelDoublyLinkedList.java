package learn.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个双向链接列表，该列表除了下一个和上一个指针外，还可以具有一个子指针，
 * 该子指针可能指向也可能不指向单独的双向链接列表。这些子列表可能有一个或多个自己的子列表，
 * 依此类推，以产生一个多级数据结构，如下例所示。
 *
 * 例 1：
 * Input: head = [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
 * Output: [1,2,3,7,8,11,12,9,10,4,5,6]
 * Explanation:
 * 输入的多级链表如下所示：
 * 1---2---3---4---5---6
 *         |
 *         7---8---9---10
 *             |
 *             11---12
 * 展开后变成这样：
 * 1---2---3---7---8---11---12---9---10---4---5---6
 *
 * 例 2：
 * Input: head = [1,2,null,3]
 * Output: [1,3,2]
 * Explanation:
 * 输入的多级链表如下所示：
 * 1--2---NULL
 * |
 * 3---NULL
 *
 * 例 3：
 * Input: head = []
 * Output: []
 *
 * 例 4：
 * Input: [37099,10580,33214,85010,91986,30187,null,
 * 85793,98560,46360,50155,37244,null,
 * null,null,27598,81763,null,
 * 40623]
 * Output: [37099,85793,98560,46360,27598,40623,81763,50155,37244,10580,33214,85010,91986,30187]
 * Explanation:
 * 37099---10580---33214---85010---91986---30187
 * |
 * 85793---98560---46360---50155---37244
 *                 |
 *                 27598---81763
 *                 |
 *                 40623
 *
 * 约束：
 * - 结点数不超过 1000
 * - 1 <= Node.val <= 10**5
 */
public class FlattenAMultilevelDoublyLinkedList {

    public static class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;

        public Node() {
        }

        public Node(int val) {
            this.val = val;
        }

        public Node(int val, Node prev) {
            this.val = val;
            this.prev = prev;
        }

        public Node(int val, Node prev, Node next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }

    public static Node newMDL(Integer... vals) {
        Node head = new Node(), ph = head, p = ph;
        boolean child = false;
        for (Integer val : vals) {
            if (val != null) {
                if (child) {
                    Node childHead = new Node(val);
                    ph.child = childHead;
                    ph = new Node(0);
                    ph.next = childHead;
                    p = ph.next;
                    child = false;
                } else {
                    p.next = new Node(val, p);
                    p = p.next;
                }
            } else {
                child = true;
                ph = ph.next;
            }
        }

        return head.next;
    }

    public static void printMDL(Node head) {
        String leadingBlanks = "";
        do {
            Node childHead = null;
            int blankLen = 0;
            System.out.print(leadingBlanks);
            for (;head != null; head = head.next) {
                String val = Integer.toString(head.val);
                System.out.print(val);
                if (head.next != null)
                    System.out.print("---");

                if (head.child != null)
                    childHead = head.child;
                if (childHead == null)
                    blankLen += val.length() + 3;
            }
            System.out.println();
            if (childHead != null) {
                head = childHead;
                leadingBlanks = leadingBlanks + IntStream.range(0, blankLen)
                        .mapToObj(i -> " ")
                        .collect(Collectors.joining());
                System.out.println(leadingBlanks + "|");
            }
        } while (head != null);
    }

    public static boolean MDLEqual(Node head, int... vals) {
        for (int i = 0; i < vals.length; i++, head = head.next) {
            if (head == null || head.val != vals[i])
                return false;
        }
        return head == null;
    }

    @Test
    public void testMDL() {
        Node head = newMDL(1, 2, 3, 4, 5, 6, null,
                null, null, 7, 8, 9, 10, null,
                null, 11, 12);
        printMDL(head);

        head = newMDL(1, 2, null,
                3);
        printMDL(head);

        head = newMDL(37099, 10580, 33214, 85010, 91986, 30187, null,
                85793, 98560, 46360, 50155, 37244, null,
                null, null, 27598, 81763, null,
                40623);
        printMDL(head);
    }

    static void test(Function<Node, Node> method) {
        Node head = newMDL(1, 2, 3, 4, 5, 6, null,
                null, null, 7, 8, 9, 10, null,
                null, 11, 12);
        head = method.apply(head);
        printMDL(head);
        assertTrue(MDLEqual(head, 1, 2, 3, 7, 8, 11, 12, 9, 10, 4, 5, 6));

        head = newMDL(1, 2, null,
                3);
        head = method.apply(head);
        printMDL(head);
        assertTrue(MDLEqual(head, 1, 3, 2));

        assertNull(method.apply(null));

        head = newMDL(1, 2, 3, 4);
        head = method.apply(head);
        printMDL(head);
        assertTrue(MDLEqual(head, 1, 2, 3, 4));

        head = newMDL(37099, 10580, 33214, 85010, 91986, 30187, null,
                85793, 98560, 46360, 50155, 37244, null,
                null, null, 27598, 81763, null,
                40623);
        head = method.apply(head);
        printMDL(head);
        assertTrue(MDLEqual(head, 37099, 85793, 98560, 46360, 27598, 40623,
                81763, 50155, 37244, 10580, 33214, 85010, 91986, 30187));
    }

    public Node flatten(Node head) {
        Node start = new Node(), p = start;
        start.next = head;
        Deque<Node> nextStack = new LinkedList<>();

        while (p.next != null) {
            p = p.next;
            while (p.next != null && p.child == null)
                p = p.next;
            if (p.child != null) {
                if (p.next != null)
                    nextStack.push(p.next);
                p.next = p.child;
                p.child.prev = p;
                p.child = null;
            } else {
                while (p.next == null && !nextStack.isEmpty()) {
                    p.next = nextStack.pop();
                    p.next.prev = p;
                    p = p.next;
                }
            }
        }

        return start.next;
    }

    @Test
    public void testFlatten() {
        test(this::flatten);
    }
}
