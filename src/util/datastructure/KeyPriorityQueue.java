package util.datastructure;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 带有 key 的优先队列，可以使用 key 在 O(1) 时间内查找元素，O(logN) 时间内删除、更新元素。
 */
public class KeyPriorityQueue<K, V> {

    private static final int INITIAL_CAPACITY = 16;


    private final Comparator<V> comparator;
    private final Map<K, Integer> key2idx;
    private K[] idx2key;
    private V[] elements;
    private int size;


    public KeyPriorityQueue() {
        this(INITIAL_CAPACITY, null, INITIAL_CAPACITY);
    }

    public KeyPriorityQueue(int capacity) {
        this(capacity, null, INITIAL_CAPACITY);
    }

    public KeyPriorityQueue(Comparator<V> comparator) {
        this(INITIAL_CAPACITY, comparator, INITIAL_CAPACITY);
    }

    public KeyPriorityQueue(int capacity, Comparator<V> comparator) {
        this(capacity, comparator, INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public KeyPriorityQueue(int capacity, Comparator<V> comparator, int keyCapacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity=" + capacity);
        }
        if (keyCapacity <= 0) {
            throw new IllegalArgumentException("keyCapacity=" + keyCapacity);
        }

        if (comparator != null) {
            this.comparator = comparator;
        } else {
            //noinspection unchecked
            this.comparator = (a, b) -> ((Comparable<V>) a).compareTo(b);
        }

        key2idx = new HashMap<>(keyCapacity);
        // idx=0 不使用，所以容量+1
        idx2key = (K[]) new Object[capacity + 1];
        elements = (V[]) new Object[capacity + 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public void push(K key, V elem) {
        Objects.requireNonNull(elem, "elem is null");

        // 没有设置过这个 key，就添加到队列最后面再上浮
        if (!key2idx.containsKey(key)) {
            ensureCapacity();
            elements[++size] = elem;
            key2idx.put(key, size);
            idx2key[size] = key;
            swim(size);
        } else {  // 已经设置过 key，则覆盖原有元素，并尝试上浮/下沉
            int idx = key2idx.get(key);
            V old = elements[idx];
            elements[idx] = elem;
            int cmp = comparator.compare(old, elem);
            if (cmp < 0) {
                sink(idx);
            } else if (cmp > 0) {
                swim(idx);
            }
        }
    }

    public V peek() {
        return elements[1];
    }

    public V peek(K key) {
        return elements[key2idx.getOrDefault(key, 0)];
    }

    public V poll() {
        if (size == 0) {
            return null;
        }

        return poll(idx2key[1], 1);
    }

    public V poll(K key) {
        int idx = key2idx.getOrDefault(key, 0);
        if (idx == 0) {
            return null;
        }

        return poll(key, idx);
    }


    private V poll(K key, int idx) {
        // 用队尾的元素覆盖 idx，再下沉
        V elem = elements[idx], tail = elements[size];
        // 防止内存泄露
        elements[size] = null;
        elements[idx] = tail;
        key2idx.remove(key);
        K tailKey = idx2key[size--];
        // 还有元素才下沉
        if (size > 0) {
            key2idx.put(tailKey, idx);
            idx2key[idx] = tailKey;
            sink(idx);
        }

        return elem;
    }

    private void swim(int idx) {
        // 当 idx 处的元素比父元素要小时，进行上浮
        int oldIdx = idx;
        K key = idx2key[idx];
        V elem = elements[idx];
        for (int parentIdx = idx / 2;
             idx > 1 && comparator.compare(elem, elements[parentIdx]) < 0;
             idx = parentIdx, parentIdx = idx / 2) {
            // 更新 parent 的位置和映射关系
            elements[idx] = elements[parentIdx];
            K parentKey = idx2key[parentIdx];
            idx2key[idx] = parentKey;
            key2idx.put(parentKey, idx);
        }

        // 如果上浮成功，更新 elem 的位置和映射关系
        if (oldIdx != idx) {
            elements[idx] = elem;
            idx2key[idx] = key;
            key2idx.put(key, idx);
        }
    }

    private void sink(int idx) {
        // 当 idx 处的元素比子元素要大时，进行下沉
        int oldIdx = idx;
        K key = idx2key[idx];
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
            K childKey = idx2key[childIdx];
            idx2key[idx] = childKey;
            key2idx.put(childKey, idx);
        }

        // 如果下沉成功，更新 elem 的位置和映射关系
        if (oldIdx != idx) {
            elements[idx] = elem;
            idx2key[idx] = key;
            key2idx.put(key, idx);
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
        K[] newIdx2Key = (K[]) new Object[newCap];
        System.arraycopy(elements, 1, newElements, 1, size);
        System.arraycopy(idx2key, 1, newIdx2Key, 1, size);
        elements = newElements;
        idx2key = newIdx2Key;
    }
}
