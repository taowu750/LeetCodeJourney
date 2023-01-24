package util.datastructure;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 随机链表结点
 */
public class RandomLinkedListNode<T extends RandomLinkedListNode<T>> {

    public int val;
    public T next;
    public T random;

    public RandomLinkedListNode(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }

    public static <T extends RandomLinkedListNode<T>> T newRandomList(Class<T> clazz, Integer... valAndRandomIndexes) {
        try {
            Constructor<T> constructor = clazz.getConstructor(int.class);
            constructor.setAccessible(true);
            T start = constructor.newInstance(0), p = start;
            List<T> nodes = new ArrayList<>(valAndRandomIndexes.length / 2);
            for (int i = 0; i < valAndRandomIndexes.length; i += 2) {
                p.next = constructor.newInstance(valAndRandomIndexes[i]);
                p = p.next;
                nodes.add(p);
            }
            p = start.next;
            for (int i = 0; i < valAndRandomIndexes.length; i += 2, p = p.next) {
                if (valAndRandomIndexes[i + 1] == null)
                    p.random = null;
                else
                    p.random = nodes.get(valAndRandomIndexes[i + 1]);
            }
            constructor.setAccessible(false);

            return start.next;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends RandomLinkedListNode<T>> void printRandomList(T head) {
        LinkedList<Integer> valLens = new LinkedList<>();
        for (T p = head; p != null; p = p.next) {
            int randomVal = p.random == null ? 1000 : p.random.val;
            valLens.add(Math.max(Integer.toString(p.val).length(),
                    Integer.toString(randomVal).length()));
            System.out.printf("%-" + valLens.peekLast() + "d", p.val);
            if (p.next != null)
                System.out.print("-->");
        }
        System.out.println();
        for (int valLen : valLens)
            System.out.printf("%-" + valLen + "s   ", "|");
        System.out.println();
        T p = head;
        for (int valLen : valLens) {
            if (p.random == null)
                System.out.printf("%-" + valLen + "s", "null");
            else
                System.out.printf("%-" + valLen + "d", p.random.val);
            if (p.next != null)
                System.out.print("-->");
            p = p.next;
        }
        System.out.println();
    }

    public static <T extends RandomLinkedListNode<T>> boolean deepEqualRandomList(T l1, T l2) {
        while (l1 != null && l2 != null) {
            if (l1 == l2)
                throw new AssertionError("l1 == l2: val=" + l1);
            if (l1.val != l2.val)
                throw new AssertionError("l1.val == l2.val: l1.val=" + l1 + ", l2.val=" + l2);
            if (l1.random == l2.random && l1.random != null)
                throw new AssertionError("l1.random == l2.random: random.val=" + l1.random);
            if ((l1.random == null && l2.random != null) || (l1.random != null && l2.random == null)) {
                throw new AssertionError("l1.random == null || l2.random == null: l1.random=" + l1.random + ", l2.random=" + l2.random);
            }
            if (l1.random != null && l1.random.val != l2.random.val)
                throw new AssertionError("l1.random.val != l2.random.val: l1.random=" + l1.random + ", l2.random=" + l2.random);
            l1 = l1.next;
            l2 = l2.next;
        }
        return l1 == null && l2 == null;
    }
}
