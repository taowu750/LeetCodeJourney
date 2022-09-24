package util.datastructure;

import java.util.Comparator;
import java.util.Objects;

/**
 * 带有整数键的优先队列，可以使用整数键在 O(1) 时间内查找元素，O(logN) 时间内删除、更新元素。
 * 注意整数键的大小范围为 [0, {@link #size()}]。
 */
public class IntKeyHeap<V> {

    private static final int INITIAL_CAPACITY = 16;


    private final Comparator<V> comparator;
    private int[] key2idx;
    private int[] idx2key;
    private V[] elements;
    private int size;


    public IntKeyHeap() {
        this(INITIAL_CAPACITY, null);
    }

    public IntKeyHeap(int capacity) {
        this(capacity, null);
    }

    public IntKeyHeap(Comparator<V> comparator) {
        this(INITIAL_CAPACITY, comparator);
    }

    public IntKeyHeap(int capacity, Comparator<V> comparator) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity=" + capacity);
        }
        if (comparator != null) {
            this.comparator = comparator;
        } else {
            //noinspection unchecked
            this.comparator = (a, b) -> ((Comparable<V>) a).compareTo(b);
        }

        // 0 表示未设置 key
        key2idx = new int[capacity + 1];
        // idx=0 不使用，所以容量+1
        idx2key = new int[capacity + 1];
        //noinspection unchecked
        elements = (V[]) new Object[capacity + 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int capacity() {
        return elements.length - 1;
    }

    public boolean isFull() {
        return size == elements.length - 1;
    }

    /**
     * 将 elem 入队，并关联整数键 key，key 的必须在 [0, {@link #size()}]。
     * 如果 key 已存在则替换元素。
     *
     * @param key 整数键
     * @param elem 入队的元素
     */
    public void push(int key, V elem) {
        checkKey(key);
        Objects.requireNonNull(elem, "elem is null");

        // 没有设置过这个 key，就添加到队列最后面再上浮
        if (key2idx[key] == 0) {
            ensureCapacity();
            elements[++size] = elem;
            key2idx[key] = size;
            idx2key[size] = key;
            swim(size);
        } else {  // 已经设置过 key，则覆盖原有元素，并尝试上浮/下沉
            int idx = key2idx[key];
            elements[idx] = elem;
            if (!swim(idx)) {
                sink(idx);
            }
        }
    }

    public V peek() {
        return size > 0 ? elements[1] : null;
    }

    public V peek(int key) {
        checkKey(key);
        return key2idx[key] != 0 ? elements[key2idx[key]] : null;
    }

    public V poll() {
        if (size == 0) {
            return null;
        }

        return poll(idx2key[1], 1);
    }

    public V poll(int key) {
        checkKey(key);

        int idx = key2idx[key];
        if (idx == 0) {
            return null;
        }

        return poll(key, idx);
    }


    private V poll(int key, int idx) {
        // 用队尾的元素覆盖 idx，再下沉
        V elem = elements[idx], tail = elements[size];
        // 防止内存泄露
        elements[size] = null;
        elements[idx] = tail;
        key2idx[key] = 0;
        int tailKey = idx2key[size--];
        // 还有元素才下沉
        if (size > 0) {
            key2idx[tailKey] = idx;
            idx2key[idx] = tailKey;
            sink(idx);
        }

        return elem;
    }

    private void checkKey(int key) {
        if (key < 0 || key > size) {
            throw new IllegalArgumentException("key=" + key + " exceed range [0," + key2idx.length + "]");
        }
    }

    private boolean swim(int idx) {
        // 当 idx 处的元素比父元素要小时，进行上浮
        int oldIdx = idx, key = idx2key[idx];
        V elem = elements[idx];
        for (int parentIdx = idx / 2;
             idx > 1 && comparator.compare(elem, elements[parentIdx]) < 0;
             idx = parentIdx, parentIdx = idx / 2) {
            // 更新 parent 的位置和映射关系
            elements[idx] = elements[parentIdx];
            int parentKey = idx2key[parentIdx];
            idx2key[idx] = parentKey;
            key2idx[parentKey] = idx;
        }

        // 如果上浮成功，更新 elem 的位置和映射关系
        if (oldIdx != idx) {
            elements[idx] = elem;
            idx2key[idx] = key;
            key2idx[key] = idx;

            return true;
        }

        return false;
    }

    private void sink(int idx) {
        // 当 idx 处的元素比子元素要大时，进行下沉
        int oldIdx = idx, key = idx2key[idx];
        V elem = elements[idx];
        for (int childIdx = idx * 2; childIdx <= size; idx = childIdx, childIdx = idx * 2) {
            // 选择最小的子元素
            if (childIdx < size && comparator.compare(elements[childIdx], elements[childIdx + 1]) > 0) {
                childIdx++;
            }
            // 当前元素 <= 子元素，则终止下沉
            if (comparator.compare(elem, elements[childIdx]) <= 0) {
                break;
            }
            // 更新 child 的位置和映射关系
            elements[idx] = elements[childIdx];
            int childKey = idx2key[childIdx];
            idx2key[idx] = childKey;
            key2idx[childKey] = idx;
        }

        // 如果下沉成功，更新 elem 的位置和映射关系
        if (oldIdx != idx) {
            elements[idx] = elem;
            idx2key[idx] = key;
            key2idx[key] = idx;
        }
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length - 1) {
            return;
        }
        if (elements.length == Integer.MAX_VALUE) {
            throw new OutOfMemoryError();
        }

        int newCap;
        if (Integer.MAX_VALUE - elements.length / 2 <= elements.length) {
            newCap = Integer.MAX_VALUE;
        } else {
            // 因为 elements 长度至少为 2（0 不使用），所以 elements.length / 2 至少为 1
            newCap = elements.length + elements.length / 2;
        }

        V[] newElements = (V[]) new Object[newCap];
        int[] newKey2idx = new int[newCap];
        int[] newIdx2Key = new int[newCap];

        System.arraycopy(elements, 1, newElements, 1, size);
        // 注意，key2idx [0, size] 都可用
        System.arraycopy(key2idx , 0, newKey2idx, 0, size + 1);
        System.arraycopy(idx2key, 1, newIdx2Key, 1, size);

        elements = newElements;
        key2idx = newKey2idx;
        idx2key = newIdx2Key;
    }
}
