package util.datastructure.test;

import org.junit.jupiter.api.Test;
import util.datastructure.IntKeyHeap;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class IntKeyHeapTest {

    @Test
    public void testNew() {
        assertThrows(IllegalArgumentException.class, () -> new IntKeyHeap<Integer>(0));
        assertThrows(IllegalArgumentException.class, () -> new IntKeyHeap<Integer>(-5));

        IntKeyHeap<Integer> heap = new IntKeyHeap<>(10);
        assertEquals(0, heap.size());
        assertEquals(10, heap.capacity());
        heap = new IntKeyHeap<>();
        assertEquals(16, heap.capacity());
    }

    @Test
    public void testPush() {
        IntKeyHeap<Integer> heap = new IntKeyHeap<>(1);
        heap.push(0, 1);
        heap.push(1, 2);
        assertEquals(2, heap.size());
        assertEquals(2, heap.capacity());

        for (int i = 2; i < 1000; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> heap.push(finalI, finalI));
        }
    }

    @Test
    public void testPushPeek() {
        IntKeyHeap<Integer> heap = new IntKeyHeap<>(1);
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
        IntKeyHeap<Integer> heap = new IntKeyHeap<>(1);
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
        IntKeyHeap<Integer> heap = new IntKeyHeap<>(1, Comparator.comparing(i -> -i));
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
}
