package test;

import datastructure.MyPriorityQueue;
import datastructure.MyQueue;
import model.Booking;
import model.WaitlistEntry;

/**
 * Reproducible benchmark for Report 3 / Report 4.
 * Measures the time to insert and then remove n pre-created bookings.
 */
public final class PerformanceBenchmark {
    private static final int[] INPUT_SIZES = {100, 500, 1_000, 5_000, 10_000};
    private static final int WARMUP_RUNS = 3;
    private static final int MEASURED_RUNS = 9;

    private PerformanceBenchmark() {
    }

    public static void main(String[] args) {
        System.out.println("n,MyQueue_FIFO_ms,MyPriorityQueue_MaxHeap_ms");
        for (int n : INPUT_SIZES) {
            Booking[] bookings = createBookings(n);
            warmUp(bookings);
            double queueMs = averageQueueMillis(bookings);
            double priorityQueueMs = averagePriorityQueueMillis(bookings);
            System.out.printf("%d,%.4f,%.4f%n", n, queueMs, priorityQueueMs);
        }
    }

    private static Booking[] createBookings(int n) {
        Booking[] bookings = new Booking[n];
        long baseTime = 1_700_000_000_000L;
        for (int i = 0; i < n; i++) {
            bookings[i] = new Booking("B" + i, "C" + i, "V" + i, "S001",
                    "2026-07-10", "MORNING", "WAITING", "UNPAID", "NONE", baseTime + i);
        }
        return bookings;
    }

    private static void warmUp(Booking[] bookings) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            runQueue(bookings);
            runPriorityQueue(bookings);
        }
    }

    private static double averageQueueMillis(Booking[] bookings) {
        long totalNanos = 0;
        for (int i = 0; i < MEASURED_RUNS; i++) totalNanos += runQueue(bookings);
        return totalNanos / (double) MEASURED_RUNS / 1_000_000.0;
    }

    private static double averagePriorityQueueMillis(Booking[] bookings) {
        long totalNanos = 0;
        for (int i = 0; i < MEASURED_RUNS; i++) totalNanos += runPriorityQueue(bookings);
        return totalNanos / (double) MEASURED_RUNS / 1_000_000.0;
    }

    private static long runQueue(Booking[] bookings) {
        MyQueue<Booking> queue = new MyQueue<>();
        long start = System.nanoTime();
        for (Booking booking : bookings) queue.enqueue(booking);
        while (!queue.isEmpty()) queue.dequeue();
        return System.nanoTime() - start;
    }

    private static long runPriorityQueue(Booking[] bookings) {
        MyPriorityQueue<WaitlistEntry> queue = new MyPriorityQueue<>();
        long start = System.nanoTime();
        for (int i = 0; i < bookings.length; i++) {
            queue.insert(new WaitlistEntry(bookings[i], (i % 4) + 1));
        }
        while (!queue.isEmpty()) queue.poll();
        return System.nanoTime() - start;
    }
}
