package util.datastructure.test;

import org.junit.jupiter.api.Test;
import util.datastructure.KeyPriorityQueue;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class KeyPriorityQueueTest {

    @Test
    public void testNew() {
        assertThrows(IllegalArgumentException.class, () -> new KeyPriorityQueue<Integer, Integer>(-5));

        KeyPriorityQueue<Integer, Integer> heap = new KeyPriorityQueue<>(10);
        assertEquals(0, heap.size());
    }

    @Test
    public void testPush() {
        KeyPriorityQueue<Integer, Integer> heap = new KeyPriorityQueue<>(1);
        heap.push(0, 1);
        heap.push(1, 2);
        assertEquals(2, heap.size());

        for (int i = 2; i < 1000; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> heap.push(finalI, finalI));
        }
    }

    @Test
    public void testPushPeek() {
        KeyPriorityQueue<Integer, Integer> heap = new KeyPriorityQueue<>(1);
        int[] numbers = {-9, 20, 1, 4, 2, 7, 9, 20, 4, 2, 3, 2, 5, 100};
        for (int i = 0; i < numbers.length; i++) {
            heap.push(i, numbers[i]);
        }

        assertEquals(Arrays.stream(numbers).min().getAsInt(), heap.peek());
        for (int i = 0; i < numbers.length; i++) {
            assertEquals(numbers[i], heap.peek(i));
        }
        assertNull(heap.peek(numbers.length));
    }

    @Test
    public void testPoll() {
        KeyPriorityQueue<Integer, Integer> heap = new KeyPriorityQueue<>(1);
        int[] numbers = {-9, 20, 1, 4, 2, 7, 9, 20, 4, 2, 3, 2, 5, 100};
        for (int i = 0; i < numbers.length; i++) {
            heap.push(i, numbers[i]);
        }
        Arrays.sort(numbers);
        for (int number : numbers) {
            assertEquals(number, heap.poll());
        }
        assertNull(heap.poll(0));
        assertEquals(0, heap.size());

        numbers = new int[]{-9, 20, 1, 4, 2, 7, 9, 20, 4, 2, 3, 2, 5, 100};
        for (int i = 0; i < numbers.length; i++) {
            heap.push(i, numbers[i]);
        }
        for (int i = numbers.length - 1; i >= 0; i--) {
            assertEquals(numbers[i], heap.poll(i));
        }
        assertEquals(0, heap.size());
    }

    @Test
    public void testOrder() {
        KeyPriorityQueue<Integer, Integer> heap = new KeyPriorityQueue<>(1, Comparator.comparing(i -> -i));
        int[] numbers = {-9, 20, 1, 4, 2, 7, 9, 20, 4, 2, 3, 2, 5, 100};
        for (int i = 0; i < numbers.length; i++) {
            heap.push(i, numbers[i]);
        }
        Arrays.sort(numbers);
        for (int i = numbers.length - 1; i >= 0; i--) {
            assertEquals(numbers[i], heap.poll());
        }
        assertNull(heap.poll(0));
        assertEquals(0, heap.size());
    }

    @Test
    public void testPut() {
        KeyPriorityQueue<Integer, Integer> pq = new KeyPriorityQueue<>();
        pq.push(0, 10);
        pq.push(1, -3);
        pq.push(2, 4);
        pq.push(3, 1);
        pq.push(1, 6);
        assertEquals(6, pq.peek(1));

        int[] actual = {1, 4, 6, 10};
        int i = 0;
        while (!pq.isEmpty()) {
            assertEquals(actual[i++], pq.poll());
        }
    }

    @Test
    public void testGetKey() {
        KeyPriorityQueue<Integer, Integer> heap = new KeyPriorityQueue<>(1);
        int[] numbers = {-9, 20, 1, 4, 2, 7, 9, 3, 5, 100};
        for (int i = 0; i < numbers.length; i++) {
            heap.push(i, numbers[i]);
        }
        assertEquals(-9, heap.peekEntry().value);
        assertEquals(0, heap.peekEntry().key);

        int[] indices = {0, 2, 4, 7, 3, 8, 5, 6, 1, 9};
        int i = 0;
        while (!heap.isEmpty()) {
            final KeyPriorityQueue.Entry<Integer, Integer> entry = heap.pollEntry();
            assertEquals(numbers[indices[i]], entry.value);
            assertEquals(indices[i++], entry.key);
        }
    }
}
