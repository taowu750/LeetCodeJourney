package util.datastructure;


/**
 * 单链表结点，定义了一组实用方法
 */
public class SingleListNode<T extends SingleListNode<T>> {

    public int val;
    public T next;

    public SingleListNode() {}

    public SingleListNode(int val) {
        this.val = val;
    }

    public SingleListNode(int val, T next) {
        this.val = val;
        this.next = next;
    }

    public static <T extends SingleListNode<T>> T newList(Class<T> clazz, int pos, int... vals) {
        try {
            T head = null, posNode = null, p = null;

            for (int i = 0; i < vals.length; i++) {
                if (head == null) {
                    head = clazz.getConstructor(int.class).newInstance(vals[i]);
                    p = head;
                } else {
                    p.next = clazz.getConstructor(int.class).newInstance(vals[i]);
                    p = p.next;
                }
                if (pos == i)
                    posNode = p;
            }
            if (posNode != null)
                p.next = posNode;

            return head;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends SingleListNode<T>> T getNodeAt(T head, int idx) {
        for (; head != null && --idx >= 0; head = head.next);

        return head;
    }

    public static <T extends SingleListNode<T>> T intersectList(Class<T> clazz, T listA, T intersect, int... listBElements) {
        T B = newList(clazz, -1, listBElements), p = B;
        for (;p.next != null; p = p.next);
        p.next = intersect;

        return B;
    }

    public static <T extends SingleListNode<T>> void printList(T head, int len) {
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

    public static <T extends SingleListNode<T>> void printList(T head) {
        printList(head, 0);
    }

    public static <T extends SingleListNode<T>> boolean listEquals(T head, int... comparedElements) {
        for (int i = 0; i < comparedElements.length; i++, head = head.next) {
            if (head == null || head.val != comparedElements[i])
                return false;
        }
        return head == null;
    }
}
