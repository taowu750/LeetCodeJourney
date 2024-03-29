package util.datastructure;


import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单链表结点，定义了一组实用方法
 */
public class SingleLinkedListNode<T extends SingleLinkedListNode<T>> {

    public int val;
    public T next;

    public SingleLinkedListNode() {}

    public SingleLinkedListNode(int val) {
        this.val = val;
    }

    public SingleLinkedListNode(int val, T next) {
        this.val = val;
        this.next = next;
    }

    public static <T extends SingleLinkedListNode<T>> T newList(Class<T> clazz, int pos, int... vals) {
        try {
            T head = null, posNode = null, p = null;

            Constructor<T> constructor = clazz.getConstructor(int.class);
            constructor.setAccessible(true);
            for (int i = 0; i < vals.length; i++) {
                if (head == null) {
                    head = constructor.newInstance(vals[i]);
                    p = head;
                } else {
                    p.next = constructor.newInstance(vals[i]);
                    p = p.next;
                }
                if (pos == i)
                    posNode = p;
            }
            if (posNode != null)
                p.next = posNode;
            constructor.setAccessible(false);

            return head;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends SingleLinkedListNode<T>> T getNodeAt(T head, int idx) {
        for (; head != null && --idx >= 0; head = head.next);

        return head;
    }

    public static <T extends SingleLinkedListNode<T>> T intersectList(Class<T> clazz, T listA, T intersect, int... listBElements) {
        T B = newList(clazz, -1, listBElements), p = B;
        for (;p.next != null; p = p.next);
        p.next = intersect;

        return B;
    }

    public static <T extends SingleLinkedListNode<T>> void printList(T head, int len) {
        if (len <= 0) {
            for (;head != null; head = head.next) {
                System.out.print(head.val);
                if (head.next != null)
                    System.out.print("->");
            }
        } else {
            for (int i = 0; i < len; i++, head = head.next) {
                System.out.print(head.val);
                if (i != len - 1)
                    System.out.print("->");
            }
        }
        System.out.println();
    }

    public static <T extends SingleLinkedListNode<T>> void printList(T head) {
        printList(head, 0);
    }

    /**
     * 直接在内部进行判断，不再返回 boolean 值。这里还返回 boolean 值是为了兼容性。
     */
    public static <T extends SingleLinkedListNode<T>> boolean listEqual(T head, int... comparedElements) {
        for (int i = 0; i < comparedElements.length; i++, head = head.next) {
            if (head == null || head.val != comparedElements[i]) {
                throw new AssertionError("diff at [" + i + "]: expected=" + comparedElements[i] +
                        ", actual=" + (head == null ? "null" : head.val));
            }
        }
        if (head != null)
            throw new AssertionError("The actual list length exceeds the expected list length: " +
                    "expected length=" + comparedElements.length);
        return true;
    }

    public static <T extends SingleLinkedListNode<T>> void circularSortedListEqual(T head, int... comparedElements) {
        T maxNode = head;
        T p = head;
        do {
            if (p.val >= maxNode.val) {
                maxNode = p;
            }
            p = p.next;
        } while (p != head);

        List<Integer> vals = new ArrayList<>(comparedElements.length);
        p = maxNode.next;
        do {
            vals.add(p.val);
            p = p.next;
        } while (p != maxNode.next);
        if (vals.size() != comparedElements.length) {
            throw new AssertionError("not equal length: actual=" + vals);
        }

        Arrays.sort(comparedElements);
        Assertions.assertEquals(Arrays.stream(comparedElements).boxed().collect(Collectors.toList()), vals);
    }
}
