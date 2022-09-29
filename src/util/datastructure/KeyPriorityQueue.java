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


    public static class Entry<K, V> {
        public final K key;
        public final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }


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
        idx2key = (K[]) new Object[capacity];
        elements = (V[]) new Object[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public void push(K key, V elem) {
        Objects.requireNonNull(elem, "elem is null");

        int idx = key2idx.getOrDefault(key, -1);
        // 没有设置过这个 key，就添加到队列最后面再上浮
        if (idx == -1) {
            ensureCapacity();
            swim(size++, key, elem);
        } else {  // 已经设置过 key，则覆盖原有元素，并尝试上浮/下沉
            int cmp = comparator.compare(elements[idx], elem);
            if (cmp < 0) {
                sink(idx, key, elem);
            } else if (cmp > 0) {
                swim(idx, key, elem);
            } else {
                //注意虽然比较结果相同，但还是需要覆盖原来的对象
                elements[idx] = elem;
            }
        }
    }

    public V peek() {
        return elements[0];
    }

    public Entry<K, V> peekEntry() {
        return size > 0 ? new Entry<>(idx2key[0], elements[0]) : null;
    }

    public V peek(K key) {
        int idx = key2idx.getOrDefault(key, -1);
        return idx != -1 ? elements[idx] : null;
    }

    public V peekOrDefault(K key, V defaultValue) {
        int idx = key2idx.getOrDefault(key, -1);
        return idx != -1 ? elements[idx] : defaultValue;
    }

    public V poll() {
        return size > 0 ? poll(idx2key[0], 0) : null;
    }

    public Entry<K, V> pollEntry() {
        return size > 0 ? new Entry<>(idx2key[0], poll(idx2key[0], 0)) : null;
    }

    public V poll(K key) {
        int idx = key2idx.getOrDefault(key, -1);
        return idx != -1 ? poll(key, idx) : null;
    }

    public V pollOrDefault(K key, V defaultValue) {
        int idx = key2idx.getOrDefault(key, -1);
        return idx != -1 ? poll(key, idx) : defaultValue;
    }


    private V poll(K key, int idx) {
        // 用队尾的元素覆盖 idx，再下沉
        V elem = elements[idx], tail = elements[--size];
        // 释放原有队尾
        elements[size] = null;
        K tailKey = idx2key[size];
        idx2key[size] = null;
        key2idx.remove(key);
        // 如果删除的就是队尾元素，则直接返回
        if (idx == size) {
            return tail;
        }
        sink(idx, tailKey, tail);
        // 如果队尾元素没有沉下去，那么就需要把它浮上来试试看
        // 这种情况是因为队尾可能不是 idx 节点的子节点（不再它的子树中），所以可能小于 idx 节点
        if (elements[idx] == tail) {
            swim(idx, tailKey, tail);
        }

        return elem;
    }

    private void swim(int idx, K key, V elem) {
        // 当 idx 处的元素比父元素要小时，进行上浮
        for (int parentIdx = (idx - 1) / 2;
             idx > 0 && comparator.compare(elem, elements[parentIdx]) < 0;
             idx = parentIdx, parentIdx = (idx - 1) / 2) {
            // 更新 parent 的位置和映射关系
            elements[idx] = elements[parentIdx];
            K parentKey = idx2key[parentIdx];
            idx2key[idx] = parentKey;
            key2idx.put(parentKey, idx);
        }

        // 更新 elem 的位置和映射关系
        elements[idx] = elem;
        idx2key[idx] = key;
        key2idx.put(key, idx);
    }

    private void sink(int idx, K key, V elem) {
        // 当 idx 处的元素比子元素要大时，进行下沉
        for (int childIdx = idx * 2 + 1; childIdx < size; idx = childIdx, childIdx = idx * 2 + 1) {
            // 选择最小的子元素
            if (childIdx + 1 < size && comparator.compare(elements[childIdx], elements[childIdx + 1]) > 0) {
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

        // 更新 elem 的位置和映射关系
        elements[idx] = elem;
        idx2key[idx] = key;
        key2idx.put(key, idx);
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length) {
            return;
        }
        if (elements.length == Integer.MAX_VALUE) {
            throw new OutOfMemoryError();
        }

        int newCap;
        if (Integer.MAX_VALUE - (elements.length + 1) / 2 <= elements.length) {
            newCap = Integer.MAX_VALUE;
        } else {
            // 因为 elements 长度可能为 1，所以这里加 1 防止计算结果为 0
            newCap = elements.length + (elements.length + 1) / 2;
        }

        V[] newElements = (V[]) new Object[newCap];
        K[] newIdx2Key = (K[]) new Object[newCap];
        System.arraycopy(elements, 0, newElements, 0, size);
        System.arraycopy(idx2key, 0, newIdx2Key, 0, size);
        elements = newElements;
        idx2key = newIdx2Key;
    }
}
