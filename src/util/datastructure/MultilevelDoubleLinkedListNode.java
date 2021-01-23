package util.datastructure;

import java.lang.reflect.Constructor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 多级双链表
 */
public class MultilevelDoubleLinkedListNode<T extends MultilevelDoubleLinkedListNode<T>> {

    public int val;
    public T prev;
    public T next;
    public T child;

    public MultilevelDoubleLinkedListNode() {
    }

    public MultilevelDoubleLinkedListNode(int val) {
        this.val = val;
    }

    public MultilevelDoubleLinkedListNode(int val, T prev) {
        this.val = val;
        this.prev = prev;
    }

    public MultilevelDoubleLinkedListNode(int val, T prev, T next) {
        this.val = val;
        this.prev = prev;
        this.next = next;
    }

    public static <T extends MultilevelDoubleLinkedListNode<T>> T newList(Class<T> clazz, Integer... vals) {
        try {
            Constructor<T> intConstructor = clazz.getConstructor(int.class);
            intConstructor.setAccessible(true);
            Constructor<T> intPrevConstructor = clazz.getConstructor(int.class, clazz);
            intPrevConstructor.setAccessible(true);
            T head = intConstructor.newInstance(0), ph = head, p = ph;
            boolean child = false;
            for (Integer val : vals) {
                if (val != null) {
                    if (child) {
                        T childHead = intConstructor.newInstance(val);
                        ph.child = childHead;
                        ph = intConstructor.newInstance(0);
                        ph.next = childHead;
                        p = ph.next;
                        child = false;
                    } else {
                        p.next = intPrevConstructor.newInstance(val, p);
                        p = p.next;
                    }
                } else {
                    child = true;
                    ph = ph.next;
                }
            }
            intConstructor.setAccessible(false);
            intPrevConstructor.setAccessible(false);

            return head.next;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends MultilevelDoubleLinkedListNode<T>> void printList(T head) {
        String leadingBlanks = "";
        do {
            T childHead = null;
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

    public static <T extends MultilevelDoubleLinkedListNode<T>> boolean listEqual(T head, int... vals) {
        for (int i = 0; i < vals.length; i++, head = head.next) {
            if (head == null || head.val != vals[i])
                return false;
        }
        return head == null;
    }
}
